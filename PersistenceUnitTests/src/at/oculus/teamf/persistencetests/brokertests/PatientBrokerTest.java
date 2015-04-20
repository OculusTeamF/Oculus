/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Norskan on 10.04.2015.
 */
public class PatientBrokerTest extends BrokerTest {
	private Patient _patient;

	@Override
	public void setUp() {
		_patient = new Patient();
		_patient.setFirstName("Donald");
		_patient.setLastName("Ahoi");
		_patient.setSocialInsuranceNr("1234567890");
		_patient.setGender(Gender.Male);
		_patient.setCountryIsoCode("AT");
		_patient.setStreet("Blastraße 18e");
		_patient.setCity("Entenhausen");
		_patient.setChildhoodAilments("Kinder krank heiten");
		_patient.setAllergy("Allergien");
		_patient.setEmail("donald.ahoi@test.com");
		_patient.setMedicineIntolerance("Medizin Unverträglichkeiten");
		_patient.setPhone("+43 664 987356 34");
		_patient.setPostalCode("4633");
        _patient.setBirthDay(new Date());
		try {
			assertTrue(Facade.getInstance().save(_patient));
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		assertTrue(_patient.getId()>0);
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_patient));
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void testGetById() {
        Patient patient = null;
        try {
            patient = Facade.getInstance().getById(Patient.class, _patient.getId());
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patient != null);
        assertTrue(patient.getFirstName().equals(_patient.getFirstName()));
        assertTrue(patient.getLastName().equals(_patient.getLastName()));
        assertTrue(patient.getSocialInsuranceNr().equals(_patient.getSocialInsuranceNr()));
        assertTrue(patient.getStreet().equals(_patient.getStreet()));
        assertTrue(patient.getCity().equals(_patient.getCity()));
        assertTrue(patient.getPostalCode().equals(_patient.getPostalCode()));
        assertTrue(patient.getEmail().equals(_patient.getEmail()));
        assertTrue(patient.getPhone().equals(_patient.getPhone()));
        assertTrue(patient.getGender() == _patient.getGender());
        assertTrue(patient.getAllergy().equals(_patient.getAllergy()));
        assertTrue(patient.getChildhoodAilments().equals(_patient.getChildhoodAilments()));
        assertTrue(patient.getMedicineIntolerance().equals(_patient.getMedicineIntolerance()));
    }

    @Override
    public void testGetAll() {
        Collection<Patient> patients = null;
        try {
            patients = Facade.getInstance().getAll(Patient.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patients != null);
        assertTrue(patients.size() > 1);

    }

	public void testReload() {
        Patient patient = null;
        try {
            patient = Facade.getInstance().getById(Patient.class, 1);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patient.getCalendarEvents() != null);
        assertTrue(patient.getCalendarEvents().size() == 1);
    }

	@Test
	public void testSearchPatient(){
		Collection<Patient> patients = null;
		// SVN only
		try {
			patients = Facade.getInstance().search(Patient.class,"5947053957", "", "");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size()==1);
		// Firstname only
		try {
			patients = Facade.getInstance().search(Patient.class,"","JaNe","");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size() == 3);
		// Lastname only
		try {
			patients = Facade.getInstance().search(Patient.class,"","","sOn");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size()==6);
		// Fulltext
		try {
			patients = Facade.getInstance().search(Patient.class,"son");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size()==6);
	}
}