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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.ExaminationProtocolEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * patient broker translating domain objects to persistence entities
 */
public class PatientBroker extends EntityBroker<Patient, PatientEntity> implements ICollectionReload, ISearch<Patient>, ILogger {

    public PatientBroker() {
        super(Patient.class, PatientEntity.class);
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
    protected Patient persistentToDomain(PatientEntity entity) throws NoBrokerMappedException, BadConnectionException, BadSessionException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        Patient patient = new Patient();
        patient.setId(entity.getId());
        patient.setFirstName(entity.getFirstName());
        patient.setLastName(entity.getLastName());
        patient.setSocialInsuranceNr(entity.getSocialInsuranceNr());

        if (entity.getDoctor() != null) {
            patient.setDoctor(
                    (Doctor) Facade.getInstance().getBroker(Doctor.class).persistentToDomain(entity.getDoctor()));
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
    protected PatientEntity domainToPersistent(Patient obj) throws NoBrokerMappedException, BadConnectionException, BadSessionException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
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
            patientEntity.setDoctor((DoctorEntity) Facade.getInstance().getBroker(Doctor.class).domainToPersistent(obj.getDoctor()));
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
        log.debug("converted " + _domainClass.getClass() + " to " + _entityClass.getClass());
        return patientEntity;
    }

    /**
     * reload the calendar events of a patient
     * @param session to be used
     * @param obj patient
     * @return collection of calendar events
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj) throws BadConnectionException, NoBrokerMappedException, BadSessionException {
        ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, CalendarEvent.class);
        log.debug("reloading calendar events");
        return reloadComponent.reloadCollection(session, ((Patient) obj).getId(), new CalendarEventsLoader());
    }

    /**
     * reload the examination protocols of a patient
     * @param session session to be used
     * @param obj patient
     * @return collection of examnation protocols
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<ExaminationProtocol> reloadExaminationProtocol(ISession session, Object obj) throws BadConnectionException, NoBrokerMappedException, BadSessionException {
        log.debug("reloading examination protocols");
        ReloadComponent reloadComponent = new ReloadComponent(PatientEntity.class, ExaminationProtocol.class);
        return reloadComponent.reloadCollection(session, ((Patient) obj).getId(), new ExaminationProtocolLoader());
    }

    /**
     * reload collections of a patient
     * @param session session to be used
     * @param obj patient
     * @param clazz to be reloaded
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidReloadClassException
     */
    @Override
    public void reload(ISession session, Object obj, Class clazz) throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException, BadSessionException {
        if (clazz == CalendarEvent.class) {
	        ((Patient) obj).setCalendarEvents(reloadCalendarEvents(session, obj));
        } else if (clazz == ExaminationProtocol.class) {
	        ((Patient) obj).setExaminationProtocol(reloadExaminationProtocol(session, obj));
        } else {
            throw new InvalidReloadClassException();
        }
    }

    /**
     * search patients
     * @param session session to be used
     * @param params  parameter in order (social insurance number, firstname, lastname | search string)
     * @return collection of search results
     */
    @Override
    public Collection<Patient> search(ISession session, String... params) throws InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException, BadSessionException {
        Collection<PatientEntity> searchResult = new LinkedList<>();

        // create query
        String query = null;

        //decide on query
        if (params.length == 1) {
            query = "getPatientByAll";
        } else if (params.length == 3) {
            query = "getPatientBySingle";
        } else {
            throw new InvalidSearchParameterException();
        }

        try {
            searchResult.addAll((Collection<PatientEntity>) (Collection<?>) session.search(query, params));
        } catch (BadSessionException e) {
            log.catching(e);
        }

        Collection<Patient> result = new LinkedList<>();
        for(PatientEntity pe : searchResult) {
            result.add(persistentToDomain((pe)));
        }


        return result;
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
}
