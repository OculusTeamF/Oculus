/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.entity.DiagnosisEntity;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * DiagnosisBroker.java Created by oculus on 16.04.15.
 */
public class DiagnosisBroker extends EntityBroker {
	public DiagnosisBroker() {
		super(Diagnosis.class, DiagnosisEntity.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity) throws FacadeException {
		DiagnosisEntity diagnosisEntity = (DiagnosisEntity) entity;

		Doctor doctor = null;
		if(diagnosisEntity.getDoctor()!=null){
			doctor = (Doctor) Facade.getInstance().getById(Doctor.class, diagnosisEntity.getDoctorId());
		}

		return new Diagnosis(diagnosisEntity.getId(), diagnosisEntity.getTitle(), diagnosisEntity.getDescription(), doctor);
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj) {
		Diagnosis diagnosis = (Diagnosis) obj;

		DoctorEntity doctorEntity = null;
		if(diagnosis.getDoctor()!=null){
			try {
				doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(Doctor.class).domainToPersistent(diagnosis.getDoctor());
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}

		return new DiagnosisEntity(diagnosis.getId(), diagnosis.getTitle(), diagnosis.getDescription(), doctorEntity);
	}
}
