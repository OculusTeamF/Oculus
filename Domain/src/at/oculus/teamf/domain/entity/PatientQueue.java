/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotRemovePatientFromQueueException;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IQueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

// Todo: add docs, implement equals, logging

/**
 * @author Fabian Salzgeber
 */
public class PatientQueue implements ILogger, IPatientQueue {

    //<editor-fold desc="Attributes">
    private IUser _user;
    private LinkedList<IQueueEntry> _entries;
    //</editor-fold>

    public PatientQueue(User user, Collection<QueueEntry> entries) {
        _entries = new LinkedList<>();
        _entries.addAll(entries);
        _user = user;
    }

    /**
     * rebuild the queue and return the entries
     *
     * @return linked list of queue entries
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    //Todo: Proxy
    @Override
    public LinkedList<IQueueEntry> getEntries()  {
        return _entries;
    }

    @Override
    public void addPatient(IPatient patient, Timestamp arrivaltime) throws CouldNotAddPatientToQueueException {
        // id of last queue element
        Integer parentId = null;
        if (!_entries.isEmpty()) {
            parentId = _entries.getLast().getId();
        }

        // new queue entry
        QueueEntry queueEntryNew = null;
        // TODO Umbau QueueEntry auf User statt Doc und Orth extra
        if (_user instanceof Doctor) {
            queueEntryNew = new QueueEntry(0, patient, (Doctor) _user, null, parentId, arrivaltime);
        } else if (_user instanceof Orthoptist) {
            queueEntryNew = new QueueEntry(0, patient, null, (Orthoptist) _user, parentId, arrivaltime);
        } else {
            queueEntryNew = new QueueEntry(0, patient, null, null, parentId, arrivaltime);
        }

        // save
        if (queueEntryNew != null) {
            try {
                Facade.getInstance().save(queueEntryNew);
            } catch (DatabaseOperationException  | NoBrokerMappedException | BadConnectionException e) {
                log.error(e.getMessage());
                throw new CouldNotAddPatientToQueueException();
            }
        }

        _entries.add(queueEntryNew);
    }

    @Override
    public void removePatient(IPatient patient) throws CouldNotRemovePatientFromQueueException {
        IQueueEntry queueEntryDel = null;
        IQueueEntry queueEntryChd = null;

        for (IQueueEntry qe : _entries) {
            //finde entry to remove
            if (qe.getPatient().getId() == patient.getId()) {
                queueEntryDel = qe;
                break;
            }
        }

        if(queueEntryDel == null) {
            throw  new CouldNotRemovePatientFromQueueException();
        }

        for (IQueueEntry qe : _entries) {
            //finde child of entry to remove
            if ((qe.getQueueIdParent()) != null && (qe.getQueueIdParent().equals(queueEntryDel.getId()))) {
                queueEntryChd = qe;
                break;
            }
        }

        if(queueEntryChd != null) {
            queueEntryChd.setQueueIdParent(queueEntryDel.getQueueIdParent());
        }


        queueEntryDel.setQueueIdParent(null);

        _entries.remove(queueEntryDel);
        try {
            if (queueEntryChd != null) {
                Facade.getInstance().save(queueEntryChd);
            }
            Facade.getInstance().delete(queueEntryDel);
        } catch (BadConnectionException | NoBrokerMappedException | InvalidSearchParameterException | DatabaseOperationException e) {
            log.error(e.getMessage());
            throw new CouldNotRemovePatientFromQueueException();
        }
    }
}