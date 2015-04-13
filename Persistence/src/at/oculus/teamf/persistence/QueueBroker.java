/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.persistence.EntityBroker;
import at.oculus.teamf.persistence.entities.QueueEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<QueueEntry, QueueEntity> {

	public QueueBroker() {
		super(QueueEntry.class, QueueEntity.class);
	}

	@Override
	protected QueueEntry persitentToDomain(QueueEntity entity) throws FacadeException {
		return new QueueEntry(entity.getId(), entity.getDoctorId(), entity.getOrthoptistId(), entity.getPatientId(),
		                      entity.getQueueIdParent(), entity.getArrivalTime(),
		                      (Patient) Facade.getInstance().getById(Patient.class, entity.getPatientId()));
	}

	@Override
	protected QueueEntity domainToPersitent(QueueEntry obj) {
		//TODO reverse
		return new QueueEntity();
	}
}