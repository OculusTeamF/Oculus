/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class QueueBrokerTest extends BrokerTest {
	@Test
	@Override
	public void testGetById() {
		Facade facade = Facade.getInstance();
		QueueEntry queueEntry = null;
		try {
			queueEntry = facade.getById(QueueEntry.class, 1);
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(queueEntry != null);
	}

	@Test
	@Override
	public void testGetAll() {
		Collection<QueueEntry> queueEntries = null;

		try {
			queueEntries = Facade.getInstance().getAll(QueueEntry.class);
		} catch (FacadeException e) {
			e.printStackTrace();
		}

		assertTrue(queueEntries != null);
		assertTrue(queueEntries.size() > 1);
	}

	@Test
	@Override
	public void testSave() {
		//TODO implement
	}
}