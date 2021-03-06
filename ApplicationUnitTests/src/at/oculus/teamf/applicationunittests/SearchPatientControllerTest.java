/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.controller.SearchPatientController;
import at.oculus.teamf.domain.entity.patient.IPatient;

import java.util.LinkedList;

public class SearchPatientControllerTest {

    SearchPatientController searchPatientController = new SearchPatientController();

    @org.junit.Test
    public void testSearchPatients() throws Exception {
        LinkedList<IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("son");
        assert (patients.size() > 0);
    }

}