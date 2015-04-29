/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.Assert.assertTrue;


public class PatientQueueTest implements ILogger {
    private Timestamp _timestamp;
    private Patient _patient;
    private Doctor _doctor;
    private PatientQueue _patientQueue;
    private int _size;

    @Before
    public void setUp() {
        _timestamp = new Timestamp(new Date().getTime());
        try {
            for (Object p : Facade.getInstance().search(Patient.class, "5835897249")) {
                _patient = (Patient) p;
            }
            for (Object d : Facade.getInstance().getAll(Doctor.class)) {
                _doctor = (Doctor) d;
                break;
            }
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(_doctor != null);
        try {
            _patientQueue = _doctor.getQueue();
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        try {
            _size = _patientQueue.getEntries().size();
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        try {
            _patientQueue.addPatient(_patient, _timestamp);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @After
    public void tearDown() {
        try {
            assertTrue(_patientQueue.getEntries().size() == _size + 1);

            _patientQueue.removePatient(_patient);

            assertTrue(_patientQueue.getEntries().size() == _size);
        } catch (FacadeException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void getQueue() {
    }
}