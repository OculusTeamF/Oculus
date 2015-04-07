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

/**
 * Created by Norskan on 07.04.2015.
 */
// TODO PropertyHelper für catalog="oculus_f"
@Entity
@Table(name = "calendarworkinghours", schema = "", catalog = "oculus_f")
@IdClass(CalendarworkinghoursEntityPK.class)
public class CalendarworkinghoursEntity {
    private int _workingHoursId;
    private int _calendarId;
    private WorkinghoursEntity _workinghours;
    private CalendarEntity _calendar;

    @Id
    @Column(name = "workingHoursId", nullable = false, insertable = true, updatable = true)
    public int getWorkingHoursId() {
        return _workingHoursId;
    }

    public void setWorkingHoursId(int workingHoursId) {
        this._workingHoursId = workingHoursId;
    }

    @Id
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return _calendarId;
    }

    public void setCalendarId(int calendarId) {
        this._calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarworkinghoursEntity that = (CalendarworkinghoursEntity) o;

        if (_workingHoursId != that._workingHoursId) return false;
        if (_calendarId != that._calendarId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _workingHoursId;
        result = 31 * result + _calendarId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "workingHoursId", referencedColumnName = "workingHoursId", nullable = false)
    public WorkinghoursEntity getWorkinghours() {
        return _workinghours;
    }

    public void setWorkinghours(WorkinghoursEntity workinghours) {
        _workinghours = workinghours;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendar() {
        return _calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        _calendar = calendar;
    }
}
