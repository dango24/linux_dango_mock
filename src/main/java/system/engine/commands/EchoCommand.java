package system.engine.commands;

import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.exceptions.IllegalOperationException;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Dan on 5/1/2017.
 */
public class EchoCommand extends ShellCommand {

    public EchoCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    @Override
    public void execute() throws IllegalOperationException {
        if (params == null) {
            throw new IllegalOperationException("Params are not valid");
        } else if (params.isEmpty()) {
            System.out.println("");
            return;
        }

        System.out.println(
            params.stream()
                  .map(this::paramEcho)
                  .collect(Collectors.joining(" "))
        );
    }

    private String paramEcho(String param) {
        String environmentVal = shell.getEnvironmentVar(param);
        return environmentVal != null ? environmentVal : param;
    }
}
