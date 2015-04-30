/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.Receptionist;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class ReceptionistBrokerTest extends BrokerTest {
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
        Receptionist receptionist = null;
        try {
            receptionist = facade.getById(Receptionist.class, 1);
        } catch (FacadeException e) {
            assertTrue(false);
            e.printStackTrace();
        } catch (BadSessionException e) {
            e.printStackTrace();
        }
        assertTrue(receptionist != null);
    }

    @Test
    @Override
    public void testGetAll() {
	    Collection<Receptionist> receptionists = null;

	    try {
		    receptionists = Facade.getInstance().getAll(Receptionist.class);
	    } catch (FacadeException e) {
            assertTrue(false);
            e.printStackTrace();
	    } catch (BadSessionException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(receptionists != null);
	    Assert.assertTrue(receptionists.size() > 0);
    }
}