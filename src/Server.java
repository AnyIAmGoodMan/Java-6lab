import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Network.*;
import Managers.*;
import Commands.*;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {

        CollectionManager cm = new CollectionManager();
        CommandManager cmm = new CommandManager();
        XmlReader xr = new XmlReader();
        XmlWriter xw = new XmlWriter(cm);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                xw.write();
                logger.info("Коллекция сохранена при завершении");
            } catch (Exception e) {
                logger.error("Ошибка сохранения при завершении");
            }
        }));

        cmm.register(new HelpCommand(cmm));
        cmm.register(new InfoCommand(cm));
        cmm.register(new ShowCommand(cm));
        cmm.register(new AddCommand(cm));
        cmm.register(new AddIfMinCommand(cm));
        cmm.register(new ClearCommand(cm));
        cmm.register(new CountByDifficultyCommand(cm));
        cmm.register(new FilterContainsName(cm));
        cmm.register(new HistoryCommand(cmm));
        cmm.register(new PrintFieldDescendingDifficultyCommand(cm));
        cmm.register(new RemoveByIdCommand(cm));
        cmm.register(new RemoveHeadCommand(cm));
        cmm.register(new SaveCommand(xw, cm));
        cmm.register(new UpdateIdCommand(cm));

        if (args.length > 0) {
            try {
                cm.setFileName(args[0]);
                xr.read(args[0], cm);
                logger.info("Коллекция загружена из файла");
            } catch (Exception e) {
                logger.error("Ошибка загрузки файла");
            }
        }

        ServerSocketChannel server;

        try {
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(12345));
            server.configureBlocking(false);
        } catch (IOException e) {
            logger.error("Порт уже занят. Сервер уже запущен?");
            return;
        }

        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Сервер запущен...");

        while (true) {
            selector.select();

            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();


                if (key.isAcceptable()) {
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);

                    logger.debug("Новое подключение");
                }


                else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    try {
                        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);

                        if (client.read(sizeBuffer) <= 0) {
                            client.close();
                            continue;
                        }

                        sizeBuffer.flip();
                        int size = sizeBuffer.getInt();

                        ByteBuffer dataBuffer = ByteBuffer.allocate(size);
                        while (dataBuffer.hasRemaining()) {
                            client.read(dataBuffer);
                        }

                        ByteArrayInputStream bais = new ByteArrayInputStream(dataBuffer.array());
                        ObjectInputStream ois = new ObjectInputStream(bais);

                        Request request = (Request) ois.readObject();

                        logger.info("Получена команда: " + request.getCommandName());

                        String result = cmm.execute(request);

                        Response response = new Response(result);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(response);

                        byte[] respBytes = baos.toByteArray();

                        ByteBuffer out = ByteBuffer.allocate(4 + respBytes.length);
                        out.putInt(respBytes.length);
                        out.put(respBytes);
                        out.flip();

                        while (out.hasRemaining()) {
                            client.write(out);
                        }

                        logger.info("Ответ отправлен");

                        client.close();

                    } catch (Exception e) {
                        logger.error("Ошибка обработки клиента: " + e.getMessage());
                        client.close();
                    }
                }
            }
        }
    }
}
