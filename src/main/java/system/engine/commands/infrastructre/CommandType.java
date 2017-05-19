package system.engine.commands.infrastructre;

import system.engine.Shell;
import system.engine.commands.*;

import java.util.List;

/**
 * Created by Dan on 3/21/2017.
 */
public enum  CommandType {

    // Types

    LS("ls") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new LsCommand(shell, params);
        }
    },
    CD("cd") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new CdCommand(shell, params);
        }
    },
    MKDIR("mkdir") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new MkdirCommand(shell, params);
        }
    },
    RMDIR("rmdir") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new RmdirCommand(shell, params);
        }
    },
    RM("rm") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new RmdirCommand(shell, params);
        }
    },
    CAT("cat") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new CatCommand(shell, params);
        }
    },
    PWD("pwd") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new PwdCommand(shell);
        }
    },
    EXIT("exit") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new ExitCommand(shell);
        }
    },
    ECHO("echo") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new EchoCommand(shell, params);
        }
    },
    WHICH("which") {
        @Override
        ShellCommand createCommand(Shell shell, List<String> params) {
            return new WhichCommand(shell, params);
        }
    };

    // Fields
    private String name;

    // Constructor
    CommandType(String name) {
        this.name = name;
    }

    // Methods
    abstract ShellCommand createCommand(Shell shell, List<String> params);

    @Override
    public String toString() {
        return name;
    }
}