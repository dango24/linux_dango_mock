package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.exceptions.IllegalOperationException;
import system.exceptions.NoSuchDirectoryException;
import system.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dan on 4/30/2017.
 */
public class CatCommand extends ShellCommand {

    public CatCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    @Override
    public void execute() throws IllegalOperationException {
        if (params == null) {
            throw new IllegalOperationException("Params are not valid");
        } // todo params.isEmpty, params.size() > 1

        try {
            concatenateFile();

        } catch (IOException e) {
            System.out.println("Failed to open file: " + e.getMessage());
        }

    }

    private void concatenateFile() throws IOException {
        FileSystemEntity fileSystemEntity = shell.getWorkingDirectory().findFileSystemEntity(params.get(0));

        if (Directory.isDirectory.test(fileSystemEntity)) {
            throw new NoSuchDirectoryException("cat: " + params.get(0) + ": is a directory");
        }

        List<String> fileContent = FileUtils.readInputFile(fileSystemEntity.getConcreteFile());
        fileContent.forEach(System.out::println);
    }
}
