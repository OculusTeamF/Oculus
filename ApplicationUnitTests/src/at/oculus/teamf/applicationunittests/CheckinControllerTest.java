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
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.domain.entity.*;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class CheckinControllerTest {

    @org.junit.Test
    public void testInsertPatientIntoQueue() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        LinkedList <IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Duck");
        IPatient iPatient = patients.getFirst();
        IUser iUser = iPatient.getDoctor();

        CheckinController checkinController = new CheckinController();
        checkinController.insertPatientIntoQueue(iPatient, iUser);

        IPatientQueue iQueue;

        if (iUser instanceof IDoctor){
            IDoctor iDoctor = (IDoctor) iUser;
            iQueue = iDoctor.getQueue();
        } else {
            IOrthoptist iOrthoptist  = (IOrthoptist) iUser;
            iQueue = iOrthoptist.getQueue();
        }
        assert (iQueue != null);
    }
}