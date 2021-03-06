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
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.examination.ExaminationResult;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.examination.IExaminationProtocol;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * examination protocol broker translating domain objects to persistence entities
 */
class ExaminationProtocolBroker extends EntityBroker implements ICollectionReload {
    public ExaminationProtocolBroker() {
        super(ExaminationProtocol.class, ExaminationProtocolEntity.class);
        addDomainClassMapping(IExaminationProtocol.class);
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
    protected IDomain persistentToDomain(IEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        ExaminationProtocolEntity examinationProtocolEntity = (ExaminationProtocolEntity) entity;
        IDoctor doctor = null;
        IOrthoptist orthoptist = null;

        if (examinationProtocolEntity.getUserId() > 0) {
            Collection<IDoctor> doctors = (Collection<IDoctor>) (Collection<?>) Facade.getInstance().search(IDoctor.class,examinationProtocolEntity.getUserId()+"");
            for(IDoctor d : doctors){
                doctor = d;
            }
            if(doctor == null){
                Collection<IOrthoptist> orthoptists = (Collection<IOrthoptist>) (Collection<?>) Facade.getInstance().search(IOrthoptist.class,examinationProtocolEntity.getUserId()+"");
                for(IOrthoptist o : orthoptists){
                    orthoptist = o;
                }
            }
        }

        IDiagnosis diagnosis = null;
        if (examinationProtocolEntity.getDiagnosisId() != null) {
            diagnosis = (IDiagnosis) Facade.getInstance().getBroker(IDiagnosis.class)
                    .persistentToDomain(examinationProtocolEntity.getDiagnosis());
        }

        IPatient patient = null;
        if (examinationProtocolEntity.getPatientId() != null) {
            patient = (IPatient) Facade.getInstance().getBroker(IPatient.class)
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
    protected IEntity domainToPersistent(IDomain obj) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        ExaminationProtocol examinationProtocol = (ExaminationProtocol) obj;

        PatientEntity patientEntity = null;
        if (examinationProtocol.getPatient() != null) {

            patientEntity = (PatientEntity) Facade.getInstance().getBroker(IPatient.class)
                    .domainToPersistent(examinationProtocol.getPatient());
        }

        UserEntity userEntity = null;
        if (examinationProtocol.getDoctor() != null) {

            userEntity = ((DoctorEntity) Facade.getInstance().getBroker(IDoctor.class)
                    .domainToPersistent(examinationProtocol.getDoctor())).getUser();
        }
        if (examinationProtocol.getOrthoptist() != null) {

            userEntity = ((OrthoptistEntity) Facade.getInstance().getBroker(IOrthoptist.class)
                    .domainToPersistent(examinationProtocol.getOrthoptist())).getUser();
        }

        DiagnosisEntity diagnosisEntity = null;
        if (examinationProtocol.getTeamFDiagnosis() != null) {
            diagnosisEntity = (DiagnosisEntity) Facade.getInstance().getBroker(IDiagnosis.class)
                    .domainToPersistent(examinationProtocol.getTeamFDiagnosis());
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
	public void saveEntity(ISession session, IDomain domainObj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
		log.info("save " + _domainClass.toString() + " with ID " + domainObj.getId());

		ExaminationProtocolEntity entity = (ExaminationProtocolEntity) domainToPersistent(domainObj);

        try {
			session.beginTransaction();

			session.saveOrUpdate(entity.getUser());
			session.saveOrUpdate(entity);

            session.commit();

			// update IDs when commit was successful
            domainObj.setId(entity.getId());
		} catch (BadSessionException | AlreadyInTransactionException | NoTransactionException e) {
			log.error(e.getMessage());
			throw new BadConnectionException();
		} catch (ClassNotMappedException e) {
			log.error(e.getMessage());
			throw new NoBrokerMappedException();
		} catch (CanNotStartTransactionException | CanNotCommitTransactionException e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException(e);
        }

        log.info(_domainClass.toString() + " with ID " + domainObj.getId() + " saved");
	}

	private class ExaminationResultsLoader implements ICollectionLoader<ExaminationResultEntity> {

		@Override
		public Collection<ExaminationResultEntity> load(Object databaseEntity) {
			return ((ExaminationProtocolEntity) databaseEntity).getResults();
		}
	}

	@Override
	public void reload(ISession session, Object obj, Class clazz) throws BadConnectionException, NoBrokerMappedException,
            InvalidReloadClassException, BadSessionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		if (clazz == ExaminationResult.class) {
			((ExaminationProtocol) obj).setResults(reloadExaminationResults(session, obj));
		} else {
			throw new InvalidReloadClassException();
		}
	}

	private Collection<ExaminationResult> reloadExaminationResults (ISession session, Object obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		ReloadComponent reloadComponent = new ReloadComponent(ExaminationProtocolEntity.class, ExaminationResult.class);
		log.debug("reloading examination results");
		return reloadComponent.reloadCollection(session, ((ExaminationProtocol) obj).getId(), new ExaminationResultsLoader());
	}
}
