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
import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitatedExceptions;
import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import javafx.collections.ObservableList;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Karo on 11.05.2015.
 */
public class PrescriptionModel {

    private static PrescriptionModel _prescriptionModel = new PrescriptionModel();
    private Model _model;

    private PrescriptionController _prescriptionController;

    public static PrescriptionModel getInstance() {
        if(_prescriptionModel == null) {
            _prescriptionModel = new PrescriptionModel();
        }

        return _prescriptionModel;
    }


    public void addNewPrescription(IPatient patient) throws NotInitatedExceptions {

        try {
            _prescriptionController = PrescriptionController.createController(patient);
        } catch (NoPatientException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoPatientException - Please contact support");
        }
    }

    public void addPrescriptionEntries(Collection<IMedicine> medicinList) throws NotInitatedExceptions {

        try {
            _prescriptionController.createPrescriptionEntry(medicinList);
        } catch (CouldNotAddPrescriptionEntryException e) {
            e.printStackTrace();
            //DialogBoxController.getInstance().showExceptionDialog(e, "CouldNotAddPrescriptionEntryException - Please contact support");
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "DatabaseOperationException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        }
    }

    public void printPrescription() {

        //TODO
        try {
            _prescriptionController.printPrescription();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "DatabaseOperationException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (COSVisitorException e) {
            e.printStackTrace();
        } catch (CantGetPresciptionEntriesException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotInitatedExceptions notInitatedExceptions) {
            notInitatedExceptions.printStackTrace();
        }
    }

    public Collection<IMedicine> getPrescribedMedicin() {

        Collection<IMedicine> prescribedMedicins = _prescriptionController.getAllPrescribedMedicines();

        return prescribedMedicins;
    }
}
