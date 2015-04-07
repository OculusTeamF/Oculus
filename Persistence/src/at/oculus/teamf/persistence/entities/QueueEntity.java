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
    private int queueId;
    private Integer doctorId;
    private Integer orthoptistId;
    private int patientId;
    private Integer queueIdParent;
    private Timestamp arrivalTime;
    private DoctorEntity doctorByDoctorId;
    private PatientEntity patientByPatientId;
    private OrthoptistEntity orthoptistByOrthoptistId;
    private QueueEntity queueByQueueIdParent;
    private Collection<QueueEntity> queuesByQueueId;

    @Id
    @Column(name = "queueId", nullable = false, insertable = true, updatable = true)
    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    @Basic
    @Column(name = "doctorId", nullable = true, insertable = true, updatable = true)
    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "orthoptistId", nullable = true, insertable = true, updatable = true)
    public Integer getOrthoptistId() {
        return orthoptistId;
    }

    public void setOrthoptistId(Integer orthoptistId) {
        this.orthoptistId = orthoptistId;
    }

    @Basic
    @Column(name = "patientId", nullable = false, insertable = true, updatable = true)
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "queueIdParent", nullable = true, insertable = true, updatable = true)
    public Integer getQueueIdParent() {
        return queueIdParent;
    }

    public void setQueueIdParent(Integer queueIdParent) {
        this.queueIdParent = queueIdParent;
    }

    @Basic
    @Column(name = "arrivalTime", nullable = false, insertable = true, updatable = true)
    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueEntity that = (QueueEntity) o;

        if (queueId != that.queueId) return false;
        if (patientId != that.patientId) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (orthoptistId != null ? !orthoptistId.equals(that.orthoptistId) : that.orthoptistId != null) return false;
        if (queueIdParent != null ? !queueIdParent.equals(that.queueIdParent) : that.queueIdParent != null)
            return false;
        if (arrivalTime != null ? !arrivalTime.equals(that.arrivalTime) : that.arrivalTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = queueId;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (orthoptistId != null ? orthoptistId.hashCode() : 0);
        result = 31 * result + patientId;
        result = 31 * result + (queueIdParent != null ? queueIdParent.hashCode() : 0);
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    public DoctorEntity getDoctorByDoctorId() {
        return doctorByDoctorId;
    }

    public void setDoctorByDoctorId(DoctorEntity doctorByDoctorId) {
        this.doctorByDoctorId = doctorByDoctorId;
    }

    @ManyToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId", nullable = false)
    public PatientEntity getPatientByPatientId() {
        return patientByPatientId;
    }

    public void setPatientByPatientId(PatientEntity patientByPatientId) {
        this.patientByPatientId = patientByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "orthoptistId", referencedColumnName = "orthoptistId")
    public OrthoptistEntity getOrthoptistByOrthoptistId() {
        return orthoptistByOrthoptistId;
    }

    public void setOrthoptistByOrthoptistId(OrthoptistEntity orthoptistByOrthoptistId) {
        this.orthoptistByOrthoptistId = orthoptistByOrthoptistId;
    }

    @ManyToOne
    @JoinColumn(name = "queueIdParent", referencedColumnName = "queueId")
    public QueueEntity getQueueByQueueIdParent() {
        return queueByQueueIdParent;
    }

    public void setQueueByQueueIdParent(QueueEntity queueByQueueIdParent) {
        this.queueByQueueIdParent = queueByQueueIdParent;
    }

    @OneToMany(mappedBy = "queueByQueueIdParent")
    public Collection<QueueEntity> getQueuesByQueueId() {
        return queuesByQueueId;
    }

    public void setQueuesByQueueId(Collection<QueueEntity> queuesByQueueId) {
        this.queuesByQueueId = queuesByQueueId;
    }
}
