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
import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.persistence.entity.DiagnosisEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.MedicineEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * MedicineBroker.java Created by oculus on 08.05.15.
 */
class MedicineBroker extends EntityBroker implements ISearch {
	public MedicineBroker() {
		super(Medicine.class, MedicineEntity.class);
		addDomainClassMapping(IMedicine.class);
	}

	/**
	 * search for medicine
	 *
	 * @param session
	 * 		to be used
	 * @param params
	 * 		in order (patientId)
	 *
	 * @return medicine collection
	 *
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws InvalidSearchParameterException
	 * @throws BadSessionException
	 * @throws ClassNotMappedException
	 * @throws DatabaseOperationException
	 * @throws SearchInterfaceNotImplementedException
	 */
	@Override
	public Collection search(ISession session, String... params)
			throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException,
			       BadSessionException, ClassNotMappedException, DatabaseOperationException,
			       SearchInterfaceNotImplementedException {
		Collection<Object> searchResult = null;

		searchResult = session.search("getMedicineByPatientId", params);

		Collection<Medicine> result = new LinkedList<>();
		for (Object o : searchResult) {
			result.add((Medicine) persistentToDomain((MedicineEntity) o));
		}

		return result;
	}

	/**
	 * converts a persitency entity to a domain object
	 *
	 * @param entity that needs to be converted
	 * @return domain object that is created from entity
	 * @throws NoBrokerMappedException
	 * @throws BadConnectionException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 * @throws SearchInterfaceNotImplementedException
	 * @throws InvalidSearchParameterException
	 */
	@Override
	protected IDomain persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		MedicineEntity medicineEntity = (MedicineEntity) entity;
		Medicine medicine = new Medicine();
		medicine.setId(medicineEntity.getId());
		medicine.setDiagnosis((IDiagnosis) Facade.getInstance().getBroker(IDiagnosis.class).persistentToDomain(medicineEntity.getDiagnosis()));
		medicine.setDose(medicineEntity.getDose());
		medicine.setName(medicineEntity.getName());
		return medicine;
	}

	/**
	 * Converts a domain object to persitency entity
	 *
	 * @param obj that needs to be converted
	 * @return return a persitency entity
	 * @throws NoBrokerMappedException
	 * @throws BadConnectionException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 */
	@Override
	protected IEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		Medicine medicine = (Medicine) obj;
		MedicineEntity medicineEntity = new MedicineEntity();
		medicineEntity.setId(medicine.getId());
		medicineEntity.setDiagnosis((DiagnosisEntity) Facade.getInstance().getBroker(IDiagnosis.class)
		                                                    .domainToPersistent(medicine.getTeamFDiagnosis()));
		medicineEntity.setDose(medicine.getDose());
		medicineEntity.setName(medicine.getName());
		return medicineEntity;
	}
}