package at.oculus.teamf.databaseconnection.session;

/**
 * SessionBroker interface to abstract a broker to deal sessions that implement the ISession interface.
 * @author Simon Angerer
 * @date 30.3.2015
 */
public interface ISessionBroker {

    /**
     * Creates a new Session and retuns it to the caller. Caching is handeld internally!
     * @return a new Session
     */
    ISession getSession();

    /**
     * releases the Session back to the broker. Note after relessing the session it can be closed or dealt
     * to an other object. So it should not be use again use getSession() to request a new Session
     * @param session a session that is no longer needed.
     */
    void releaseSession(ISession session);
}
