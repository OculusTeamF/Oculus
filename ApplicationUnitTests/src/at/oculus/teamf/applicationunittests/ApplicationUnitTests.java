/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.domain.entity.Patient;

import java.util.Collection;

/**
 * Created by oculus on 09.04.15.
 */
public class ApplicationUnitTests {

    public static void main (String [] args){
        SearchPatientController controller =  new SearchPatientController();
        Collection<Patient> patients = controller.searchPatients(null, "Berry", null);
        System.out.println(patients.size());
        for (Patient patient : patients){
            System.out.println(patient.getLastName() + " " + patient.getFirstName());
        }
    }
}