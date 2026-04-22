package Commands;

import LabWorks.*;
import Managers.*;

import java.util.Arrays;

/**
 * Команда {@code count_by_difficulty} подсчитывает количество элементов
 * {@link LabWork} в коллекции с заданным значением {@link Difficulty}.
 */
public class CountByDifficultyCommand implements Command {

    /**
     * Менеджер коллекции.
     */
    CollectionManager cm;

    /**
     * Создаёт команду.
     *
     * @param cm менеджер коллекции
     */
    public CountByDifficultyCommand(CollectionManager cm) {
        this.cm = cm;
    }

    /**
     * Возвращает имя команды.
     *
     * @return "count_by_difficulty"
     */
    public String getName() {
        return "count_by_difficulty";
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание
     */
    public String getDescription() {
        return "Подсчитывает количество элементов в коллекции с данной сложностью";
    }

    /**
     * Выполняет команду.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Проверяет наличие аргумента</li>
     *     <li>Преобразует аргумент в {@link Difficulty}</li>
     *     <li>Вызывает {@link CollectionManager#countByDifficulty(Difficulty)}</li>
     *     <li>Выводит результат</li>
     * </ol>
     *
     * @param arg строковое представление сложности (не должно быть {@code null})
     */
    public String execute(Object arg) {
        if (arg == null || ((String)arg).trim().isEmpty()) {
            return "Введите difficulty";
        }

        try {
            String val = ((String) arg).trim().toUpperCase();
            Difficulty d = Difficulty.valueOf(val);
            return String.valueOf(cm.countByDifficulty(d));
        } catch (Exception e) {
            return "Неверное значение difficulty";
        }
    }
}
