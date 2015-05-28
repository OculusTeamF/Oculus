/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.ExaminationResult;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;

/**
 * ExaminationResultTest.java Created by oculus on 30.04.15.
 */
public class ExaminationResultTest extends BrokerTest {
	private ExaminationResult _examinationResult;

	@Override
	public void setUp() {
		User user = null;
		ExaminationProtocol examinationProtocol = null;
		try {
			user = Facade.getInstance().getById(Orthoptist.class, 1);
			examinationProtocol = Facade.getInstance().getById(ExaminationProtocol.class, 46);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		_examinationResult =
				new ExaminationResult(0, examinationProtocol, user, "Ergebnis der Untersuchung", new Date(),
				                      "Device 001-2015", null);
		try {
			assertTrue(Facade.getInstance().save(_examinationResult));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_examinationResult));
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException | InvalidSearchParameterException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		ExaminationResult examinationResult = null;
		try {
			examinationResult = Facade.getInstance().getById(ExaminationResult.class,_examinationResult.getId());
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(_examinationResult.getId() == examinationResult.getId());
		assertTrue(_examinationResult.getExaminationProtocol().equals(examinationResult.getExaminationProtocol()));
		assertTrue(_examinationResult.getUser().equals(examinationResult.getUser()));
        assertTrue(_examinationResult.getResult().equals(examinationResult.getResult()));
		assertTrue(_examinationResult.getDevice().equals(examinationResult.getDevice()));
		assertTrue(_examinationResult.getDeviceData() == examinationResult.getDeviceData());
	}

	@Override
	public void testGetAll() {
        /*Collection<ExaminationResult> examinationResults = null;
		try {
            examinationResults = Facade.getInstance().getAll(ExaminationResult.class);
		} catch (BadConnectionException e) {
			e.printStackTrace();
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
		} catch (BadSessionException e) {
            e.printStackTrace();
        }
        assertTrue(examinationResults.size()>0);*/
    }

    @Test
    public void testSearch(){
        Collection<ExaminationResult> examinationResults = null;

        try {
            examinationResults = Facade.getInstance().search(ExaminationResult.class, "1");
        } catch (SearchInterfaceNotImplementedException | DatabaseOperationException | InvalidSearchParameterException | NoBrokerMappedException | BadConnectionException e) {
			e.printStackTrace();
			assertTrue(false);
        }

		assertTrue(examinationResults.size()==21);
    }
}
