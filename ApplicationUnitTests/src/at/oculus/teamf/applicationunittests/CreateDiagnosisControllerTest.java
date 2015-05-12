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
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import org.junit.After;
import org.junit.Before;

import java.util.Date;
import java.util.LinkedList;

public class CreateDiagnosisControllerTest {

    private SearchPatientController searchPatientController = new SearchPatientController();
    private ReceivePatientController receivePatientController = new ReceivePatientController();

    private LinkedList<IPatient> patients;
    private IPatient iPatient;
    private IDoctor iDoctor;
    private IExaminationProtocol iExaminationProtocol;
    private IDiagnosis iDiagnosis;

    @Before
    public void setUp() throws Exception {
        patients = (LinkedList<IPatient>) searchPatientController.searchPatients("duck");
        iPatient = patients.getLast();
        iDoctor = iPatient.getIDoctor();
        iExaminationProtocol = receivePatientController.createNewExaminationProtocol(new Date(), new Date(), "description", iPatient, iDoctor, null);
    }

    @After
    public void tearDown() throws Exception{
        IFacade facade = Facade.getInstance();
        facade.delete(iDiagnosis);
        facade.delete(iExaminationProtocol);
    }

    @org.junit.Test
    public void testCreateDiagnosis() throws Exception {
        CreateDiagnosisController createDiagnosisController =  CreateDiagnosisController.CreateController(iExaminationProtocol);
        iDiagnosis = createDiagnosisController.createDiagnosis("title", "description", iDoctor);

        assert (iExaminationProtocol.getDiagnosis().getTitle().equals("title"));
        assert (iExaminationProtocol.getDiagnosis().getDescription().equals("description"));
        assert (iExaminationProtocol.getDiagnosis().getDoctor().equals(iDoctor));
        assert (iPatient.getExaminationProtocol().contains(iExaminationProtocol));
    }

}