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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class PatientQueue implements ILogger{

    //<editor-fold desc="Attributes">
    private int _userID;
	private LinkedList<QueueEntry> _entries;
    //</editor-fold>

	public PatientQueue(Doctor doctor){
		_entries = new LinkedList<QueueEntry>();
		// get all queue entities of a doctor
		HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntry = null;
		try {
			for(Object obj : Facade.getInstance().getAll(QueueEntry.class)){
				QueueEntry qe = (QueueEntry) obj;
				if(qe.getDoctor() != null){
					if(qe.getDoctor()== doctor){
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
		while(actEntry!=null){
			_entries.add(actEntry);
			actEntry = queueEntries.get(actEntry.getId());
		}

        log.info("Retrieved queue from doctor '" + doctor.getLastName() + " / Queuesize: " + _entries.size());
	}

	public PatientQueue(Orthoptist orthoptist){
		_entries = new LinkedList<QueueEntry>();
		// get all queue entities of a orthoptist
		HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntry = null;
		HashMap<Integer, QueueEntry> queueEntriesEx = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntryEx = null;
		try {
			for(Object obj : Facade.getInstance().getAll(QueueEntry.class)){
				QueueEntry qe = (QueueEntry) obj;
				if(qe.getOrthoptist() != null){
					if(qe.getOrthoptist()== orthoptist){
						queueEntries.put(qe.getQueueIdParent(),qe);
						// set first entity
						if(qe.getQueueIdParent()==null){
							actEntry = qe;
						}
					}
				}
				if(qe.getOrthoptist()==null && qe.getDoctor()==null){
					queueEntriesEx.put(qe.getQueueIdParent(),qe);
						// set first entity
						if(qe.getQueueIdParent()==null) {
							actEntryEx = qe;
						}
				}
			}
		} catch (FacadeException e) {
            log.error("Facade Exception", e);
		}

		// set linked list
		while(actEntry!=null){
			_entries.add(actEntry);
			actEntry = queueEntries.get(actEntry.getId());
		}
		while(actEntryEx!=null){
			_entries.add(actEntryEx);
			actEntry = queueEntriesEx.get(actEntryEx.getId());
		}

        log.info("Retrieved queue from orthoptist '" + orthoptist.getLastName() + " / Queuesize: " + _entries.size());
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
        QueueEntry newEntry = null;

        // set entry data
        newEntry.setPatient(patient);
        newEntry.setDoctor(doctor);
        newEntry.setOrthoptist(orthoptist);
        newEntry.setArrivalTime(arrivaltime);

        // set queue id
        Integer newQueueID = null;
        try {
            newQueueID = Facade.getInstance().getAll(QueueEntry.class).size();
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
        }
        // newEntry.setId(newQueueID);
        // using '0' for auto add as new entry and last position in queue line
        newEntry.setId(0);

        // set queue parent id
        Integer newParentID = _entries.size();
        if (newParentID == 0) {
            newParentID = null;     // set null for first entry parentID
        }
        newEntry.setQueueIdParent(newParentID);

        // add & save new entry
        _entries.addLast(newEntry);
        try {
            Facade.getInstance().save(newEntry);
        } catch (FacadeException e) {
            log.error("Facade Exception", e);
        }

        // logging
        if (newEntry.getDoctor() != null) {
            log.info("add Patient '" + patient.getLastName() + "' to '"
                    + newEntry.getDoctor().getUserGroup().getUserGroupName() + ": "
                    + newEntry.getDoctor().getLastName() + "' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newQueueID + "'");
        } else if (newEntry.getOrthoptist() != null) {
            log.info("add Patient '" + patient.getLastName() + "' to '"
                    + newEntry.getOrthoptist().getUserGroup().getUserGroupName() + ": "
                    + newEntry.getOrthoptist().getLastName() + "' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newQueueID + "'");
        } else {
            log.info("add Patient '" + patient.getLastName() + "' to 'no user' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newQueueID + "'");
        }

    }

    public void removePatient(Patient patient) {
        LinkedList<QueueEntry> queue = null;
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

        // send rebuild queue to persistence layer
        //Facade.getInstance().deleteAll(QueueEntry.class);
        //TODO: deleteAll queue entries OR update entries and remove last entry
        for (QueueEntry queueRebuild : rebuildQueueEntries){
            try {
                Facade.getInstance().save(queueRebuild);
            } catch (FacadeException e) {
                log.error("Facade Exception", e);
            }
        }



        log.info("Removed patient '" + patient.getFirstName() + " " + patient.getLastName() + "' from queue of " + patient.getDoctor().getLastName());
    }

    public QueueEntry getNext() {
        // get next waiting patient from line
        log.info("Get next patient from queue: '" + _entries.getFirst().getPatient().getLastName());
        return _entries.getFirst();
    }
}
