package at.oculus.teamf.technical.loggin;

/**
 * Created by Fabian on 31.03.2015.
 */
import org.apache.logging.log4j.Logger;

public class testing {

    final static Logger logger = LoggerBroker.getInstance();

    public static void main(final String... args) {



        logger.trace("trace message test");
        logger.debug("debug message test");
        logger.info("info message test");
        logger.warn("warn message test");
        logger.error("error message test");
        logger.fatal("fatal message test");


    }
}
