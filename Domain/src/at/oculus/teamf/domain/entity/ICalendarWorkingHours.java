/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.entity.WeekDayKey;

/**
 * Created by Simon Angerer on 28.05.2015.
 */
public interface ICalendarWorkingHours extends IDomain {
    @Override
    int getId();

    @Override
    void setId(int id);

    int getWorkingHoursId();

    void setWorkingHoursId(int workingHoursId);

    int getCalendarId();

    void setCalendarId(int calendarId);

    IWorkingHours getWorkinghours();

    void setWorkinghours(IWorkingHours workinghours);

    ICalendar getCalendar();

    void setCalendar(ICalendar calendar);

    WeekDayKey getWeekday();

    void setWeekday(WeekDayKey weekday);

    boolean contains(ICalendarEvent calendarEvent);
}
