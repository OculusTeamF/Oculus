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
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.persistence.entities.CalendarEntity;
import at.oculus.teamf.persistence.entities.DoctorEntity;
import at.oculus.teamf.persistence.entities.PatientEntity;
import at.oculus.teamf.persistence.entities.UserEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.InvalidReloadParameterException;
import at.oculus.teamf.persistence.exceptions.NoBrokerMappedException;

import java.util.Collection;

/**
 * DoctorBroker.java Created by oculus on 08.04.15.
 */
public class DoctorBroker extends EntityBroker<Doctor, DoctorEntity> implements ICollectionReload {
	public DoctorBroker() {
		super(Doctor.class, DoctorEntity.class);
	}

	@Override
	protected Doctor persitentToDomain(DoctorEntity entity) {
		Doctor doctor = new Doctor();
		doctor.setId(entity.getId());
		try {
			doctor.setCalendar(
					(Calendar) Facade.getInstance().getBroker(Calendar.class).persitentToDomain(entity.getCalendar()));

			if(doctor.getDoctorSubstitude() != null) {
				doctor.setDoctorSubstitude((Doctor) Facade.getInstance().getBroker(Doctor.class)
						.persitentToDomain(entity.getDoctorSubstitute()));
			}
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		doctor.setQueue(new PatientQueue(doctor));
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
	protected DoctorEntity domainToPersitent(Doctor obj) {
		Doctor entity = (Doctor) obj;
		DoctorEntity doctorEntity = new DoctorEntity();
		doctorEntity.setId(entity.getId());
		try {
			doctorEntity.setCalendar((CalendarEntity) Facade.getInstance().getBroker(Calendar.class)
			                                                .persitentToDomain(entity.getCalendar()));
			doctorEntity.setDoctorSubstitute((DoctorEntity) Facade.getInstance().getBroker(Doctor.class)
			                                                      .persitentToDomain(entity.getDoctorSubstitude()));
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		doctorEntity.setCalendarId(entity.getCalendar().getCalendarID());
		doctorEntity.setDoctorIdSubstitute(entity.getDoctorSubstitude().getId());
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

	private class PatientsLoader implements CollectionLoader<PatientEntity> {

		@Override
		public Collection<PatientEntity> load(Object databaseEntity) {
			return ((DoctorEntity) databaseEntity).getPatients();
		}
	}

	private Collection<Patient> reloadPatients(ISession session, Object obj) throws FacadeException {
		ReloadComponent reloadComponent =
				new ReloadComponent(DoctorEntity.class, Patient.class);

		return reloadComponent.reloadCollection(session, ((Doctor) obj).getId(), new PatientsLoader());
	}
}
