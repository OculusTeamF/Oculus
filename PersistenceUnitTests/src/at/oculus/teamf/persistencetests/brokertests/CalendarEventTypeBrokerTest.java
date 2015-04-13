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
import at.oculus.teamf.persistence.exceptions.FacadeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class CalendarEventTypeBrokerTest extends BrokerTest{
	private Integer _saveId;
	private EventType _eventType;

	@Override
	public void setUp() {
		_eventType = new RegularAppointment(0,"Daniels Spezialtermin",45,"blabla wow");
		try {
			System.out.println("before save id = " + _eventType.getId());
			assertTrue(Facade.getInstance().save(_eventType));
			_saveId = _eventType.getId();
			System.out.println("after save id = " + _eventType.getId());
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		try {
			System.out.println("before delete id = " + _eventType.getId());
			assertTrue(Facade.getInstance().delete(_eventType));
		} catch (FacadeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void testGetById() {
		EventType eventType = null;

		try {
			eventType = Facade.getInstance().getById(EventType.class, 1);
		} catch (FacadeException e) {
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
			e.printStackTrace();
		}

		assertTrue(eventTypes != null);
		assertTrue(eventTypes.size() > 0);
	}
}