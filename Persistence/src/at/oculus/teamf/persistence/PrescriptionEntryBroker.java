/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.Medicine;
import at.oculus.teamf.domain.entity.Prescription;
import at.oculus.teamf.domain.entity.PrescriptionEntry;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.MedicineEntity;
import at.oculus.teamf.persistence.entity.PrescriptionEntity;
import at.oculus.teamf.persistence.entity.PrescriptionEntryEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

/**
 * PrescriptionEntryBroker.java Created by oculus on 08.05.15.
 */
class PrescriptionEntryBroker extends EntityBroker {
	public PrescriptionEntryBroker() {
		super(PrescriptionEntry.class, PrescriptionEntryEntity.class);
		addDomainClassMapping(IPrescriptionEntry.class);
	}

	/**
	 * converts a persitency entity to a domain object
	 *
	 * @param entity
	 * 		that needs to be converted
	 *
	 * @return domain object that is created from entity
	 *
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
		PrescriptionEntryEntity prescriptionEntryEntity = (PrescriptionEntryEntity) entity;
		PrescriptionEntry prescriptionEntry = new PrescriptionEntry();
		prescriptionEntry.setId(prescriptionEntryEntity.getId());
        if(prescriptionEntryEntity.getMedicine()!=null) {
            prescriptionEntry.setMedicine((Medicine) Facade.getInstance().getBroker(Medicine.class)
                    .persistentToDomain(prescriptionEntryEntity.getMedicine()));
        }
        if(prescriptionEntryEntity.getPrescription()!=null) {
            prescriptionEntry.setPrescription((Prescription) Facade.getInstance().getBroker(Prescription.class)
                    .persistentToDomain(
                            prescriptionEntryEntity.getPrescription()));
        }
		return prescriptionEntry;
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
		PrescriptionEntry prescriptionEntry = (PrescriptionEntry) obj;
		PrescriptionEntryEntity prescriptionEntryEntity = new PrescriptionEntryEntity();
		prescriptionEntryEntity.setId(prescriptionEntry.getId());

        if(prescriptionEntry.getMedicine()!=null) {
            prescriptionEntryEntity.setMedicine((MedicineEntity) Facade.getInstance().getBroker(Medicine.class)
                    .domainToPersistent(
                            prescriptionEntry.getMedicine()));
			prescriptionEntryEntity.setMedicineId(prescriptionEntry.getMedicine().getId());
        }

		if (prescriptionEntry.getPrescription() != null) {
			prescriptionEntryEntity.setPrescription(
					(PrescriptionEntity) Facade.getInstance().getBroker(Prescription.class)
					                           .domainToPersistent(prescriptionEntry.getPrescription()));
			prescriptionEntryEntity.setPrescriptionId(prescriptionEntry.getPrescription().getId());
        }

		return prescriptionEntryEntity;
	}
}