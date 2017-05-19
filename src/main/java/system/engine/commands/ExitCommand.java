package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.exceptions.IllegalOperationException;

/**
 * Created by Dan on 4/30/2017.
 */
public class ExitCommand extends ShellCommand {

    public ExitCommand(Shell shell) {
        super(shell);
    }

    @Override
    public void execute() throws IllegalOperationException {
        shell.terminate();
        System.out.println("logout");
        System.exit(0);
    }
}
