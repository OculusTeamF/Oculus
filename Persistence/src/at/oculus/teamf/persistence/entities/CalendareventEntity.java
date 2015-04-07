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

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "calendarevent", schema = "", catalog = "oculus_f")
public class CalendareventEntity {
    private int calendarEventId;
    private int calendarId;
    private Integer patientId;
    private int eventTypeId;
    private Timestamp eventStart;
    private Timestamp eventEnd;
    private String description;
    private String patientName;
    private byte isOpen;
    private CalendarEntity calendarByCalendarId;
    private PatientEntity patientByPatientId;
    private EventtypeEntity eventtypeByEventTypeId;

    @Id
    @Column(name = "calendarEventId", nullable = false, insertable = true, updatable = true)
    public int getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(int calendarEventId) {
        this.calendarEventId = calendarEventId;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Basic
    @Column(name = "patientId", nullable = true, insertable = true, updatable = true)
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "eventTypeId", nullable = false, insertable = true, updatable = true)
    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
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

    @Basic
    @Column(name = "patientName", nullable = true, insertable = true, updatable = true, length = 100)
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Basic
    @Column(name = "isOpen", nullable = false, insertable = true, updatable = true)
    public byte getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(byte isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendareventEntity that = (CalendareventEntity) o;

        if (calendarEventId != that.calendarEventId) return false;
        if (calendarId != that.calendarId) return false;
        if (eventTypeId != that.eventTypeId) return false;
        if (isOpen != that.isOpen) return false;
        if (patientId != null ? !patientId.equals(that.patientId) : that.patientId != null) return false;
        if (eventStart != null ? !eventStart.equals(that.eventStart) : that.eventStart != null) return false;
        if (eventEnd != null ? !eventEnd.equals(that.eventEnd) : that.eventEnd != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (patientName != null ? !patientName.equals(that.patientName) : that.patientName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = calendarEventId;
        result = 31 * result + calendarId;
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + eventTypeId;
        result = 31 * result + (eventStart != null ? eventStart.hashCode() : 0);
        result = 31 * result + (eventEnd != null ? eventEnd.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (patientName != null ? patientName.hashCode() : 0);
        result = 31 * result + (int) isOpen;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendarByCalendarId() {
        return calendarByCalendarId;
    }

    public void setCalendarByCalendarId(CalendarEntity calendarByCalendarId) {
        this.calendarByCalendarId = calendarByCalendarId;
    }

    @ManyToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId")
    public PatientEntity getPatientByPatientId() {
        return patientByPatientId;
    }

    public void setPatientByPatientId(PatientEntity patientByPatientId) {
        this.patientByPatientId = patientByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "eventTypeId", referencedColumnName = "eventTypeId", nullable = false)
    public EventtypeEntity getEventtypeByEventTypeId() {
        return eventtypeByEventTypeId;
    }

    public void setEventtypeByEventTypeId(EventtypeEntity eventtypeByEventTypeId) {
        this.eventtypeByEventTypeId = eventtypeByEventTypeId;
    }
}
