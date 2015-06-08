/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.queue;

import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotRemovePatientFromQueueException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Fabian Salzgeber
 */
public class PatientQueue implements ILogger, IPatientQueue {

    //<editor-fold desc="Attributes">
    private IUser _user;
    private LinkedList<IQueueEntry> _entries;
    //</editor-fold>

    /**
     * rebuild the queue and return the entries
     *
     * @return linked list of queue entries
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    @Override
    public Collection<IQueueEntry> getEntries() {
        return _entries;
    }

    @Override
    public void setEntries(Collection<IQueueEntry> entries) {
        _entries = new LinkedList<>(entries);
    }

    @Override
    public void setUser(IUser user) {
        _user = user;
    }

    @Override
    public void addPatient(IPatient patient, Timestamp arrivaltime) throws CouldNotAddPatientToQueueException {
        log.debug("add patient " + patient + " to " + this);

        // patient already in queue
        if (patient != null && !_entries.isEmpty()) {
            if (_entries.contains(patient)) {
                throw new CouldNotAddPatientToQueueException();
            }
        }

        // id of last queue element
        Integer parentId = null;
        if (!_entries.isEmpty()) {
            parentId = _entries.getLast().getId();
        }

        // new queue entry
        QueueEntry queueEntryNew = new QueueEntry(0, patient, _user, parentId, arrivaltime);

        _entries.add(queueEntryNew);
    }

    /**
     * removes the patient from this queue
     *
     * @param patient to remove
     * @throws CouldNotRemovePatientFromQueueException
     */
    @Override
    public IQueueEntry removePatient(IPatient patient) throws CouldNotRemovePatientFromQueueException {
        log.debug("remove patient " + patient + " from " + this);

        IQueueEntry queueEntryDel = null;
        IQueueEntry queueEntryChd = null;

        for (IQueueEntry qe : _entries) {
            //finde entry to remove
            if (qe.getPatient().getId() == patient.getId()) {
                queueEntryDel = qe;
                break;
            }
        }

        if (queueEntryDel == null) {
            throw new CouldNotRemovePatientFromQueueException();
        }

        for (IQueueEntry qe : _entries) {
            //finde child of entry to remove
            if ((qe.getQueueIdParent()) != null && (qe.getQueueIdParent().equals(queueEntryDel.getId()))) {
                queueEntryChd = qe;
                break;
            }
        }

        if (queueEntryChd != null) {
            queueEntryChd.setQueueIdParent(queueEntryDel.getQueueIdParent());
        }


        queueEntryDel.setQueueIdParent(null);

        _entries.remove(queueEntryDel);

        return queueEntryDel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientQueue)) return false;

        PatientQueue that = (PatientQueue) o;

        if (_entries != null ? !_entries.equals(that._entries) : that._entries != null) return false;
        if (_user != null ? !_user.equals(that._user) : that._user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _user != null ? _user.hashCode() : 0;
        result = 31 * result + (_entries != null ? _entries.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return _user + "'s queue";
    }

    @Override
    public int getId() {
        //not used
        return 0;
    }

    @Override
    public void setId(int id) {
        //not used
    }
}