/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.factory.QueueFactory;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.entity.QueueEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Simon Angerer on 30.04.2015.
 */
public class QueueFactoryTest {

    private Doctor _doctor;
    private PatientQueue _queue;
    private Patient _patient1;
    private Patient _patient2;
    private Patient _patient3;

    private Patient createPatien(String name) {
        Patient patient = new Patient();
        patient.setFirstName(name);
        patient.setLastName(name);
        patient.setSocialInsuranceNr(name);
        try {
            Facade.getInstance().save(patient);
        } catch (BadConnectionException | NoBrokerMappedException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        return patient;
    }

    @Before
    public void setUp() {
        try {
            _doctor = (Doctor) Facade.getInstance().getAll(Doctor.class).toArray()[0];
            _queue = QueueFactory.getInstance().getUserQueue(_doctor);
        } catch (BadConnectionException | NoBrokerMappedException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        _patient1 = createPatien("Test1");
        _patient2 = createPatien("Test2");
        _patient3 = createPatien("Test3");

        try {
            _queue.addPatient(_patient1, new Timestamp(new Date().getTime()));
            _queue.addPatient(_patient2, new Timestamp(new Date().getTime()));
            _queue.addPatient(_patient3, new Timestamp(new Date().getTime()));
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }


    private void deletePatient(String name) {
        try {
            Patient patient = (Patient) Facade.getInstance().search(Patient.class, name).toArray()[0];
            Facade.getInstance().delete(patient);
        } catch (SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | NoBrokerMappedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @After
    public void tearDown() {

        try {
            _queue.removePatient(_patient1);
            _queue.removePatient(_patient2);
            _queue.removePatient(_patient3);
        } catch (NoBrokerMappedException | BadConnectionException | InvalidSearchParameterException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        deletePatient("Test1");
        deletePatient("Test2");
        deletePatient("Test3");
    }

    @Test
    public void testGetUserQueue() throws Exception {
        assertTrue(_queue.getEntries().size() >= 3);
    }
}