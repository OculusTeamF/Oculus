/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Broker that deals HibernateSessions to the top layer. Aquire a {@code #HibernateSession} through {@code
 * #getSession()} it again through {@code #releaseSession()}
 *
 * @author Simon Angerer
 * @date 30.3.2015
 * @version 1.0
 */
public class HibernateSessionBroker implements ISessionBroker {

    private Collection<Class> _clazzes;

    private SessionFactory _sessionFactory;

    private EntityManagerFactory _entityManagerFactory;

    /**
     * Creates a new {@code #HibernateSessionBroker}
     *
     * @param clazzes annotated classes to configure the hibernate session factory
     */
    public HibernateSessionBroker(Collection<Class> clazzes) {
        Configuration configuration = new Configuration();

        HibernateProperties ph = new HibernateProperties("config.properties");

        configuration.setProperty("hibernate.connection.url", ph.getURL());
        configuration.setProperty("hibernate.connection.driver_class", ph.getDriver());
        configuration.setProperty("hibernate.connection.username", ph.getUser());
        configuration.setProperty("hibernate.connection.password", ph.getPassword());

        _clazzes = clazzes;
        for (Class clazz : clazzes) {
            configuration.addAnnotatedClass(clazz);
        }

        ServiceRegistry serviceRegistry =
                new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        _sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        //reason for the double connection!
        Map<String, Object> configOverrides = new HashMap<String, Object>();
        configOverrides.put("hibernate.connection.url", ph.getURL());
        configOverrides.put("hibernate.connection.driver_class", ph.getDriver());
        configOverrides.put("hibernate.connection.username", ph.getUser());
        configOverrides.put("hibernate.connection.password", ph.getPassword());
        _entityManagerFactory = Persistence.createEntityManagerFactory("oculus_f",configOverrides);
    }

    /**
     * Creates a new {@code #HibernateSession} and retuns it to the caller. Caching is handeld internally!
     *
     * @return a new SessionHibernateSession
     */
    @Override
    public ISession getSession() {
        return new HibernateSession(_entityManagerFactory.createEntityManager(), _sessionFactory.openSession(), _clazzes);
    }

    /**
     * Releases the Session back to the broker. Note after relessing the session it can be closed or dealt to an other
     * object. So it should not be use again use getSession() to request a new Session
     *
     * @param session a session that is no longer needed.
     */
    @Override
    public void releaseSession(ISession session) {

        ((ISessionClosable) session).close();
    }
}
