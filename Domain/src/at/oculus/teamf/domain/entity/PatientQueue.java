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
						if(qe.getQueueIdParent()==0){
							actEntry = qe;
						}
					}
				}
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		// set linked list
		while(actEntry!=null){
			_entries.add(actEntry);
			actEntry = queueEntries.get(actEntry.getId());
		}
	}

	public PatientQueue(Orthoptist orthoptist){
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
						if(qe.getQueueIdParent()==0){
							actEntry = qe;
						}
					}
				}
				if(qe.getOrthoptist()==null && qe.getDoctor()==null){
					queueEntriesEx.put(qe.getQueueIdParent(),qe);
						// set first entity
						if(qe.getQueueIdParent()==0) {
							actEntryEx = qe;
						}
				}
			}
		} catch (FacadeException e) {
			e.printStackTrace();
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

        // set queue id (set last position id)
        Integer newQueueID = null;
        try {
            newQueueID = Facade.getInstance().getAll(QueueEntry.class).size();
        } catch (FacadeException e) {
            e.printStackTrace();
        }
        if (newQueueID == 0) {
            newQueueID = null;      // use null for first entry in database
        }
        newEntry.setId(newQueueID);

        // set queue parent id
        Integer newParentID = _entries.getLast().getId();
        newEntry.setQueueIdParent(newParentID);

        // add & save new entry
        _entries.addLast(newEntry);
        try {
            Facade.getInstance().save(newEntry);
        } catch (FacadeException e) {
            e.printStackTrace();
        }

        // logging
        if (newEntry.getDoctor() != null) {
            log.info("add Patient '" + patient.getLastName() + "' to '"
                    + newEntry.getDoctor().getUserGroup().getUserGroupName() + ": "
                    + newEntry.getDoctor().getLastName() + "' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId() + "'");
        } else if (newEntry.getOrthoptist() != null) {
            log.info("add Patient '" + patient.getLastName() + "' to '"
                    + newEntry.getOrthoptist().getUserGroup().getUserGroupName() + ": "
                    + newEntry.getOrthoptist().getLastName() + "' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId() + "'");
        } else {
            log.info("add Patient '" + patient.getLastName() + "' to 'no user' in queue position '"
                    + newEntry.getQueueIdParent() + " / queueID:" + newEntry.getId() + "'");
        }

    }

    public void removePatient(Patient patient) {
        LinkedList<QueueEntry> queueEntries = null;

        try {
            // get whole queue table
            for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
                QueueEntry qe = (QueueEntry) obj;
                queueEntries.add(qe);
            }
        } catch (FacadeException e) {
            e.printStackTrace();
            }
        }

    public QueueEntry getNext() {
        // get next waiting patient from line
        return _entries.getFirst();
    }
}
