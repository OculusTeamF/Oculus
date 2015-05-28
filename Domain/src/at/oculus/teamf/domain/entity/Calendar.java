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
import at.oculus.teamf.persistence.entity.WeekDayKey;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalTime;
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
    private Collection<CalendarWorkingHours> _workinghours;

    //excluded because of circular dependencies
    //private User _user;
    //</editor-fold>

    public Calendar() {
    }

    //<editor-fold desc="Getter/Setter">
    public int getId() {
        return _id;
    }

    public void setId(int calendarID) {
        _id = calendarID;
    }

    public Collection<CalendarEvent> getEvents()
            throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
            NoBrokerMappedException, DatabaseOperationException {
        if(_events==null) {
            Facade facade = Facade.getInstance();
            facade.reloadCollection(this, CalendarEvent.class);
        }

        return _events;
    }

    public void setEvents(Collection<CalendarEvent> events) {
        _events = events;
    }

    public Collection<CalendarWorkingHours> getWorkingHours()
            throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
            NoBrokerMappedException, DatabaseOperationException {
        if(_workinghours==null) {
            Facade facade = Facade.getInstance();
            facade.reloadCollection(this, CalendarWorkingHours.class);
        }

        return _workinghours;
    }

    public void setWorkingHours(Collection<CalendarWorkingHours> workinghours) {
        _workinghours = workinghours;
    }

    //</editor-fold>

    public boolean isAvailableEvent(ICalendarEvent calendarEvent) {
        Date from = calendarEvent.getEventStart();
        Date to = calendarEvent.getEventEnd();

        // alle vorhandenen Termine ueberpruefen
        for (CalendarEvent c : _events) {
            // wenn Startzeitpunnkt innerhalb eines Termins
            if (c.getEventStart().before(from) && c.getEventEnd().after(from)) {
                return false;
            }
            // wenn Endzeitpunkt innerhalb eines Termins
            if (c.getEventStart().before(to) && c.getEventEnd().after(to)) {
                return false;
            }
            // wenn genau der selbe Termin
            if (c.getEventStart().equals(from) && c.getEventEnd().equals(to)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInWorkingTime(ICalendarEvent calendarEvent) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        WeekDayKey weekDayKey = WeekDayKey.getWeekDayKey(calendarEvent.getEventStart());
        LocalTime eventStart = LocalTime.ofSecondOfDay(calendarEvent.getEventStart().getTime());
        LocalTime eventEnd = LocalTime.ofSecondOfDay(calendarEvent.getEventEnd().getTime());

        for(CalendarWorkingHours c : getWorkingHours()){
            // wenn wochentag stimmt
            if(c.getWeekday()==weekDayKey){
                // und morgen oeffnungszeiten vorhanden
                if(c.getWorkinghours().getMorningFrom()!=null){
                    // start nach oeffnung
                    if(c.getWorkinghours().getMorningFrom().isAfter(eventStart)){
                        if(c.getWorkinghours().getMorningTo()!=null){
                            // ende vor schliessung
                            if(c.getWorkinghours().getMorningTo().isBefore(eventEnd)){
                                return true;
                            }
                        }
                    }
                }

                // wenn nachmittagszeiten vorhanden
                if(c.getWorkinghours().getAfternoonFrom()!=null){
                    // start nach oeffnung
                    if(c.getWorkinghours().getAfternoonFrom().isAfter(eventStart)){
                        if(c.getWorkinghours().getAfternoonTo()!=null){
                            // ende vor schliessung
                            if(c.getWorkinghours().getAfternoonTo().isBefore(eventEnd)){
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public Iterator<CalendarEvent> availableEventsIterator(Collection<Criteria> criterias, int duration)
            throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
            NoBrokerMappedException, DatabaseOperationException {
        Iterator<CalendarEvent> iterator = new CalendarEventIterator(this, criterias, duration);

        return iterator;
    }

    public class CalendarEventIterator implements Iterator<CalendarEvent>, ILogger {
        private Calendar _calendar;
        private int _duration;
        private Collection<Criteria> _criterias;
        private CalendarEvent _lastEvent;
        private CalendarEvent _nextEvent;

        public CalendarEventIterator(Calendar calendar, Collection<Criteria> criterias, int duration)
                throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
                NoBrokerMappedException, DatabaseOperationException {
            _calendar = calendar;
            _duration = duration;
            _lastEvent = new CalendarEvent();
            _lastEvent.setEventEnd(new Date());
            _lastEvent.setEventStart(new Date());
            _criterias = criterias;
        }

        @Override
        public boolean hasNext() {
            if(_nextEvent!=null) {
                return true;
            } else {
                return setNextEvent();
            }
        }

        @Override
        public CalendarEvent next() {
            if(_nextEvent==null){
                setNextEvent();
            }

            _lastEvent = _nextEvent;
            _nextEvent = null;

            return _lastEvent;
        }

        private boolean setNextEvent(){
            CalendarEvent calendarEvent = null;

            while(calendarEvent==null) {
                // Startzeitpunkt setzen
                //TODO ganze stunden setzen
                calendarEvent = new CalendarEvent();
                calendarEvent.setEventStart(_lastEvent.getEventEnd());

                // Endzeitpunkt setzen
                Date eventEnd = _lastEvent.getEventEnd();
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(eventEnd);
                calendar.add(java.util.Calendar.MINUTE, _duration);
                eventEnd = calendar.getTime();
                calendarEvent.setEventEnd(eventEnd);

                // innerhalb der Ordinationszeiten
                try {
                    if(!isInWorkingTime(calendarEvent)){
                        calendarEvent = null;
                    }
                } catch (ReloadInterfaceNotImplementedException | BadConnectionException | DatabaseOperationException | InvalidReloadClassException | NoBrokerMappedException e) {
                    //TODO Exception??
                    log.error(e.getMessage());
                    return false;
                }

                // nicht belegt
                if(!isAvailableEvent(calendarEvent)){
                    calendarEvent = null;
                }

                // Kriterienn
                for(Criteria c : _criterias){
                    if(!c.isValidEvent(calendarEvent)){
                        calendarEvent = null;
                    }
                }
            }

            // CalendarEvent setzen
            _nextEvent = calendarEvent;

            return true;
        }
    }
}