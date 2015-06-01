/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.calendar.Calendar;
import at.oculus.teamf.domain.entity.calendar.CalendarEvent;
import at.oculus.teamf.domain.entity.calendar.CalendarWorkingHours;
import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.calendar.ICalendarEvent;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;

public class CalendarBrokerTest extends BrokerTest {
	@Override
	public void setUp() {

	}

	@Override
	public void tearDown() {

	}

	@Test
	@Override
	public void testGetById() {
		Facade facade = Facade.getInstance();
		Calendar cal = null;
		try {
			cal = facade.getById(Calendar.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(cal != null);
	}

	@Test
	@Override
	public void testGetAll() {
		//not needed
	}

	@Test
	public void testReloadEvents() {
		Facade facade = Facade.getInstance();
		Calendar cal = null;
		try {
			cal = facade.getById(Calendar.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(cal != null);

		try {
			facade.reloadCollection(cal, CalendarEvent.class);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		try {
			assertTrue(cal.getEvents() != null);
			assertTrue(cal.getEvents().size() > 1);
		} catch (InvalidReloadClassException | ReloadInterfaceNotImplementedException | BadConnectionException | NoBrokerMappedException e) {
			assertTrue(false);
			e.printStackTrace();
		} catch (DatabaseOperationException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	public void testReloadWorkingHours() {
		Facade facade = Facade.getInstance();
		Calendar cal = null;
		try {
			cal = facade.getById(Calendar.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(cal != null);

		try {
			facade.reloadCollection(cal, CalendarWorkingHours.class);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		try {
			assertTrue(cal.getWorkingHours() != null);
			assertTrue(cal.getWorkingHours().size() > 1);
		} catch (InvalidReloadClassException | ReloadInterfaceNotImplementedException | BadConnectionException | NoBrokerMappedException e) {
			assertTrue(false);
			e.printStackTrace();
		} catch (DatabaseOperationException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	@Test
	public void testNextAvailEvent() {
		Facade facade = Facade.getInstance();
		ICalendar cal = null;
		try {
			cal = facade.getById(Calendar.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(cal != null);

		Iterator<ICalendarEvent> iterator = null;
		try {
			iterator = cal.availableEventsIterator(null, 30);
		} catch (InvalidReloadClassException | ReloadInterfaceNotImplementedException | BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	    /*iterator.hasNext();
        for(int i = 0; i<10; i++) {
            System.out.println("next available event: " + iterator.next());
        }*/
	}

}