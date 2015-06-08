/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.calendar.calendarevent.CalendarEvent;
import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.patient.Patient;
import at.oculus.teamf.domain.entity.user.Gender;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.examination.IExaminationProtocol;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedList;

/**
 * patient broker translating domain objects to persistence entities
 */
class PatientBroker extends EntityBroker<IPatient, PatientEntity> implements ICollectionReload, ISearch<IPatient>, ILogger {

    public PatientBroker() {
        super(IPatient.class, PatientEntity.class);
		addDomainClassMapping(Patient.class);
    }

	/**
	 * reload collections of a patient
	 *
	 * @param session
	 * 		session to be used
	 * @param obj
	 * 		patient
	 * @param clazz
	 * 		to be reloaded
	 *
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws InvalidReloadClassException
	 */
	@Override
	public void reload(ISession session, Object obj, Class clazz)
			throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException,
			       DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException,
			       InvalidSearchParameterException, BadSessionException {
		if (clazz == CalendarEvent.class) {
			((IPatient) obj).setCalendarEvents((Collection<ICalendarEvent>) (Collection<?>) reloadCalendarEvents(session, obj));
		} else if (clazz == ExaminationProtocol.class) {
            ((IPatient) obj).setExaminationProtocol((Collection<IExaminationProtocol>) (Collection<?>) reloadExaminationProtocol(session, obj));
        } else if (clazz == IPrescription.class) {
			((IPatient) obj).setPrescriptions(reloadPrescriptions(session, obj));
		} else {
			throw new InvalidReloadClassException();
		}
	}

	/**
	 * reload the calendar events of a patient
	 * @param session to be used
	 * @param obj patient
	 * @return collection of calendar events
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 */
	private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj)
			throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, CalendarEvent.class);
		log.debug("reloading calendar events");
		return reloadComponent.reloadCollection(session, ((IPatient) obj).getId(), new CalendarEventsLoader());
	}

	/**
	 * reload the examination protocols of a patient
	 * @param session session to be used
	 * @param obj patient
	 * @return collection of examnation protocols
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 */
	private Collection<ExaminationProtocol> reloadExaminationProtocol(ISession session, Object obj)
			throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("reloading examination protocols");
		ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, ExaminationProtocol.class);
		return reloadComponent.reloadCollection(session, ((IPatient) obj).getId(), new ExaminationProtocolLoader());
	}

	private Collection<IPrescription> reloadPrescriptions(ISession session, Object obj)
			throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("reloading prescriptions");
		ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, IPrescription.class);
		return reloadComponent.reloadCollection(session, ((IPatient) obj).getId(), new PrescriptionLoader());
	}

    /**
     * search patients
     * @param session session to be used
     * @param params  parameter in order (social insurance number, firstname, lastname | search string)
     * @return collection of search results
     */
    @Override
    public Collection<IPatient> search(ISession session, String... params)
		    throws InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException,
		           DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException,
		           BadSessionException {
	    Collection<Object> searchResult = null;

	    // create query
	    String query = null;

	    for (int i = 0; i < params.length; ++i) {
		    if (params[i].length() > 0) {
			    params[i] = new String("%" + params[i].replace(' ', '%') + "%");
		    }
	    }

	    //decide on query
	    if (params.length == 1) {
		    if(params[0].contains("@")){
			    query = "getPatientByEmail";
			    log.debug("search patient by email");
		    } else {
			    query = "getPatientByAll";
			    log.debug("search patient by all");
		    }
	    } else if (params.length == 3) {
		    query = "getPatientBySingle";
		    log.debug("search patient by single");
	    } else {
		    throw new InvalidSearchParameterException();
	    }

	    searchResult = session.search(query, params);

	    Collection<IPatient> result = new LinkedList<>();
	    for (Object o : searchResult) {
		    result.add(persistentToDomain((PatientEntity) o));
	    }

        return result;
    }

	/**
	 * converts a persitency entity to a domain object
	 *
	 * @param entity that needs to be converted
	 * @return domain object that is created from entity
	 * @throws NoBrokerMappedException
	 * @throws BadConnectionException
	 */
	@Override
	protected IPatient persistentToDomain(PatientEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " +
				_domainClass.getClass());
		IPatient patient = (IPatient) DomainWrapperFactory.getInstance().create(getDomainClass());
		patient.setId(entity.getId());
		patient.setFirstName(entity.getFirstName());
		patient.setLastName(entity.getLastName());
		patient.setSocialInsuranceNr(entity.getSocialInsuranceNr());

		if (entity.getDoctor() != null) {
			patient.setDoctor(
					(IDoctor) Facade.getInstance().getBroker(IDoctor.class).persistentToDomain(entity.getDoctor()));
		}

		if (entity.getGender().equals("M")) {
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
		patient.setPasswordHash(entity.getPassword());
		patient.setMedicineIntolerance(entity.getMedicineIntolerance());
		patient.setPhone(entity.getPhone());
		patient.setPostalCode(entity.getPostalCode());
		patient.setStreet(entity.getStreet());

		return patient;
	}

	/**
	 * Converts a domain object to persitency entity
	 * @param obj that needs to be converted
	 * @return return a persitency entity
	 */
	@Override
	protected PatientEntity domainToPersistent(IPatient obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " +
		          _entityClass.getClass());
		PatientEntity patientEntity = new PatientEntity();
		patientEntity.setId(obj.getId());
		patientEntity.setFirstName(obj.getFirstName());
		patientEntity.setLastName(obj.getLastName());
		patientEntity.setSocialInsuranceNr(obj.getSocialInsuranceNr());
		patientEntity.setAllergy(obj.getAllergy());
		if (obj.getBirthDay() != null) {
			patientEntity.setBirthDay(new Date(obj.getBirthDay().getTime()));
		}
		patientEntity.setChildhoodAilments(obj.getChildhoodAilments());
		patientEntity.setCity(obj.getCity());
		patientEntity.setCountryIsoCode(obj.getCountryIsoCode());
		if (obj.getDoctor() != null) {
			patientEntity.setDoctor(
					(DoctorEntity) Facade.getInstance().getBroker(IDoctor.class).domainToPersistent(obj.getDoctor()));
		}
		patientEntity.setEmail(obj.getEmail());
		patientEntity.setPassword(obj.getPasswordHash());
		patientEntity.setMedicineIntolerance(obj.getMedicineIntolerance());
		if (obj.getGender() == Gender.Male) {
			patientEntity.setGender("M");
		} else {
			patientEntity.setGender("F");
		}
		patientEntity.setPhone(obj.getPhone());
		patientEntity.setPostalCode(obj.getPostalCode());
		patientEntity.setStreet(obj.getStreet());
		log.debug("converted " + _domainClass.getClass() + " to " + _entityClass.getClass());
		return patientEntity;
    }

    private class CalendarEventsLoader implements ICollectionLoader<CalendarEventEntity> {

        @Override
        public Collection<CalendarEventEntity> load(Object databaseEntity) {
            return ((PatientEntity) databaseEntity).getCalendarevents();
        }
    }

    private class ExaminationProtocolLoader implements ICollectionLoader<ExaminationProtocolEntity> {

        @Override
        public Collection<ExaminationProtocolEntity> load(Object databaseEntity) {
            return ((PatientEntity) databaseEntity).getExaminationProtocol();
        }
    }

	private class PrescriptionLoader implements ICollectionLoader<PrescriptionEntity> {

		@Override
		public Collection<PrescriptionEntity> load(Object databaseEntity) {
			return ((PatientEntity) databaseEntity).getPrescriptions();
		}
	}
}
