package at.oculus.teamf.technical.loggin;

/**
 * Created by Fabian on 31.03.2015.
 */
public class testing implements at.oculus.teamf.technical.loggin.Logger{

    //final static Logger logger = LoggerBroker.getInstance();

    public static void main(final String... args) {



        logger.trace("trace message test");
        logger.debug("debug message test");
        logger.info("info message test");
        logger.warn("warn message test");
        logger.error("error message test");
        logger.fatal("fatal message test");


    }
}
