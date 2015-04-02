/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnectiontests;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.HibernateSessionBroker;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.ISessionBroker;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Hibernate {@code #HibernateSessionBrokerTest} class.
 *
 * @author Simon Angerer
 * @date 01.04.2015
 */
public class HibernateSessionBrokerTest extends TestCase {

	/**
	 * Tests if a {@code #HibernateSession} can be acquried an dused
	 * 1. Aquire HibernateSession {@code #HibernateSession from {@code #HibernateSessionBroker}
	 * 2. Test if a {@code #HibernateSession} was acquired
	 */
	public void testGetSession() throws Exception {
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);

		ISessionBroker broker = new HibernateSessionBroker(clazzes);
		ISession session = broker.getSession();

		if (session == null) {
			fail("No session was acquired!");
		}
	}

	/**
	 * Tests if a {@code #HibernateSession} cannot be used again after it was released
	 * 1. Aquire HibernateSession {@code#HibernateSession} from {@code #HibernateSessionBroker}
	 * 2. Test if a {@code #HibernateSession} was aquired
	 * 3. Release the {@code #HibernateSession}
	 * 4. Test if the Hibernate session can not be use after release
	 */
	public void testReleaseSession() throws Exception {
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);


		ISessionBroker broker = new HibernateSessionBroker(clazzes);
		ISession session = broker.getSession();
		broker.releaseSession(session);

		try {
			session.getByID(PatientEntity.class, 0);
		} catch (BadSessionException e) {
			assertTrue(true);
		}
	}
}