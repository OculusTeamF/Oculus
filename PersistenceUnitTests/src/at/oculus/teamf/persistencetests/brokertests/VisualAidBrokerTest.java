/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.VisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * VisualAidBrokerTest.java Created by oculus on 11.05.15.
 */
public class VisualAidBrokerTest extends BrokerTest {
	private VisualAid _visualAid;

	@Override
	public void setUp() {
		_visualAid = new VisualAid();
		_visualAid.setDescription("Beschreibung blabla");
		_visualAid.setIssueDate(new Date());
		_visualAid.setLastPrint(new Date());
		_visualAid.setDioptreLeft(3.5f);
		_visualAid.setDioptreRight(2.25f);

		Diagnosis diagnosis = null;
		try {
			diagnosis = Facade.getInstance().getById(Diagnosis.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_visualAid.setDiagnosis(diagnosis);

		try {
			assertTrue(Facade.getInstance().save(_visualAid));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void tearDown() {
		try {
			Facade.getInstance().delete(_visualAid);
		} catch (BadConnectionException | InvalidSearchParameterException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		VisualAid visualAid = null;
		try {
			visualAid = Facade.getInstance().getById(VisualAid.class, _visualAid.getId());
		} catch (NoBrokerMappedException | DatabaseOperationException | BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(visualAid.equals(_visualAid));
	}

	@Override
	public void testGetAll() {
		Collection<VisualAid> visualAids = null;
		try {
			visualAids = Facade.getInstance().getAll(VisualAid.class);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(visualAids.size() > 0);
	}
}
