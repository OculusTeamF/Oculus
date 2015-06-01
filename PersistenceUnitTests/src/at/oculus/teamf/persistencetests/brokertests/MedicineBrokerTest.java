/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

/**
 * MedicineBrokerTest.java Created by oculus on 09.05.15.
 */
public class MedicineBrokerTest extends BrokerTest {

	@Override
	public void setUp() {
		// no insert
	}

	@Override
	public void tearDown() {
		// no delete
	}

	@Override
	public void testGetById() {
		Medicine medicine = null;
		try {
			medicine = Facade.getInstance().getById(Medicine.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(medicine.getName().equals("Sodium Fluoride"));
		assertTrue(medicine.getDose().equals("2x1"));
	}

	@Override
	public void testGetAll() {
		Collection<Medicine> medicineCollection = null;
		try {
			medicineCollection = Facade.getInstance().getAll(Medicine.class);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(medicineCollection.size() > 0);
	}
}
