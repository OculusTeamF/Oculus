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
public class CalendarEntity implements IEntity {
    private int _id;
    private String _title;
    private Collection<CalendarEventEntity> _calendarevents;
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

    @OneToMany(mappedBy = "calendar")
    public Collection<CalendarEventEntity> getCalendarEvents() {
        return _calendarevents;
    }

    public void setCalendarEvents(Collection<CalendarEventEntity> calendarEvents) {
        _calendarevents = calendarEvents;
    }

    /*
    @OneToMany(mappedBy = "calendar")
    public Collection<CalendarworkinghoursEntity> getCalendarWorkingHours() {
        return _calendarworkinghours;
    }

    public void setCalendarWorkingHours(Collection<CalendarworkinghoursEntity> calendarworkinghourses) {
        _calendarworkinghours = calendarworkinghourses;
    }
    */
    @OneToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public DoctorEntity getDoctor() {
        return _doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        _doctor = doctor;
    }

    @OneToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public OrthoptistEntity getOrthoptist() {
        return _orthoptist;
    }

    public void setOrthoptist(OrthoptistEntity orthoptist) {
        _orthoptist = orthoptist;
    }
}