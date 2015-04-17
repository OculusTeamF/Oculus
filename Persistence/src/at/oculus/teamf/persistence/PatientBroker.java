/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.AlreadyInTransactionException;
import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.NoTransactionException;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.InvalidReloadParameterException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 08.04.2015.
 */
public class PatientBroker extends EntityBroker<Patient, PatientEntity> implements ICollectionReload {

	public PatientBroker() {
		super(Patient.class, PatientEntity.class);
	}

	@Override
	protected Patient persistentToDomain(PatientEntity entity) throws FacadeException {
		Patient patient = new Patient();
		patient.setId(entity.getId());
		patient.setFirstName(entity.getFirstName());
		patient.setLastName(entity.getLastName());
		patient.setSocialInsuranceNr(entity.getSocialInsuranceNr());

		if (entity.getDoctor() != null) {
			patient.setDoctor(
					(Doctor) Facade.getInstance().getBroker(Doctor.class).persistentToDomain(entity.getDoctor()));
		}

		if (entity.getGender().equals('M')) {
			patient.setGender(Gender.Male);
		} else {
			patient.setGender(Gender.Female);
		}

		patient.setAllergy(entity.getAllergy());
		patient.setBirthDay(entity.getBirthDay());
		patient.setChildhoodAilments(entity.getChildhoodAilments());
		patient.setCity(entity.getCity());
		patient.setCountryIsoCode(entity.getCountryIsoCode());
		patient.setEmail(entity.getEmail());
		patient.setMedicineIntolerance(entity.getMedicineIntolerance());
		patient.setPhone(entity.getPhone());
		patient.setPostalCode(entity.getPostalCode());
		patient.setStreet(entity.getStreet());

		return patient;
	}

	@Override
	protected PatientEntity domainToPersistent(Patient obj) {
		PatientEntity patientEntity = new PatientEntity();
		patientEntity.setId(obj.getId());
		patientEntity.setFirstName(obj.getFirstName());
		patientEntity.setLastName(obj.getLastName());
		patientEntity.setSocialInsuranceNr(obj.getSocialInsuranceNr());
		patientEntity.setAllergy(obj.getAllergy());
		patientEntity.setBirthDay(obj.getBirthDay());
		patientEntity.setChildhoodAilments(obj.getChildhoodAilments());
		patientEntity.setCity(obj.getCity());
		patientEntity.setCountryIsoCode(obj.getCountryIsoCode());
		if (obj.getDoctor() != null) {
			patientEntity.setDoctorId(obj.getDoctor().getId());
		}
		patientEntity.setEmail(obj.getEmail());
		patientEntity.setMedicineIntolerance(obj.getMedicineIntolerance());
		if (obj.getGender() == Gender.Male) {
			patientEntity.setGender("M");
		} else {
			patientEntity.setGender("F");
		}
		patientEntity.setPhone(obj.getPhone());
		patientEntity.setPostalCode(obj.getPostalCode());
		patientEntity.setStreet(obj.getStreet());
		return patientEntity;
	}

	@Override
	public void reload(ISession session, Object obj, Class clazz) throws FacadeException {
		if (clazz == CalendarEvent.class) {
			((Patient) obj).setCalendarEvents(reloadCalendarEvents(session, obj));
		} else {
			throw new InvalidReloadParameterException();
		}
	}

	/*public Collection<Patient> searchPatient(ISession session, String svn, String firstName, String lastName) {
		Collection<Object> patientsResult = null;
		Collection<Patient> patients = new LinkedList<Patient>();

		// create query
		String query = "FROM PatientEntity where upper(socialInsuranceNr) like ?0 and upper(firstName) like ?1 and upper(lastName) like ?2";
		String[] parameters = new String[3];
		parameters[0] = "%" + svn.toUpperCase() + "%";
		parameters[1] = "%" + firstName.toUpperCase() + "%";
		parameters[2] = "%" + lastName.toUpperCase() + "%";

		try {
			session.beginTransaction();

			patientsResult = session.getQueryResult(query, parameters);

			session.commit();
		} catch (NoTransactionException | BadSessionException | AlreadyInTransactionException e) {
			log.catching(e);
		}

		try {
			for (Object obj : patientsResult) {
				PatientEntity patientEntity = (PatientEntity) obj;
				patients.add(persistentToDomain(patientEntity));
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		return patients;
	}*/

	/**
	 *
	 * @param session   Session
	 * @param params    Parameter in der Reichenfolge (SVN, Firstname, Lastname, Suchstring)
	 * @return
	 */
	public Collection<Patient> search(ISession session, String... params) {
		Collection<Object> patientsResult = null;
		Collection<Patient> patients = new LinkedList<Patient>();

		// create query
		String query = null;

		if(params[3].equals("")){
			query = "getPatientBySingle";
		} else {
			query = "getPatientByAll";
		}

		try {
			patientsResult = session.search(query, params);
		} catch (BadSessionException e) {
			log.catching(e);
		}

		try {
			for (Object obj : patientsResult) {
				PatientEntity patientEntity = (PatientEntity) obj;
				patients.add(persistentToDomain(patientEntity));
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		return patients;
	}

	private class CalendarEventsLoader implements CollectionLoader<CalendarEventEntity> {

		@Override
		public Collection<CalendarEventEntity> load(Object databaseEntity) {
			return ((PatientEntity) databaseEntity).getCalendarevents();
		}
	}

	private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj) throws FacadeException {
		ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, CalendarEvent.class);

		return reloadComponent.reloadCollection(session, ((Patient) obj).getId(), new CalendarEventsLoader());
	}
}
