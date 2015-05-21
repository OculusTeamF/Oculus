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
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetPrescriptionException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
import at.oculus.teamf.technical.printing.IPrinter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class PrescriptionControllerTest implements IPrinter {

    PrescriptionController prescriptionController;
    IPatient iPatient;
    IPrescription iPrescription;
	private SearchPatientController searchPatientController = new SearchPatientController();

    @org.junit.Before
    public void setUp() throws Exception {
        LinkedList<IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Meyer");
        iPatient = patients.getFirst();
        prescriptionController = PrescriptionController.createController(iPatient);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        /*IFacade facade = Facade.getInstance();
        facade.delete(iPrescription);*/
    }

    @org.junit.Test
    public void testCreatePrescriptionEntry() throws Exception, CouldNotGetMedicineException {
        LinkedList<IMedicine> medicines = (LinkedList<IMedicine>) prescriptionController.getAllPrescribedMedicines();
        try {
            iPrescription = prescriptionController.createPrescriptionEntry(medicines);
        } catch (CouldNotAddPrescriptionEntryException e) {
            e.printStackTrace();
        }
        Collection<IPrescriptionEntry> entries = iPrescription.getPrescriptionEntries();
        System.out.println(entries.size());
        System.out.println(medicines.size());
        assert(iPrescription.getPrescriptionEntries().size() == medicines.size());
    }

    @org.junit.Test
    public void testGetAllPrescribedMedicines() throws Exception, CouldNotGetMedicineException {
        LinkedList<IMedicine> medicines = (LinkedList<IMedicine>) prescriptionController.getAllPrescribedMedicines();

        try {
            assert(iPatient.getMedicine().equals(medicines));
        } catch (CouldNotGetMedicineException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testPrintPrescription() throws Exception {
        StartupController start = new StartupController();
        try {
            LinkedList<IPrescription> iPrescriptions = new LinkedList<>();
            iPrescriptions.addAll(iPatient.getPrescriptions());
            for (IPrescriptionEntry entry : iPrescriptions.getFirst().getPrescriptionEntries()) {
                System.out.println(entry.getMedicine());
            }
            printer.printPrescription(iPrescriptions.getFirst(), iPatient.getIDoctor());
        } catch (CouldNotGetPrescriptionException e) {
            e.printStackTrace();
        } catch (NoPrescriptionToPrintException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testGetNotPrintedPrescriptions(){
        LinkedList<IPrescription> iPrescriptions = new LinkedList<>();
        Collection<IPrescription> notPrinted = new ArrayList<>();
        try {
            iPrescriptions.addAll(iPatient.getPrescriptions());
            //notPrinted = prescriptionController.getNotPrintedPrescriptions();
        } catch (CouldNotGetPrescriptionException e) {
            e.printStackTrace();
        }

        System.out.println(iPrescriptions.size());
        System.out.println(notPrinted.size());
    }

}