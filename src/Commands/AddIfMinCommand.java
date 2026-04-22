package Commands;
import LabWorks.*;
import Managers.*;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Команда {@code add_if_min} добавляет новый элемент {@link LabWork}
 * в коллекцию, если он меньше минимального элемента в коллекции.
 *
 * <p>Сравнение выполняется через {@link LabWork#compareTo(LabWork)}.</p>
 *
 * <p>Если коллекция пуста — элемент добавляется без сравнения.</p>
 */

public class AddIfMinCommand implements Command {

    CollectionManager cm;

    public AddIfMinCommand(CollectionManager cm) {
        this.cm = cm;
    }
    public String getName() {
        return "add_if_min";
    }
    public String getDescription() {
        return "Добавляет элемент, если он меньше минимального в коллекции";
    }

    /**
     * Выполняет команду.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Проверяет, что аргументы отсутствуют</li>
     *     <li>Если коллекция пуста → вызывает {@link AddCommand}</li>
     *     <li>Иначе запрашивает данные нового элемента</li>
     *     <li>Находит минимальный элемент в коллекции</li>
     *     <li>Сравнивает новый элемент с минимальным</li>
     *     <li>Добавляет, если новый меньше</li>
     * </ol>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        LabWork lw = (LabWork) arg;

        LabWork newLW = new LabWork(
                cm.generateId(),
                lw.getName(),
                lw.getCoordinates(),
                null,
                lw.getMinimalPoint(),
                lw.getDifficulty(),
                lw.getAuthor()
        );

        LabWork min = cm.getCollection().stream()
                .min(LabWork::compareTo)
                .orElse(null);

        if (min == null || newLW.compareTo(min) < 0) {
            cm.add(newLW);
            return "Элемент добавлен";
        }

        return "Элемент не меньше минимального";
    }
}
