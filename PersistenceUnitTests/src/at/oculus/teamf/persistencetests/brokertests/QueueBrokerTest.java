/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.DomainFactory;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.domain.entity.queue.QueueEntry;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static junit.framework.Assert.assertTrue;

public class QueueBrokerTest extends BrokerTest {
	private QueueEntry _newDoctorEntry;
	private QueueEntry _newOrthoptistEntry;
	private QueueEntry _newEntry;

	private IPatient patientOne;
	private IPatient patientTwo;
	private IPatient patientThree;

	private void generatePatient(String firstName, String svn) throws NoBrokerMappedException, BadConnectionException {
		IPatient patient = (IPatient) DomainFactory.create(IPatient.class);
		patient.setFirstName(firstName);
		patient.setLastName(firstName);
		patient.setSocialInsuranceNr(svn);
		patient.setEmail(firstName);


		try {
			Facade.getInstance().save(patient);
		} catch (DatabaseOperationException e) {
			assertTrue(false);
			e.printStackTrace();
		}

	}

	@Override
	public void setUp() {

		IDoctor doctor = null;
		IOrthoptist orthoptist = null;
		try {
			generatePatient("UnitTestPatient1", "999999991");
			generatePatient("UnitTestPatient2", "999999992");
			generatePatient("UnitTestPatient3", "999999993");

			Collection<IPatient> patients = Facade.getInstance().getAll(IPatient.class);
			patientOne = (IPatient) ((LinkedList<Object>) Facade.getInstance().search(IPatient.class, "UnitTestPatient1"))
					.get(0);
			patientTwo = (IPatient) ((LinkedList<Object>) Facade.getInstance().search(IPatient.class, "UnitTestPatient2"))
					.get(0);
			patientThree =
					(IPatient) ((LinkedList<Object>) Facade.getInstance().search(IPatient.class, "UnitTestPatient3"))
							.get(0);
			doctor = Facade.getInstance().getById(IDoctor.class, 1);
			orthoptist = Facade.getInstance().getById(IOrthoptist.class, 1);

		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(patientOne != null);
		assertTrue(patientTwo != null);
		assertTrue(patientThree != null);
		assertTrue(doctor != null);
		assertTrue(orthoptist != null);

		_newDoctorEntry = new QueueEntry(0, patientOne, doctor, 0, new Timestamp(new Date().getTime()));
		_newOrthoptistEntry = new QueueEntry(0, patientTwo, orthoptist, 0, new Timestamp(new Date().getTime()));
		_newEntry = new QueueEntry(0, patientThree, null, 0, new Timestamp(new Date().getTime()));

		try {
			assertTrue(Facade.getInstance().save(_newDoctorEntry));
			assertTrue(Facade.getInstance().save(_newOrthoptistEntry));
			assertTrue(Facade.getInstance().save(_newEntry));
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_newDoctorEntry));
			assertTrue(Facade.getInstance().delete(_newOrthoptistEntry));
			assertTrue(Facade.getInstance().delete(_newEntry));

			assertTrue(Facade.getInstance().delete((IDomain) ((LinkedList<Object>) Facade.getInstance()
			                                                                             .search(IPatient.class,
			                                                                                     "UnitTestPatient1"))
					.get(0)));
			assertTrue(Facade.getInstance().delete((IDomain) ((LinkedList<Object>) Facade.getInstance()
			                                                                             .search(IPatient.class,
			                                                                                     "UnitTestPatient2"))
					.get(0)));
			assertTrue(Facade.getInstance().delete((IDomain) ((LinkedList<Object>) Facade.getInstance()
			                                                                             .search(IPatient.class,
			                                                                                     "UnitTestPatient3"))
					.get(0)));
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	@Override
	public void testGetById() {
		Facade facade = Facade.getInstance();
		QueueEntry queueEntry = null;
		try {
			queueEntry = facade.getById(QueueEntry.class, _newEntry.getId());
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(queueEntry != null);
	}

	@Test
	@Override
	public void testGetAll() {
		Collection<QueueEntry> queueEntries = null;

		try {
			queueEntries = Facade.getInstance().getAll(QueueEntry.class);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(queueEntries != null);
		assertTrue(queueEntries.size() > 1);
	}

	@Test
	public void search() {
		try {
			Collection<QueueEntry> result = Facade.getInstance().search(QueueEntry.class, "Doctor", "1");
			assertTrue(result.size() > 0);
			result = Facade.getInstance().search(QueueEntry.class, "General");
			assertTrue(result.size() > 0);
			result = Facade.getInstance().search(QueueEntry.class, "Orthopist", "1");
			assertTrue(result.size() > 0);
		} catch (SearchInterfaceNotImplementedException | DatabaseOperationException | BadConnectionException | NoBrokerMappedException | InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}