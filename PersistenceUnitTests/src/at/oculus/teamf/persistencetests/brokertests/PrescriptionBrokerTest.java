/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.DomainFactory;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.prescription.prescriptionentry.IPrescriptionEntry;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static junit.framework.Assert.assertTrue;

/**
 * PrescriptionBrokerTest.java Created by oculus on 09.05.15.
 */
public class PrescriptionBrokerTest extends BrokerTest {
	private IPrescription _prescription;
	private IPatient _patient;

	@Override
	public void setUp() {
		try {
			_patient = Facade.getInstance().getById(IPatient.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_prescription = (IPrescription) DomainFactory.create(IPrescription.class);
		_prescription.setPatient(_patient);
		_prescription.setIssueDate(new Date());
		_prescription.setLastPrint(new Date());

		try {
			assertTrue(Facade.getInstance().save(_prescription));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_prescription));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException | InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		IPrescription prescription = null;
		try {
			prescription = Facade.getInstance().getById(IPrescription.class, _prescription.getId());
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(prescription.equals(_prescription));
	}

	@Override
	public void testGetAll() {
		Collection<IPrescription> prescriptions = null;
		try {
			prescriptions = Facade.getInstance().getAll(IPrescription.class);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(prescriptions.size() > 0);
	}

	@Test
	public void testReload() {
		IPrescription prescription = null;
		Collection<IPrescriptionEntry> prescriptionEntries = null;
		try {
			prescription = Facade.getInstance().getById(IPrescription.class, 1);
			prescriptionEntries = prescription.getPrescriptionEntries();
		} catch (BadConnectionException | NoBrokerMappedException | CantGetPresciptionEntriesException | DatabaseOperationException e) {
			e.printStackTrace();
		}
		assertTrue(prescriptionEntries.size() > 0);
	}
}
