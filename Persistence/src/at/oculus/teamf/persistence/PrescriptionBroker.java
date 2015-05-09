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
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.Prescription;
import at.oculus.teamf.domain.entity.PrescriptionEntry;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.entity.PrescriptionEntity;
import at.oculus.teamf.persistence.entity.PrescriptionEntryEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * PrescriptionBroker.java Created by oculus on 08.05.15.
 */
public class PrescriptionBroker extends EntityBroker implements ICollectionReload {
	public PrescriptionBroker() {
		super(Prescription.class, PrescriptionEntity.class);
		addDomainClass(IPrescription.class);
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
		PrescriptionEntity prescriptionEntity = (PrescriptionEntity) entity;
		Prescription prescription = new Prescription();
		prescription.setId(prescriptionEntity.getId());
		prescription.setPatient((Patient) Facade.getInstance().getBroker(Patient.class)
		                                        .persistentToDomain(prescriptionEntity.getPatient()));
		prescription.setIssueDate(prescriptionEntity.getIssueDate());
		prescription.setLastPrint(prescriptionEntity.getLastPrint());
		return prescription;
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
		Prescription prescription = (Prescription) obj;
		PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
		prescriptionEntity.setId(prescription.getId());
		prescriptionEntity.setPatient((PatientEntity) Facade.getInstance().getBroker(Patient.class)
		                                                    .domainToPersistent(prescription.getPatient()));
		if (prescription.getIssueDate() != null) {
			prescriptionEntity.setIssueDate(new Timestamp(prescription.getIssueDate().getTime()));
		}
		if (prescription.getLastPrint() != null) {
			prescriptionEntity.setLastPrint(new Timestamp(prescription.getLastPrint().getTime()));
		}
		return prescriptionEntity;
	}

	/**
	 * reload a collection of prescription
	 * @param session to be used
	 * @param obj prescription
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
	public void reload(ISession session, Object obj, Class clazz)
			throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException, BadSessionException,
			       DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException,
			       InvalidSearchParameterException {
		if (clazz == PrescriptionEntry.class) {
			((Prescription) obj).setPrescriptionEntries(reloadPrescriptionEntries(session, obj));
		} else {
			throw new InvalidReloadClassException();
		}
	}

	/**
	 * reload entries of prescription
	 * @param session to be used
	 * @param obj prescription
	 * @return entry collection
	 * @throws BadConnectionException
	 * @throws NoBrokerMappedException
	 * @throws DatabaseOperationException
	 * @throws ClassNotMappedException
	 * @throws SearchInterfaceNotImplementedException
	 * @throws InvalidSearchParameterException
	 */
	private Collection<PrescriptionEntry> reloadPrescriptionEntries(ISession session, Object obj)
			throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		log.debug("reloading prescription entries");
		ReloadComponent reloadComponent = new ReloadComponent(PrescriptionEntity.class, PrescriptionEntry.class);
		return reloadComponent.reloadCollection(session, ((Prescription) obj).getId(), new PrescriptionEntryLoader());
	}

	private class PrescriptionEntryLoader implements ICollectionLoader<PrescriptionEntryEntity> {
		@Override
		public Collection<PrescriptionEntryEntity> load(Object databaseEntity) {
			return ((PrescriptionEntity) databaseEntity).getPrescriptionEntries();
		}
	}
}