package Commands;

import LabWorks.*;
import Managers.*;

import Managers.CollectionManager;

/**
 * Команда {@code filter_contains_name} выводит элементы коллекции,
 * имя которых содержит указанную подстроку.
 */
public class FilterContainsName implements Command {

    CollectionManager cm;

    public FilterContainsName(CollectionManager cm){
        this.cm = cm;
    }

    /**
     * Выполняет фильтрацию элементов по подстроке в имени.
     *
     * <p>Если аргумент отсутствует — выводит сообщение об ошибке.
     * Иначе вызывает {@link CollectionManager#filterInName(String)}
     * и выводит результат.</p>
     *
     * @param arg подстрока для поиска (не может быть {@code null})
     */
    public String execute(Object arg) {
        if (arg == null || ((String)arg).trim().isEmpty()) {
            return "Введите строку";
        }

        return cm.filterInName(((String) arg).trim()).toString();
    }

    public String getName(){
        return "filter_contains_name";
    }

    public String getDescription(){
        return "Выводит все элементы, имя которых содержит подстроку";
    }
}
