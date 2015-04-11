/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.persistence.entities.QueueEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<QueueEntry, QueueEntity> {

	public QueueBroker() {
		super(QueueEntry.class, QueueEntity.class);
	}

	@Override
	protected QueueEntry persitentToDomain(QueueEntity entity) throws FacadeException {
		/*return new QueueEntry(entity.getId(),
				(Patient) Facade.getInstance().getById(Patient.class, entity.getPatientId()),
				(Orthoptist) Facade.getInstance().getById(Orthoptist.class, entity.getOrthoptistId()),
				entity.getQueueIdParent(), new Timestamp(10));*/
		Patient patient = (Patient) Facade.getInstance().getById(Patient.class, entity.getPatientId());
		Doctor doctor = null;
		if (entity.getDoctorId() != null) {
			doctor = (Doctor) Facade.getInstance().getById(Doctor.class, entity.getDoctorId());
		}
		Orthoptist orthoptist = null;
		if (entity.getDoctorId() != null) {
			orthoptist = (Orthoptist) Facade.getInstance().getById(Orthoptist.class, entity.getDoctorId());
		}
		return new QueueEntry(entity.getId(), patient, doctor, orthoptist, entity.getQueueIdParent(),
		                      entity.getArrivalTime());
	}

	@Override
	protected QueueEntity domainToPersitent(QueueEntry queueEntry) {
		Doctor doctor = queueEntry.getDoctor();
		Orthoptist orthoptist = queueEntry.getOrthoptist();

		Integer doctorId = null;
		if (doctor != null) {
			doctorId = doctor.getId();
		}
		Integer orthoptistId = null;
		if (orthoptist != null) {
			orthoptistId = orthoptist.getId();
		}
		return new QueueEntity(queueEntry.getId(), doctorId, orthoptistId, queueEntry.getPatient().getPatientID(),
		                       queueEntry.getQueueIdParent(), queueEntry.getArrivalTime());
	}
}