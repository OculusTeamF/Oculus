/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import java.sql.Timestamp;

/**
 * QueueEntry.java Created by oculus on 09.04.15.
 */
public class QueueEntry {
	private int _id;
	private Integer _doctorId;
	private Integer _orthoptistId;
	private int _patientId;
	private Integer _queueIdParent;
	private Timestamp _arrivalTime;
	private Patient _patient;
	//private Orthoptist _orthoptist;
	//private QueueEntry _queueParent;
	//private Doctor _doctor;

	public QueueEntry() {
	}
	public QueueEntry(int id, Integer doctorId, Integer orthoptistId, int patientId, Integer queueIdParent,
	                  Timestamp arrivalTime, Patient patient) {
		_id = id;
		_doctorId = doctorId;
		_orthoptistId = orthoptistId;
		_patientId = patientId;
		_queueIdParent = queueIdParent;
		_arrivalTime = arrivalTime;
		_patient = patient;
	}

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	public Integer getDoctorId() {
		return _doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		_doctorId = doctorId;
	}

	public Integer getOrthoptistId() {
		return _orthoptistId;
	}
	public void setOrthoptistId(Integer orthoptistId) {
		_orthoptistId = orthoptistId;
	}

	public int getPatientId() {
		return _patientId;
	}
	public void setPatientId(int patientId) {
		_patientId = patientId;
	}

	public Integer getQueueIdParent() {
		return _queueIdParent;
	}
	public void setQueueIdParent(Integer queueIdParent) {
		_queueIdParent = queueIdParent;
	}

	public Timestamp getArrivalTime() {
		return _arrivalTime;
	}
	public void setArrivalTime(Timestamp arrivalTime) {
		_arrivalTime = arrivalTime;
	}

	public Patient getPatient() {
		return _patient;
	}
	public void setPatient(Patient patient) {
		_patient = patient;
	}

	/*public Doctor getDoctor() {
		return _doctor;
	}
	public void setDoctor(Doctor doctor) {
		_doctor = doctor;
	}

	public Orthoptist getOrthoptist() {
		return _orthoptist;
	}
	public void setOrthoptist(Orthoptist orthoptist) {
		_orthoptist = orthoptist;
	}

	public QueueEntry getQueueParent() {
		return _queueParent;
	}
	public void setQueueParent(QueueEntry queueParent) {
		_queueParent = queueParent;
	}*/
}
