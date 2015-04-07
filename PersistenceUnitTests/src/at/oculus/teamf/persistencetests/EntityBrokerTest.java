/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests;

import at.oculus.teamf.domain.entity.Weekday;
import at.oculus.teamf.persistence.broker.EntityBroker;
import at.oculus.teamf.persistence.broker.WeekdayBroker;
import at.oculus.teamf.persistence.facade.Facade;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class EntityBrokerTest {

	@org.junit.Test
	public void testGetEntity() throws Exception {
		Facade facade = Facade.getInstance();
		Collection<EntityBroker> brokers = new LinkedList<EntityBroker>();
		brokers.add(new WeekdayBroker());
		facade.init(brokers);

		Weekday wd = (Weekday) facade.getById(Weekday.class, 1);
		System.out.println(wd.getName());
		assertTrue(wd!=null);
	}
}