package Commands;
import LabWorks.*;
import Managers.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Команда {@code add} добавляет новый элемент {@link LabWork} в коллекцию.
 *
 * <p>Реализует интерактивный ввод данных через консоль:
 * пользователь последовательно вводит все поля объекта.</p>
 *
 * <p>В процессе ввода выполняется валидация данных.</p>
 */

public class AddCommand implements Command, Serializable {

    CollectionManager cm;

    public AddCommand(CollectionManager cm) {
        this.cm = cm;
    }

    public String getName() {
        return "add";
    }

    public String getDescription() {
        return "Добавляет новый элемент в коллекцию";
    }


    /**
     * Выполняет команду добавления.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Проверяет отсутствие аргументов</li>
     *     <li>Запрашивает у пользователя значения полей</li>
     *     <li>Проводит валидацию каждого поля</li>
     *     <li>Создаёт объект {@link LabWork}</li>
     *     <li>Добавляет его в {@link CollectionManager}</li>
     * </ol>
     *
     * <p>Особенности:
     * <ul>
     *     <li>Поддерживает {@code null} для некоторых полей (minimalPoint, difficulty, author)</li>
     *     <li>Использует {@link BigDecimal} для точной проверки чисел</li>
     *     <li>Повторяет ввод до получения корректного значения</li>
     * </ul>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        LabWork lw = (LabWork) arg;

        LabWork newLabWork = new LabWork(
                cm.generateId(),
                lw.getName(),
                lw.getCoordinates(),
                null,
                lw.getMinimalPoint(),
                lw.getDifficulty(),
                lw.getAuthor()
        );

        cm.add(newLabWork);

        return "Элемент добавлен";
    }
}
