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
import at.oculus.teamf.application.controller.VisualAidController;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class VisualAidControllerTest {

    private SearchPatientController searchPatientController = new SearchPatientController();
    private VisualAidController visualAidController;
    private LinkedList<IPatient> patients;
    private IPatient iPatient;
    private IDiagnosis iDiagnosis;
    private IVisualAid visualAid;

    @org.junit.Before
    public void setUp() throws Exception {
        patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Meyer");
        iPatient = patients.getFirst();
        LinkedList<IDiagnosis> diagnoses = (LinkedList<IDiagnosis>) iPatient.getDiagnoses();
        iDiagnosis = diagnoses.getFirst();
        visualAidController = VisualAidController.createController(iDiagnosis);

    }

    @org.junit.After
    public void tearDown() throws Exception {
        IFacade facade = Facade.getInstance();
        facade.delete(visualAid);
    }

    @org.junit.Test
    public void createVisualAidPrescription(){
        visualAid = null;
        try {
            visualAid = visualAidController.createVisualAidPrescription("this is a description", 2.5f, 2.5f);
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NotInitiatedExceptions notInitiatedExceptions) {
            notInitiatedExceptions.printStackTrace();
        }

        ArrayList<IVisualAid> visualAids = null;
        try {
            visualAids = (ArrayList<IVisualAid>) iDiagnosis.getVisualAid();
        } catch (CouldNotGetVisualAidException e) {
            e.printStackTrace();
        }

        assertTrue((visualAids.contains(visualAid)));
    }
}