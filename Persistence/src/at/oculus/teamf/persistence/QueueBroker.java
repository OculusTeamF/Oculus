/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.entities.DoctorEntity;
import at.oculus.teamf.persistence.entities.OrthoptistEntity;
import at.oculus.teamf.persistence.entities.PatientEntity;
import at.oculus.teamf.persistence.entities.QueueEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.NoBrokerMappedException;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<QueueEntry, QueueEntity> {

	public QueueBroker() {
		super(QueueEntry.class, QueueEntity.class);
	}

	@Override
	protected QueueEntry persitentToDomain(QueueEntity entity) throws FacadeException {
		Patient patient = (Patient) Facade.getInstance().getById(Patient.class, entity.getPatientId());
		Doctor doctor = null;
		if (entity.getDoctorId() != null) {
			doctor = (Doctor) Facade.getInstance().getById(Doctor.class, entity.getDoctorId());
		}
		Orthoptist orthoptist = null;
		if (entity.getOrthoptistId() != null) {
			orthoptist = (Orthoptist) Facade.getInstance().getById(Orthoptist.class, entity.getOrthoptistId());
		}
		return new QueueEntry(entity.getId(), patient, doctor, orthoptist, entity.getQueueIdParent(),
		                      entity.getArrivalTime());
	}

	@Override
	protected QueueEntity domainToPersitent(QueueEntry queueEntry) {
		Doctor doctor = queueEntry.getDoctor();
		Orthoptist orthoptist = queueEntry.getOrthoptist();

		Integer doctorId = null;
        DoctorEntity doctorEntity = null;
		if (doctor != null) {
			doctorId = doctor.getId();
            try {
                doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(Doctor.class).domainToPersitent(doctor);
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }
        }
		Integer orthoptistId = null;
        OrthoptistEntity orthoptistEntity = null;
		if (orthoptist != null) {
			orthoptistId = orthoptist.getId();
            try {
                orthoptistEntity = (OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class).domainToPersitent(orthoptist);
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }
		}

        PatientEntity patientEntity = null;
        try {
            patientEntity = (PatientEntity) Facade.getInstance().getBroker(Patient.class).domainToPersitent(queueEntry.getPatient());
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // TODO QueueParent laden
        /*QueueEntity queueEntity = null;
        if (queueEntry.getQueueIdParent() != null) {
            try {
                queueEntity = (QueueEntity) Facade.getInstance().getBroker(QueueEntry.class).domainToPersitent((QueueEntry) Facade.getInstance().getById(QueueEntry.class, queueEntry.getQueueIdParent()));
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            } catch (FacadeException e) {
                e.printStackTrace();
            }
        }*/
		/*return new QueueEntity(queueEntry.getId(), doctorId, orthoptistId, queueEntry.getPatient().getId(),
		                       queueEntry.getQueueIdParent(), queueEntry.getArrivalTime());*/
        return new QueueEntity(queueEntry.getId(), doctorEntity, orthoptistEntity, patientEntity, queueEntry.getPatient().getId(), null, queueEntry.getArrivalTime());
    }
}