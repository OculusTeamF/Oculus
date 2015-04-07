/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "queue", schema = "", catalog = "oculus_f")
public class QueueEntity {
    private int _id;
    private Integer _doctorId;
    private Integer _orthoptistId;
    private int _patientId;
    private Integer _queueIdParent;
    private Timestamp _arrivalTime;
    private DoctorEntity _doctor;
    private PatientEntity _patient;
    private OrthoptistEntity _orthoptist;
    private QueueEntity _queue;
    private Collection<QueueEntity> _queues;

    @Id
    @Column(name = "queueId", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return _id;
    }

    public void setId(int queueId) {
        this._id = queueId;
    }

    @Basic
    @Column(name = "doctorId", nullable = true, insertable = true, updatable = true)
    public Integer getDoctorId() {
        return _doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this._doctorId = doctorId;
    }

    @Basic
    @Column(name = "orthoptistId", nullable = true, insertable = true, updatable = true)
    public Integer getOrthoptistId() {
        return _orthoptistId;
    }

    public void setOrthoptistId(Integer orthoptistId) {
        this._orthoptistId = orthoptistId;
    }

    @Basic
    @Column(name = "patientId", nullable = false, insertable = true, updatable = true)
    public int getPatientId() {
        return _patientId;
    }

    public void setPatientId(int patientId) {
        this._patientId = patientId;
    }

    @Basic
    @Column(name = "queueIdParent", nullable = true, insertable = true, updatable = true)
    public Integer getQueueIdParent() {
        return _queueIdParent;
    }

    public void setQueueIdParent(Integer queueIdParent) {
        this._queueIdParent = queueIdParent;
    }

    @Basic
    @Column(name = "arrivalTime", nullable = false, insertable = true, updatable = true)
    public Timestamp getArrivalTime() {
        return _arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this._arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueEntity that = (QueueEntity) o;

        if (_id != that._id) return false;
        if (_patientId != that._patientId) return false;
        if (_doctorId != null ? !_doctorId.equals(that._doctorId) : that._doctorId != null) return false;
        if (_orthoptistId != null ? !_orthoptistId.equals(that._orthoptistId) : that._orthoptistId != null) return false;
        if (_queueIdParent != null ? !_queueIdParent.equals(that._queueIdParent) : that._queueIdParent != null)
            return false;
        if (_arrivalTime != null ? !_arrivalTime.equals(that._arrivalTime) : that._arrivalTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_doctorId != null ? _doctorId.hashCode() : 0);
        result = 31 * result + (_orthoptistId != null ? _orthoptistId.hashCode() : 0);
        result = 31 * result + _patientId;
        result = 31 * result + (_queueIdParent != null ? _queueIdParent.hashCode() : 0);
        result = 31 * result + (_arrivalTime != null ? _arrivalTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    public DoctorEntity getDoctor() {
        return _doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this._doctor = doctor;
    }

    @ManyToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId", nullable = false)
    public PatientEntity getPatient() {
        return _patient;
    }

    public void setPatient(PatientEntity patientByPatientId) {
        this._patient = patientByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "orthoptistId", referencedColumnName = "orthoptistId")
    public OrthoptistEntity getOrthoptist() {
        return _orthoptist;
    }

    public void setOrthoptist(OrthoptistEntity orthoptist) {
        this._orthoptist = orthoptist;
    }

    @ManyToOne
    @JoinColumn(name = "queueIdParent", referencedColumnName = "queueId")
    public QueueEntity getQueue() {
        return _queue;
    }

    public void setQueue(QueueEntity queue) {
        this._queue = queue;
    }

    @OneToMany(mappedBy = "_queue")
    public Collection<QueueEntity> getQueues() {
        return _queues;
    }

    public void setQueues(Collection<QueueEntity> queues) {
        this._queues = queues;
    }
}
