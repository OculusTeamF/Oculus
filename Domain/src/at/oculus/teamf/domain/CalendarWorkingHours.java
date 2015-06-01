/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain;

import at.oculus.teamf.persistence.entity.WeekDayKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * CalendarWorkingHours.java Created by oculus on 27.05.15.
 */
public class CalendarWorkingHours implements ICalendarWorkingHours {
	private int _id;
	private int _workingHoursId;
	private int _calendarId;
	private IWorkingHours _workinghours;
	private ICalendar _calendar;
	private WeekDayKey _weekday;

	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
	public int getWorkingHoursId() {
		return _workingHoursId;
	}

	@Override
	public void setWorkingHoursId(int workingHoursId) {
		_workingHoursId = workingHoursId;
	}

	@Override
	public int getCalendarId() {
		return _calendarId;
	}

	@Override
	public void setCalendarId(int calendarId) {
		_calendarId = calendarId;
	}

	@Override
	public IWorkingHours getWorkinghours() {
		return _workinghours;
	}

	@Override
	public void setWorkinghours(IWorkingHours workinghours) {
		_workinghours = workinghours;
	}

	@Override
	public ICalendar getCalendar() {
		return _calendar;
	}

	@Override
	public void setCalendar(ICalendar calendar) {
		_calendar = calendar;
	}

	@Override
	public WeekDayKey getWeekday() {
		return _weekday;
	}

	@Override
	public void setWeekday(WeekDayKey weekday) {
		_weekday = weekday;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ICalendarWorkingHours))
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
	public String toString() {
		return _weekday + ", " + _workinghours;
	}

	public boolean contains(ICalendarEvent calendarEvent) {
		WeekDayKey weekDayKey = WeekDayKey.getWeekDayKey(calendarEvent.getEventStart());

		// event start date to localtime
		Instant instant = Instant.ofEpochMilli(calendarEvent.getEventStart().getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalTime eventStart = localDateTime.toLocalTime();

		// event end date to localtime
		instant = Instant.ofEpochMilli(calendarEvent.getEventEnd().getTime());
		localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalTime eventEnd = localDateTime.toLocalTime();

		// wenn wochentag stimmt
		if (getWeekday() == weekDayKey) {
			// und morgen oeffnungszeiten vorhanden
			if (getWorkinghours().getMorningFrom() != null) {
				// start nach oeffnung
				if ((getWorkinghours().getMorningFrom().isBefore(eventStart) &&
				     getWorkinghours().getMorningFrom().isBefore(eventEnd)) ||
				    getWorkinghours().getMorningFrom().equals(eventStart)) {
					if (getWorkinghours().getMorningTo() != null) {
						// ende vor schliessung
						if (getWorkinghours().getMorningTo().isAfter(eventEnd) ||
						    getWorkinghours().getMorningTo().equals(eventEnd)) {
							return true;
						}
					}
				}
			}

			// oder nachmittagszeiten vorhanden
			if (getWorkinghours().getAfternoonFrom() != null) {
				// start nach oeffnung
				if ((getWorkinghours().getAfternoonFrom().isBefore(eventStart) &&
				     getWorkinghours().getAfternoonFrom().isBefore(eventEnd)) ||
				    getWorkinghours().getAfternoonFrom().equals(eventStart)) {
					if (getWorkinghours().getAfternoonTo() != null) {
						// ende vor schliessung
						if (getWorkinghours().getAfternoonTo().isAfter(eventEnd) ||
						    getWorkinghours().getAfternoonTo().equals(eventEnd)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}