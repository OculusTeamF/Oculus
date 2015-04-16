/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;

import java.util.Collection;

import static org.junit.Assert.*;

public class DiagnosisBrokerTest extends BrokerTest {
	private Calendar _calendar;
	private Doctor _doctor;
	private Diagnosis _diagnosis;

	@Override
	public void setUp() {
		_doctor = null;
		try {
			_doctor = Facade.getInstance().getById(Doctor.class, 1);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_diagnosis = new Diagnosis();
		_diagnosis.setDoctor(_doctor);
		_diagnosis.setTitle("Diagnose Test");
		_diagnosis.setDescription("Testbeschreibung");
		try {
			assertTrue(Facade.getInstance().save(_diagnosis));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		assertTrue(_doctor.getId()>0);
		assertTrue(_diagnosis.getId()>0);
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
		Diagnosis diagnosis = null;
		try {
			diagnosis = Facade.getInstance().getById(Diagnosis.class, _diagnosis.getId());
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(diagnosis!=null);
		assertTrue(diagnosis.getId()>0);
		assertTrue(diagnosis.getTitle().equals("Diagnose Test"));
		assertTrue(diagnosis.getDescription().equals("Testbeschreibung"));
		assertTrue(diagnosis.getDoctor().getId()==_doctor.getId());
	}

	@Override
	public void testGetAll() {
		Collection<Diagnosis> diagnoses = null;
		try {
			diagnoses = Facade.getInstance().getAll(Diagnosis.class);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(diagnoses!=null);
		assertTrue(diagnoses.size()>1);
	}
}