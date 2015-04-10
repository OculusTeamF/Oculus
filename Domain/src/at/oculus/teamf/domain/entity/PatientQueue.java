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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class PatientQueue {

	//<editor-fold desc="Attributes">
	private int _userID;
	private LinkedList<QueueEntry> _entries;
	private Doctor _doctor;
	private Orthoptist _orthoptist;
	//</editor-fold>

	public PatientQueue(Doctor doctor) {
		_doctor = doctor;
		// get all queue entities of a doctor
		HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntry = null;
		try {
			for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
				QueueEntry qe = (QueueEntry) obj;
				if (qe.getDoctorId() != null) {
					if (qe.getDoctorId() == doctor.getId()) {
						queueEntries.put(qe.getQueueIdParent(), qe);
						// set first entity
						if (qe.getQueueIdParent() == 0) {
							actEntry = qe;
						}
					}
				}
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		// set linked list
		while (actEntry != null) {
			_entries.add(actEntry);
			actEntry = queueEntries.get(actEntry.getId());
		}
	}

	public PatientQueue(Orthoptist orthoptist) {
		_orthoptist = orthoptist;
		// get all queue entities of a orthoptist
		HashMap<Integer, QueueEntry> queueEntries = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntry = null;
		HashMap<Integer, QueueEntry> queueEntriesEx = new HashMap<Integer, QueueEntry>();
		QueueEntry actEntryEx = null;
		try {
			for (Object obj : Facade.getInstance().getAll(QueueEntry.class)) {
				QueueEntry qe = (QueueEntry) obj;
				if (qe.getOrthoptistId() != null) {
					if (qe.getOrthoptistId() == orthoptist.getId()) {
						queueEntries.put(qe.getQueueIdParent(), qe);
						// set first entity
						if (qe.getQueueIdParent() == 0) {
							actEntry = qe;
						}
					}
				}
				if (qe.getOrthoptistId() == null && qe.getDoctorId() == null) {
					queueEntriesEx.put(qe.getQueueIdParent(), qe);
					// set first entity
					if (qe.getQueueIdParent() == 0) {
						actEntryEx = qe;
					}
				}
			}
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		// set linked list
		while (actEntry != null) {
			_entries.add(actEntry);
			actEntry = queueEntries.get(actEntry.getId());
		}
		while (actEntryEx != null) {
			_entries.add(actEntryEx);
			actEntryEx = queueEntriesEx.get(actEntryEx.getId());
		}
	}

	//<editor-fold desc="Getter/Setter">
	public int getUserID() {
		return _userID;
	}

	public void setUserID(int userID) {
		_userID = userID;
	}

	public Collection<QueueEntry> getEntries() {
		return _entries;
	}

	public void setPatients(LinkedList<QueueEntry> entries) {
		_entries = entries;
	}
	//</editor-fold>

	public void insert(Patient patient, Patient parent) {
	}

	public QueueEntry getNext() {
		return null;
	}
}
