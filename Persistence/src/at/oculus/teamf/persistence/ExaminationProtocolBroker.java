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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * ExaminationProtocolBroker.java Created by oculus on 16.04.15.
 */
public class ExaminationProtocolBroker extends EntityBroker {
	public ExaminationProtocolBroker() {
		super(ExaminationProtocol.class, ExaminationProtocolEntity.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity) throws FacadeException {
		ExaminationProtocolEntity examinationProtocolEntity = (ExaminationProtocolEntity) entity;
		Doctor doctor = null;
		Orthoptist orthoptist = null;
		if (examinationProtocolEntity.getUserId()>0) {
			for (Object obj : Facade.getInstance().getAll(Doctor.class)) {
				if (((Doctor) obj).getUserId() == (int) examinationProtocolEntity.getUserId()) {
					doctor = (Doctor) obj;
				}
			}
			for (Object obj : Facade.getInstance().getAll(Orthoptist.class)) {
				if (((Orthoptist) obj).getUserId() == (int) examinationProtocolEntity.getUserId()) {
					orthoptist = (Orthoptist) obj;
				}
			}
		}

		Diagnosis diagnosis = null;
		if (examinationProtocolEntity.getDiagnosisId() != null) {
			diagnosis = (Diagnosis) Facade.getInstance().getBroker(Diagnosis.class)
			                              .persistentToDomain(examinationProtocolEntity.getDiagnosis());
		}

		Patient patient = null;
		if(examinationProtocolEntity.getPatientId() != null){
			patient = (Patient) Facade.getInstance().getBroker(Patient.class)
			                            .persistentToDomain(examinationProtocolEntity.getPatient());
		}

		return new ExaminationProtocol(examinationProtocolEntity.getId(), examinationProtocolEntity.getStartTime(),
		                               examinationProtocolEntity.getEndTime(),
		                               examinationProtocolEntity.getDescription(), patient, doctor, orthoptist, diagnosis);
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj) {
		ExaminationProtocol examinationProtocol = (ExaminationProtocol) obj;

		PatientEntity patientEntity = null;
		if(examinationProtocol.getPatient()!=null){
			try {
				patientEntity = (PatientEntity) Facade.getInstance().getBroker(Patient.class)
				                         .domainToPersistent(examinationProtocol.getPatient());
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}

		UserEntity userEntity = null;
		if(examinationProtocol.getDoctor()!=null){
			try {
				userEntity = ((DoctorEntity) Facade.getInstance().getBroker(Doctor.class)
				                                                  .domainToPersistent(examinationProtocol.getDoctor())).getUser();
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}
		if(examinationProtocol.getOrthoptist()!=null){
			try {
				userEntity = ((OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class)
				                                .domainToPersistent(examinationProtocol.getOrthoptist())).getUser();
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}

		DiagnosisEntity diagnosisEntity = null;
		if(examinationProtocol.getDiagnosis()!=null){
			try {
				diagnosisEntity = (DiagnosisEntity) Facade.getInstance().getBroker(Diagnosis.class)
				                                      .domainToPersistent(examinationProtocol.getDiagnosis());
			} catch (NoBrokerMappedException e) {
				e.printStackTrace();
			}
		}

		return new ExaminationProtocolEntity(examinationProtocol.getId(),
		                                     new java.sql.Date(examinationProtocol.getStartTime().getTime()),
		                                     new java.sql.Date(examinationProtocol.getEndTime().getTime()),
		                                     examinationProtocol.getDescription(), patientEntity, userEntity,
		                                     diagnosisEntity);
	}
}
