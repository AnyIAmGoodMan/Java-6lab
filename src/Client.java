import LabWorks.*;
import Network.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Client {
    static Set<String> executingScripts = new HashSet<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("Введите команду: ");
            if (!sc.hasNextLine()) {
                System.out.println("Ввод завершён");
                break;
            }
            String cmd = sc.nextLine().trim();

            if (cmd.isEmpty()) continue;

            String[] parts = cmd.split("\\s+", 2);
            String name = parts[0];
            String arg = parts.length > 1 ? parts[1] : null;
            Request request;

            switch (name) {

                case "exit":
                    System.out.print("Выйти? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        running = false;
                    }
                    continue;

                case "execute_script":
                    executeScript(arg, sc);
                    continue;

                case "save":
                    System.out.println("Команда недоступна на клиенте. Используйте серверную команду");
                    continue;

                case "add": {
                    LabWork lw = createLabWork(sc);
                    request = new Request("add", lw);
                    break;
                }

                case "add_if_min": {
                    LabWork lw = createLabWork(sc);
                    request = new Request("add_if_min", lw);
                    break;
                }

                case "update_id": {
                    if (arg == null) {
                        System.out.println("Введите id");
                        continue;
                    }

                    try {
                        Long id = Long.parseLong(arg);
                        LabWork lw = createLabWork(sc);
                        request = new Request("update_id", new UpdateRequest(id, lw));
                    } catch (Exception e) {
                        System.out.println("Неверный id");
                        continue;
                    }
                    break;
                }

                default:
                    request = new Request(name, arg);
                    break;
            }

            sendRequest(request);
        }
    }
    public static void sendRequest(Request request) {

        int attempts = 3;

        for (int i = 1; i <= attempts; i++) {
            try {
                Socket socket = new Socket("localhost", 12345);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(request);

                byte[] data = baos.toByteArray();

                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                DataOutputStream dos = new DataOutputStream(out);
                dos.writeInt(data.length);
                dos.write(data);
                dos.flush();

                DataInputStream dis = new DataInputStream(in);
                int size = dis.readInt();

                byte[] resp = new byte[size];
                dis.readFully(resp);

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(resp));
                Response response = (Response) ois.readObject();

                System.out.println(response.getMessage());

                socket.close();

                return;

            } catch (IOException e) {

                System.out.println("Попытка " + i + " не удалась");

                if (i == attempts) {
                    System.out.println("Сервер недоступен");
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                }

            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка ответа");
                return;
            }
        }
    }
    public static void executeScript(String filename, Scanner sc) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("Введите имя файла");
            return;
        }

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Файл не найден");
                return;
            }

            String path = file.getCanonicalPath();

            if (executingScripts.contains(path)) {
                System.out.println("Обнаружена рекурсия!");
                return;
            }

            executingScripts.add(path);

            try (Scanner fileScanner = new Scanner(file)) {

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("\\s+", 2);
                    String name = parts[0];
                    String arg = parts.length > 1 ? parts[1] : null;

                    if (name.equals("execute_script")) {
                        if (arg == null || arg.isEmpty()) {
                            System.out.println("Не указано имя файла");
                            continue;
                        }

                        File nestedFile = (file.getParent() == null)
                                ? new File(arg.trim())
                                : new File(file.getParent(), arg.trim());

                        executeScript(nestedFile.getPath(), sc);
                        continue;
                    }

                    if (name.equals("add") || name.equals("add_if_min") || name.equals("update_id")) {
                        System.out.println("Команда " + name + " не поддерживается в скрипте");
                        continue;
                    }

                    sendRequest(new Request(name, arg));
                }

            } catch (Exception e) {
                System.out.println("Ошибка чтения скрипта: " + e.getMessage());
            } finally {
                executingScripts.remove(path);
            }

        } catch (IOException e) {
            System.out.println("Ошибка пути файла");
        }
    }



    public static LabWork createLabWork(Scanner sc) {

        String name;

        while (true) {
            System.out.print("Введите name:");
            name = sc.nextLine();
            if (!name.trim().isEmpty()) {
                break;
            }
            System.out.println("Имя не может быть пустым");
        }

        long x;
        while (true) {
            System.out.print("Введите coordinates.x:");
            try {
                x = Long.valueOf(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ожидалось целое число (long)");
            }
        }

        Float y;
        while (true) {
            System.out.print("Введите coordinates.y (<= 352):");
            try {
                String input = sc.nextLine();
                BigDecimal bd = new BigDecimal(input);

                if (bd.compareTo(new BigDecimal("352")) > 0) {
                    System.out.println("y должно быть <= 352");
                    continue;
                }

                y = bd.floatValue();
                break;

            } catch (NumberFormatException e) {
                System.out.println("Ожидалось число (float), меньшее 353");
            }
        }

        Coordinates coordinates = new Coordinates(x, y);

        Long minimalPoint = null;
        while (true) {
            System.out.print("Введите minimalPoint (пустая строка = null):");
            String mp = sc.nextLine().trim();

            if (mp.equals("")) break;

            try {
                BigDecimal bd = new BigDecimal(mp);

                if (bd.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("minimalPoint должен быть больше 0");
                    continue;
                }

                minimalPoint = bd.longValue();
                break;

            } catch (NumberFormatException e) {
                System.out.println("Ожидалось целое число (Long), большее 0");
            }
        }

        Difficulty difficulty = null;
        while (true) {
            System.out.print("Введите difficulty " + Arrays.toString(Difficulty.values()) + " (пустая строка = null):");
            String diffStr = sc.nextLine();

            if (diffStr.equals("")) break;

            try {
                difficulty = Difficulty.valueOf(diffStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение. Доступные: " + Arrays.toString(Difficulty.values()) + "или null");
            }
        }

        String authorName;
        Person author = null;
        while (true) {
            System.out.print("Введите имя автора (если строка пустая, то author = null):");
            authorName = sc.nextLine().trim();

            if (authorName.equals("")) {
                author = null;
                break;
            }

            if (authorName.isEmpty()) {
                System.out.println("Имя автора не может быть пустым");
                continue;
            }
            break;
        }

        if (!authorName.equals("")) {

            double height;
            while (true) {
                System.out.print("Введите height (> 0):");
                try {
                    String input = sc.nextLine();
                    BigDecimal bd = new BigDecimal(input);

                    if (bd.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Рост должен быть большее 0");
                        continue;
                    }

                    height = bd.doubleValue();
                    break;

                } catch (NumberFormatException e) {
                    System.out.println("Ожидалось число (double)");
                }
            }
            String passportID;
            while (true) {
                System.out.print("Введите passportID (пустая строка = null):");
                passportID = sc.nextLine().trim();

                if (passportID.equals("")) {
                    passportID = null;
                    break;
                }

                if (passportID.length() < 10) {
                    System.out.println("passportID должен быть не менее 10 символов");
                    continue;
                }

                break;
            }

            Color hairColor = null;
            while (true) {
                System.out.print("Введите hairColor " + Arrays.toString(Color.values()) + " (пустая строка = null):");
                String hair = sc.nextLine().trim();

                if (hair.equals("")) break;

                try {
                    hairColor = Color.valueOf(hair);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверное значение. Доступные: " + Arrays.toString(Color.values()) + "или null");
                }
            }

            Integer lx;
            while (true) {
                System.out.print("Введите location.x:");
                try {
                    lx = Integer.valueOf(sc.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ожидалось целое число (Integer)");
                }
            }

            Float ly;
            while (true) {
                System.out.print("Введите location.y:");
                try {
                    ly = Float.valueOf(sc.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ожидалось число (Float)");
                }
            }

            String lname;
            while (true) {
                System.out.print("Введите location.name (пустая строка = null):");
                lname = sc.nextLine().trim();

                if (lname.equals("")) {
                    lname = null;
                    break;
                }

                if (lname.isEmpty()) {
                    System.out.println("Имя не может быть пустым");
                    continue;
                }
                break;
            }
            Location location = new Location(lx, ly, lname);
            author = new Person(authorName, height, passportID, hairColor, location);
        }

        return new LabWork(
                0L,
                name,
                coordinates,
                null,
                minimalPoint,
                difficulty,
                author
        );
    }
}
