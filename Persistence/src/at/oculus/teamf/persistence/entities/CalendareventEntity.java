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
    private int _id;
    private int _calendarId;
    private Integer _patientId;
    private int _eventTypeId;
    private Timestamp _eventStart;
    private Timestamp _eventEnd;
    private String _description;
    private String _patientName;
    private byte _isOpen;
    private CalendarEntity _calendar;
    private PatientEntity _patient;
    private EventtypeEntity _eventtype;

    @Id
    @Column(name = "calendarEventId", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return _calendarId;
    }

    public void setCalendarId(int calendarId) {
        this._calendarId = calendarId;
    }

    @Basic
    @Column(name = "patientId", nullable = true, insertable = true, updatable = true)
    public Integer getPatientId() {
        return _patientId;
    }

    public void setPatientId(Integer patientId) {
        this._patientId = patientId;
    }

    @Basic
    @Column(name = "eventTypeId", nullable = false, insertable = true, updatable = true)
    public int getEventTypeId() {
        return _eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this._eventTypeId = eventTypeId;
    }

    @Basic
    @Column(name = "eventStart", nullable = false, insertable = true, updatable = true)
    public Timestamp getEventStart() {
        return _eventStart;
    }

    public void setEventStart(Timestamp eventStart) {
        this._eventStart = eventStart;
    }

    @Basic
    @Column(name = "eventEnd", nullable = false, insertable = true, updatable = true)
    public Timestamp getEventEnd() {
        return _eventEnd;
    }

    public void setEventEnd(Timestamp eventEnd) {
        this._eventEnd = eventEnd;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    @Basic
    @Column(name = "patientName", nullable = true, insertable = true, updatable = true, length = 100)
    public String getPatientName() {
        return _patientName;
    }

    public void setPatientName(String patientName) {
        this._patientName = patientName;
    }

    @Basic
    @Column(name = "isOpen", nullable = false, insertable = true, updatable = true)
    public byte getIsOpen() {
        return _isOpen;
    }

    public void setIsOpen(byte isOpen) {
        this._isOpen = isOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendareventEntity that = (CalendareventEntity) o;

        if (_id != that._id) return false;
        if (_calendarId != that._calendarId) return false;
        if (_eventTypeId != that._eventTypeId) return false;
        if (_isOpen != that._isOpen) return false;
        if (_patientId != null ? !_patientId.equals(that._patientId) : that._patientId != null) return false;
        if (_eventStart != null ? !_eventStart.equals(that._eventStart) : that._eventStart != null) return false;
        if (_eventEnd != null ? !_eventEnd.equals(that._eventEnd) : that._eventEnd != null) return false;
        if (_description != null ? !_description.equals(that._description) : that._description != null) return false;
        if (_patientName != null ? !_patientName.equals(that._patientName) : that._patientName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + _calendarId;
        result = 31 * result + (_patientId != null ? _patientId.hashCode() : 0);
        result = 31 * result + _eventTypeId;
        result = 31 * result + (_eventStart != null ? _eventStart.hashCode() : 0);
        result = 31 * result + (_eventEnd != null ? _eventEnd.hashCode() : 0);
        result = 31 * result + (_description != null ? _description.hashCode() : 0);
        result = 31 * result + (_patientName != null ? _patientName.hashCode() : 0);
        result = 31 * result + (int) _isOpen;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendar() {
        return _calendar;
    }

    public void setCalendar(CalendarEntity calendarByCalendarId) {
        this._calendar = calendarByCalendarId;
    }

    @ManyToOne
    @JoinColumn(name = "patientId", referencedColumnName = "patientId")
    public PatientEntity getPatient() {
        return _patient;
    }

    public void setPatient(PatientEntity patientByPatientId) {
        this._patient = patientByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "eventTypeId", referencedColumnName = "eventTypeId", nullable = false)
    public EventtypeEntity getEventtype() {
        return _eventtype;
    }

    public void setEventtype(EventtypeEntity eventtypeByEventTypeId) {
        this._eventtype = eventtypeByEventTypeId;
    }
}
