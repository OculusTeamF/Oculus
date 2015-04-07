package at.oculus.teamf.technical.loggin;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoggerBroker {
    private static final Logger loggerInstance = LogManager.getLogger(LoggerBroker.class);

    public static Logger getInstance() {
        return loggerInstance;
    }

    private LoggerBroker() {
        //...
    }

    public Logger getLogger() {
        return null;
    }
}
