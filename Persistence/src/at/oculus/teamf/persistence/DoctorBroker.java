/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.*;
import at.oculus.teamf.databaseconnection.session.exception.AlreadyInTransactionException;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.exception.NoTransactionException;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadParameterException;

import java.util.Collection;

/**
 * DoctorBroker.java Created by oculus on 08.04.15.
 */
public class DoctorBroker extends EntityBroker<Doctor, DoctorEntity> implements ICollectionReload {
	public DoctorBroker() {
		super(Doctor.class, DoctorEntity.class);
		addClassMapping(UserEntity.class);
	}

	@Override
	protected Doctor persistentToDomain(DoctorEntity entity) {
		Doctor doctor = new Doctor();
		doctor.setId(entity.getId());
		try {
			doctor.setCalendar(
					(Calendar) Facade.getInstance().getBroker(Calendar.class).persistentToDomain(entity.getCalendar()));

			if(doctor.getDoctorSubstitude() != null) {
				doctor.setDoctorSubstitude((Doctor) Facade.getInstance().getBroker(Doctor.class)
						.persistentToDomain(entity.getDoctorSubstitute()));
			}
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		doctor.setQueue(null);
		// user data
		UserEntity userEntity = entity.getUser();
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

	@Override
	protected DoctorEntity domainToPersistent(Doctor obj) {
		Doctor entity = (Doctor) obj;
		DoctorEntity doctorEntity = new DoctorEntity();
		doctorEntity.setId(entity.getId());
		CalendarEntity calendarEntity = null;
		if(entity.getCalendar()!=null){
			try {
				calendarEntity = (CalendarEntity) Facade.getInstance().getBroker(Calendar.class).domainToPersistent(entity.getCalendar());
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}
		doctorEntity.setCalendar(calendarEntity);
		// user data
		UserEntity userEntity = new UserEntity();
		userEntity.setId(entity.getUserId());
		userEntity.setUserGroupId(entity.getUserGroupId());
		userEntity.setUserName(entity.getUserName());
		userEntity.setPassword(entity.getPassword());
		userEntity.setTitle(entity.getTitle());
		userEntity.setFirstName(entity.getFirstName());
		userEntity.setLastName(entity.getLastName());
		userEntity.setEmail(entity.getEmail());
		userEntity.setCreateDate(entity.getCreateDate());
		userEntity.setIdleDate(entity.getIdleDate());
		//userEntity.setUserGroup(entity.getUserGroup());
		doctorEntity.setUser(userEntity);
		return doctorEntity;
	}

	@Override
	public void reload(ISession session, Object obj, Class clazz) throws FacadeException {
		if (clazz == Patient.class) {
			((Doctor) obj).setPatients(reloadPatients(session, obj));
		} else {
			throw new InvalidReloadParameterException();
		}
	}

	private class PatientsLoaderI implements ICollectionLoader<PatientEntity> {

		@Override
		public Collection<PatientEntity> load(Object databaseEntity) {
			return ((DoctorEntity) databaseEntity).getPatients();
		}
	}

	private Collection<Patient> reloadPatients(ISession session, Object obj) throws FacadeException {
		ReloadComponent reloadComponent =
				new ReloadComponent(DoctorEntity.class, Patient.class);

		return reloadComponent.reloadCollection(session, ((Doctor) obj).getId(), new PatientsLoaderI());
	}

	/**
	 * Saves an object in the database
	 *
	 * @param session that should be use
	 * @param domainObj that needs to be saved
	 * @return {@code true} if the object was saved, {@code false} if the object could not be saved
	 */
	@Override
	public boolean saveEntity(ISession session, Doctor domainObj) {
		DoctorEntity entity = domainToPersistent(domainObj);
		Boolean returnValue = true;

		try {
			session.beginTransaction();

			session.saveOrUpdate(entity.getUser());
			session.saveOrUpdate(entity);

			returnValue = session.commit();

			// update IDs when commit was successful
			if(returnValue){
				domainObj.setId(entity.getId());
			}
		} catch (BadSessionException e) {
			log.catching(e);
			return false;
		} catch (AlreadyInTransactionException e) {
			log.catching(e);
			return false;
		} catch (NoTransactionException e) {
			log.catching(e);
			return false;
		} catch (ClassNotMappedException e) {
			log.catching(e);
			return false;
		}

		return returnValue;
	}
}
