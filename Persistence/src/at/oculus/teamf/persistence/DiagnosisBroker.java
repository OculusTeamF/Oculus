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
import at.oculus.teamf.domain.entity.diagnosis.Diagnosis;
import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.visualadi.VisualAid;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * diagnosis broker translating domain objects to persistence entities
 */
class DiagnosisBroker extends EntityBroker implements ISearch, ICollectionReload {
    public DiagnosisBroker() {
        super(IDiagnosis.class, DiagnosisEntity.class);
		addDomainClassMapping(Diagnosis.class);
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

        IDoctor doctor = null;
        if (diagnosisEntity.getDoctor() != null) {
            doctor = Facade.getInstance().getById(IDoctor.class, diagnosisEntity.getDoctorId());
        }

		IDiagnosis diagnosis = (IDiagnosis) DomainWrapperFactory.getInstance().create(IDiagnosis.class);
		diagnosis.setTitle(diagnosisEntity.getTitle());
		diagnosis.setDescription(diagnosisEntity.getDescription());
		diagnosis.setDoctor(doctor);
		diagnosis.setId(diagnosisEntity.getId());

		return diagnosis;
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
		IDiagnosis diagnosis = (IDiagnosis) obj;

        DoctorEntity doctorEntity = null;
        if (diagnosis.getDoctor() != null) {
            doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(IDoctor.class).domainToPersistent(diagnosis.getDoctor());
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
    public Collection<IDiagnosis> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException, DatabaseOperationException, ClassNotMappedException {
        if (params.length == 1) {
            Collection<Object> diagnosisEntities = session.search("getAllDiagnosisOfPatient", params[0]);

			Collection<IDiagnosis> diagnosises = new LinkedList<>();
			for(Object o : diagnosisEntities) {
				diagnosises.add((IDiagnosis) persistentToDomain((DiagnosisEntity)o));
			}

			return diagnosises;

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
			((IDiagnosis) obj).setMedicine(reloadMedicine(session, obj));
		} else if (clazz == VisualAid.class) {
			((IDiagnosis) obj).setVisualAid(reloadVisualAid(session, obj));
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
		return reloadComponent.reloadCollection(session, ((IDiagnosis) obj).getId(), new MedicineLoader());
	}

	/**
	 * reload visualaid of diagnosis
	 *
	 * @param session
	 * 		to be used
	 * @param obj
	 * 		diagnosis
	 *
	 * @return medicine collection
	 *
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 * @throws SearchInterfaceNotImplementedException
	 * @throws InvalidSearchParameterException
	 */
    private Collection<IVisualAid> reloadVisualAid(ISession session, Object obj)
            throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("reloading visual aid");
		ReloadComponent reloadComponent = new ReloadComponent(DiagnosisEntity.class, VisualAid.class);
		return reloadComponent.reloadCollection(session, ((IDiagnosis) obj).getId(), new VisualAidLoader());
	}

	private class MedicineLoader implements ICollectionLoader<MedicineEntity> {

		@Override
		public Collection<MedicineEntity> load(Object databaseEntity) {
			return ((DiagnosisEntity) databaseEntity).getMedicine();
		}
	}

	private class VisualAidLoader implements ICollectionLoader<VisualAidEntity> {

		@Override
		public Collection<VisualAidEntity> load(Object databaseEntity) {
			return ((DiagnosisEntity) databaseEntity).getVisualAid();
		}
	}
}
