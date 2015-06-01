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
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

/**
 * @author Simon Angerer
 */
public class Calendar implements ICalendar {

	//<editor-fold desc="Attributes">
	private int _id;
	private Collection<ICalendarEvent> _events;
	private Collection<ICalendarWorkingHours> _workinghours;

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

	public Collection<ICalendarEvent> getEvents()
			throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		if (_events == null) {
			Facade facade = Facade.getInstance();
			facade.reloadCollection(this, CalendarEvent.class);
		}

		return _events;
	}

	@Override
	public void setEvents(Collection<ICalendarEvent> events) {
		_events = events;
	}

	public Collection<ICalendarWorkingHours> getWorkingHours()
			throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		if (_workinghours == null) {
			Facade facade = Facade.getInstance();
			facade.reloadCollection(this, CalendarWorkingHours.class);
		}

		return _workinghours;
	}

	public void setWorkingHours(Collection<ICalendarWorkingHours> workinghours) {
		_workinghours = workinghours;
	}

	//</editor-fold>

	public boolean isAvailableEvent(ICalendarEvent calendarEvent)
			throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		Date from = calendarEvent.getEventStart();
		Date to = calendarEvent.getEventEnd();

		// alle vorhandenen Termine ueberpruefen
		for (ICalendarEvent c : getEvents()) {
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

	public boolean isInWorkingTime(ICalendarEvent calendarEvent)
			throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		for (ICalendarWorkingHours c : getWorkingHours()) {
			if(c.contains(calendarEvent)){
                return true;
            }
		}
		return false;
	}

	@Override
	public Iterator<ICalendarEvent> availableEventsIterator(Collection<ICriteria> criterias, int duration)
			throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		Iterator<ICalendarEvent> iterator = new CalendarEventIterator(this, criterias, duration);

		return iterator;
	}

	public class CalendarEventIterator implements Iterator<ICalendarEvent>, ILogger {
		private Calendar _calendar;
		private int _duration;
		private Collection<ICriteria> _I_criterias;
		private CalendarEvent _lastEvent;
		private CalendarEvent _nextEvent;
		private final int INTERVAL = 15;

		public CalendarEventIterator(Calendar calendar, Collection<ICriteria> ICriterias, int duration)
				throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
				       NoBrokerMappedException, DatabaseOperationException {
			// round up to next hour
			_lastEvent = new CalendarEvent();
			java.util.Calendar calendarRound = java.util.Calendar.getInstance();
			calendarRound.setTime(new Date());
			calendarRound.add(java.util.Calendar.HOUR, 1);
			calendarRound.set(java.util.Calendar.MINUTE, 0);
            calendarRound.set(java.util.Calendar.SECOND, 0);
            calendarRound.set(java.util.Calendar.MILLISECOND, 0);
			Date nextHour = calendarRound.getTime();
			_lastEvent.setEventStart(nextHour);


			// set attributes
			_calendar = calendar;
			_duration = duration;

			_lastEvent.setEventEnd(nextHour);

			_I_criterias = ICriterias;
		}

		@Override
		public boolean hasNext() {
			if (_nextEvent != null) {
				return true;
			} else {
				try {
					return setNextEvent();
				} catch (ReloadInterfaceNotImplementedException | InvalidReloadClassException | NoBrokerMappedException | DatabaseOperationException | BadConnectionException e) {
					//TODO exceptions
					e.printStackTrace();
					return false;
				}
			}
		}

		@Override
		public ICalendarEvent next() {
			if (_nextEvent == null) {
				try {
					setNextEvent();
				} catch (ReloadInterfaceNotImplementedException | InvalidReloadClassException | NoBrokerMappedException | DatabaseOperationException | BadConnectionException e) {
					//TODO exceptions
					e.printStackTrace();
				}
			}

			_lastEvent = _nextEvent;
			_nextEvent = null;

			return _lastEvent;
		}

		private boolean setNextEvent()
				throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
				       NoBrokerMappedException, DatabaseOperationException {
			CalendarEvent calendarEvent = null;
			CalendarEvent calendarEventLast = (CalendarEvent) _lastEvent.clone();

			while (calendarEvent == null) {
				// set start date
				calendarEvent = new CalendarEvent();
				calendarEvent.setEventStart(calendarEventLast.getEventEnd());

				// set end date
				Date eventEnd = calendarEventLast.getEventEnd();
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				calendar.setTime(eventEnd);
				calendar.add(java.util.Calendar.MINUTE, _duration);
				eventEnd = calendar.getTime();
				calendarEvent.setEventEnd(eventEnd);

				// check working time
				try {
					if (!isInWorkingTime(calendarEvent)) {
						calendarEvent = null;
					}
				} catch (ReloadInterfaceNotImplementedException | BadConnectionException | DatabaseOperationException | InvalidReloadClassException | NoBrokerMappedException e) {
					//TODO Exception??
					log.error(e.getMessage());
					return false;
				}

				// check availability
				if (calendarEvent != null) {
					if (!isAvailableEvent(calendarEvent)) {
						calendarEvent = null;
					}
				}

				// check criterias
				if (calendarEvent != null && _I_criterias !=null) {
					for (ICriteria c : _I_criterias) {
						if (!c.isValidEvent(calendarEvent)) {
							calendarEvent = null;
							break;
						}
					}
				}

				// try next timeslot in 15min
				if (calendarEvent == null) {
					Date newDate = calendarEventLast.getEventStart();
					java.util.Calendar calendarNext = java.util.Calendar.getInstance();
					calendarNext.setTime(newDate);
					calendarNext.add(java.util.Calendar.MINUTE, INTERVAL);
					newDate = calendarNext.getTime();
					calendarEventLast.setEventStart(newDate);
					calendarEventLast.setEventEnd(newDate);
				}

			}

			// set next event
			_nextEvent = calendarEvent;

			return true;
		}
	}
}