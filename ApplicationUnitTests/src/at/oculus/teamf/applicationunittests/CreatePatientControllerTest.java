/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.CreatePatientController;
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.application.facade.additional.RemovePatientController;
import at.oculus.teamf.domain.entity.IDoctor;
import at.oculus.teamf.domain.entity.IPatient;
import at.oculus.teamf.domain.entity.Patient;

import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class CreatePatientControllerTest {

    @org.junit.Test
    public void testCreatePatient() throws Exception {
        StartupController startupController = new StartupController();
        LinkedList<IDoctor> iDoctors = (LinkedList<IDoctor>) startupController.getAllDoctors();
        CreatePatientController createPatientController = new CreatePatientController();
        createPatientController.createPatient("male", "Gruber", "Hans", "1234567890", new Date(), "Schlumpfweg", "6830", "Muntlix", "0987654321", "hans.gruber@email.com", iDoctors.getFirst(), "at");

        SearchPatientController searchPatientController = new SearchPatientController();
        LinkedList <IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("gruber");
        Patient temppatient = new Patient();
        for (IPatient patient : patients){
            Patient patient1 = (Patient) patient;
            if (patient1.getFirstName().equals("Hans")){
                temppatient = patient1;
            }
        }
        assert (temppatient.getFirstName().equals("Hans"));
        assert (temppatient.getCity().equals("Muntlix"));

        RemovePatientController removePatientController = new RemovePatientController();
        removePatientController.removePatientFromDatabase(temppatient);
    }
}