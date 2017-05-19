package system.engine;


import system.engine.commands.infrastructre.CommandType;
import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.exceptions.IllegalOperationException;
import system.exceptions.NoSuchCommandException;
import system.exceptions.WriteFailureException;

import java.util.List;

/**
 * Created by Dan on 3/26/2017.
 */
public interface Shell extends DirectoryOperations {
    void run(); // init and run the shell
    void terminate();
    void saveFileEntity(FileSystemEntity newDir);
    void executeCommand(CommandType commandType, List<String> params) throws IllegalOperationException, NoSuchCommandException, WriteFailureException;
    String getEnvironmentVar(String environmentKey);
    void setEnvironmentVar(String key, String val) throws WriteFailureException;
}
