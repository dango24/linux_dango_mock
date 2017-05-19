package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.exceptions.IllegalOperationException;

import java.util.List;

/**
 * Created by Dan on 3/20/2017.
 */
public class MkdirCommand extends ShellCommand {

    public MkdirCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    @Override
    public void execute() throws IllegalOperationException {
        String dirName;
        Directory newDir;

        if (params == null) {
            throw new IllegalOperationException("params are corrupt");
        } else if (params.isEmpty()) {
            throw new IllegalOperationException("mkdir: missing operand");
        }

        dirName = params.get(0);
        Directory workingDirectory = shell.getWorkingDirectory();

        if (workingDirectory.findDirectory(dirName) != null) {
            throw new IllegalOperationException(String.format("mkdir: cannot create directory '%s': File exists", dirName));
        }

        newDir = new Directory(dirName, workingDirectory);
        workingDirectory.addFileSystemEntity(newDir);
        shell.saveFileEntity(newDir);
    }
}
