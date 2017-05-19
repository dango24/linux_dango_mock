package system.engine;

import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.entities.User;
import system.exceptions.ReadFailureException;
import system.exceptions.WriteFailureException;
import system.utils.FileUtils;

import java.util.List;
import java.util.Map;


/**
 * Created by Dan on 3/20/2017.
 */
public abstract class LinuxOS implements DirectoryOperations {

    // Fields
    protected Shell shell;
    protected Directory rootDirectory;
    protected List<User> users;
    protected Map<String, String> environmentalVars;

    protected LinuxOS() throws ReadFailureException {
        environmentalVars = FileUtils.readEnvironmentalVars();
    }

    // Abstract methods
    public abstract void init();
    public abstract void saveFileEntity(FileSystemEntity fileSystemEntity);

    // Methods
    public String getEnvironmentVar(String environmentKey) {
        return environmentalVars.get(environmentKey);
    }

    public void setEnvironmentVar(String key, String val) throws WriteFailureException {
        environmentalVars.put(key, val);
        FileUtils.saveEnvironmentVar(environmentalVars);
    }
}
