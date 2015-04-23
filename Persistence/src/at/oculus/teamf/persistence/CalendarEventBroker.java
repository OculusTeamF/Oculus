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
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * Created by Norskan on 08.04.2015.
 */
class CalendarEventBroker extends EntityBroker<CalendarEvent, CalendarEventEntity> {
	public CalendarEventBroker() {
		super(CalendarEvent.class, CalendarEventEntity.class);
	}

	@Override
	protected CalendarEvent persistentToDomain(CalendarEventEntity entity) throws NoBrokerMappedException, BadConnectionException {
		CalendarEvent event = new CalendarEvent();
		event.setDescription(entity.getDescription());
		event.setEventEnd(entity.getEventEnd());
		event.setId(entity.getId());
		event.setEventStart(entity.getEventStart());

		Integer patientID = entity.getPatientId();
		if(patientID != null) {
			event.addPatient((Patient) Facade.getInstance().getById(Patient.class, entity.getPatientId()));
		}

		return event;
	}

	@Override
	protected CalendarEventEntity domainToPersistent(CalendarEvent entity) {
		//Todo: reverse
		return null;
	}
}
