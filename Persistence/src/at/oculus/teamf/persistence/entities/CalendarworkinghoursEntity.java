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
@Entity
@Table(name = "calendarworkinghours", schema = "", catalog = "oculus_f")
@IdClass(CalendarworkinghoursEntityPK.class)
public class CalendarworkinghoursEntity {
    private int workingHoursId;
    private int calendarId;
    private WorkinghoursEntity workinghoursByWorkingHoursId;
    private CalendarEntity calendarByCalendarId;

    @Id
    @Column(name = "workingHoursId", nullable = false, insertable = true, updatable = true)
    public int getWorkingHoursId() {
        return workingHoursId;
    }

    public void setWorkingHoursId(int workingHoursId) {
        this.workingHoursId = workingHoursId;
    }

    @Id
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarworkinghoursEntity that = (CalendarworkinghoursEntity) o;

        if (workingHoursId != that.workingHoursId) return false;
        if (calendarId != that.calendarId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workingHoursId;
        result = 31 * result + calendarId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "workingHoursId", referencedColumnName = "workingHoursId", nullable = false)
    public WorkinghoursEntity getWorkinghoursByWorkingHoursId() {
        return workinghoursByWorkingHoursId;
    }

    public void setWorkinghoursByWorkingHoursId(WorkinghoursEntity workinghoursByWorkingHoursId) {
        this.workinghoursByWorkingHoursId = workinghoursByWorkingHoursId;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendarByCalendarId() {
        return calendarByCalendarId;
    }

    public void setCalendarByCalendarId(CalendarEntity calendarByCalendarId) {
        this.calendarByCalendarId = calendarByCalendarId;
    }
}
