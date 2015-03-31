package at.oculus.teamf.databaseconnectiontests;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.HibernateSessionBroker;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.ISessionBroker;
import at.oculus.teamf.databaseconnectiontests.entity.PatientEntity;
import at.oculus.teamf.databaseconnectiontests.entity.UserEntity;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 31.03.2015.
 */
public class HibernateSessionBrokerTest extends TestCase {


    public void testGetSession() throws Exception {
        Collection<Class> clazzes = new LinkedList<Class>();
        clazzes.add(PatientEntity.class);
        clazzes.add(UserEntity.class);

        ISessionBroker broker = new HibernateSessionBroker(clazzes);
        ISession session = broker.getSession();

        if(session == null) {
            fail("No session was acquired!");
        }
    }

    public void testReleaseSession() throws Exception {
        Collection<Class> clazzes = new LinkedList<Class>();
        clazzes.add(PatientEntity.class);
        clazzes.add(UserEntity.class);

        ISessionBroker broker = new HibernateSessionBroker(clazzes);
        ISession session = broker.getSession();
        broker.releaseSession(session);

        try {
            session.commit();
        } catch (BadSessionException e) {
            assertTrue(true);
        }
    }
}