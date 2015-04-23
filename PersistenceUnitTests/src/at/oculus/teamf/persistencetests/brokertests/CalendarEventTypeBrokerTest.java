/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.EventType;
import at.oculus.teamf.domain.entity.RegularAppointment;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

public class CalendarEventTypeBrokerTest extends BrokerTest{
	private EventType _eventType;

	@Override
	public void setUp() {
		_eventType = new RegularAppointment(0,"Daniels Spezialtermin",45,"blabla wow");
		try {
			assertTrue(Facade.getInstance().save(_eventType));
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_eventType));
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Override
	public void testGetById() {
		EventType eventType = null;

		try {
			eventType = Facade.getInstance().getById(EventType.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(eventType != null);
	}

	@Override
	public void testGetAll() {
		Collection<EventType> eventTypes = null;

		try {
			eventTypes = Facade.getInstance().getAll(EventType.class);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(eventTypes != null);
		assertTrue(eventTypes.size() > 0);
	}
}