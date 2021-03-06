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
import at.oculus.teamf.databaseconnection.session.exception.*;
import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.user.doctor.Doctor;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.entity.CalendarEntity;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.entity.UserEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * doctor broker translating domain objects to persistence entities
 */
class DoctorBroker extends EntityBroker<IDoctor, DoctorEntity> implements ICollectionReload, ISearch {
    public DoctorBroker() {
        super(IDoctor.class, DoctorEntity.class);
        addDomainClassMapping(Doctor.class);
        addEntityClassMapping(UserEntity.class);
    }

    /**
     * Converts a persitency entity to a domain object
     *
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    @Override
    protected IDoctor persistentToDomain(DoctorEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, SearchInterfaceNotImplementedException, InvalidSearchParameterException, ClassNotMappedException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        IDoctor doctor = (IDoctor) DomainWrapperFactory.getInstance().create(IDoctor.class);
        doctor.setId(entity.getId());

        doctor.setCalendar(
                (ICalendar) Facade.getInstance().getBroker(ICalendar.class).persistentToDomain(entity.getCalendar()));

        if (doctor.getDoctorSubstitude() != null) {
            doctor.setDoctorSubstitude((IDoctor) Facade.getInstance().getBroker(IDoctor.class)
                    .persistentToDomain(entity.getDoctorSubstitute()));
        }
        doctor.setQueue(null);
        // user data
        UserEntity userEntity = entity.getUser();
        doctor.setUserId(entity.getUserId());
        doctor.setUserGroupId(userEntity.getUserGroupId());
        doctor.setUserName(userEntity.getUserName());
        doctor.setPassword(userEntity.getPassword());
        doctor.setTitle(userEntity.getTitle());
        doctor.setFirstName(userEntity.getFirstName());
        doctor.setLastName(userEntity.getLastName());
        doctor.setEmail(userEntity.getEmail());
        doctor.setCreateDate(userEntity.getCreateDate());
        doctor.setIdleDate(userEntity.getIdleDate());
        //doctor.setUserGroup(userEntity.getUserGroup());

        return doctor;
    }

    /**
     * Converts a domain object to persitency entity
     * @param obj that needs to be converted
     * @return return a persitency entity
     */
    @Override
    protected DoctorEntity domainToPersistent(IDoctor obj) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        IDoctor entity = obj;
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(entity.getId());
        CalendarEntity calendarEntity = null;
        if (entity.getCalendar() != null) {
                calendarEntity = (CalendarEntity) Facade.getInstance().getBroker(ICalendar.class).domainToPersistent(entity.getCalendar());

        }
        doctorEntity.setCalendar(calendarEntity);
        // user data
        UserEntity userEntity = new UserEntity();
        userEntity.setId(entity.getTeamFUserId());
        userEntity.setUserGroupId(entity.getUserGroupId());
        userEntity.setUserName(entity.getUserName());
        userEntity.setPassword(entity.getPassword());
        userEntity.setTitle(entity.getTitle());
        userEntity.setFirstName(entity.getFirstName());
        userEntity.setLastName(entity.getLastName());
        userEntity.setEmail(entity.getEmail());
        userEntity.setCreateDate((Timestamp) entity.getCreateDate());
        userEntity.setIdleDate((Timestamp) entity.getTeamFIdleDate());
        //userEntity.setUserGroup(entity.getUserGroup());
        doctorEntity.setUser(userEntity);
        return doctorEntity;
    }

    /**
     * reload collections of a doctor
     * @param session session to be used
     * @param obj patient
     * @param clazz to be reloaded
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidReloadClassException
     */
    @Override
    public void reload(ISession session, Object obj, Class clazz) throws InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, ClassNotMappedException, DatabaseOperationException, InvalidSearchParameterException, SearchInterfaceNotImplementedException {
        if (clazz == IPatient.class) {
            ((IDoctor) obj).setPatients((Collection<IPatient>) (Collection<?>) reloadPatients(session, obj));
        } else {
            throw new InvalidReloadClassException();
        }
    }

    /**
     * reload the patients of a doctor
     * @param session to be used
     * @param obj patient
     * @return collection of calendar events
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<IPatient> reloadPatients(ISession session, Object obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, SearchInterfaceNotImplementedException, InvalidSearchParameterException, ClassNotMappedException {
        log.debug("reloading patients");
        ReloadComponent reloadComponent =
                new ReloadComponent(DoctorEntity.class, IPatient.class);

        return reloadComponent.reloadCollection(session, ((IDoctor) obj).getId(), new PatientsLoader());
    }

    /**
     * Saves an object in the database
     *
     * @param session   that should be use
     * @param domainObj that needs to be saved
     * @return {@code true} if the object was saved, {@code false} if the object could not be saved
     */
    @Override
    public void saveEntity(ISession session, IDoctor domainObj) throws BadConnectionException, NoBrokerMappedException, ClassNotMappedException, DatabaseOperationException {
        log.info("save " + _domainClass.toString() + " with ID " + domainObj.getId());
        DoctorEntity entity = null;

        entity = domainToPersistent(domainObj);

        Boolean returnValue = true;

        try {
            session.beginTransaction();

            session.saveOrUpdate(entity.getUser());
            session.saveOrUpdate(entity);

            session.commit();

            // update IDs when commit was successful
            if (returnValue) {
                domainObj.setId(entity.getId());
            }
        } catch (BadSessionException | AlreadyInTransactionException | NoTransactionException e) {
            log.error(e);
            throw new BadConnectionException();
        } catch (CanNotStartTransactionException | CanNotCommitTransactionException e) {
            log.error(e);
            throw new DatabaseOperationException(e);
        }
    }

    private class PatientsLoader implements ICollectionLoader<PatientEntity> {

        @Override
        public Collection<PatientEntity> load(Object databaseEntity) {
            return ((DoctorEntity) databaseEntity).getPatients();
        }
    }

    @Override
    public Collection<IDoctor> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException {
        if (params.length == 1) {
            Collection<DoctorEntity> result = (Collection<DoctorEntity>)(Collection<?>)session.search("getDoctorByUserId", params[0]);

	        LinkedList<IDoctor> domainDoctors = new LinkedList<>();

	        for(DoctorEntity de : result) {
		        domainDoctors.add(persistentToDomain(de));
	        }

	        return domainDoctors;

        } else {
            return null;
        }
    }
}
