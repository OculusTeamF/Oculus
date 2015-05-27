/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.WeekDayKey;

/**
 * CalendarWorkingHours.java
 * Created by oculus on 27.05.15.
 */
public class CalendarWorkingHours implements IDomain {
    private int _id;
    private int _workingHoursId;
    private int _calendarId;
    private WorkingHours _workinghours;
    private Calendar _calendar;
    private WeekDayKey _weekday;

	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void setId(int id) {
		_id = id;
	}

	public int getWorkingHoursId() {
		return _workingHoursId;
	}

	public void setWorkingHoursId(int workingHoursId) {
		_workingHoursId = workingHoursId;
	}

	public int getCalendarId() {
		return _calendarId;
	}

	public void setCalendarId(int calendarId) {
		_calendarId = calendarId;
	}

	public WorkingHours getWorkinghours() {
		return _workinghours;
	}

	public void setWorkinghours(WorkingHours workinghours) {
		_workinghours = workinghours;
	}

	public Calendar getCalendar() {
		return _calendar;
	}

	public void setCalendar(Calendar calendar) {
		_calendar = calendar;
	}

	public WeekDayKey getWeekday() {
		return _weekday;
	}

	public void setWeekday(WeekDayKey weekday) {
		_weekday = weekday;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CalendarWorkingHours))
			return false;

		CalendarWorkingHours that = (CalendarWorkingHours) o;

		if (_calendarId != that._calendarId)
			return false;
		if (_id != that._id)
			return false;
		if (_workingHoursId != that._workingHoursId)
			return false;
		if (_calendar != null ? !_calendar.equals(that._calendar) : that._calendar != null)
			return false;
		if (_weekday != that._weekday)
			return false;
		if (_workinghours != null ? !_workinghours.equals(that._workinghours) : that._workinghours != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + _workingHoursId;
		result = 31 * result + _calendarId;
		result = 31 * result + (_workinghours != null ? _workinghours.hashCode() : 0);
		result = 31 * result + (_calendar != null ? _calendar.hashCode() : 0);
		result = 31 * result + (_weekday != null ? _weekday.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
		return _weekday + ", " + _workinghours;
	}
}
