package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.entities.Directory;
import system.exceptions.IllegalOperationException;
import system.utils.Utils;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static system.utils.Utils.HOME_DIR_PATH;
import static system.utils.Utils.ROOT_DIR_PATH;

/**
 * Created by Dan on 4/30/2017.
 */
public class PwdCommand extends ShellCommand {

    public PwdCommand(Shell shell) {
        super(shell);
    }

    @Override
    public void execute() throws IllegalOperationException {
        Directory currentDir = shell.getWorkingDirectory();
        System.out.println(getDirPath(currentDir));
    }

    private String getDirPath(Directory directory) {
        LinkedList<String> pathDirs = new LinkedList<>();

        while (!(directory.getName().equals(ROOT_DIR_PATH) || directory.getName().equals(HOME_DIR_PATH))) {
            pathDirs.addFirst(directory.getName());
            directory = directory.getParent();
        }

        pathDirs.addFirst(directory.getName());

        return "/" + pathDirs.stream()
                             .collect(Collectors.joining("/"));
    }
}
