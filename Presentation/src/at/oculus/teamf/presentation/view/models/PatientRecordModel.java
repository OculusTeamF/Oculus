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
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import com.sun.javafx.tk.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Fabian on 10.05.2015.
 */
public class PatientRecordModel {

    private static PatientRecordModel _patientmodel = new PatientRecordModel();
    private Model _model;
    private PrescriptionController _prescriptionController;

    public static PatientRecordModel getInstance() {
        if(_patientmodel == null) {
            _patientmodel = new PatientRecordModel();
        }

        return _patientmodel;
    }

    /**
     * saves a updated Patient object
     */
    public void savePatient(IPatient patient){
        _model = Model.getInstance();
        try {
            _model.getCreatePatientController().saveIPatient(patient);
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequrementsNotMetException - Please contact support");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }
    }

    /**
     * creates & saves a new Patient object
     */
    public boolean saveNewPatient(String gender, String lastname, String firstname, String svn, Date bday, String street, String postalcode, String city, String phone, String email, IDoctor doctor, String countryIsoCode){
        _model = Model.getInstance();
        try {
            _model.getCreatePatientController().createPatient(gender, lastname, firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
            return true;
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequirementsNotMetException - Please contact support.");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }
        return false;
    }


    /**
     * opens a popupwindow with the not printed prescriptions
     * @param patient
     */
    public void openPrescriptionsToPrint(IPatient patient) {

        IPatient currPatient = patient;

        Stage stage = new Stage();
        stage.setTitle("Open Prescriptions");

        final Popup popup = new Popup();
        popup.setX(300);
        popup.setY(200);

        GridPane pane = new GridPane();

        //Content of Popup
        Label label = new Label("Not printed Prescriptions:");
        final ListView<IPrescription> openPrescriptions = new ListView<>();
        Button printPrescriptionButton = new Button("Print Prescription");
        Button deletePrescriptionButton = new Button("Delete Prescription");

       //Button ActionHandler
        printPrescriptionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IPrescription prescription = openPrescriptions.getSelectionModel().getSelectedItem();

                //TODO: _prescriptbionController.printPrescription();
            }
        });

        deletePrescriptionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IPrescription prescription = openPrescriptions.getSelectionModel().getSelectedItem();

                //TODO: _prescriptionController.deletePrescription();
            }
        });


        //TODO: get all saved and not printed Prescriptions
        //ObservableList<IPrescription> prescriptionList = FXCollections.observableList((List) _prescriptionController.)

        pane.add(label, 1, 1);
        pane.add(openPrescriptions, 1, 2, 2, 1);
        pane.add(printPrescriptionButton, 1, 3);
        pane.add(deletePrescriptionButton, 2, 3);

        stage.setScene(new Scene(pane));

        stage.show();

    }
}
