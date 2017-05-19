package system.engine;

import system.engine.commands.infrastructre.CommandType;
import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.entities.User;
import system.exceptions.*;
import system.utils.FileUtils;
import system.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static system.utils.FileUtils.*;
import static system.utils.Utils.*;

/**
 * Created by Dan on 4/26/2017.
 */
public class CentosOS extends LinuxOS {

    // Fields
    private Directory workingDirectory;
    private Directory prevWorkingDirectory;

    // Constructor
    public CentosOS() throws InitFailureException, ReadFailureException, WriteFailureException {
        initRootDirectory();
        users = FileUtils.readUsersFromFile();
        addUsersToHomeDir(users);
        shell = new CentosShell(this, getAdminUser(users));
    }

    // Methods

    @Override
    public void init() {
        shell.run();
    }

    @Override
    public void saveFileEntity(FileSystemEntity fileSystemEntity) {
        File newFile;
        File parentFile = fileSystemEntity.getParent().getConcreteFile();

        if (!parentFile.exists()) {
            new IllegalOperationException("No such directory: " + parentFile.toString());
        }

        newFile = new File(parentFile, fileSystemEntity.getName());
        newFile.mkdir();
        fileSystemEntity.setConcreteFile(newFile);
    }

    private void initRootDirectory() throws InitFailureException, ReadFailureException, WriteFailureException {
        Directory rootDir = readRootDir();

        if (rootDir != null) {
            rootDirectory = rootDir;
            initWorkingDirs();
        } else {
            rootDirectory = new Directory(ROOT_DIR_PATH);
            initWorkingDirs();
            buildRootDirectories();
        }
    }

    private void initWorkingDirs() {
        workingDirectory = rootDirectory;
        prevWorkingDirectory = workingDirectory;
    }

    private void addUsersToHomeDir(List<User> users) {
        Directory homeDir = rootDirectory.findDirectory("home");

        users.stream()
             .map(User::getUserName)
             .filter(userName -> homeDir.findDirectory(userName) == null) // user not exist at home directory
             .map(Utils::createHomeDirectory)
             .forEach(homeDir::addFileSystemEntity);
    }

    private void buildRootDirectories() throws InitFailureException, WriteFailureException {
        List<String> rootDirectoriesNames;

        try {
            rootDirectoriesNames = readInputFile(ROOT_DIRECTORIES);

            for (String dirName : rootDirectoriesNames) {
                shell.executeCommand(CommandType.MKDIR, Arrays.asList(dirName));
            }

        } catch (IOException | IllegalOperationException | NoSuchCommandException e) {
            throw new InitFailureException(e.getMessage());
        }
    }

    @Override
    public Directory getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public void setWorkingDirectory(Directory directory) throws WriteFailureException {
        prevWorkingDirectory = workingDirectory;
        workingDirectory = directory;
        setEnvironmentVar("$OLDPWD", getDirectoryPath(workingDirectory));
    }

    @Override
    public void setToHomeDirectory() throws WriteFailureException {
        Directory homeDir = getHomeDir(workingDirectory);
        setWorkingDirectory(homeDir);
    }

    @Override
    public void setToPrevWorkingDirectory() {
        Directory temp = workingDirectory;
        workingDirectory = prevWorkingDirectory;
        prevWorkingDirectory = temp;
    }

    private Directory getHomeDir(Directory directory) {

        if(directory.getName().equals(ROOT_DIR_PATH) || directory.getParent().getName().equals(HOME_DIR_PATH)) {
            return directory;
        } else {
            return getHomeDir(directory.getParent());
        }
    }
}
