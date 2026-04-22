package Commands;
import LabWorks.*;
import Managers.*;

import Managers.XmlWriter;

/**
 * Команда {@code save} сохраняет текущую коллекцию в XML файл.
 */
public class SaveCommand implements Command {

    XmlWriter xw;
    CollectionManager cm;

    public SaveCommand(XmlWriter xw, CollectionManager cm) {
        this.xw = xw;
        this.cm = cm;
    }

    /**
     * Выполняет сохранение коллекции.
     *
     * <p>Проверяет отсутствие аргументов и вызывает {@link XmlWriter#write()}.</p>
     *
     * <p>В случае ошибки выводит сообщение.</p>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        String argStr = (String) arg;
        if (argStr != null && !argStr.isEmpty()) {
            return "Команда save не принимает аргументы";
        }

        try {
            xw.write();
            cm.setModified(false);
            return "Коллекция сохранена";
        } catch (Exception e) {
            return "Коллекция не сохранена: " + e.getMessage();
        }
    }

    public String getName(){
        return "save";
    }

    public String getDescription(){
        return "Записывает в указанный файл коллекцию";
    }
}
