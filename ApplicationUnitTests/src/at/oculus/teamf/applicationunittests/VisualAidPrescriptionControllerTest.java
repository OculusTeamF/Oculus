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
import at.oculus.teamf.application.facade.VisualAidPrescriptionController;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class VisualAidPrescriptionControllerTest {

    private SearchPatientController searchPatientController = new SearchPatientController();
    private VisualAidPrescriptionController visualAidPrescriptionController;
    private LinkedList<IPatient> patients;
    private IPatient iPatient;
    private IDiagnosis iDiagnosis;

    @org.junit.Before
    public void setUp() throws Exception {
        patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Meyer");
        iPatient = patients.getFirst();
        LinkedList<IDiagnosis> diagnoses = (LinkedList<IDiagnosis>) iPatient.getDiagnoses();
        iDiagnosis = diagnoses.getFirst();
        visualAidPrescriptionController = VisualAidPrescriptionController.createController(iDiagnosis);

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void createVisualAidPrescription(){
        IVisualAid visualAid = null;
        try {
            visualAid = visualAidPrescriptionController.createVisualAidPrescription("this is a description");
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        }

        //TODO

    }
}