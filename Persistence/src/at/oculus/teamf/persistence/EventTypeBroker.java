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
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.calendar.calendarevent.EventType;
import at.oculus.teamf.domain.entity.calendar.calendarevent.IEventType;
import at.oculus.teamf.persistence.entity.EventtypeEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

/**
 * EventTypeBroker.java Created by oculus on 08.06.15.
 */
public class EventTypeBroker extends EntityBroker {
	public EventTypeBroker() {
		super(EventType.class, EventtypeEntity.class);
		addDomainClassMapping(IEventType.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		EventtypeEntity eventtypeEntity = (EventtypeEntity) entity;
		EventType eventType = new EventType();

		eventType.setId(eventtypeEntity.getId());
		eventType.setDescription(eventtypeEntity.getDescription());
		eventType.setEstimatedTime(eventtypeEntity.getEstimatedTime());
		eventType.setEventTypeName(eventtypeEntity.getEventTypeName());

		return eventType;
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		EventType eventType = (EventType) obj;
		EventtypeEntity eventtypeEntity = new EventtypeEntity();

		eventtypeEntity.setId(eventType.getId());
		eventtypeEntity.setEventTypeName(eventType.getEventTypeName());
		eventtypeEntity.setDescription(eventType.getDescription());
		eventtypeEntity.setEstimatedTime(eventType.getEstimatedTime());

		return eventtypeEntity;
	}
}
