/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnectiontests;

import at.oculus.teamf.databaseconnection.session.HibernateSessionBroker;
import at.oculus.teamf.databaseconnection.session.ISession;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * TODO: Create Tests
 *
 * @author Simon Angerer
 * @date 30.03.2015
 */
public class HibernateSessionTest extends TestCase {

	/**
	 * Test if a transaction can be started correctly
	 * 1. Acquire a {@code HibernateSession} from a {@code HibernateSessionBroker}
	 * 2. Start transaction
	 * 3. Release session to {@code HibernateSessionBroker}
	 *
	 * @throws Exception if an error occurs
	 */
	public void testBeginTransaction() throws Exception {
		//1.
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);

		HibernateSessionBroker hsb = new HibernateSessionBroker(clazzes);
		ISession session = hsb.getSession();

		//2.
		session.beginTransaction();

		//3.
		hsb.releaseSession(session);
	}


	/**
	 * Test if an transaction can be commited if nothing was changed
	 * 1. Acquire a {@code HibernateSession} from a {@code HibernateSessionBroker}
	 * 2. Start transaction
	 * 3. Commit empty transaction
	 * 4. Release session to {@code HibernateSessionBroker}
	 *
	 * @throws Exception if an error occurs
	 */
	public void testCommitNothing() throws Exception {
		//1.
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);

		HibernateSessionBroker hsb = new HibernateSessionBroker(clazzes);
		ISession session = hsb.getSession();

		//2.
		session.beginTransaction();

		//3.
		session.commit();

		//4.
		hsb.releaseSession(session);
	}

	/**
	 * Test if an transaction can be commited if a entity was saved in the DB
	 * 1. Acquire a {@code HibernateSession} from a {@code HibernateSessionBroker}
	 * 2. Start transaction
	 * 3. Save a entity into the database
	 * 4. Commit transaction
	 * 5. Start transaction
	 * 6. Delete a entity from the database
	 * 7. Commit transaction
	 * 8. Release session to {@code HibernateSessionBroker}
	 *
	 * @throws Exception if an error occurs
	 */
	public void testSaveCommitDeleteCommit() throws Exception {
		//1.
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);

		HibernateSessionBroker hsb = new HibernateSessionBroker(clazzes);
		ISession session = hsb.getSession();

		//2.
		assertTrue(session.beginTransaction());

		//3.
		PatientEntity patient = new PatientEntity();
		patient.setPatientId(500);
		patient.setFirstName("Tom");
		patient.setLastName("Turbo");
		patient.setGender("M");
		Serializable id = session.save(patient);

		//4.
		assertTrue(session.commit());

		//5.
		assertTrue(session.beginTransaction());

		//6.
		assertTrue(session.delete(patient));

		//7.
		assertTrue(session.commit());

		//8.
		hsb.releaseSession(session);
	}

	/**
	 * Test if an transaction can be reseted
	 * 1. Acquire a {@code HibernateSession} from a {@code HibernateSessionBroker}
	 * 2. Start transaction
	 * 3. Insert patient into database
	 * 4. Commit
	 * 5. Start transaction
	 * 6. Delete a entity from the database
	 * 7. Call rollback
	 * 8. Start transaction
	 * 9. Delete a entity from the database
	 * 11. Commit
	 * 10. Release session to {@code HibernateSessionBroker}
	 *
	 * @throws Exception if an error occurs
	 */
	public void testRollback() throws Exception {
		//1.
		Collection<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);

		HibernateSessionBroker hsb = new HibernateSessionBroker(clazzes);
		ISession session = hsb.getSession();

		//2.
		boolean started = session.beginTransaction();
		assertTrue(started);

		//3.
		PatientEntity patient = new PatientEntity();
		patient.setPatientId(500);
		patient.setFirstName("Tom");
		patient.setLastName("Turbo");
		patient.setGender("M");
		Serializable id = session.save(patient);

		//4.
		assertTrue(session.commit());

		//5.
		assertTrue(session.beginTransaction());

		//6.
		session.delete(patient);

		//7.
		assertTrue(session.rollback());

		//8.
		assertTrue(session.beginTransaction());

		//9.
		assertTrue(session.delete(patient));

		//10.
		assertTrue(session.commit());

		//11.
		hsb.releaseSession(session);
	}
}