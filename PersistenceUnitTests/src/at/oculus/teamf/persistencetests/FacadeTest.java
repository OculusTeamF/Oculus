package at.oculus.teamf.persistencetests;/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */
import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ClassNotMappedException;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.facade.Facade;
import junit.framework.TestCase;

import java.util.LinkedList;

/**
 * at.oculus.teamf.persistencetests.FacadeTest.java Created by oculus on 06.04.15.
 */
public class FacadeTest extends TestCase{
	public void testCalendar(){
		LinkedList<Class> clazzes = new LinkedList<Class>();
		clazzes.add(CalendarEntity.class);
		clazzes.add(CalendareventEntity.class);
		Facade facade = Facade.getInstance(clazzes);
		Calendar openCalendar = null;
		try {
			openCalendar = (Calendar) facade.getObjectById(Calendar.class,1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		assertTrue(openCalendar!=null);
	}
}
