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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.OrthoptistEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.entity.QueueEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<QueueEntry, QueueEntity> {

    public QueueBroker() {
        super(QueueEntry.class, QueueEntity.class);
    }

    @Override
    protected QueueEntry persistentToDomain(QueueEntity entity) throws NoBrokerMappedException, BadConnectionException {
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
    protected QueueEntity domainToPersistent(QueueEntry queueEntry) throws NoBrokerMappedException, BadConnectionException {
        Doctor doctor = queueEntry.getDoctor();
        Orthoptist orthoptist = queueEntry.getOrthoptist();

        Integer doctorId = null;
        DoctorEntity doctorEntity = null;
        if (doctor != null) {
            doctorId = doctor.getId();
            try {
                doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(Doctor.class).domainToPersistent(doctor);
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }
        }
        Integer orthoptistId = null;
        OrthoptistEntity orthoptistEntity = null;
        if (orthoptist != null) {
            orthoptistId = orthoptist.getId();
            try {
                orthoptistEntity = (OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class).domainToPersistent(
                        orthoptist);
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }
        }

        PatientEntity patientEntity = null;
        try {
            patientEntity = (PatientEntity) Facade.getInstance().getBroker(Patient.class).domainToPersistent(
                    queueEntry.getPatient());
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // TODO QueueParent laden
        QueueEntity queueEntityParent = null;
        if (queueEntry.getQueueIdParent() != null && !queueEntry.getQueueIdParent().equals(0)) {
            queueEntityParent = (QueueEntity) Facade.getInstance().getBroker(QueueEntry.class).domainToPersistent((QueueEntry) Facade.getInstance().getById(QueueEntry.class, queueEntry.getQueueIdParent()));
        }

        return new QueueEntity(queueEntry.getId(), doctorEntity, orthoptistEntity, patientEntity, queueEntityParent, queueEntry.getArrivalTime());
    }
}