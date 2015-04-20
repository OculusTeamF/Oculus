/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.*;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadParameterException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Norskan on 08.04.2015.
 */
public class CalendarBrokerTest extends BrokerTest{
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
	public void testReload() {
		Facade facade = Facade.getInstance();
		Calendar cal = null;
		try {
			cal = (Calendar)facade.getById(Calendar.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(cal != null);

		try {
			facade.reloadCollection(cal, CalendarEvent.class);
		} catch (InvalidReloadParameterException invalidReloadParameterException) {
			invalidReloadParameterException.printStackTrace();
			assertTrue(false);
		} catch (ReloadInterfaceNotImplementedException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (NotAbleToLoadClassException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		assertTrue(cal.getEvents() != null);
		assertTrue(cal.getEvents().size() > 1);
	}
}