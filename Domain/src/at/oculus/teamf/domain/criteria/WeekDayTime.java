/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.criteria;

import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTime;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.persistence.entity.WeekDayKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;


/**
 * Created by Karo on 28.05.2015.
 * Description: Check if the available Event is at the same weekday as the given weekday
 */
public class WeekDayTime implements IWeekDayTime {
    private WeekDayKey _weekDay;
    private LocalTime _from;
    private LocalTime _to;

    public WeekDayTime(WeekDayKey _weekDay, LocalTime _from, LocalTime _to) {
        this._weekDay = _weekDay;
        this._from = _from;
        this._to = _to;
    }

    @Override
    public int hashCode() {
	    int result = _weekDay != null ? _weekDay.hashCode() : 0;
	    result = 31 * result + (_from != null ? _from.hashCode() : 0);
	    result = 31 * result + (_to != null ? _to.hashCode() : 0);
	    return result;
    }

    @Override
    public boolean equals(Object o) {
	    if (this == o)
		    return true;
	    if (!(o instanceof WeekDayTime))
		    return false;

	    WeekDayTime that = (WeekDayTime) o;

	    if (_from != null ? !_from.equals(that._from) : that._from != null)
		    return false;
	    if (_to != null ? !_to.equals(that._to) : that._to != null)
		    return false;
	    if (_weekDay != that._weekDay)
		    return false;

	    return true;
    }

	@Override
	public String toString() {
		return "WeekDayTime{" +
		       "_weekDay=" + _weekDay +
		       ", _from=" + _from +
		       ", _to=" + _to +
		       '}';
	}

    public boolean isInTime(ICalendarEvent calendarEvent){
        WeekDayKey weekDayKey = WeekDayKey.getWeekDayKey(calendarEvent.getEventStart());

        // start and ent dat not the same day
        if(!weekDayKey.equals(WeekDayKey.getWeekDayKey(calendarEvent.getEventEnd()))) {
            return false;
        }

        // event start date to localtime
        Instant instant = Instant.ofEpochMilli(calendarEvent.getEventStart().getTime());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalTime eventStart = localDateTime.toLocalTime();

        // event end date to localtime
        instant = Instant.ofEpochMilli(calendarEvent.getEventEnd().getTime());
        localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalTime eventEnd = localDateTime.toLocalTime();

        // weekday not equal
        if(weekDayKey.equals(_weekDay)){
            // und morgen oeffnungszeiten vorhanden
            if (_from != null) {
                // start nach oeffnung
                if ((_from.isBefore(eventStart) &&
                        _from.isBefore(eventEnd)) ||
                        _from.equals(eventStart)) {
                    if (_to != null) {
                        // ende vor schliessung
                        if (_to.isAfter(eventEnd) ||
                                _to.equals(eventEnd)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}