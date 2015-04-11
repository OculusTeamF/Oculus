/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.InvalidReloadParameterException;
import at.oculus.teamf.persistence.exceptions.NotAbleToLoadClassException;
import at.oculus.teamf.persistence.exceptions.ReloadInterfaceNotImplementedException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class DoctorBrokerTest extends BrokerTest {
	private Doctor _doctor;

	@Before
	public void setUp() throws Exception {
		// TODO implement
	}

	@Test
    @Override
    public void testGetById() {
        Facade facade = Facade.getInstance();
        Doctor doctor = null;
        try {
            doctor = facade.getById(Doctor.class, 1);
        } catch (FacadeException e) {
            assertTrue(false);
            e.printStackTrace();
        }
        assertTrue(doctor != null);
    }

    @Test
    @Override
    public void testGetAll() {
	    Collection<Doctor> doctors = null;

	    try {
		    doctors = Facade.getInstance().getAll(Doctor.class);
	    } catch (FacadeException e) {
		    e.printStackTrace();
	    }

	    assertTrue(doctors != null);
	    assertTrue(doctors.size() > 1);
    }

    @Test
    @Override
    public void testSave() {
	    //not needed
    }

	@Override
	public void testDelete() {
		//not needed
	}

	@Test
    public void testReload() {
        Facade facade = Facade.getInstance();
        Doctor doctor = null;
        try {
            doctor = (Doctor)facade.getById(Doctor.class, 1);
        } catch (FacadeException e) {
            assertTrue(false);
            e.printStackTrace();
        }
        assertTrue(doctor != null);

        try {
            facade.reloadCollection(doctor, Patient.class);
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

        assertTrue(doctor.getPatients() != null);
        assertTrue(doctor.getPatients().size() > 0);
    }
}