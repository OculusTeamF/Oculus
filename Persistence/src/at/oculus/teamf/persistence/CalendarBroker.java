/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.entities.CalendarEntity;
import at.oculus.teamf.persistence.entities.CalendarEventEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.InvalideReloadParameterExeption;
import at.oculus.teamf.persistence.exceptions.NoBrokerMappedException;
import at.oculus.teamf.persistence.exceptions.NotAbleToLoadClassException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Norskan on 08.04.2015. //Todo: add docs
 */
class CalendarBroker extends EntityBroker<Calendar, CalendarEntity> implements ICollectionReload {

	public CalendarBroker() {
		super(Calendar.class, CalendarEntity.class);
	}

	@Override
	protected Calendar persitentToDomain(CalendarEntity entity) {
		Calendar calendar = new Calendar();
		calendar.setCalendarID(entity.getId());
		return calendar;
	}

	@Override
	protected CalendarEntity domainToPersitent(Calendar obj) {
		//Todo: reverse
		return null;
	}

	@Override
	public void reload(ISession session, Object obj, Class clazz) throws FacadeException {
		if (clazz == CalendarEvent.class) {
			((Calendar)obj).setEvents(reloadCalendarEvents(session, obj));
		} else {
			throw new InvalideReloadParameterExeption();
		}
	}

	//Todo: extract into generic reload component
	private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj) throws FacadeException {
		Facade facade = Facade.getInstance();

		//get broker
		EntityBroker broker = null;
		try {
			broker = facade.getBroker(obj.getClass());
		} catch (NoBrokerMappedException e) {
			//Todo: add Loging
			e.printStackTrace();

			throw new NotAbleToLoadClassException();
		}

		//load calendar

		CalendarEntity calendarEntity = null;
		try {
			calendarEntity = (CalendarEntity) session.getByID(CalendarEntity.class, ((Calendar)obj).getCalendarID());
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		}
		if (calendarEntity == null) {
			throw new NotAbleToLoadClassException();
		}

		//load entity collection
		Collection<CalendarEventEntity> calendarEntities =
				(Collection<CalendarEventEntity>) (Collection<?>) calendarEntity.getCalendarEvents();


		//convert to domain object
		try {
			broker = facade.getBroker(CalendarEvent.class);
		} catch (NoBrokerMappedException e) {
			//Todo: add Loging
			e.printStackTrace();

			throw new NotAbleToLoadClassException();
		}

		Collection<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		for(CalendarEventEntity cee : calendarEntities) {
			calendarEvents.add((CalendarEvent)broker.persitentToDomain(cee));
		}

		return calendarEvents;
	}
}
