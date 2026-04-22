package Network;
import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private Object argument;

    public Request(String commandName, Object argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object getArgument() {
        return argument;
    }
}
