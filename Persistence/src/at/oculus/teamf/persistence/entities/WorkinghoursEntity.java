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
import java.sql.Time;
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "workinghours", schema = "", catalog = "oculus_f")
public class WorkinghoursEntity {
    private int workingHoursId;
    private String weekDayKey;
    private Time morningFrom;
    private Time morningTo;
    private Time afternoonFrom;
    private Time afternoonTo;
    private Collection<CalendarworkinghoursEntity> calendarworkinghoursesByWorkingHoursId;
    private WeekdayEntity weekdayByWeekDayKey;

    @Id
    @Column(name = "workingHoursId", nullable = false, insertable = true, updatable = true)
    public int getWorkingHoursId() {
        return workingHoursId;
    }

    public void setWorkingHoursId(int workingHoursId) {
        this.workingHoursId = workingHoursId;
    }

    @Basic
    @Column(name = "weekDayKey", nullable = false, insertable = true, updatable = true, length = 3)
    public String getWeekDayKey() {
        return weekDayKey;
    }

    public void setWeekDayKey(String weekDayKey) {
        this.weekDayKey = weekDayKey;
    }

    @Basic
    @Column(name = "morningFrom", nullable = true, insertable = true, updatable = true)
    public Time getMorningFrom() {
        return morningFrom;
    }

    public void setMorningFrom(Time morningFrom) {
        this.morningFrom = morningFrom;
    }

    @Basic
    @Column(name = "morningTo", nullable = true, insertable = true, updatable = true)
    public Time getMorningTo() {
        return morningTo;
    }

    public void setMorningTo(Time morningTo) {
        this.morningTo = morningTo;
    }

    @Basic
    @Column(name = "afternoonFrom", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonFrom() {
        return afternoonFrom;
    }

    public void setAfternoonFrom(Time afternoonFrom) {
        this.afternoonFrom = afternoonFrom;
    }

    @Basic
    @Column(name = "afternoonTo", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonTo() {
        return afternoonTo;
    }

    public void setAfternoonTo(Time afternoonTo) {
        this.afternoonTo = afternoonTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkinghoursEntity that = (WorkinghoursEntity) o;

        if (workingHoursId != that.workingHoursId) return false;
        if (weekDayKey != null ? !weekDayKey.equals(that.weekDayKey) : that.weekDayKey != null) return false;
        if (morningFrom != null ? !morningFrom.equals(that.morningFrom) : that.morningFrom != null) return false;
        if (morningTo != null ? !morningTo.equals(that.morningTo) : that.morningTo != null) return false;
        if (afternoonFrom != null ? !afternoonFrom.equals(that.afternoonFrom) : that.afternoonFrom != null)
            return false;
        if (afternoonTo != null ? !afternoonTo.equals(that.afternoonTo) : that.afternoonTo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = workingHoursId;
        result = 31 * result + (weekDayKey != null ? weekDayKey.hashCode() : 0);
        result = 31 * result + (morningFrom != null ? morningFrom.hashCode() : 0);
        result = 31 * result + (morningTo != null ? morningTo.hashCode() : 0);
        result = 31 * result + (afternoonFrom != null ? afternoonFrom.hashCode() : 0);
        result = 31 * result + (afternoonTo != null ? afternoonTo.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "workinghoursByWorkingHoursId")
    public Collection<CalendarworkinghoursEntity> getCalendarworkinghoursesByWorkingHoursId() {
        return calendarworkinghoursesByWorkingHoursId;
    }

    public void setCalendarworkinghoursesByWorkingHoursId(Collection<CalendarworkinghoursEntity> calendarworkinghoursesByWorkingHoursId) {
        this.calendarworkinghoursesByWorkingHoursId = calendarworkinghoursesByWorkingHoursId;
    }

    @ManyToOne
    @JoinColumn(name = "weekDayKey", referencedColumnName = "weekDayKey", nullable = false)
    public WeekdayEntity getWeekdayByWeekDayKey() {
        return weekdayByWeekDayKey;
    }

    public void setWeekdayByWeekDayKey(WeekdayEntity weekdayByWeekDayKey) {
        this.weekdayByWeekDayKey = weekdayByWeekDayKey;
    }
}
