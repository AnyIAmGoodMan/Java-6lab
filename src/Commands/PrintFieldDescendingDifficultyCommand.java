package Commands;
import LabWorks.*;
import Managers.*;

import Managers.CollectionManager;

import java.util.Comparator;
import java.util.Objects;

/**
 * Команда {@code print_field_descending_difficulty} выводит значения
 * сложности элементов коллекции в порядке убывания.
 *
 * <p>Список формируется в {@link CollectionManager#PFDD()}.</p>
 */
public class PrintFieldDescendingDifficultyCommand implements Command {

    private CollectionManager cm;

    public PrintFieldDescendingDifficultyCommand(CollectionManager cm) {
        this.cm = cm;
    }

    public String getName() {
        return "print_field_descending_difficulty";
    }

    public String getDescription() {
        return "Выводит значения difficulty всех элементов в порядке убывания";
    }

    /**
     * Выводит значения {@link Difficulty} в порядке убывания.
     *
     * <p>Если список пуст — выводит сообщение.</p>
     *
     * @param arg аргумент команды (игнорируется)
     */
    public String execute(Object arg) {
        return cm.getCollection().stream()
                .map(LabWork::getDifficulty)
                .filter(Objects::nonNull)
                .sorted(Comparator.reverseOrder())
                .map(Object::toString)
                .reduce("", (a, b) -> a + b + "\n");
    }
}
