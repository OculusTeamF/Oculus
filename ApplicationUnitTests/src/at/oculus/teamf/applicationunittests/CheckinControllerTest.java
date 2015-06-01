/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.controller.CheckinController;
import at.oculus.teamf.application.controller.ReceivePatientController;
import at.oculus.teamf.application.controller.SearchPatientController;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;
import org.junit.After;
import org.junit.Before;

import java.util.LinkedList;

public class CheckinControllerTest implements ILogger {

    private LinkedList<IPatient> patients;
    private IPatient iPatient;
    private IDoctor iDoctor;
    private IPatientQueue iQueue;
    private int size;

    @Before
    public void setUp() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        patients = (LinkedList<IPatient>) searchPatientController.searchPatients("duck");

        iPatient = patients.getFirst();
        iDoctor = iPatient.getIDoctor();
        iQueue = iDoctor.getQueue();
        System.out.println(iPatient.getFirstName() + " " + iPatient.getLastName());
        System.out.println(iDoctor.getFirstName() + " " + iPatient.getLastName());
        System.out.println(iQueue.getEntries());

        System.out.println(iQueue.getEntries().size());

        CheckinController checkinController = new CheckinController();
        checkinController.insertPatientIntoQueue(iPatient, iDoctor);

        size = iQueue.getEntries().size();
        assert(iQueue != null);
        assert(iQueue.getEntries().size() == size);
        System.out.println(iQueue.getEntries().size());
    }

    @After
    public void tearDown() throws Exception{
        ReceivePatientController receivePatientController = new ReceivePatientController();
        receivePatientController.removePatientFromQueue(iPatient, iQueue);
        size = size - 1;
        System.out.println(iQueue.getEntries().size());
        System.out.println(size);
        System.out.println(iQueue.getEntries());
        assert(iQueue.getEntries().size() == size);
    }

    @org.junit.Test
    public void testInsertPatientIntoQueue() throws Exception {

    }
}