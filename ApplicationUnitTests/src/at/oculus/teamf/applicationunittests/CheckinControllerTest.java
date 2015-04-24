/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.CheckinController;
import at.oculus.teamf.application.facade.ReceivePatientController;
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;
import org.junit.After;
import org.junit.Before;

import java.util.LinkedList;

public class CheckinControllerTest implements ILogger {

    private LinkedList<IPatient> patients;
    private IPatient iPatient;
    private IDoctor iDoctor;
    private IPatientQueue iQueue;



    @Before
    public void setUp() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Hanson");

        iPatient = patients.getFirst();
        iDoctor = iPatient.getIDoctor();
        iQueue = iDoctor.getQueue();

        CheckinController checkinController = new CheckinController();
        checkinController.insertPatientIntoQueue(iPatient, iDoctor);

        assert(iQueue != null);
    }

    @After
    public void tearDown() throws Exception{
        ReceivePatientController receivePatientController = new ReceivePatientController();
        receivePatientController.removePatientFromQueue(iPatient, iQueue);
        assert(iQueue.getEntries().size() > 0);
    }

}