package at.oculus.teamf.domain.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "calendarevent", schema = "", catalog = "oculus")
public class CalendareventEntity {
    private int calendarEventId;
    private Timestamp eventStart;
    private Timestamp eventEnd;
    private String description;
    private EventtypeEntity eventTypeId;
    private PatientEntity patientId;

    @Id
    @Column(name = "calendarEventId", nullable = false, insertable = true, updatable = true)
    public int getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(int calendarEventId) {
        this.calendarEventId = calendarEventId;
    }

    @Basic
    @Column(name = "eventStart", nullable = false, insertable = true, updatable = true)
    public Timestamp getEventStart() {
        return eventStart;
    }

    public void setEventStart(Timestamp eventStart) {
        this.eventStart = eventStart;
    }

    @Basic
    @Column(name = "eventEnd", nullable = false, insertable = true, updatable = true)
    public Timestamp getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Timestamp eventEnd) {
        this.eventEnd = eventEnd;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendareventEntity that = (CalendareventEntity) o;

        if (calendarEventId != that.calendarEventId) return false;
        if (eventStart != null ? !eventStart.equals(that.eventStart) : that.eventStart != null) return false;
        if (eventEnd != null ? !eventEnd.equals(that.eventEnd) : that.eventEnd != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = calendarEventId;
        result = 31 * result + (eventStart != null ? eventStart.hashCode() : 0);
        result = 31 * result + (eventEnd != null ? eventEnd.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "eventTypeId", referencedColumnName = "eventTypeId", nullable = false)
    public EventtypeEntity getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(EventtypeEntity eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    @OneToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId")
    public PatientEntity getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientEntity patientId) {
        this.patientId = patientId;
    }
}
