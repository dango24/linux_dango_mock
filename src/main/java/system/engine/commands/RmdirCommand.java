package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.entities.Directory;
import system.exceptions.IllegalOperationException;
import system.exceptions.WriteFailureException;

import java.util.List;

import static system.utils.Utils.getInnerDirectoryFromDirsPath;
import static system.utils.Utils.separateConcatDirPathToList;

/**
 * Created by Dan on 5/3/2017.
 */
public class RmdirCommand extends ShellCommand {

    // Constants
    private final static String DELETE_INNER_CONTENT = "-rf";

    // Constructor
    public RmdirCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    // Methods

    @Override
    public void execute() throws IllegalOperationException, WriteFailureException {
        if (params == null) {
            throw new IllegalOperationException("Params are not valid");
        } else if (params.isEmpty()) {
            throw new IllegalOperationException("rmdir: missing operand");
        }

        removeDirectory();
    }

    public void removeDirectory() throws IllegalOperationException {
        int dirPathLocation = 0;
        boolean deleteInnerContent = false;
        List<String> removableDirPath;

        if (params.get(0).equals(DELETE_INNER_CONTENT)) {
            deleteInnerContent = true;
            dirPathLocation = 1;
        }

        if (params.size() < dirPathLocation) {
            throw new IllegalOperationException("You suck: Try help for more information");
        }

        removableDirPath = separateConcatDirPathToList(params.get(dirPathLocation));
        Directory directoryToRemove = getInnerDirectoryFromDirsPath(shell.getWorkingDirectory(), removableDirPath);

        if (!deleteInnerContent && !directoryToRemove.isEmpty()) {
            throw new IllegalOperationException("rmdir: failed to remove '" + directoryToRemove.getName() + "': Directory not empty");
        }

        directoryToRemove.delete();
    }
}
