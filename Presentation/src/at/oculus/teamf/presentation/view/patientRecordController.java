/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;
/**
 * Created by Karo on 09.04.2015.
 */

import at.oculus.teamf.application.facade.CheckinController;
import at.oculus.teamf.application.facade.CreatePatientController;
import at.oculus.teamf.application.facade.ReceivePatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

public class patientRecordController implements Initializable {


    @FXML public TextField patientRecordLastname;
    @FXML public TextField patientRecordFirstname;
    @FXML public TextField patientRecordSVN;
    @FXML public TextField patientRecordCountryIsoCode;
    @FXML public TextField patientRecordPhone;
    @FXML public TextField patientRecordEmail;
    @FXML public TextField patientRecordPLZ;
    @FXML public TextField patientRecordStreet;
    @FXML public ComboBox<IDoctor> patientRecordDoctor;
    @FXML public RadioButton patientRecordradioGenderFemale;
    @FXML public RadioButton patientRecordradioGenderMale;
    @FXML public ListView patientRecordAppointmentList;
    @FXML public Button patientRecordSaveButton;
    @FXML public Button patientRecordEditButton;
    @FXML public TextField patientRecordCity;
    @FXML public TextArea patientRecordAllergies;
    @FXML public Tab patientRecordTab;
    @FXML public TextArea patientRecordIntolerance;
    @FXML public TextArea patientRecordChildhood;
    @FXML public DatePicker patientRecordBday;
    @FXML public ComboBox<IUser> addToQueue;
    @FXML public Button addPatientToQueue;
    @FXML public Button examinationProtocolButton;

    private boolean isFormEdited = false;
    private ToggleGroup group = new ToggleGroup();
    private IPatient patient;
    private StartupController startupController = new StartupController();
    private CreatePatientController createPatientController = new CreatePatientController();
    public CheckinController checkinController = new CheckinController();
    private ReceivePatientController receivePatientController = new ReceivePatientController();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        patient =  (IPatient)resources.getObject(null);
        IUser user = Main.controller.user;

