/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;

/**
 * QueueEntry.java Created by Fabian on 10.04.15.
 */
public class QueueEntry implements IDomain, ILogger {
    private int _id;
    private Doctor _doctor;
    private Orthoptist _orthoptist;
    private Integer _queueIdParent;
    private Timestamp _arrivalTime;
    private Patient _patient;

    public QueueEntry() {
    }

    public QueueEntry(int id, Patient patient, Doctor doctor, Orthoptist orthoptist, Integer queueIdParent,
                      Timestamp arrivalTime) {
        _id = id;
        _doctor = doctor;
        _orthoptist = orthoptist;
        _queueIdParent = queueIdParent;
        _arrivalTime = arrivalTime;
        _patient = patient;

        // logging
        Integer dID = null;
        Integer oID = null;
        if(_doctor != null) { dID = _doctor.getId();}
        if(_orthoptist != null) { oID = _orthoptist.getId();}
        log.debug("[CREATE QueueEntry] id: " + _id + " | doctorID: " + dID + " | orthoptistID: " + oID
                + " | patientID: " + _patient.getId() + " | queueIDParent: " + _queueIdParent + " | arrivalTime: " + _arrivalTime.toString());
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Doctor getDoctor() {
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

    public Patient getPatient() {
        return _patient;
    }

    public void setPatient(Patient patient) {
        _patient = patient;
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

    @Override
    public String toString() {
        return "QueueID " + getId() + " with Patient: " + getPatient() + ", Doctor: " + getDoctor() + ", Orthoptist: " +
                getOrthoptist();
    }
}
