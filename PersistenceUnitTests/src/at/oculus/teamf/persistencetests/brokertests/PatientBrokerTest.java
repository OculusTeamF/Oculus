/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
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
		} catch (BadSessionException e) {
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
		} catch (BadSessionException e) {
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
        } catch (BadSessionException e) {
            e.printStackTrace();
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
        } catch (BadSessionException e) {
            e.printStackTrace();
        }

        assertTrue(patients != null);
        assertTrue(patients.size() > 1);

    }

	@Test
	public void testReload() {
        Patient patient = null;
        try {
	        for(Object p : Facade.getInstance().search(Patient.class, "5678151082")){
		        patient = (Patient) p;
	        }
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        try {
			assertTrue(patient.getCalendarEvents() != null);
			assertTrue(patient.getCalendarEvents().size() == 3);
			assertTrue(patient.getExaminationProtocol() != null);
			assertTrue(patient.getExaminationProtocol().size()==5);
		} catch (InvalidReloadClassException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (ReloadInterfaceNotImplementedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadSessionException e) {
            e.printStackTrace();
        }
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

	@Test
	public void testAddExaminationProtocol(){
		// load patient and doctor
		Patient patient = null;
		try {
			for(Object p : Facade.getInstance().search(Patient.class, "5678151082")){
				patient = (Patient) p;
			}
		} catch (SearchInterfaceNotImplementedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}

        // create diagnosis
		Diagnosis diagnosis = new Diagnosis();
		diagnosis.setDoctor(patient.getDoctor());
		diagnosis.setTitle("Test Diagnosis");
		diagnosis.setDescription("Test Text einer Diagnose.");
		try {
			assertTrue(Facade.getInstance().save(diagnosis));
		} catch (BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadSessionException e) {
            e.printStackTrace();
        }

        // create examination protocol
		ExaminationProtocol examinationProtocol = new ExaminationProtocol();
		examinationProtocol.setDoctor(patient.getDoctor());
		examinationProtocol.setOrthoptist(null);
		examinationProtocol.setDescription("Test Text eines Protokolls.");
		examinationProtocol.setStartTime(new Date());
		examinationProtocol.setEndTime(new Date());
		examinationProtocol.setDiagnosis(diagnosis);
		try {
			assertTrue(Facade.getInstance().save(examinationProtocol));
		} catch (BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadSessionException e) {
            e.printStackTrace();
        }

        // delete
		try {
			assertTrue(Facade.getInstance().save(examinationProtocol.getDoctor()));
			assertTrue(Facade.getInstance().delete(examinationProtocol));
			assertTrue(Facade.getInstance().delete(diagnosis));
		} catch (BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (BadSessionException e) {
            e.printStackTrace();
        }
    }
}