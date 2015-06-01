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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.doctor.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IOrthoptist;
import at.oculus.teamf.domain.entity.interfaces.IQueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.entity.DoctorEntity;
import at.oculus.teamf.persistence.entity.OrthoptistEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.entity.QueueEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * queue broker translating domain objects to persistence entities
 */
class QueueBroker extends EntityBroker<QueueEntry, QueueEntity> implements ISearch<QueueEntry> {

    public QueueBroker() {
        super(QueueEntry.class, QueueEntity.class);
        addDomainClassMapping(IQueueEntry.class);
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
    protected QueueEntry persistentToDomain(QueueEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        IPatient patient = Facade.getInstance().getById(IPatient.class, entity.getPatientId());
        IUser user = null;
        if (entity.getDoctorId() != null) {
            user = Facade.getInstance().getById(IDoctor.class, entity.getDoctorId());
        }
        if (user == null && entity.getOrthoptistId() != null) {
            user = Facade.getInstance().getById(Orthoptist.class, entity.getOrthoptistId());
        }
        return new QueueEntry(entity.getId(), patient, user, entity.getQueueIdParent(),
                entity.getArrivalTime());
    }

    /**
     * Converts a domain object to persitency entity
     *
     * @param queueEntry that needs to be converted
     * @return return a persitency entity
     */
    @Override
    protected QueueEntity domainToPersistent(QueueEntry queueEntry) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        IDoctor doctor = queueEntry.getDoctor();
        IOrthoptist orthoptist = queueEntry.getOrthoptist();

        DoctorEntity doctorEntity = null;
        if (doctor != null) {
            try {
                doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(IDoctor.class).domainToPersistent(doctor);
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }
        }
        OrthoptistEntity orthoptistEntity = null;
        if (orthoptist != null) {

            orthoptistEntity = (OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class).domainToPersistent(
                    orthoptist);

        }

        PatientEntity patientEntity = null;

        patientEntity = (PatientEntity) Facade.getInstance().getBroker(IPatient.class).domainToPersistent(
                    queueEntry.getPatient());


        QueueEntity queueEntityParent = null;
        if (queueEntry.getQueueIdParent() != null && !queueEntry.getQueueIdParent().equals(0)) {
            queueEntityParent = (QueueEntity) Facade.getInstance().getBroker(QueueEntry.class).domainToPersistent((QueueEntry) Facade.getInstance().getById(QueueEntry.class, queueEntry.getQueueIdParent()));
        }

        return new QueueEntity(queueEntry.getId(), doctorEntity, orthoptistEntity, patientEntity, queueEntityParent, queueEntry.getArrivalTime());
    }

    @Override
    public Collection<QueueEntry> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException, ClassNotMappedException, DatabaseOperationException {
        if (params.length == 0) {
            return null;
        }

        String query = "";
        Collection<QueueEntity> result = null;
        switch (params[0]) {
            case("Doctor"): {
                query = "getDocotorQueueEntries";
                result = (Collection<QueueEntity>)(Collection<?>)session.search(query, params[1]);
                break;
            }
            case("Orthopist"): {
                query = "getOrthoptistQueueEntries";
                result =  (Collection<QueueEntity>)(Collection<?>)session.search(query, params[1]);
                break;
            }
            case("General"): {
                query = "getGeneralQueueEntries";
                result =  (Collection<QueueEntity>)(Collection<?>)session.search(query);
                break;
            }
            default: {
                return new LinkedList<>();
            }
        }

        Collection<QueueEntry> domainEntries = new LinkedList<>();
        for(QueueEntity qw : result) {
            domainEntries.add(persistentToDomain(qw));
        }
        return domainEntries;
    }
}