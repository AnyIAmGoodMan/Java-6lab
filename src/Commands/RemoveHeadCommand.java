package Commands;
import LabWorks.*;
import Managers.*;

import Managers.CollectionManager;

/**
 * Команда {@code remove_head} удаляет первый элемент коллекции.
 */
public class RemoveHeadCommand implements Command {

    CollectionManager cm;

    public RemoveHeadCommand(CollectionManager cm) {
        this.cm = cm;
    }

    /**
     * Удаляет и выводит первый элемент коллекции.
     *
     * <p>Если коллекция пуста — выводит сообщение.</p>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        if (cm.size() == 0){
            return "Коллекция пуста\n";
        } else {
            cm.setModified(true);
            return "Элемент " + cm.removeHead() + " удален\n";
        }
    }

    public String getName() {
        return "remove_head";
    }

    public String getDescription() {
        return "Удаляет первый элемент коллекции";
    }
}
