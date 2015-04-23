/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.*;

/**
 * Todo: add docs, implement equals, logging
 *
 * @author Fabian Salzgeber
 * @date 03.4.2015
 */
public class PatientQueue implements ILogger, IPatientQueue {

    //<editor-fold desc="Attributes">
    private int _userID;
    private LinkedList<QueueEntry> _entries;
    //</editor-fold>

    public PatientQueue(){
        // empty
    }

    public PatientQueue(Doctor doctor){
        _entries = new LinkedList<QueueEntry>();

        // get all queue entities of a doctor
        HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
        QueueEntry actEntry = null;


        //Todo: Exception Handling
        try {
            for(Object obj : Facade.getInstance().getAll(QueueEntry.class)){
                QueueEntry qe = (QueueEntry) obj;
                if(qe.getDoctor() != null){
                    if(qe.getDoctor().getId() == doctor.getId()){
                        queueEntries.put(qe.getQueueIdParent(),qe);
                        // set first entity
                        if(qe.getQueueIdParent()== null){
                            actEntry = qe;
                        }
                    }
                }
            }
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // set linked list
        while(actEntry != null){
            _entries.add(actEntry);
            actEntry = queueEntries.get(actEntry.getId());
        }

        log.info("[CREATE PatientQueue for DOCTOR '" + doctor.getFirstName() + " " + doctor.getLastName() + "' / Queuesize: " + _entries.size());
    }

    public PatientQueue(Orthoptist orthoptist){
        _entries = new LinkedList<QueueEntry>();

        // get all queue entities of a orthoptist
        HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
        QueueEntry actEntry = null;

        //Todo: Exception Handling
        try {
            for(Object obj : Facade.getInstance().getAll(QueueEntry.class)){
                QueueEntry qe = (QueueEntry) obj;
                if(qe.getOrthoptist() != null){
                    if(qe.getOrthoptist().getId() == orthoptist.getId()){
                        queueEntries.put(qe.getQueueIdParent(),qe);
                        // set first entity
                        if(qe.getQueueIdParent()==null){
                            actEntry = qe;
                        }
                    }
                }
            }
        }  catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // set linked list
        while(actEntry != null){
            _entries.add(actEntry);
            actEntry = queueEntries.get(actEntry.getId());
        }

        log.info("Retrieved queue from orthoptist '" + orthoptist.getLastName() + " / Queuesize: " + _entries.size());
    }

    public PatientQueue(Object unassigned){
        _entries = new LinkedList<QueueEntry>();

        // get all queue entities of unassigned patients
        HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
        QueueEntry actEntry = null;


        //Todo: Exception Handling
        try {
            for(Object obj : Facade.getInstance().getAll(QueueEntry.class)){
                QueueEntry qe = (QueueEntry) obj;
                if(qe.getOrthoptist() == null && qe.getDoctor() == null){
                    queueEntries.put(qe.getQueueIdParent(),qe);
                    // set first entity
                    if(qe.getQueueIdParent()== null){
                        actEntry = qe;
                    }
                }
            }
        }  catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // set linked list
        while(actEntry != null){
            _entries.add(actEntry);
            actEntry = queueEntries.get(actEntry.getId());
        }

        log.info("Retrieved queue from unassigned patients -> Queuesize: " + _entries.size());
    }

    @Override
    public int getUserID() { return _userID; }

    @Override
    public void setUserID(int userID) {
        _userID = userID;
    }

    public Collection<QueueEntry> getEntries() {
        return _entries;
    }

    public void setQueueEntry(LinkedList<QueueEntry> entries) {
        _entries = entries;
    }

    public void addPatient(Patient patient, Doctor doctor, Orthoptist orthoptist, Timestamp arrivaltime) {
        // get queue from user
        PatientQueue tempQueue = null;
        if(doctor != null) { tempQueue = new PatientQueue(doctor); }
        if(orthoptist != null) { tempQueue = new PatientQueue(orthoptist); }
        if(orthoptist == null && doctor == null) { tempQueue = new PatientQueue(); }

        // set queue id & Parent ID
        Integer newParentID = tempQueue.getEntries().size();
        if (newParentID == 0) {
            newParentID = null;     // set null for first entry parentID
        }

        // set new queueEntry data
        QueueEntry newEntry = new QueueEntry(0, patient, doctor, orthoptist, newParentID, arrivaltime);

        // save new entry (send to persistence)

        //Todo: Exception Handling
        try {
            Facade.getInstance().save(newEntry);
        }  catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // logging
        if (doctor != null) {
            log.info("[ADDPatient] added Patient '" + patient.getLastName() + "' to '"
                    + doctor.getLastName() + "' in queue position "
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId() );
        }
        if (orthoptist != null) {
            log.info("[ADDPatient] added Patient '" + patient.getLastName() + "' to '"
                    + orthoptist.getLastName() + "' in queue position "
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId() );
        }
        if (orthoptist == null && doctor == null){
            log.info("[ADDPatient] added Patient '" + patient.getLastName() + "' to 'no user' in queue position "
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId());
        }

    }

    public void removePatient(Patient patient) throws NoBrokerMappedException, BadConnectionException, InvalidSearchParameterException {
        LinkedList<QueueEntry> queue = new LinkedList();
        QueueEntry queueEntryDel = null;
        QueueEntry queueEntryChd = null;

        // getAll queueEntrys from queue table
        // Todo: Exception Handling
        try {
            for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
                QueueEntry qe = (QueueEntry) obj;
                queue.add(qe);
            }
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // remove patient from queuelist
        for (QueueEntry qe : queue) {
            if (qe.getPatient().getId() == patient.getId()) {
                queueEntryDel = qe;
                break;
            }
        }

        if(queueEntryDel != null){
            for (QueueEntry qe : queue) {
                // child entry update
                if(queueEntryDel.getQueueIdParent() == null){
                    if(qe.getOrthoptist() == queueEntryDel.getOrthoptist() && qe.getDoctor() == queueEntryDel.getDoctor()){
                        queueEntryChd = qe;
                        queueEntryChd.setQueueIdParent(null);
                        break;
                    }
                } else {
                    if(qe.getQueueIdParent() != null) {
                        if ((int) qe.getQueueIdParent() == queueEntryDel.getId()) {
                            queueEntryChd = qe;
                            queueEntryChd.setQueueIdParent(queueEntryDel.getQueueIdParent());
                            break;
                        }
                    }
                }
            }
        }

        if(queueEntryChd != null) {
            Facade.getInstance().save(queueEntryChd);
        }
        Facade.getInstance().delete(queueEntryDel);

        _entries.remove(queueEntryDel);

        log.info("[REMOVEPatient] Removed patient '" + patient.getFirstName() + " " + patient.getLastName() + "' from queue of " + patient.getDoctor().getLastName());
    }

    private void deleteAllQueueEntries(){
        LinkedList<QueueEntry> queueAll = new LinkedList();

        // getAll queueEntrys from queue table
        //Todo: Exception Handling
        try {
            for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
                QueueEntry qe = (QueueEntry) obj;
                queueAll.add(qe);
            }
        }  catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // deleteAll
        //Todo: Exception Handling
        for (QueueEntry queueDelete : queueAll) {
            try {
                Facade.getInstance().delete(queueDelete);
            } catch (BadConnectionException e) {
                e.printStackTrace();
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            } catch (InvalidSearchParameterException e) {
                e.printStackTrace();
            }
        }
    }

    public QueueEntry getNext() {
        // get next waiting patient from line
        log.info("[GETNEXT] Retrieve next patient from queue: '"  + _entries.getFirst().getPatient().getFirstName() + " "
                + _entries.getFirst().getPatient().getLastName() + "' id: " + _entries.getFirst().getPatient().getId());
        return _entries.getFirst();
    }
}
