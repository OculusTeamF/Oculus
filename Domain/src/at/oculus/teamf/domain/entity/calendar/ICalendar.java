/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.calendar;

import at.oculus.teamf.domain.criteria.interfaces.ICriteria;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by FabianLaptop on 08.04.2015.
 */
public interface ICalendar extends IDomain {
    int getId();

    void setWorkingHours(Collection<ICalendarWorkingHours> calendarWorkingHourses);

    void setEvents(Collection<ICalendarEvent> iCalendarEvents);

	Iterator<ICalendarEvent> availableEventsIterator(Collection<ICriteria> criterias, int duration) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
            NoBrokerMappedException, DatabaseOperationException;

    Collection<ICalendarEvent> getEvents() throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
                                                  NoBrokerMappedException, DatabaseOperationException;

    /*
    void setId(int calendarID);

    void setEvents(Collection<CalendarEvent> events);
    */
}