        patientRecordTab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (isFormEdited) {
                    MessageBox mb1 = new MessageBox("Do you want to save changes?", MessageBoxType.YES_NO);
                    mb1.setHeight(150);
                    mb1.centerOnScreen();
                    mb1.showAndWait();

                    if (mb1.getMessageBoxResult() == MessageBoxResult.OK) {
                        saveChanges();
                    } else {
                        t.consume();
                        //TODO: close Tab;
                    }
                }
            }
        });

        try {
            patientRecordDoctor.setItems(FXCollections.observableArrayList(startupController.getAllDoctors()));
        } catch (FacadeException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        }

        patientRecordSaveButton.setDisable(true);

        patientRecordradioGenderFemale.setToggleGroup(group);
        patientRecordradioGenderMale.setToggleGroup(group);

        patientRecordLastname.setText(patient.getLastName());
        patientRecordLastname.setDisable(true);
        patientRecordFirstname.setText(patient.getFirstName());
        patientRecordFirstname.setDisable(true);

        if(patient.getGender().equals("female"))
        {
            patientRecordradioGenderFemale.setSelected(true);
            patientRecordradioGenderFemale.setDisable(true);
        }else{
            patientRecordradioGenderMale.setSelected(true);
            patientRecordradioGenderMale.setDisable(true);
        }

        patientRecordSVN.setText(patient.getSocialInsuranceNr());
        patientRecordSVN.setDisable(true);

        if(patient.getBirthDay() != null)
        {
            Date input = patient.getBirthDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            patientRecordBday.setValue(date);
        }

        patientRecordBday.setDisable(true);
        patientRecordStreet.setText(patient.getStreet());
        patientRecordStreet.setDisable(true);
        patientRecordPLZ.setText(patient.getPostalCode());
        patientRecordPLZ.setDisable(true);
        patientRecordCity.setText(patient.getCity());
        patientRecordCity.setDisable(true);
        patientRecordCountryIsoCode.setText(patient.getCountryIsoCode());
        patientRecordCountryIsoCode.setDisable(true);
        patientRecordPhone.setText(patient.getPhone());
        patientRecordPhone.setDisable(true);
        patientRecordEmail.setText(patient.getEmail());
        patientRecordEmail.setDisable(true);

        patientRecordDoctor.setValue(patient.getIDoctor());
        patientRecordDoctor.setDisable(true);

        if(patient.getAllergy() == null || patient.getAllergy().length() < 1)
        {
            patientRecordAllergies.setText("No Allergies known");
        }else{
            patientRecordAllergies.setText(patient.getAllergy());
        }
        if(patient.getMedicineIntolerance() == null || patient.getMedicineIntolerance().length() < 1)
        {
            patientRecordIntolerance.setText("No Intolerance known");
        }else{
            patientRecordIntolerance.setText(patient.getMedicineIntolerance());
        }
        if(patient.getChildhoodAilments() == null || patient.getChildhoodAilments().length() < 1)
        {
            patientRecordChildhood.setText("No childhood Ailments");
        }else{
            patientRecordChildhood.setText(patient.getChildhoodAilments());
        }
        try {
            addToQueue.setItems(FXCollections.observableArrayList(startupController.getAllDoctorsAndOrthoptists()));
        } catch (FacadeException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        }

        patientRecordAllergies.setDisable(true);
        patientRecordIntolerance.setDisable(true);
        patientRecordChildhood.setDisable(true);

        addToQueue.setValue(user);
    }

    @FXML
    public void editForm(ActionEvent actionEvent)
    {
        patientRecordradioGenderMale.setDisable(false);
        patientRecordradioGenderFemale.setDisable(false);
        patientRecordLastname.setDisable(false);
        patientRecordLastname.setEditable(true);
        patientRecordFirstname.setDisable(false);
        patientRecordFirstname.setEditable(true);
        patientRecordSVN.setDisable(false);
        patientRecordSVN.setEditable(true);
        patientRecordBday.setDisable(false);
        patientRecordBday.setEditable(true);
        patientRecordStreet.setDisable(false);
        patientRecordStreet.setEditable(true);
        patientRecordPLZ.setDisable(false);
        patientRecordPLZ.setEditable(true);
        patientRecordCity.setDisable(false);
        patientRecordCity.setEditable(true);
        patientRecordCountryIsoCode.setDisable(false);
        patientRecordCountryIsoCode.setEditable(true);
        patientRecordPhone.setDisable(false);
        patientRecordPhone.setEditable(true);
        patientRecordEmail.setDisable(false);
        patientRecordEmail.setEditable(true);
        patientRecordDoctor.setDisable(false);
        patientRecordAllergies.setDisable(false);
        patientRecordAllergies.setEditable(true);
        patientRecordIntolerance.setDisable(false);
        patientRecordIntolerance.setEditable(true);
        patientRecordChildhood.setDisable(false);
        patientRecordChildhood.setEditable(true);

        isFormEdited = true;
        patientRecordSaveButton.setDisable(false);
    }
    /**
     * saves the changes in the patient record
     */
    @FXML
    public void saveChangedForm(ActionEvent actionEvent)
    {
        saveChanges();

        patientRecordradioGenderMale.setDisable(true);
        patientRecordradioGenderFemale.setDisable(true);
        patientRecordLastname.setDisable(true);
        patientRecordLastname.setEditable(false);
        patientRecordFirstname.setDisable(true);
        patientRecordFirstname.setEditable(false);
        patientRecordSVN.setDisable(true);
        patientRecordSVN.setEditable(false);
        patientRecordBday.setDisable(true);
        patientRecordBday.setEditable(false);
        patientRecordStreet.setDisable(true);
        patientRecordStreet.setEditable(false);
        patientRecordPLZ.setDisable(true);
        patientRecordPLZ.setEditable(false);
        patientRecordCity.setDisable(true);
        patientRecordCity.setEditable(false);
        patientRecordCountryIsoCode.setDisable(true);
        patientRecordCountryIsoCode.setEditable(false);
        patientRecordPhone.setDisable(true);
        patientRecordPhone.setEditable(false);
        patientRecordEmail.setDisable(true);
        patientRecordEmail.setEditable(false);
        patientRecordDoctor.setDisable(true);
        patientRecordAllergies.setDisable(true);
        patientRecordAllergies.setEditable(false);
        patientRecordIntolerance.setDisable(true);
        patientRecordIntolerance.setEditable(false);
        patientRecordChildhood.setDisable(true);
        patientRecordChildhood.setEditable(false);
    }

    @FXML
    public void addPatientToQueue(){

        DialogBoxController.getInstance().showInformationDialog("added", patient.getFirstName());
        try {
            IUser user = (IUser) addToQueue.getSelectionModel().getSelectedItem();
            checkinController.insertPatientIntoQueue(patient, user);
            IPatientQueue queue = startupController.getQueueByUserId(user);
            Main.controller.refreshQueue(queue, user);
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
            DialogBoxController.getInstance().showInformationDialog("Error", "Patient already in Queue.");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (CheckinControllerException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CheckinControllerException - Please contact support");
        }
    }

    /**
     * save Changes in Patient Record Form
     */
    private void saveChanges()
    {
        if(patientRecordradioGenderFemale.isSelected())
        {
            patient.setGender("female");
        }else{
            patient.setGender("male");
        }
        patient.setLastName(patientRecordLastname.getText());
        patient.setFirstName(patientRecordFirstname.getText());
        patient.setSocialInsuranceNr(patientRecordSVN.getText());
        LocalDate localDate = patientRecordBday.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date bday = java.sql.Date.from(instant);
        patient.setBirthDay(bday);
        patient.setStreet(patientRecordStreet.getText());
        patient.setPostalCode(patientRecordPLZ.getText());
        patient.setCity(patientRecordCity.getText());
        patient.setCountryIsoCode(patientRecordCountryIsoCode.getText());
        patient.setPhone(patientRecordPhone.getText());
        patient.setEmail(patientRecordEmail.getText());
        patient.setIDoctor((IDoctor) patientRecordDoctor.getValue());
        patient.setAllergy(patientRecordAllergies.getText());
        patient.setMedicineIntolerance(patientRecordIntolerance.getText());
        patient.setChildhoodAilments(patientRecordChildhood.getText());

        try {
            createPatientController.saveIPatient(patient);
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequrementsNotMetException - Please contact support");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        }
        DialogBoxController.getInstance().showInformationDialog("Patient record edited", "Changes saved");
    }

    public void openExamination(ActionEvent actionEvent) {
        try {
            Main.controller.getTabPane().getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/ExaminationTab.fxml"), new SingleResourceBundle(patient)));
            Main.controller.getTabPane().getSelectionModel().select(Main.controller.getTabPane().getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
        try {
            IUser user = addToQueue.getSelectionModel().getSelectedItem();
            System.out.println(user.getLastName());
            IPatientQueue patientqueue = startupController.getQueueByUserId(user);
            System.out.println(patient.getFirstName());
            System.out.println(patientqueue.getEntries().size());
            receivePatientController.removePatientFromQueue(patient, patientqueue);
            Main.controller.refreshQueue(patientqueue, user);
            DialogBoxController.getInstance().showInformationDialog("removed ", patient.getFirstName());
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "InvalidSearchParameterException - Please contact support");
        }

    }
}
