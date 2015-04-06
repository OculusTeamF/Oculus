package at.oculus.teamf.technical.loggin;

/**
 * Created by Fabian on 31.03.2015.
 */

public class testing implements ILogger {

    public static void main(final String... args) {

        // test logging messages levels and pattern
        logger.trace("trace message test");
        logger.debug("debug message test");
        logger.info("info message test");
        logger.warn("warn message test");
        logger.error("error message test");
        logger.fatal("fatal message test");

        System.out.println(logger.getName());
    }
}
