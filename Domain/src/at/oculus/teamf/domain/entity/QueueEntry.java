/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.doctor.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.domain.entity.patient.IPatient;

import java.sql.Timestamp;

/**
 * QueueEntry.java Created by Fabian on 10.04.15.
 */
public class QueueEntry implements IQueueEntry {
    private int _id;
    private IDoctor _doctor;
    private IOrthoptist _orthoptist;
    private Integer _queueIdParent;
    private Timestamp _arrivalTime;
    private IPatient _patient;

    /**
     *
     * adds new queueEntry to PatientQueue for doctor, orthopthist or unassigned queue
     *
     * @param id  queue id, set by database. if queue id is 0 then new entry else update entry of given id
     * @param patient set patient which shoul dbe added to queue
     * @param user add to user, use 'null' if not assigned to a doctor
     * @param queueIdParent  parentid for queueupart for a user
     * @param arrivalTime set timestamp
     */
    public QueueEntry(int id, IPatient patient, IUser user, Integer queueIdParent,
                      Timestamp arrivalTime) {
        _id = id;
        if(user instanceof IDoctor){
            _doctor = (IDoctor) user;
        } else if (user instanceof Orthoptist) {
            _orthoptist = (Orthoptist) user;
        }

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

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }

    @Override
    public IDoctor getDoctor() {
        return _doctor;
    }

    @Override
    public void setDoctor(IDoctor doctor) {
        _doctor = doctor;
    }

    @Override
    public IOrthoptist getOrthoptist() {
        return _orthoptist;
    }

    @Override
    public void setOrthoptist(IOrthoptist orthoptist) {
        _orthoptist = orthoptist;
    }

    @Override
    public IPatient getPatient() {
        return _patient;
    }

    @Override
    public void setPatient(IPatient patient) {
        _patient = patient;
    }

    @Override
    public Integer getQueueIdParent() {
        return _queueIdParent;
    }

    @Override
    public void setQueueIdParent(Integer queueIdParent) {
        _queueIdParent = queueIdParent;
    }

    @Override
    public Timestamp getArrivalTime() {
        return _arrivalTime;
    }

    @Override
    public String toString() {
        return "QueueID " + getId() + " with Patient: " + getPatient() + ", Doctor: " + getDoctor() + ", Orthoptist: " +
                getOrthoptist();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QueueEntry)) return false;

        QueueEntry that = (QueueEntry) o;

        if (_id != that._id) return false;
        if (_arrivalTime != null ? !_arrivalTime.equals(that._arrivalTime) : that._arrivalTime != null) return false;
        if (_doctor != null ? !_doctor.equals(that._doctor) : that._doctor != null) return false;
        if (_orthoptist != null ? !_orthoptist.equals(that._orthoptist) : that._orthoptist != null) return false;
        if (_patient != null ? !_patient.equals(that._patient) : that._patient != null) return false;
        if (_queueIdParent != null ? !_queueIdParent.equals(that._queueIdParent) : that._queueIdParent != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
        result = 31 * result + (_orthoptist != null ? _orthoptist.hashCode() : 0);
        result = 31 * result + (_queueIdParent != null ? _queueIdParent.hashCode() : 0);
        result = 31 * result + (_arrivalTime != null ? _arrivalTime.hashCode() : 0);
        result = 31 * result + (_patient != null ? _patient.hashCode() : 0);
        return result;
    }
}
