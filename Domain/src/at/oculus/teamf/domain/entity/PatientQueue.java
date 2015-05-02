/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

// Todo: add docs, implement equals, logging

/**
 * @author Fabian Salzgeber
 */
public class PatientQueue implements ILogger, IPatientQueue {

    //<editor-fold desc="Attributes">
    private User _user;
    private LinkedList<QueueEntry> _entries;
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
    public LinkedList<QueueEntry> getEntries() throws NoBrokerMappedException, BadConnectionException {
        return _entries;
    }


    //TODO Voruntersuchung static Methode

    /**
     * add patient to queue
     *
     * @param patient     patient to be added
     * @param arrivaltime time of arrival
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    public void addPatient(Patient patient, Timestamp arrivaltime) throws NoBrokerMappedException, BadConnectionException {
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
            Facade.getInstance().save(queueEntryNew);
        }

        _entries.add(queueEntryNew);
    }

    /**
     * removes patient from queue
     *
     * @param patient patient to be removed
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     * @throws InvalidSearchParameterException
     */
    public void removePatient(Patient patient) throws NoBrokerMappedException, BadConnectionException, InvalidSearchParameterException {
        QueueEntry queueEntryDel = null;
        QueueEntry queueEntryChd = null;

        // remove patient from queuelist
        for (QueueEntry qe : _entries) {
            if (qe.getPatient().getId() == patient.getId()) {
                queueEntryDel = qe;
                break;
            }
        }

        if (queueEntryDel != null) {
            for (QueueEntry qe : _entries) {
                // child entry update
                if (queueEntryDel.getQueueIdParent() == null) {
                    if (qe.getOrthoptist() == queueEntryDel.getOrthoptist() && qe.getDoctor() == queueEntryDel.getDoctor()) {
                        // update to null
                        queueEntryChd = qe;
                        queueEntryChd.setQueueIdParent(null);
                        break;
                    }
                } else {
                    if (qe.getQueueIdParent() != null) {
                        if (qe.getQueueIdParent() == queueEntryDel.getId()) {
                            // update to parent id from entry to delete
                            queueEntryChd = qe;
                            queueEntryChd.setQueueIdParent(queueEntryDel.getQueueIdParent());
                            break;
                        }
                    }
                }
            }
        }

        if (queueEntryChd != null) {
            Facade.getInstance().save(queueEntryChd);
        }
        Facade.getInstance().delete(queueEntryDel);

        // reload entries
        getEntries();

        //log.info("[REMOVEPatient] Removed patient '" + patient.getFirstName() + " " + patient.getLastName() + "' from queue of " + patient.getDoctor().getLastName());
    }
}