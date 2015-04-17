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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.entity.CalendarEntity;
import at.oculus.teamf.persistence.entity.CalendarEventEntity;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.InvalidReloadParameterException;

import java.util.Collection;

/**
 * Created by Norskan on 08.04.2015. //Todo: add docs
 */
class CalendarBroker extends EntityBroker<Calendar, CalendarEntity> implements ICollectionReload {

	public CalendarBroker() {
		super(Calendar.class, CalendarEntity.class);
	}

	@Override
	protected Calendar persistentToDomain(CalendarEntity entity) throws FacadeException {
		Calendar calendar = new Calendar();
		calendar.setId(entity.getId());


		/*if (entity.getDoctor() != null) {
			calendar.setUser(
					(Doctor) Facade.getInstance().getBroker(Doctor.class).persistentToDomain(entity.getDoctor()));
		} else if (entity.getOrthoptist() != null) {
			calendar.setUser((Orthoptist) Facade.getInstance().getBroker(Orthoptist.class)
			                                    .persistentToDomain(entity.getOrthoptist()));
		} else {
			//Todo: add Logging
			System.out.println("No User mapped to calendar!");
		}*/
		return calendar;
	}

	@Override
	protected CalendarEntity domainToPersistent(Calendar obj) {
		CalendarEntity calendarEntity = new CalendarEntity();
		calendarEntity.setId(obj.getId());
		return calendarEntity;
	}

	@Override
	public void reload(ISession session, Object obj, Class clazz) throws FacadeException {
		if (clazz == CalendarEvent.class) {
			((Calendar) obj).setEvents(reloadCalendarEvents(session, obj));
		} else {
			throw new InvalidReloadParameterException();
		}
	}

	private class CalendarEventsLoader implements CollectionLoader<CalendarEventEntity> {

		@Override
		public Collection<CalendarEventEntity> load(Object databaseEntity) {
			return ((CalendarEntity) databaseEntity).getCalendarEvents();
		}
	}

	private Collection<CalendarEvent> reloadCalendarEvents(ISession session, Object obj) throws FacadeException {
		ReloadComponent reloadComponent =
				new ReloadComponent(CalendarEntity.class, CalendarEvent.class);

		return reloadComponent.reloadCollection(session, ((Calendar) obj).getId(), new CalendarEventsLoader());
	}
}
