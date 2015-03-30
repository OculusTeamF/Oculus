package at.oculus.teamf.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "queue", schema = "", catalog = "oculus")
public class QueueEntity {
    private int queueId;
    private Timestamp arrivalTime;
    private DoctorEntity doctorId;
    private OrthoptistEntity orthoptistId;
    private PatientEntity patientId;
    private QueueEntity queueIdParent;

    @Id
    @Column(name = "queueId", nullable = false, insertable = true, updatable = true)
    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
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
        if (arrivalTime != null ? !arrivalTime.equals(that.arrivalTime) : that.arrivalTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = queueId;
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    public DoctorEntity getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorEntity doctorId) {
        this.doctorId = doctorId;
    }

    @OneToOne
    @JoinColumn(name = "orthoptistId", referencedColumnName = "orthoptistId")
    public OrthoptistEntity getOrthoptistId() {
        return orthoptistId;
    }

    public void setOrthoptistId(OrthoptistEntity orthoptistId) {
        this.orthoptistId = orthoptistId;
    }

    @OneToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId", nullable = false)
    public PatientEntity getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientEntity patientId) {
        this.patientId = patientId;
    }

    @OneToOne
    @JoinColumn(name = "queueIdParent", referencedColumnName = "orthoptistId")
    public QueueEntity getQueueIdParent() {
        return queueIdParent;
    }

    public void setQueueIdParent(QueueEntity queueIdParent) {
        this.queueIdParent = queueIdParent;
    }
}
