package Commands;

import LabWorks.*;
import Managers.*;

import Managers.CollectionManager;

import java.time.format.DateTimeFormatter;

/**
 * Команда {@code info} выводит информацию о коллекции.
 */
public class InfoCommand implements Command {

    CollectionManager manager;

    public InfoCommand(CollectionManager cm) {
        this.manager = cm;
    }

    /**
     * Выводит информацию о коллекции:
     * тип, дату создания и количество элементов.
     *
     * <p>Дата форматируется с помощью {@link DateTimeFormatter}.</p>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        return (
                "Тип коллекции - ArrayDeque\n" +
                        "Время создания - " + manager.getTime().format(formatter) + "\n" +
                        "Количество элементов в коллекции - " + manager.size() + "\n"
        );
    }

    public String getName(){
        return "info";
    }

    public String getDescription() {
        return "Показывает тип коллекции, время ее создания и количество элементов в ней";
    }
}
