package system.engine.commands.infrastructre;

import system.engine.Shell;
import system.exceptions.NoSuchCommandException;

import java.util.List;

/**
 * Created by Dan on 3/20/2017.
 */
public class CommandFactory {

    // Fields
    private Shell shell;

    // Constructor
    public CommandFactory(Shell shell) {
        this.shell = shell;
    }

    // Methods
    public ShellCommand createCommand(CommandType commandType, List<String> params) throws NoSuchCommandException {
        return commandType.createCommand(shell, params);
    }
}
