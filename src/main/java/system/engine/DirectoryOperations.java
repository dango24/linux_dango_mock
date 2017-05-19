package system.engine;

import system.entities.Directory;
import system.exceptions.WriteFailureException;

/**
 * Created by Dan on 4/7/2017.
 */
public interface DirectoryOperations {
    void setToHomeDirectory() throws WriteFailureException;
    void setWorkingDirectory(Directory directory) throws WriteFailureException;
    void setToPrevWorkingDirectory();
    Directory getWorkingDirectory();
}
