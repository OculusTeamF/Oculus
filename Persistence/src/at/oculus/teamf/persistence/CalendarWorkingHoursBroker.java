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
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.CalendarWorkingHours;
import at.oculus.teamf.domain.entity.ICalendarWorkingHours;
import at.oculus.teamf.domain.entity.WorkingHours;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.entity.CalendarWorkingHoursEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

/**
 * CalendarWorkingHoursBroker.java Created by oculus on 27.05.15.
 */
class CalendarWorkingHoursBroker extends EntityBroker {
	public CalendarWorkingHoursBroker() {
		super(CalendarWorkingHours.class, CalendarWorkingHoursEntity.class);
		addDomainClassMapping(ICalendarWorkingHours.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		ICalendarWorkingHours calendarWorkingHours = new CalendarWorkingHours();
		CalendarWorkingHoursEntity calendarWorkingHoursEntity = (CalendarWorkingHoursEntity) entity;

		calendarWorkingHours.setId(calendarWorkingHoursEntity.getId());
		calendarWorkingHours.setCalendarId(calendarWorkingHoursEntity.getCalendarId());
		calendarWorkingHours.setCalendar((Calendar) Facade.getInstance().getBroker(Calendar.class).persistentToDomain(
				calendarWorkingHoursEntity.getCalendar()));
		calendarWorkingHours.setWeekday(calendarWorkingHoursEntity.getWeekday());
		calendarWorkingHours.setWorkingHoursId(calendarWorkingHoursEntity.getWorkingHoursId());
		calendarWorkingHours.setWorkinghours((WorkingHours) Facade.getInstance().getBroker(WorkingHours.class).persistentToDomain(calendarWorkingHoursEntity.getWorkinghours()));

		return calendarWorkingHours;
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		return null;
	}
}
