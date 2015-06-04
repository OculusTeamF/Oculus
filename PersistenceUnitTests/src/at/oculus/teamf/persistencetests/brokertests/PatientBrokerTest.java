/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.factory.DomainFactory;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.user.Gender;
import at.oculus.teamf.domain.entity.exception.*;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.technical.accessrights.ILogin;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.accessrights.Login;
import at.oculus.teamf.technical.exceptions.HashGenerationException;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Norskan on 10.04.2015.
 */
public class PatientBrokerTest extends BrokerTest {
	private IPatient _patient;

	@Override
	public void setUp() {
		_patient = (IPatient) DomainFactory.create(IPatient.class);
		try {
			_patient.setDoctor(Facade.getInstance().getById(IDoctor.class, 1));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
		}
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
			assertTrue(false);
		}
		assertTrue(_patient.getId() > 0);
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_patient));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		IPatient patient = null;
		try {
			patient = Facade.getInstance().getById(IPatient.class, _patient.getId());
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
		Collection<IPatient> patients = null;
		try {
			patients = Facade.getInstance().getAll(IPatient.class);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		assertTrue(patients != null);
		assertTrue(patients.size() > 1);

	}

	@Test
	public void testReload() {
		IPatient patient = null;
		try {
			for (Object p : Facade.getInstance().search(IPatient.class, "5678151082")) {
				patient = (IPatient) p;
			}
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		try {
			assertTrue(patient.getCalendarEvents() != null);
			assertTrue(patient.getCalendarEvents().size() > 0);
			assertTrue(patient.getExaminationProtocol() != null);
			assertTrue(patient.getExaminationProtocol().size() > 0);
			assertTrue(patient.getPrescriptions() != null);
			assertTrue(patient.getPrescriptions().size() > 0);
			assertTrue(patient.getDiagnoses() != null);
			assertTrue(patient.getDiagnoses().size() > 0);
			assertTrue(patient.getMedicine() != null);
			assertTrue(patient.getMedicine().size() > 0);
		} catch (CouldNotGetExaminationProtolException | CouldNotGetCalendarEventsException |
				CouldNotGetPrescriptionException | CouldNotGetMedicineException | CouldNotGetDiagnoseException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testSearchPatient() {
		Collection<IPatient> patients = null;
		// SVN only
		try {
			patients = Facade.getInstance().search(IPatient.class, "5947053957");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size() == 1);
		// Firstname only
		try {
			patients = Facade.getInstance().search(IPatient.class, "JaNe");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size() == 3);
		// Lastname only
		try {
			patients = Facade.getInstance().search(IPatient.class, "sOn");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size() == 6);
		// Fulltext
		try {
			patients = Facade.getInstance().search(IPatient.class, "son");
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(patients.size() == 6);
	}

	@Test
	public void testAddExaminationProtocol() {
		// load patient and doctor
		IPatient patient = null;
		try {
			for (Object p : Facade.getInstance().search(IPatient.class, "5678151082")) {
				patient = (IPatient) p;
			}
		} catch (DatabaseOperationException | SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// create diagnosis
		IDiagnosis diagnosis = (IDiagnosis) DomainFactory.create(IDiagnosis.class);
		diagnosis.setDoctor((IDoctor) patient.getDoctor());
		diagnosis.setTitle("Test Diagnosis");
		diagnosis.setDescription("Test Text einer Diagnose.");
		try {
			assertTrue(Facade.getInstance().save(diagnosis));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// create examination protocol
		ExaminationProtocol examinationProtocol = new ExaminationProtocol();
		examinationProtocol.setDoctor((IDoctor) patient.getDoctor());
		examinationProtocol.setOrthoptist(null);
		examinationProtocol.setDescription("Test Text eines Protokolls.");
		examinationProtocol.setStartTime(new Date());
		examinationProtocol.setEndTime(new Date());
		examinationProtocol.setDiagnosis(diagnosis);
		try {
			assertTrue(Facade.getInstance().save(examinationProtocol));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		// delete
		try {
			assertTrue(Facade.getInstance().save(examinationProtocol.getDoctor()));
			assertTrue(Facade.getInstance().delete(examinationProtocol));
			assertTrue(Facade.getInstance().delete(diagnosis));
		} catch (BadConnectionException | DatabaseOperationException | InvalidSearchParameterException | NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testLogin() {
		IPatient patient = null;
		try {
			for (Object p : Facade.getInstance().search(IPatient.class, "spitze.biene@hotmail.com")) {
				patient = (IPatient) p;
			}
		} catch (DatabaseOperationException | SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | NoBrokerMappedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		if (patient == null) {
			assertTrue(false);
		}

		try {
			if (!Login.login((ILogin) patient, "letmein")) {
				assertTrue(false);
			}
		} catch (HashGenerationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}