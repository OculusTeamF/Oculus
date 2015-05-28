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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
		if (_events == null) {
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
		if (_workinghours == null) {
			Facade facade = Facade.getInstance();
			facade.reloadCollection(this, CalendarWorkingHours.class);
		}

		return _workinghours;
	}

	public void setWorkingHours(Collection<CalendarWorkingHours> workinghours) {
		_workinghours = workinghours;
	}

	//</editor-fold>

	public boolean isAvailableEvent(ICalendarEvent calendarEvent)
			throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
			       NoBrokerMappedException, DatabaseOperationException {
		Date from = calendarEvent.getEventStart();
		Date to = calendarEvent.getEventEnd();

		// alle vorhandenen Termine ueberpruefen
		for (CalendarEvent c : getEvents()) {
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
		WeekDayKey weekDayKey = WeekDayKey.getWeekDayKey(calendarEvent.getEventStart());

		// event start date to localtime
		Instant instant = Instant.ofEpochSecond(calendarEvent.getEventStart().getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalTime eventStart = localDateTime.toLocalTime();

		// event end date to localtime
		instant = Instant.ofEpochSecond(calendarEvent.getEventEnd().getTime());
		localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalTime eventEnd = localDateTime.toLocalTime();

		for (CalendarWorkingHours c : getWorkingHours()) {
			// wenn wochentag stimmt
			if (c.getWeekday() == weekDayKey) {
				// und morgen oeffnungszeiten vorhanden
				if (c.getWorkinghours().getMorningFrom() != null) {
					// start nach oeffnung
					if (c.getWorkinghours().getMorningFrom().isAfter(eventStart)) {
						if (c.getWorkinghours().getMorningTo() != null) {
							// ende vor schliessung
							if (c.getWorkinghours().getMorningTo().isBefore(eventEnd)) {
								return true;
							}
						}
					}
				}

				// wenn nachmittagszeiten vorhanden
				if (c.getWorkinghours().getAfternoonFrom() != null) {
					// start nach oeffnung
					if (c.getWorkinghours().getAfternoonFrom().isAfter(eventStart)) {
						if (c.getWorkinghours().getAfternoonTo() != null) {
							// ende vor schliessung
							if (c.getWorkinghours().getAfternoonTo().isBefore(eventEnd)) {
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
		private final int _interval = 15;

		public CalendarEventIterator(Calendar calendar, Collection<Criteria> criterias, int duration)
				throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException,
				       NoBrokerMappedException, DatabaseOperationException {
			// round up to next hour
			java.util.Calendar calendarRound = java.util.Calendar.getInstance();
			calendarRound.setTime(new Date());
			calendarRound.add(java.util.Calendar.HOUR, 1);
			calendarRound.set(java.util.Calendar.MINUTE, 0);
			Date nextHour = calendarRound.getTime();

			// set attributes
			_calendar = calendar;
			_duration = duration;
			_lastEvent = new CalendarEvent();
			_lastEvent.setEventEnd(nextHour);
			_lastEvent.setEventStart(nextHour);
			_criterias = criterias;
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
		public CalendarEvent next() {
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
			CalendarEvent calendarEventLast = _lastEvent;

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
				if (calendarEvent != null && _criterias!=null) {
					for (Criteria c : _criterias) {
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
					calendarNext.add(java.util.Calendar.MINUTE, _interval);
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