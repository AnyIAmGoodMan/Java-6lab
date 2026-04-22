package Commands;

import LabWorks.*;
import Managers.*;

/**
 * Команда {@code history} выводит последние выполненные команды.
 */
public class HistoryCommand implements Command {

    private CommandManager cmm;

    public HistoryCommand(CommandManager cmm) {
        this.cmm = cmm;
    }

    public String getName() {
        return "history";
    }

    public String getDescription() {
        return "Выводит последние 14 команд";
    }

    /**
     * Выводит историю команд.
     *
     * <p>История хранится в {@link CommandManager#history}
     * и содержит последние выполненные команды (до 14).</p>
     *
     * @param arg аргумент команды (должен быть {@code null})
     */
    public String execute(Object arg) {
        if (arg != null) {
            return "Команда history не принимает аргументы";
        }

        if (cmm.history.isEmpty()) {
            return "История пуста\n";
        }

        StringBuilder sb = new StringBuilder();

        for (String commandName : cmm.history) {
            sb.append(commandName).append("\n");
        }

        return sb.toString();
    }
}
