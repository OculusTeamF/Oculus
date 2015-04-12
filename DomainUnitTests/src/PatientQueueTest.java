/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


public class PatientQueueTest implements ILogger{

    @org.junit.Test
    public void testUpdateQueueEntry() throws Exception {
        PatientQueue patQueue = new PatientQueue();
        patQueue.updateQueueEntry();
    }

    @org.junit.Test
    public void testSetUserID() throws Exception {

    }

    @org.junit.Test
    public void testGetEntries() throws Exception {
    }

    @org.junit.Test
    public void testSetQueueEntry() throws Exception {

    }

    @org.junit.Test
    public void testAddPatient() throws Exception {
        Timestamp tstamp = new Timestamp(new Date().getTime());
        Doctor doc = Facade.getInstance().getById(Doctor.class, 1);
        Patient pat = Facade.getInstance().getById(Patient.class, 7);
        Orthoptist ortho = null;
        PatientQueue patQueue = new PatientQueue();

        log.debug("Add patient to queue for doctor: '" + doc.getFirstName() + " " + doc.getLastName() + "' / Patient: '" + pat.getFirstName() + " " + pat.getLastName() + "'");

        assertNotNull(pat);
        assertNotNull(doc);
        assertNull(ortho);
        assertNotNull(tstamp);

        patQueue.addPatient(pat, doc, ortho, tstamp);
    }

    @org.junit.Test
    public void testRemovePatient() throws Exception {
        //Patient pat = Facade.getInstance().getById(Patient.class, 7);
        //PatientQueue patQueue = new PatientQueue();

        //log.debug("Remove patient from queue for doctor: '" + pat.getFirstName() + " " + pat.getLastName() + "' / patID: " + pat.getId());
        //log.debug("testcase disabled");
        //TODO writing tableentry tests
        //patQueue.removePatient(pat);
    }

    @org.junit.Test
    public void testGetNext() throws Exception {
        Doctor doc = Facade.getInstance().getById(Doctor.class, 1);
        PatientQueue pqDoctor = new PatientQueue(doc);

        log.debug("GETNEXT PatientQueue bound to doctor: '" + doc.getFirstName() + " " + doc.getLastName() + "' / QueueEntries: " + pqDoctor.getEntries().size());

        QueueEntry nextQueueEntry = null;
        nextQueueEntry = pqDoctor.getNext();

        log.debug("GETNEXT queue entry for doctor: '" + doc.getLastName() + "' / next patient in queue: '" + nextQueueEntry.getPatient().getLastName() + "' id: " + nextQueueEntry.getPatient().getId());

        assertNotNull(nextQueueEntry);
    }
}