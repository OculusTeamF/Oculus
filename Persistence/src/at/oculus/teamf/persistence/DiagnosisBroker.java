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
import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Medicine;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.DiagnosisEntity;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.MedicineEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.util.Collection;

/**
 * diagnosis broker translating domain objects to persistence entities
 */
public class DiagnosisBroker extends EntityBroker implements ISearch, ICollectionReload {
    public DiagnosisBroker() {
        super(Diagnosis.class, DiagnosisEntity.class);
        addDomainClass(IDiagnosis.class);
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
    protected IDomain persistentToDomain(IEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        DiagnosisEntity diagnosisEntity = (DiagnosisEntity) entity;

        Doctor doctor = null;
        if (diagnosisEntity.getDoctor() != null) {
            doctor = Facade.getInstance().getById(Doctor.class, diagnosisEntity.getDoctorId());
        }

        return new Diagnosis(diagnosisEntity.getId(), diagnosisEntity.getTitle(), diagnosisEntity.getDescription(), doctor);
    }

    /**
     * Converts a domain object to persitency entity
     *
     * @param obj that needs to be converted
     * @return return a persitency entity
     */
    @Override
    protected IEntity domainToPersistent(IDomain obj) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        Diagnosis diagnosis = (Diagnosis) obj;

        DoctorEntity doctorEntity = null;
        if (diagnosis.getDoctor() != null) {

            doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(Doctor.class).domainToPersistent(diagnosis.getDoctor());

        }

        return new DiagnosisEntity(diagnosis.getId(), diagnosis.getTitle(), diagnosis.getDescription(), doctorEntity);
    }

	/**
	 * search diagnoses
	 *
	 * @param session
	 * 		to be used
	 * @param params
	 * 		in order (patientId)
	 *
	 * @return
	 *
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws InvalidSearchParameterException
	 * @throws BadSessionException
	 */
	@Override
    public Collection<Diagnosis> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException {
        if (params.length == 1) {
            return (Collection<Diagnosis>) (Collection<?>) session.search("getAllDiagnosisOfPatient", params[0]);
        } else {
	        return null;
        }
    }

	/**
	 * reload collection of diagnosis
	 * @param session to be used
	 * @param obj diagnosis
	 * @param clazz to be reloaded
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws InvalidReloadClassException
	 * @throws BadSessionException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 * @throws SearchInterfaceNotImplementedException
	 * @throws InvalidSearchParameterException
	 */
	@Override
	public void reload(ISession session, Object obj, Class clazz)
			throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException, BadSessionException,
			       DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException,
			       InvalidSearchParameterException {
		if (clazz == Medicine.class) {
			((Diagnosis) obj).setMedicine(reloadMedicine(session, obj));
		} else {
			throw new InvalidReloadClassException();
		}
	}

	/**
	 * reload medicine of diagnosis
	 * @param session to be used
	 * @param obj diagnosis
	 * @return medicine collection
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 * @throws SearchInterfaceNotImplementedException
	 * @throws InvalidSearchParameterException
	 */
	private Collection<Medicine> reloadMedicine(ISession session, Object obj)
			throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("reloading medicine");
		ReloadComponent reloadComponent = new ReloadComponent(DiagnosisEntity.class, Medicine.class);
		return reloadComponent.reloadCollection(session, ((Diagnosis) obj).getId(), new MedicineLoader());
	}

	private class MedicineLoader implements ICollectionLoader<MedicineEntity> {

		@Override
		public Collection<MedicineEntity> load(Object databaseEntity) {
			return ((DiagnosisEntity) databaseEntity).getMedicine();
		}
	}
}
