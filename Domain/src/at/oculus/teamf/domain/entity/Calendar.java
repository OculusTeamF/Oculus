/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.criteria.Criteria;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Date;

/**
 * @author Simon Angerer
 */
public class Calendar implements ICalendar {

    //<editor-fold desc="Attributes">
    private int _id;
    private Collection<CalendarEvent> _events;

    //excluded because of circular dependencies
    //private User _user;
    //</editor-fold>

	public Calendar() {	}

	//<editor-fold desc="Getter/Setter">
    public int getId() {
        return _id;
    }

    public void setId(int calendarID) {
        _id = calendarID;
    }

    public Collection<CalendarEvent> getEvents() throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        Facade facade = Facade.getInstance();

        facade.reloadCollection(this, CalendarEvent.class);

        return _events;
    }

    public void setEvents(Collection<CalendarEvent> events) {
        _events =events;
    }

    //</editor-fold>

    public boolean isAvailableEvent(Date from, Date to){
        // alle vorhandenen Termine ueberpruefen
        for(ICalendarEvent calendarEvent : _events){
            // wenn Startzeitpunnkt innerhalb eines Termins
            if(calendarEvent.getEventStart().before(from) && calendarEvent.getEventEnd().after(from)){
                return false;
            }
            // wenn Endzeitpunkt innerhalb eines Termins
            if(calendarEvent.getEventStart().before(to) && calendarEvent.getEventEnd().after(to)){
                return false;
            }
            // wenn genau der selbe Termin
            if(calendarEvent.getEventStart().equals(from) && calendarEvent.getEventEnd().equals(to)){
                return false;
            }
        }
        return true;
    }

    public Iterator<CalendarEvent> getAvailableEvents(Criteria... criterias) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        Iterator<CalendarEvent> iterator = new CalendarEventIterator(this, criterias);

        return iterator;
    }

    public class CalendarEventIterator implements Iterator<CalendarEvent>{
        private Collection<CalendarEvent> _calendarEvents;

        public CalendarEventIterator(Calendar calendar, Criteria... criterias) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
            _calendarEvents = calendar.getEvents();
        }

        @Override
        public boolean hasNext() {
            return _calendarEvents.isEmpty();
        }

        @Override
        public CalendarEvent next() {
            return ((LinkedList<CalendarEvent>) _calendarEvents).removeFirst();
        }
    }
}