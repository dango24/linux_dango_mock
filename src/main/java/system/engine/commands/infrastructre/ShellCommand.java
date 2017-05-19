package system.engine.commands.infrastructre;

import system.engine.Shell;
import system.exceptions.IllegalOperationException;
import system.exceptions.WriteFailureException;

import java.util.Collections;
import java.util.List;

/**
 * Created by Dan on 3/20/2017.
 */
public abstract class ShellCommand {

    // Fields
    protected Shell shell;
    protected List<String> params;

    // Constructor
    public ShellCommand(Shell shell) {
        this(shell, Collections.emptyList());
    }

    public ShellCommand(Shell shell, List<String> params) {
        this.params = params;
        this.shell = shell;
    }

    public abstract void execute() throws IllegalOperationException, WriteFailureException;
}
