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
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.prescription.prescriptionentry.PrescriptionEntry;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

/**
 * PrescriptionEntryBrokerTest.java Created by oculus on 09.05.15.
 */
public class PrescriptionEntryBrokerTest extends BrokerTest {
	private Medicine _medicine;
	private IPrescription _prescription;
	private PrescriptionEntry _prescriptionEntry;

	@Override
	public void setUp() {
		try {
			_medicine = Facade.getInstance().getById(Medicine.class, 1);
			_prescription = Facade.getInstance().getById(IPrescription.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_prescriptionEntry = new PrescriptionEntry();
		_prescriptionEntry.setPrescription(_prescription);
		_prescriptionEntry.setMedicine(_medicine);

		try {
			assertTrue(Facade.getInstance().save(_prescriptionEntry));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_prescriptionEntry));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException | InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		PrescriptionEntry prescriptionEntry = null;
		try {
			prescriptionEntry = Facade.getInstance().getById(PrescriptionEntry.class, _prescriptionEntry.getId());
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(prescriptionEntry.equals(_prescriptionEntry));
	}

	@Override
	public void testGetAll() {
		Collection<PrescriptionEntry> prescriptionEntries = null;
		try {
			prescriptionEntries = Facade.getInstance().getAll(PrescriptionEntry.class);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(prescriptionEntries.size() > 0);
	}
}
