/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.CreateDiagnosisController;
import at.oculus.teamf.application.facade.ReceivePatientController;
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IPatient;

import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class CreateDiagnosisControllerTest {

    @org.junit.Test
    public void testCreateDiagnosis() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        LinkedList<IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("duck");

        IPatient iPatient = patients.getFirst();
        IDoctor iDoctor = iPatient.getIDoctor();

        ReceivePatientController receivePatientController = new ReceivePatientController();

        IExaminationProtocol iExaminationProtocol = receivePatientController.createNewExaminationProtocol(new Date(), new Date(), "description", iPatient, iDoctor, null);

        CreateDiagnosisController createDiagnosisController =  CreateDiagnosisController.CreateController(iExaminationProtocol);
        createDiagnosisController.createDiagnosis("title", "description", iDoctor);

        assert (iExaminationProtocol.getDiagnosis().getTitle().equals("title"));
        assert (iExaminationProtocol.getDiagnosis().getDescription().equals("description"));
        assert (iExaminationProtocol.getDiagnosis().getDoctor().equals(iDoctor));
    }
}