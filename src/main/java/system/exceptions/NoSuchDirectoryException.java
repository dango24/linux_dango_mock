package system.exceptions;

/**
 * Created by Dan on 3/20/2017.
 */
public class NoSuchDirectoryException extends RuntimeException {

    public NoSuchDirectoryException() {
        super("No such file or directory");
    }

    public NoSuchDirectoryException(String message) {
        super(message);
    }
}
