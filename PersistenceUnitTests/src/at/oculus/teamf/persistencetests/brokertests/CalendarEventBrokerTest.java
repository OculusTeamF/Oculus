/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Norskan on 10.04.2015.
 */
public class CalendarEventBrokerTest extends BrokerTest {

	@Override
	public void setUp() {

	}

	@Override
	public void tearDown() {

	}

	@Override
	public void testGetById() {
		CalendarEvent calendarEvent = null;

		try {
			calendarEvent = Facade.getInstance().getById(CalendarEvent.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(calendarEvent != null);
	}

	@Override
	public void testGetAll() {
		Collection<CalendarEvent> calendarEvents = null;

		try {
			calendarEvents = Facade.getInstance().getAll(CalendarEvent.class);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}

		assertTrue(calendarEvents != null);
		assertTrue(calendarEvents.size() == 49);
	}
}