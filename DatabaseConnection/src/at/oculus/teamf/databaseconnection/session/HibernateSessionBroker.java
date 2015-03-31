package at.oculus.teamf.databaseconnection.session;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Collection;

/**
 * Broker that deals HibernateSessions to the top layer.
 *
 * TODO: Implement and docs
 * @author Simon Angerer
 * @date 30.3.2015
 */
public class HibernateSessionBroker implements ISessionBroker {

    private SessionFactory _sessionFactory;

    public HibernateSessionBroker (Collection<Class> clazzes) {
        Configuration configuration = new Configuration();
        configuration.configure();

        for(Class clazz : clazzes) {
            configuration.addAnnotatedClass(clazz);
        }

        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        _sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * Creates a new {@code #HibernateSession} and retuns it to the caller. Caching is handeld internally!
     * @return a new SessionHibernateSession
     */
    @Override
    public ISession getSession() {
        return new HibernateSession(_sessionFactory.openSession());
    }

    /**
     * Releases the Session back to the broker. Note after relessing the session it can be closed or dealt
     * to an other object. So it should not be use again use getSession() to request a new Session
     * @param session a session that is no longer needed.
     */
    @Override
    public void releaseSession(ISession session) {

        ((ISessionClosable)session).close();
    }
}
