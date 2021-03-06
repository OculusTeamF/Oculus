/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.DomainFactory;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class DiagnosisBrokerTest extends BrokerTest {
	private ICalendar _calendar;
	private IDoctor _doctor;
	private IDiagnosis _diagnosis;

	@Override
	public void setUp() {
		_doctor = null;
		try {
			_doctor = Facade.getInstance().getById(IDoctor.class, 1);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_diagnosis = (IDiagnosis) DomainFactory.create(IDiagnosis.class);
		_diagnosis.setDoctor(_doctor);
		_diagnosis.setTitle("Diagnose Test");
		_diagnosis.setDescription("Testbeschreibung");
		try {
			assertTrue(Facade.getInstance().save(_diagnosis));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		assertTrue(_doctor.getId() > 0);
		assertTrue(_diagnosis.getId() > 0);
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_diagnosis));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		IDiagnosis diagnosis = null;
		try {
			diagnosis = Facade.getInstance().getById(IDiagnosis.class, _diagnosis.getId());
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(diagnosis != null);
		assertTrue(diagnosis.getId() > 0);
		assertTrue(diagnosis.getTitle().equals("Diagnose Test"));
		assertTrue(diagnosis.getDescription().equals("Testbeschreibung"));
		assertTrue(diagnosis.getDoctor().getId() == _doctor.getId());
	}

	@Override
	public void testGetAll() {
		Collection<IDiagnosis> diagnoses = null;
		try {
			diagnoses = Facade.getInstance().getAll(IDiagnosis.class);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(diagnoses != null);
		assertTrue(diagnoses.size() > 1);
	}

	@Test
	public void testSearchByPatient() {
		Collection<IDiagnosis> diagnoses = null;
		try {
			diagnoses = Facade.getInstance().search(IDiagnosis.class, "59");
		} catch (DatabaseOperationException | NoBrokerMappedException | SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(diagnoses.size() == 3);
	}

	@Test
	public void testReload() throws CouldNotGetMedicineException, CouldNotGetVisualAidException {
		// reload medicine, visual aid
		IDiagnosis diagnosis = null;
		try {
			diagnosis = Facade.getInstance().getById(IDiagnosis.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		Collection<IMedicine> medicine = null;
		Collection<IVisualAid> visualAid = null;
		medicine = diagnosis.getMedicine();
		visualAid = diagnosis.getVisualAid();
		System.out.println(medicine.size());
		System.out.println(visualAid.size());
		assertTrue(medicine.size() > 0);
		assertTrue(visualAid.size() > 0);
	}
}