package Commands;
import LabWorks.*;
import Managers.*;

import Managers.CollectionManager;

import java.util.Comparator;

/**
 * Команда {@code show} выводит все элементы коллекции {@link LabWork}.
 */
public class ShowCommand implements Command {

    CollectionManager manager;

    public String getName(){
        return "show";
    }

    public String getDescription(){
        return "Показывает все LabWork-и в коллекции";
    }

    /**
     * Выводит все элементы коллекции.
     *
     * <p>Перебирает коллекцию из {@link CollectionManager}
     * и выводит строковое представление каждого элемента.</p>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        return manager.getCollection().stream()
                .sorted(Comparator.comparing(LabWork::getName))
                .map(LabWork::toString)
                .reduce("", (a, b) -> a + b + "\n");
    }

    public ShowCommand(CollectionManager cm){
        this.manager = cm;
    }
}
