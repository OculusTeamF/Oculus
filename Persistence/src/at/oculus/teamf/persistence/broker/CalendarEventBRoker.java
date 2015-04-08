/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.entities.CalendareventEntity;

/**
 * Created by Norskan on 08.04.2015.
 */
public class CalendarEventBroker extends EntityBroker<CalendarEvent, CalendareventEntity> {
	public CalendarEventBroker() {
		super(CalendarEvent.class, CalendareventEntity.class);
	}

	@Override
	protected CalendarEvent persitentToDomain(CalendareventEntity entity) {
		//Todo:
		return null;
	}

	@Override
	protected CalendareventEntity domainToPersitent(CalendarEvent entity) {
		//Todo: reverse
		return null;
	}
}
