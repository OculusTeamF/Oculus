/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.calendar.CalendarEvent;
import at.oculus.teamf.domain.entity.calendar.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * calendar event broker translating domain objects to persistence entities
 */
class CalendarEventBroker extends EntityBroker<CalendarEvent, CalendarEventEntity> {
	public CalendarEventBroker() {
		super(CalendarEvent.class, CalendarEventEntity.class);
		addDomainClassMapping(ICalendarEvent.class);
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
	protected CalendarEvent persistentToDomain(CalendarEventEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        CalendarEvent event = new CalendarEvent();
        event.setDescription(entity.getDescription());
		event.setEventEnd(entity.getEventEnd());
		event.setId(entity.getId());
		event.setEventStart(entity.getEventStart());

		Integer patientID = entity.getPatientId();
		if(patientID != null) {
			event.setPatient(Facade.getInstance().getById(IPatient.class, entity.getPatientId()));
		}

		return event;
    }

    /**
     * Converts a domain object to persitency entity
     * @param entity that needs to be converted
     * @return return a persitency entity
     */
    @Override
	protected CalendarEventEntity domainToPersistent(CalendarEvent entity) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass() + " !!NOT IMPLEMENTED!!");
        return null;
    }
}
