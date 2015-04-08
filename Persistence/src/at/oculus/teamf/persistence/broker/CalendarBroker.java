/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.entities.CalendarEntity;
import at.oculus.teamf.persistence.facade.Facade;

import java.util.Collection;

/**
 * Created by Norskan on 08.04.2015.
 */
public class CalendarBroker extends EntityBroker<Calendar, CalendarEntity> implements ICollectionReload {

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
	protected CalendarEntity domainToPersitent(Calendar entity) {
		//Todo: reverse
		return null;
	}

	@Override
	public void reload(ISession session, Object entity, Class clazz) {
		Facade facade = Facade.getInstance();

		//Todo:instance of

		((Calendar)entity).setEvents((Collection<CalendarEvent>)(Collection<?>)facade.getAll(CalendarEvent.class));

	}
}
