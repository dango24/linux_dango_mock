package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.exceptions.IllegalOperationException;
import system.utils.FileUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dan on 5/1/2017.
 */
public class WhichCommand extends ShellCommand {

    public WhichCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    @Override
    public void execute() throws IllegalOperationException {
        if (params == null || params.isEmpty()) {
            throw new IllegalOperationException("Params are not valid");
        }

        List<String> commandsLocation = Arrays.asList(shell.getEnvironmentVar("$PATH").split(":"));
        params.forEach(command -> System.out.println(getCommandLocation(command, commandsLocation)));
    }

    private String getCommandLocation(String command, List<String> commandsLocation) {

        for (String commandLocation : commandsLocation) {
            List<String> potentialLocation = FileUtils.convertPathToCommands(commandLocation);

            if (potentialLocation.contains(command)) {
                return commandLocation + "/" + command;
            }
        }

        return "no " + command + " in " + shell.getEnvironmentVar("$PATH");
    }
}
