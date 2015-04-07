package at.oculus.teamf.technical.loggin;

public interface ILogger {
    final static org.apache.logging.log4j.Logger logger = LoggerBroker.getInstance();
}
