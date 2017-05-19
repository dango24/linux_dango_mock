package system.exceptions;

/**
 * Created by Dan on 4/25/2017.
 */
public class InitFailureException extends Exception {

    public InitFailureException(String message) {
        super("Failed to init Shell: " + message);
    }
}
