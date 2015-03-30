package at.oculus.teamf.databaseconnection.session;

/**
 * A session needs to implement this interface so it can be closed by a broker.
 * @author Simon Angerer
 * @date 30.3.2015
 */
interface ISessionClosable {

    /**
     * Closes a session.
     */
    void close();
}
