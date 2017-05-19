import system.engine.CentosOS;
import system.engine.LinuxOS;
import system.exceptions.InitFailureException;
import system.exceptions.ReadFailureException;
import system.exceptions.WriteFailureException;

/**
 * Created by Dan on 4/25/2017.
 */
public class App {

    public static void main(String[] args) throws InitFailureException, ReadFailureException, WriteFailureException {
        LinuxOS linuxOS = new CentosOS();
        linuxOS.init();
    }
}
