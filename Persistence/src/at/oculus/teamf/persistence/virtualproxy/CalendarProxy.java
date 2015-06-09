/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.criteria.interfaces.ICriteria;
import at.oculus.teamf.domain.entity.calendar.*;
import at.oculus.teamf.domain.entity.calendar.calendarevent.CalendarEvent;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.calendar.calendarworkinghours.CalendarWorkingHours;
import at.oculus.teamf.domain.entity.calendar.calendarworkinghours.ICalendarWorkingHours;
import at.oculus.teamf.domain.entity.exception.calendar.NoWorkingHoursException;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Simon Angerer on 03.06.2015.
 */
class CalendarProxy extends VirtualProxy<ICalendar> implements ICalendar {
    protected CalendarProxy(ICalendar real) {
        super(real);
    }

    @Override
    public int getId() {
        return _real.getId();
    }

    @Override
    public Collection<ICalendarWorkingHours> getWorkingHours() throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        if(_real.getWorkingHours() == null) {
            Facade.getInstance().reloadCollection(_real, CalendarWorkingHours.class);
        }
        return _real.getWorkingHours();
    }

    @Override
    public void setId(int id) {
        _real.setId(id);
    }

    @Override
    public void setWorkingHours(Collection<ICalendarWorkingHours> calendarWorkingHourses) {
        _real.setWorkingHours(calendarWorkingHourses);
    }

    @Override
    public void setEvents(Collection<ICalendarEvent> iCalendarEvents) {
        _real.setEvents(iCalendarEvents);
    }

    @Override
    public Collection<ICalendarEvent> getEvents() throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        if(_real.getEvents() == null) {
            Facade.getInstance().reloadCollection(_real, CalendarEvent.class);
        }

        return _real.getEvents();
    }

    @Override
    public boolean isInWorkingTime(ICalendarEvent calendarEvent) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException, NoWorkingHoursException {
        return false;
    }

    @Override
    public boolean isAvailableEvent(ICalendarEvent calendarEvent) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        return false;
    }
}
