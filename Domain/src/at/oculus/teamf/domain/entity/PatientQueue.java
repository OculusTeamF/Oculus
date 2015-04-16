/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.*;

/**
 * Todo: add docs, implement equals
 *
 * @author Fabian Salzgeber
 * @date 03.4.2015
 */
public class PatientQueue implements ILogger{

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
		} catch (FacadeException e) {
			log.error("Facade Exception", e);
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
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
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
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
        }

        // set linked list
        while(actEntry != null){
            _entries.add(actEntry);
            actEntry = queueEntries.get(actEntry.getId());
        }

        log.info("Retrieved queue from unassigned patients -> Queuesize: " + _entries.size());
    }

    public int getUserID() { return _userID; }

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
        try {
            Facade.getInstance().save(newEntry);
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
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

    public void removePatient(Patient patient) {
        LinkedList<QueueEntry> queue = new LinkedList();
        LinkedList<QueueEntry> rebuildQueueEntries = new LinkedList();
        LinkedList<QueueEntry> tempDoctorQueueEntries = new LinkedList();
        LinkedList<QueueEntry> tempOrthoptistQueueEntries = new LinkedList();
        LinkedList<QueueEntry> tempUnassignedQueueEntries = new LinkedList();

        // getAll queueEntrys from queue table
        try {
            for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
                QueueEntry qe = (QueueEntry) obj;
                queue.add(qe);
            }
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
        }

        // remove patient from queuelist
        for (QueueEntry qe : queue) {
            if (qe.getPatient().getId() == patient.getId()) {
                queue.remove(qe);
            }
        }

        // create openlist from edited queuelist
        ArrayList<Integer> openList = new ArrayList<Integer>();
        for (QueueEntry qe : queue) { openList.add(qe.getId());}

        // rebuild queueIDs and parentIDs
        int newID = 0;

        while (!openList.isEmpty()) {
            for (QueueEntry qe : queue){
                if (qe.getId() == openList.get(0)) {

                    // new ID
                    openList.remove(0);
                    qe.setId(++newID);

                    // new parentID
                    if (qe.getQueueIdParent() != null) {

                        // look for same doctors & orthoptists & unassigned patients
                        for (QueueEntry qeRebuild : rebuildQueueEntries ){
                            if (qeRebuild.getDoctor().getId() == qe.getDoctor().getId() && qeRebuild.getDoctor() != null){
                                tempDoctorQueueEntries.add(qeRebuild);
                            }
                            if (qeRebuild.getOrthoptist().getId() == qe.getOrthoptist().getId() && qeRebuild.getOrthoptist() != null ){
                                tempOrthoptistQueueEntries.add(qeRebuild);
                            }
                            if (qeRebuild.getDoctor().getId() == qe.getDoctor().getId() && qeRebuild.getOrthoptist().getId() == qe.getOrthoptist().getId()
                                    && qeRebuild.getDoctor() == null && qeRebuild.getOrthoptist() == null){
                                tempUnassignedQueueEntries.add(qeRebuild);
                            }
                        }

                        // assign new doctor queue parentID & check for null values
                        if (!tempDoctorQueueEntries.isEmpty()){
                            qe.setQueueIdParent(tempDoctorQueueEntries.getLast().getId());
                        } else {
                            if (qe.getDoctor() != null){ qe.setQueueIdParent(null); }
                        }

                        // assign new orthoptist queue parentID & check for null values
                        if (!tempOrthoptistQueueEntries.isEmpty()){
                            qe.setQueueIdParent(tempOrthoptistQueueEntries.getLast().getId());
                        } else {
                            if (qe.getOrthoptist() != null){ qe.setQueueIdParent(null); }
                        }

                        // assign new unassigned patients queue parentID & check for null values
                        if (!tempUnassignedQueueEntries.isEmpty()){
                            qe.setQueueIdParent(tempUnassignedQueueEntries.getLast().getId());
                        } else {
                            if (qe.getDoctor() == null && qe.getOrthoptist() == null){ qe.setQueueIdParent(null); }
                        }

                        tempDoctorQueueEntries.clear();
                        tempOrthoptistQueueEntries.clear();
                        tempUnassignedQueueEntries.clear();
                    }

                    rebuildQueueEntries.add(qe);
                }
            }
        }

        // delete whole queue and send rebuild queue to persistence layer
        deleteAllQueueEntries();

        for (QueueEntry queueRebuild : rebuildQueueEntries){
            try {
                Facade.getInstance().save(queueRebuild);
            } catch (FacadeException e) {
                log.error("Facade Exception", e);
            }
        }


        log.info("[REMOVEPatient] Removed patient '" + patient.getFirstName() + " " + patient.getLastName() + "' from queue of " + patient.getDoctor().getLastName());
    }

    private void deleteAllQueueEntries(){
        LinkedList<QueueEntry> queueAll = new LinkedList();

        // getAll queueEntrys from queue table
        try {
            for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
                QueueEntry qe = (QueueEntry) obj;
                queueAll.add(qe);
            }
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
        }

        // deleteAll
        for (QueueEntry queueDelete : queueAll) {
            try {
                Facade.getInstance().delete(queueDelete);
            } catch (FacadeException e) {
                log.error("Facade Exception", e);
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
