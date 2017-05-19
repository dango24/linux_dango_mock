package system.exceptions;

/**
 * Created by Dan on 3/20/2017.
 */
public class NoSuchCommandException extends Exception {

    public NoSuchCommandException() {
        super("command not found");
    }
}
