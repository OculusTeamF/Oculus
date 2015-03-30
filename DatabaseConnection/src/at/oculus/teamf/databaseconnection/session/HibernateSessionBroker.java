package at.oculus.teamf.databaseconnection.session;

import java.util.Collection;

/**
 * Broker that deals HibernateSessions to the top layer.
 *
 * TODO: Implement and docs
 * @author Simon Angerer
 * @date 30.3.2015
 */
public class HibernateSessionBroker implements ISessionBroker {


    public HibernateSessionBroker (Collection<Class> clazzes) {

    }

    /**
     * Creates a new at.oculus.teamf.databaseconnection.session.HibernateSession and retuns it to the caller. Caching is handeld internally!
     * @return a new SessionHibernateSession
     */
    @Override
    public ISession getSession() {
        return null;
    }

    /**
     * releases the Session back to the broker. Note after relessing the session it can be closed or dealt
     * to an other object. So it should not be use again use getSession() to request a new Session
     * @param session a session that is no longer needed.
     */
    @Override
    public void releaseSession(ISession session) {

    }
}
