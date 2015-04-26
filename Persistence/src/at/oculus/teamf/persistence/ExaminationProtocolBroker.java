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
import at.oculus.teamf.databaseconnection.session.exception.AlreadyInTransactionException;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.exception.NoTransactionException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

import java.sql.Timestamp;

/**
 * examination protocol broker translating domain objects to persistence entities
 */
public class ExaminationProtocolBroker extends EntityBroker {
    public ExaminationProtocolBroker() {
        super(ExaminationProtocol.class, ExaminationProtocolEntity.class);
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
    protected IDomain persistentToDomain(IEntity entity) throws NoBrokerMappedException, BadConnectionException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        ExaminationProtocolEntity examinationProtocolEntity = (ExaminationProtocolEntity) entity;
        Doctor doctor = null;
        Orthoptist orthoptist = null;
        if (examinationProtocolEntity.getUserId() > 0) {
            for (Object obj : Facade.getInstance().getAll(Doctor.class)) {
                if (((Doctor) obj).getUserId() == examinationProtocolEntity.getUserId()) {
                    doctor = (Doctor) obj;
                }
            }
            for (Object obj : Facade.getInstance().getAll(Orthoptist.class)) {
                if (((Orthoptist) obj).getUserId() == examinationProtocolEntity.getUserId()) {
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
        if (examinationProtocolEntity.getPatientId() != null) {
            patient = (Patient) Facade.getInstance().getBroker(Patient.class)
                    .persistentToDomain(examinationProtocolEntity.getPatient());
        }

        return new ExaminationProtocol(examinationProtocolEntity.getId(), examinationProtocolEntity.getStartTime(),
                examinationProtocolEntity.getEndTime(),
                examinationProtocolEntity.getDescription(), patient, doctor, orthoptist, diagnosis);
    }

    /**
     * Converts a domain object to persitency entity
     * @param obj that needs to be converted
     * @return return a persitency entity
     */
    @Override
    protected IEntity domainToPersistent(IDomain obj) throws NoBrokerMappedException, BadConnectionException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        ExaminationProtocol examinationProtocol = (ExaminationProtocol) obj;

        PatientEntity patientEntity = null;
        if (examinationProtocol.getPatient() != null) {

            patientEntity = (PatientEntity) Facade.getInstance().getBroker(Patient.class)
                    .domainToPersistent(examinationProtocol.getPatient());
        }

        UserEntity userEntity = null;
        if (examinationProtocol.getDoctor() != null) {

            userEntity = ((DoctorEntity) Facade.getInstance().getBroker(Doctor.class)
                    .domainToPersistent(examinationProtocol.getDoctor())).getUser();
        }
        if (examinationProtocol.getOrthoptist() != null) {

            userEntity = ((OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class)
                    .domainToPersistent(examinationProtocol.getOrthoptist())).getUser();
        }

        DiagnosisEntity diagnosisEntity = null;
        if (examinationProtocol.getDiagnosis() != null) {
            diagnosisEntity = (DiagnosisEntity) Facade.getInstance().getBroker(Diagnosis.class)
                    .domainToPersistent(examinationProtocol.getDiagnosis());
        }

        return new ExaminationProtocolEntity(examinationProtocol.getId(),
                new Timestamp(examinationProtocol.getStartTime().getTime()),
                new Timestamp(examinationProtocol.getEndTime().getTime()),
                examinationProtocol.getDescription(), patientEntity, userEntity,
                diagnosisEntity);
    }

	/**
	 * save the examination protocol (override methog because of transient instance user)
	 * @param session that should be used
	 * @param domainObj that needs to be saved
	 * @return
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 */
	@Override
	public boolean saveEntity(ISession session, IDomain domainObj) throws BadConnectionException, NoBrokerMappedException {
		log.info("save " + _domainClass.toString() + " with ID " + domainObj.getId());

		ExaminationProtocolEntity entity = (ExaminationProtocolEntity) domainToPersistent(domainObj);

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
		} catch (BadSessionException | AlreadyInTransactionException | NoTransactionException e) {
			log.error(e.getMessage());
			throw new BadConnectionException();
		} catch (ClassNotMappedException e) {
			log.error(e.getMessage());
			throw new NoBrokerMappedException();
		}

		log.info(_domainClass.toString() + " with ID " + domainObj.getId() + " saved");

		return returnValue;
	}
}
