package Commands;
import LabWorks.*;
import Managers.*;
import Network.UpdateRequest;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Команда {@code update_id} обновляет элемент коллекции по указанному id.
 */
public class UpdateIdCommand implements Command {

    CollectionManager cm;

    public UpdateIdCommand(CollectionManager cm) {
        this.cm = cm;
    }

    public String getName() {
        return "update_id";
    }

    public String getDescription() {
        return "Обновляет элемент коллекции по id";
    }

    /**
     * Обновляет элемент коллекции.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Проверяет корректность id</li>
     *     <li>Ищет элемент в коллекции</li>
     *     <li>Запрашивает новые значения полей</li>
     *     <li>Удаляет старый элемент</li>
     *     <li>Создаёт и добавляет новый</li>
     * </ol>
     *
     * <p>Если элемент с указанным id не найден — выводит сообщение.</p>
     *
     * @param arg id элемента (не может быть {@code null})
     */
    public String execute(Object arg) {
        UpdateRequest req = (UpdateRequest) arg;

        Long id = req.getId();
        LabWork lw = req.getLabWork();

        boolean found = false;

        for (LabWork el : cm.getCollection()) {
            if (el.getId().equals(id)) {
                found = true;
                break;
            }
        }

        if (!found) {
            return "Элемента с таким id нет";
        }

        cm.removeById(id);

        LabWork newLabWork = new LabWork(
                id,
                lw.getName(),
                lw.getCoordinates(),
                null,
                lw.getMinimalPoint(),
                lw.getDifficulty(),
                lw.getAuthor()
        );

        cm.add(newLabWork);

        return "Элемент обновлен";
    }
}
