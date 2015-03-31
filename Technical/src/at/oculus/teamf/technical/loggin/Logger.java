package at.oculus.teamf.technical.loggin;

public interface Logger {
    final static org.apache.logging.log4j.Logger logger = LoggerBroker.getInstance();
}
