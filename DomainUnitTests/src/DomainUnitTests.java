/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;

import static junit.framework.Assert.assertTrue;

/**
 *
 * Created by Fabian on 12.04.2015.
 *
 */

public class DomainUnitTests implements ILogger {

    @org.junit.Test
    public void testGetDoctorPatientQueue() throws Exception {
        Doctor doc = Facade.getInstance().getById(Doctor.class, 1);
        log.debug("Queue bound to doctor: '" + doc.getFirstName() + " " + doc.getLastName() + "' / UserID: " + doc.getId());

        PatientQueue pqDoctor = new PatientQueue(doc);
        Collection<QueueEntry> entriesDoctor = pqDoctor.getEntries();

        assertTrue(entriesDoctor != null);
        assertTrue(entriesDoctor.size() > 0);
    }

    @org.junit.Test
    public void testGetOrthoptistPatientQueue() throws Exception {
        Orthoptist ortho = Facade.getInstance().getById(Orthoptist.class, 1);
        log.debug("Queue bound to orthoptist: '" + ortho.getFirstName() + " " + ortho.getLastName() + "' / UserID: " + ortho.getId());

        PatientQueue pqOrthoptist = new PatientQueue(ortho);
        Collection<QueueEntry> entriesOrthoptist = pqOrthoptist.getEntries();

        assertTrue(entriesOrthoptist != null);
        assertTrue(entriesOrthoptist.size() > 0);
    }
}
