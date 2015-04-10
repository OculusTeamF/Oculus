/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Norskan on 10.04.2015.
 */
public class PatientBrokerTest extends BrokerTest {


    @Override
    public void testGetById() {
        Patient patient = null;
        try {
            patient = Facade.getInstance().getById(Patient.class, 1);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patient != null);
        assertTrue(patient.getFirstName().equals("Donald"));
    }

    @Override
    public void testGetAll() {
        Collection<Patient> patients = null;
        try {
            patients = Facade.getInstance().getAll(Patient.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patients != null);
        assertTrue(patients.size() == 72);

    }

    @Override
    public void testSave() {
        //not needed currently
    }

    @Override
    public void testReload() {
        Patient patient = null;
        try {
            patient = Facade.getInstance().getById(Patient.class, 1);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(patient.getCalendarEvents() != null);
        assertTrue(patient.getCalendarEvents().size() == 1);


    }
}