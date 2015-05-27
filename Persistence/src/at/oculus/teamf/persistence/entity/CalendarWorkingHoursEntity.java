/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entity;

import javax.persistence.*;

/**
 * Hibernate annotated calendar working hours class
 */
@Entity
@Table(name = "calendarworkinghours", schema = "", catalog = "oculus_f")
public class CalendarWorkingHoursEntity implements IEntity {
	private int _id;
    private int _workingHoursId;
    private int _calendarId;
    private WorkingHoursEntity _workinghours;
    private CalendarEntity _calendar;
    private WeekDayKey _weekday;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "calendarWorkingHoursId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}

	public void setId(int calendarWorkingHoursId) {
		_id = calendarWorkingHoursId;
	}

    @Basic
    @Column(name = "workingHoursId", nullable = false, insertable = false, updatable = false)
    public int getWorkingHoursId() {
        return _workingHoursId;
    }

    public void setWorkingHoursId(int workingHoursId) {
        this._workingHoursId = workingHoursId;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = false, updatable = false)
    public int getCalendarId() {
        return _calendarId;
    }

    public void setCalendarId(int calendarId) {
        this._calendarId = calendarId;
    }

	@Column(name = "weekDayKey", columnDefinition = "ENUM('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN')")
	@Enumerated(EnumType.STRING)
	public WeekDayKey getWeekday() {
		return _weekday;
	}

	public void setWeekday(WeekDayKey weekday) {
		_weekday = weekday;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarWorkingHoursEntity that = (CalendarWorkingHoursEntity) o;

        return _id == that._id && _workingHoursId == that._workingHoursId && _calendarId == that._calendarId;
    }

    @Override
    public int hashCode() {
        int result = _workingHoursId;
        result = 31 * result + _calendarId;
	    result = 31 * result + _id;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "workingHoursId", referencedColumnName = "workingHoursId", nullable = false)
    public WorkingHoursEntity getWorkinghours() {
        return _workinghours;
    }

    public void setWorkinghours(WorkingHoursEntity workinghours) {
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
