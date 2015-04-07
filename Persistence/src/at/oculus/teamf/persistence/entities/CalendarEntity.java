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
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "calendar", schema = "", catalog = "oculus_f")
public class CalendarEntity {
    private int _id;
    private String _title;
    private Collection<CalendareventEntity> _calendarevents;
    private Collection<CalendarworkinghoursEntity> _calendarworkinghours;
    private DoctorEntity _doctor;
    private OrthoptistEntity _orthoptist;

    @Id
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return _id;
    }

    public void setId(int calendarId) {
        this._id = calendarId;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 255)
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEntity that = (CalendarEntity) o;

        if (_id != that._id) return false;
        if (_title != null ? !_title.equals(that._title) : that._title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_title != null ? _title.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "_calendar")
    public Collection<CalendareventEntity> getCalendarevents() {
        return _calendarevents;
    }

    public void setCalendarevents(Collection<CalendareventEntity> calendareventsByCalendarId) {
        this._calendarevents = calendareventsByCalendarId;
    }

    @OneToMany(mappedBy = "calendarByCalendarId")
    public Collection<CalendarworkinghoursEntity> getCalendarworkinghours() {
        return _calendarworkinghours;
    }

    public void setCalendarworkinghours(Collection<CalendarworkinghoursEntity> calendarworkinghoursesByCalendarId) {
        this._calendarworkinghours = calendarworkinghoursesByCalendarId;
    }

    @OneToMany(mappedBy = "_calendar")
    public DoctorEntity get_doctor() {
        return _doctor;
    }

    public void set_doctor(DoctorEntity doctorByCalendarId) {
        this._doctor = _doctor;
    }

    @OneToMany(mappedBy = "_calendar")
    public OrthoptistEntity get_orthoptist() {
        return _orthoptist;
    }

    public void set_orthoptist(OrthoptistEntity orthoptist) {
        this._orthoptist = orthoptist;
    }
}