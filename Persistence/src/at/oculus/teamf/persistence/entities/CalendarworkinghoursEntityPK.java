/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Norskan on 07.04.2015.
 */
public class CalendarworkinghoursEntityPK implements Serializable {
    private int workingHoursId;
    private int calendarId;

    @Column(name = "workingHoursId", nullable = false, insertable = true, updatable = true)
    @Id
    public int get_workingHoursId() {
        return workingHoursId;
    }

    public void set_workingHoursId(int workingHoursId) {
        this.workingHoursId = workingHoursId;
    }

    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    @Id
    public int get_calendarId() {
        return calendarId;
    }

    public void set_calendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarworkinghoursEntityPK that = (CalendarworkinghoursEntityPK) o;

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
}
