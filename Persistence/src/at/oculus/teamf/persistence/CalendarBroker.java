/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.entity.CalendarEntity;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;

import java.util.Collection;

/**
 * calendar broker translating domain objects to persistence entities
 */
class CalendarBroker extends EntityBroker<Calendar, CalendarEntity> implements ICollectionReload {

	public CalendarBroker() {
		super(Calendar.class, CalendarEntity.class);
	}

    /**
     * converts a persitency entity to a domain object
     *
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    @Override
	protected Calendar persistentToDomain(CalendarEntity entity) {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        Calendar calendar = new Calendar();
        calendar.setId(entity.getId());
		return calendar;
    }

    /**
     * Converts a domain object to persitency entity
     * @param obj that needs to be converted
     * @return return a persitency entity
     */
    @Override
	protected CalendarEntity domainToPersistent(Calendar obj) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        CalendarEntity calendarEntity = new CalendarEntity();
        calendarEntity.setId(obj.getId());
		return calendarEntity;
    }

    /**
     * reload collections of a calendar
     * @param session session to be used
     * @param obj patient
     * @param clazz to be reloaded
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidReloadClassException
     */
    @Override
	public void reload(ISession session, Object obj, Class clazz) throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException, BadSessionException {
		if (clazz == CalendarEvent.class) {
			((Calendar) obj).setEvents(reloadCalendarEvents(session, obj));
		} else {
			throw new InvalidReloadClassException();
        }
    }

    /**
     * reload the calendar events of a calendar
     *
     * @param session session to be used
     * @param obj     patient
     * @return collection of examnation protocols
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj) throws BadConnectionException, NoBrokerMappedException, BadSessionException {
        log.debug("reloading calendar events");
        ReloadComponent reloadComponent =
                new ReloadComponent(CalendarEntity.class, CalendarEvent.class);

        return reloadComponent.reloadCollection(session, ((Calendar) obj).getId(), new CalendarEventsLoader());
    }

	private class CalendarEventsLoader implements ICollectionLoader<CalendarEventEntity> {

		@Override
		public Collection<CalendarEventEntity> load(Object databaseEntity) {
			return ((CalendarEntity) databaseEntity).getCalendarEvents();
		}
	}
}
