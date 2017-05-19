package system.engine;

import system.engine.commands.infrastructre.CommandFactory;
import system.engine.commands.infrastructre.CommandType;
import system.engine.commands.infrastructre.ShellCommand;
import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.entities.User;
import system.exceptions.*;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static system.utils.FileUtils.getCommandsFromPath;
import static system.utils.Utils.createParams;
import static system.utils.Utils.getDirName;

/**
 * Created by Dan on 3/20/2017.
 */
public class CentosShell implements Shell {

    // Constants
    private final String USER_PROMPT = "[%s@%s %s]%s ";

    // Fields
    private LinuxOS linuxOS;
    private User connectedUser;
    private CommandFactory commandFactory;
    private Map<String, CommandType> commands;

    // Constructor
    public CentosShell(LinuxOS linuxOS, User connectedUser) throws InitFailureException, ReadFailureException {
        this.linuxOS = linuxOS;
        this.connectedUser = connectedUser;
        commands = buildCommandsType();
        commandFactory = new CommandFactory(this);
    }

    // Methods

    private Map<String,CommandType> buildCommandsType() {
        List<String> availableCommands = getCommandsFromPath(linuxOS.getEnvironmentVar("$PATH"));

        return Arrays.stream(CommandType.values())
                     .filter(commandType -> availableCommands.contains(commandType.toString()))
                     .collect(toMap(CommandType::toString, Function.identity()));
    }

    public void run() {
        String[] userInput;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print(printPrompt());
                userInput = scanner.nextLine().trim().split(" ");

                if (userInput.length > 0) {
                    executeCommand(userInput);
                }

            } catch (NoSuchDirectoryException | NoSuchCommandException | IllegalOperationException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.err.print(e.getMessage() + " " + e);
                System.exit(1);
            }
        }
    }

    @Override
    public void terminate() {
        // todo currently do nothing
    }

    @Override
    public void saveFileEntity(FileSystemEntity fileSystemEntity) {
        linuxOS.saveFileEntity(fileSystemEntity);
    }

    private void executeCommand(String[] userInput) throws NoSuchCommandException, IllegalOperationException, WriteFailureException {
        List<String> params;
        String command = userInput[0];

        if (!commands.containsKey(command)) {
            throw new NoSuchCommandException();
        }

        if (userInput.length > 1) {
            params = createParams(userInput);
        } else {
            params = Collections.emptyList();
        }

        executeCommand(commands.get(command), params);
    }

    @Override
    public void executeCommand(CommandType commandType, List<String> params) throws IllegalOperationException, NoSuchCommandException, WriteFailureException {
        ShellCommand shellCommand = commandFactory.createCommand(commandType, params);
        shellCommand.execute();
    }

    @Override
    public String getEnvironmentVar(String environmentKey) {
        return linuxOS.getEnvironmentVar(environmentKey);
    }

    @Override
    public void setEnvironmentVar(String key, String val) throws WriteFailureException {
        linuxOS.setEnvironmentVar(key, val);
    }

    @Override
    public Directory getWorkingDirectory() {
        return linuxOS.getWorkingDirectory();
    }

    @Override
    public void setToPrevWorkingDirectory() {
        linuxOS.setToPrevWorkingDirectory();
    }

    @Override
    public void setWorkingDirectory(Directory directory) throws WriteFailureException {
        linuxOS.setWorkingDirectory(directory);
    }

    @Override
    public void setToHomeDirectory() throws WriteFailureException {
        linuxOS.setToHomeDirectory();
    }

    private String printPrompt() {
        return String.format(USER_PROMPT, connectedUser.getUserName(), connectedUser.getHost(), getDirName(linuxOS.getWorkingDirectory()), getUserIndication(connectedUser));
    }

    private String getUserIndication(User connectedUser) {
        if (connectedUser.isAdmin()) {
            return "#";
        } else {
            return "$";
        }
    }
}
