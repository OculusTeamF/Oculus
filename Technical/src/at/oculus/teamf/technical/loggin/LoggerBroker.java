package at.oculus.teamf.technical.loggin;

/**
 * Created by Norskan on 30.03.2015.
 */
public class LoggerBroker {

    //<editor-fold desc="Singelton">
    private static LoggerBroker ourInstance = new LoggerBroker();

    public static LoggerBroker getInstance() {
        return ourInstance;
    }

    private LoggerBroker() {
    }
    //</editor-fold>

    public Logger getLogger() {
        return null;
    }
}
