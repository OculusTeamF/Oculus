/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.facade.PrescriptionController;
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.persistence.Facade;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class PrescriptionControllerTest {

    private SearchPatientController searchPatientController = new SearchPatientController();
    PrescriptionController prescriptionController;
    IPatient iPatient;
    IPrescription iPrescription;

    @org.junit.Before
    public void setUp() throws Exception {
        LinkedList<IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Meyer");
        iPatient = patients.getFirst();
        prescriptionController = PrescriptionController.createController(iPatient);

    }

    @org.junit.After
    public void tearDown() throws Exception {
        Facade facade = Facade.getInstance();
//        facade.delete(iPrescription);
    }

    @org.junit.Test
    public void testCreatePrescriptionEntry() throws Exception {
        LinkedList<IMedicine> medicines = (LinkedList<IMedicine>) prescriptionController.getAllPrescribedMedicines();
        try {
            iPrescription = prescriptionController.createPrescriptionEntry(medicines);
        } catch (CouldNotAddPrescriptionEntryException e) {
            e.printStackTrace();
        }

        assert(iPrescription.getPrescriptionEntries().size() == medicines.size());
    }

    @org.junit.Test
    public void testGetAllPrescribedMedicines() throws Exception {
        LinkedList<IMedicine> medicines = (LinkedList<IMedicine>) prescriptionController.getAllPrescribedMedicines();

        try {
            assert(iPatient.getMedicine().equals(medicines));
        } catch (CouldNotGetMedicineException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testPrintPrescription() throws Exception {
        //TODO implement testPrintPrescription in PrescriptionControllerTest
    }

}