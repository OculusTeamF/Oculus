/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.controller.PrescriptionController;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.controller.exceptions.NoPatientException;
import at.oculus.teamf.application.controller.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.controller.exceptions.RequirementsNotMetException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetPrescriptionException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import at.oculus.teamf.presentation.view.StatusBarController;
import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Fabian on 10.05.2015.
 */
public class PatientRecordModel {

    private static PatientRecordModel _patientmodel = new PatientRecordModel();
    private Model _model;

    public static PatientRecordModel getInstance() {
        if(_patientmodel == null) {
            _patientmodel = new PatientRecordModel();
        }

        return _patientmodel;
    }

    /**
     * saves a updated Patient object
     */
    public void savePatient(IPatient patient)throws RequirementsNotMetException, PatientCouldNotBeSavedException, BadConnectionException, CriticalDatabaseException, CriticalClassException{

        _model = Model.getInstance();
        _model.getCreatePatientController().saveIPatient(patient);
    }

    /**
     * creates & saves a new Patient object
     */
    public boolean saveNewPatient(String gender, String lastname, String firstname, String svn, Date bday, String street, String postalcode, String city, String phone, String email, IDoctor doctor, String countryIsoCode)
    {
        _model = Model.getInstance();
        try {
            _model.getCreatePatientController().createPatient(gender, lastname, firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
            return true;

        } catch (RequirementsNotMetException requirementsNotMetException) {
            requirementsNotMetException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("RequirementsNotMetException", "Please contact support");
        } catch (PatientCouldNotBeSavedException patientCouldNotBeSavedException) {
            patientCouldNotBeSavedException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("PatientCouldNotBeSavedException", "Please contact support");
        } catch (BadConnectionException badConnectionException) {
            badConnectionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Please contact support");
        } catch (CriticalDatabaseException criticalDatabaseException) {
            criticalDatabaseException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CriticalDatabaseException", "Please contact support");
        } catch (CriticalClassException criticalClassException) {
            criticalClassException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CriticalClassException", "Please contact support");
        }
        return false;
    }


    /**
     * opens a popupwindow with the not printed prescriptions
     * @param patient
     */
    public void openPrescriptionsToPrint(final PrescriptionController prescriptionController, IPatient patient) {

        IPatient currPatient = patient;

        Stage stage = new Stage();
        stage.setTitle("Open Prescriptions");

        Group root = new Group();
        Scene scene = new Scene(root, 400, 300, Color.WHITE);

        GridPane pane = new GridPane();
        for(int i = 0; i < 3; i++){
            ColumnConstraints column = new ColumnConstraints(150);
            pane.getColumnConstraints().add(column);
        }
        pane.setPadding(new Insets(5));
        pane.setHgap(10);
        pane.setVgap(10);

        //Content of Popup
        Label label = new Label("Not printed Prescriptions:");
        final ListView<IPrescription> openPrescriptions = new ListView<>();
        Button printPrescriptionButton = new Button("Print");
        Button deletePrescriptionButton = new Button("Delete");

        // setup buttons
        Image imagePrintIcon = new Image(getClass().getResourceAsStream("/res/icon_print.png"));
        Image imageDelete = new Image(getClass().getResourceAsStream("/res/icon_delete.png"));
        printPrescriptionButton.setGraphic(new ImageView(imagePrintIcon));
        deletePrescriptionButton.setGraphic(new ImageView(imageDelete));
        deletePrescriptionButton.setDisable(true);
        printPrescriptionButton.setMinHeight(31);
        printPrescriptionButton.setMinWidth(150);
        deletePrescriptionButton.setMinHeight(31);
        deletePrescriptionButton.setMinWidth(150);

       //Button ActionHandler
        printPrescriptionButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                IPrescription prescription = openPrescriptions.getSelectionModel().getSelectedItem();

                try {
                    if(prescription != null){
                        prescriptionController.printPrescription(prescription);
                        StatusBarController.getInstance().setText("Print Prescription...");
                    }else{
                        DialogBoxController.getInstance().showInformationDialog("Cannot print Prescription", "Please choose a Prescription");
                    }
                } catch (COSVisitorException cosVisitorException) {
                    cosVisitorException.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("COSVisitorException", "Please contact support");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("IOException", "Please contact support");
                } catch (CantGetPresciptionEntriesException cantGetPresciptionEntriesException) {
                    cantGetPresciptionEntriesException.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("CantGetPresciptionEntriesException", "Please contact support");
                } catch (NoPrescriptionToPrintException noPrescriptionToPrintException) {
                    noPrescriptionToPrintException.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("NoPrescriptionToPrintException", "Please contact support");
                }
            }
        });

        deletePrescriptionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IPrescription prescription = openPrescriptions.getSelectionModel().getSelectedItem();

                //TODO: _prescriptionController.deletePrescription();
            }
        });

        try {
           
            prescriptionController.getNotPrintedPrescriptions(currPatient);
        } catch (CouldNotGetPrescriptionException couldNotGetPrescriptionException) {
            couldNotGetPrescriptionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotGetPrescriptionException", "Please contact support");
        }


        pane.add(label, 0, 1, 2, 1);
        pane.add(openPrescriptions, 0, 2, 3, 1);
        pane.add(printPrescriptionButton, 0, 3);
        pane.add(deletePrescriptionButton, 2, 3);
        pane.setHgap(10);
        pane.setVgap(5);

        stage.setScene(new Scene(pane));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/res/32x32.png"));
        stage.setResizable(false);
        stage.show();

        try {
            try {
                PrescriptionController _prescriptionController = PrescriptionController.createController(currPatient);
                ObservableList<IPrescription> prescriptionList = FXCollections.observableList((List) _prescriptionController.getNotPrintedPrescriptions(currPatient));
                openPrescriptions.setItems(prescriptionList);

            } catch (NoPatientException noPatientException) {
                noPatientException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NoPatientException", "Please contact support");
            } catch (NotInitiatedExceptions notInitiatedExceptions) {
                notInitiatedExceptions.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact support");
            }
        } catch (CouldNotGetPrescriptionException couldNotGetPrescriptionException) {
            couldNotGetPrescriptionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotGetPrescriptionException", "Please contact support");
        }
    }
}
