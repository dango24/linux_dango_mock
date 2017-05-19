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
 * Created by Dan on 3/20/2017.
 */
public class CdCommand extends ShellCommand {

    // Constants
    private final static String PREV_DIRECTORY_SYMBOL = "-";

    // Constructor
    public CdCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    // Methods

    @Override
    public void execute() throws IllegalOperationException, WriteFailureException {
        if (params == null) {
            throw new IllegalOperationException("Params are not valid");
        } else if (params.isEmpty()) {
            shell.setToHomeDirectory();
        } else if (params.get(0).equals(PREV_DIRECTORY_SYMBOL)) {
            shell.setToPrevWorkingDirectory();
        } else {
            changeToRequestedDir(shell.getWorkingDirectory(), separateConcatDirPathToList(params.get(0)));
        }
    }

    public void changeToRequestedDir(Directory workingDirectory, List<String> directoriesPath) throws WriteFailureException {
        Directory nextDirectory = getInnerDirectoryFromDirsPath(workingDirectory, directoriesPath);
        shell.setWorkingDirectory(nextDirectory);
    }
}
