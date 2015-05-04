/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.*;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IPatient;

import java.util.Collection;
import java.util.LinkedList;

public class CreateDiagnosisControllerTest {

    @org.junit.Test
    public void testCreateDiagnosis() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        LinkedList<IPatient> patients = new LinkedList<>();
        patients.addAll(searchPatientController.searchPatients("duck"));

        ReceivePatientController receivePatientController = new ReceivePatientController();
        LinkedList<IExaminationProtocol> examinationProtocols = (LinkedList<IExaminationProtocol>) receivePatientController.getAllExaminationProtocols(patients.getFirst());

        CreateDiagnosisController createDiagnosisController = new CreateDiagnosisController(examinationProtocols.getFirst());
        StartupController startupController = new StartupController();
        LinkedList<IDoctor> docs = new LinkedList<>();
        docs.addAll(startupController.getAllDoctors());
        IDiagnosis diagnosis = createDiagnosisController.createDiagnosis("test123", "description123", docs.getFirst());
        assert (diagnosis.getTitle().equals("test123"));
    }
}