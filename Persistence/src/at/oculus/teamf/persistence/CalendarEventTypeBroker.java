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
import at.oculus.teamf.persistence.entities.EventtypeEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.LinkedList;

/**
 * Created by Norskan on 10.04.2015.
 */
public class CalendarEventTypeBroker extends EntityBroker<EventType, EventtypeEntity> {

	public CalendarEventTypeBroker() {
		super(EventType.class, EventtypeEntity.class);

		// add domain subclasses to broker
		addDomainClass(FirstAppointment.class);
		addDomainClass(OrthoptistAppointment.class);
		addDomainClass(RegularAppointment.class);
	}

	@Override
	protected EventType persitentToDomain(EventtypeEntity entity) throws FacadeException {
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

	@Override
	protected EventtypeEntity domainToPersitent(EventType obj) {
		return new EventtypeEntity(obj.getId(),obj.getEventTypeName(),obj.getEstimatedTime(),obj.getDescription());
	}
}
