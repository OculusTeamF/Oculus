/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.EventType;
import at.oculus.teamf.domain.entity.FirstAppointment;
import at.oculus.teamf.domain.entity.OrthoptistAppointment;
import at.oculus.teamf.domain.entity.RegularAppointment;
import at.oculus.teamf.persistence.entity.EventtypeEntity;

/**
 * calendar event typ broker translating domain objects to persistence entities
 */
public class CalendarEventTypeBroker extends EntityBroker<EventType, EventtypeEntity> {

	public CalendarEventTypeBroker() {
		super(EventType.class, EventtypeEntity.class);

		// add domain subclasses to broker
		addDomainClass(FirstAppointment.class);
		addDomainClass(OrthoptistAppointment.class);
		addDomainClass(RegularAppointment.class);
	}

    /**
     * converts a persitency entity to a domain object
     *
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     */
    @Override
	protected EventType persistentToDomain(EventtypeEntity entity) {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        EventType eventType = null;

		switch (entity.getEventTypeName()) {
			case ("Ersttermin"):
				eventType = new FirstAppointment(entity.getId(), entity.getEventTypeName(), entity.getEstimatedTime(),
				                                 entity.getDescription());
				break;
			case ("Orthoptistentermin"):
				eventType =
						new OrthoptistAppointment(entity.getId(), entity.getEventTypeName(), entity.getEstimatedTime(),
						                          entity.getDescription());
				break;
			default:
				eventType = new RegularAppointment(entity.getId(), entity.getEventTypeName(), entity.getEstimatedTime(),
				                                   entity.getDescription());
				break;
		}

		return eventType;
    }

    /**
     * Converts a domain object to persitency entity
     * @param obj that needs to be converted
     * @return return a persitency entity
     */
    @Override
	protected EventtypeEntity domainToPersistent(EventType obj) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        return new EventtypeEntity(obj.getId(), obj.getEventTypeName(), obj.getEstimatedTime(), obj.getDescription());
    }
}
