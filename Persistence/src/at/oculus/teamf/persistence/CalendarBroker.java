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
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.calendar.Calendar;
import at.oculus.teamf.domain.entity.calendar.calendarevent.CalendarEvent;
import at.oculus.teamf.domain.entity.calendar.calendarworkinghours.CalendarWorkingHours;
import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.calendar.calendarworkinghours.ICalendarWorkingHours;
import at.oculus.teamf.domain.entity.calendar.workinghours.WorkingHours;
import at.oculus.teamf.persistence.entity.CalendarEntity;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.entity.CalendarWorkingHoursEntity;
import at.oculus.teamf.persistence.entity.WorkingHoursEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;

import java.util.Collection;

/**
 * calendar broker translating domain objects to persistence entities
 */
class CalendarBroker extends EntityBroker<ICalendar, CalendarEntity> implements ICollectionReload {

    public CalendarBroker() {
        super(ICalendar.class, CalendarEntity.class);
        addDomainClassMapping(Calendar.class);
    }

    /**
     * converts a persitency entity to a domain object
     *
     * @param entity
     * 		that needs to be converted
     *
     * @return domain object that is created from entity
     *
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    @Override
    protected ICalendar persistentToDomain(CalendarEntity entity) {
        log.debug("converting persistence entity " + _entityClass + " to domain object " + _domainClass);
        ICalendar calendar = (ICalendar) DomainWrapperFactory.getInstance().create(ICalendar.class);
        calendar.setId(entity.getId());

	    return calendar;
    }

    /**
     * Converts a domain object to persitency entity
     *
     * @param obj
     * 		that needs to be converted
     *
     * @return return a persitency entity
     */
    @Override
    protected CalendarEntity domainToPersistent(ICalendar obj) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " +
                _entityClass.getClass());
        CalendarEntity calendarEntity = new CalendarEntity();
        calendarEntity.setId(obj.getId());
        return calendarEntity;
    }

    /**
     * reload collections of a calendar
     *
     * @param session
     * 		session to be used
     * @param obj
     * 		patient
     * @param clazz
     * 		to be reloaded
     *
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidReloadClassException
     */
    @Override
    public void reload(ISession session, Object obj, Class clazz)
            throws BadConnectionException, NoBrokerMappedException, InvalidReloadClassException,
            DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException,
            InvalidSearchParameterException {
        if (clazz == CalendarEvent.class) {
            ((ICalendar) obj).setEvents(reloadCalendarEvents(session, obj));
        } else if (clazz == CalendarWorkingHours.class) {
            ((ICalendar) obj).setWorkingHours(reloadCalendarWorkingHours(session, obj));
        } else {
            throw new InvalidReloadClassException();
        }
    }

    /**
     * reload the calendar events of a calendar
     *
     * @param session
     * 		session to be used
     * @param obj
     * 		patient
     *
     * @return collection of examnation protocols
     *
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<ICalendarEvent> reloadCalendarEvents(ISession session, Object obj)
            throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
            SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        log.debug("reloading calendar events");
        ReloadComponent reloadComponent = new ReloadComponent(CalendarEntity.class, CalendarEvent.class);

        return reloadComponent.reloadCollection(session, ((ICalendar) obj).getId(), new CalendarEventsLoader());
    }

    private class CalendarEventsLoader implements ICollectionLoader<CalendarEventEntity> {

        @Override
        public Collection<CalendarEventEntity> load(Object databaseEntity) {
            return ((CalendarEntity) databaseEntity).getCalendarEvents();
        }
    }

    /**
     * reload the working hours of a calendar
     *
     * @param session
     * 		session to be used
     * @param obj
     * 		patient
     *
     * @return collection of examnation protocols
     *
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    private Collection<ICalendarWorkingHours> reloadCalendarWorkingHours(ISession session, Object obj)
            throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException,
            SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        log.debug("reloading working hours");
        ReloadComponent reloadComponent = new ReloadComponent(CalendarEntity.class, CalendarWorkingHours.class);

        return reloadComponent.reloadCollection(session, ((ICalendar) obj).getId(), new CalendarWorkingHoursLoader());
    }

    private class CalendarWorkingHoursLoader implements ICollectionLoader<CalendarWorkingHoursEntity> {

        @Override
        public Collection<CalendarWorkingHoursEntity> load(Object databaseEntity) {
            return ((CalendarEntity) databaseEntity).getCalendarWorkingHours();
        }
    }
}
