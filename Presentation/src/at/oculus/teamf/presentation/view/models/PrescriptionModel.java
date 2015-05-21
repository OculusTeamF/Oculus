/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.facade.PrescriptionController;
import at.oculus.teamf.application.facade.VisualAidController;
import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Karo on 11.05.2015.
 */
public class PrescriptionModel {

    private static PrescriptionModel _prescriptionModel = new PrescriptionModel();

    private PrescriptionController _prescriptionController;
    private VisualAidController _visualAidPrescriptionController;

    private IPatient _currPatient;


    public static PrescriptionModel getInstance() {
        if(_prescriptionModel == null) {
            _prescriptionModel = new PrescriptionModel();
        }

        return _prescriptionModel;
    }


    /**
     *
     * @param patient
     * @throws NotInitiatedExceptions
     */
    public void fetchPrescriptionController(IPatient patient) throws NotInitiatedExceptions, NoPatientException {

        _prescriptionController = PrescriptionController.createController(patient);

        _currPatient = patient;
    }

    /**
     * creates a new PrescriptionEntry
     * @param medicinList
     * @return
     * @throws NotInitiatedExceptions
     */

    public void addPrescriptionEntries(Collection<IMedicine> medicinList) throws NotInitiatedExceptions, CouldNotAddPrescriptionEntryException, DatabaseOperationException, BadConnectionException, NoBrokerMappedException {

        _prescriptionController.createPrescriptionEntry(medicinList);
    }

    public void printPrescription() throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException, COSVisitorException, IOException, NotInitiatedExceptions, NoPrescriptionToPrintException, CantGetPresciptionEntriesException {

        _prescriptionController.printPrescription();
    }

    public Collection<IMedicine> getPrescribedMedicin() {

        Collection<IMedicine> prescribedMedicins = null;

        try {
            prescribedMedicins = _prescriptionController.getAllPrescribedMedicines();
        } catch (CouldNotGetMedicineException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotGetMedicineEcxeption", "Please contact Support");
        }

        return prescribedMedicins;
    }

    public void addNewVisualAidPrescription(IDiagnosis diagnose) throws NoPatientException, NotInitiatedExceptions {

        _visualAidPrescriptionController = VisualAidController.createController(diagnose);

    }

    /**
     * creates a new VisualAid prescription entry
     * @param description
     * @param dioptersLeft
     * @param dioptersRight
     */
    public void addVisualAidPrescriptionEntries(String description, Float dioptersLeft, Float dioptersRight) throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException, NotInitiatedExceptions {

        _visualAidPrescriptionController.createVisualAidPrescription(description, dioptersLeft, dioptersRight);

    }
}
