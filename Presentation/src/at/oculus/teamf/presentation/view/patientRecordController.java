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


import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.FacadeException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
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
    @FXML public ChoiceBox patientRecordDoctor;
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
    @FXML public ChoiceBox addToQueue;

    private boolean isFormEdited = false;
    private ToggleGroup group = new ToggleGroup();
    private IPatient patient = Main.controller.getPatient();
    private StartupController startupController = new StartupController();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
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
        patientRecordBday.setPromptText(patient.getBirthDay().toString());
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
            MessageBox mb1 = new MessageBox("Error", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
    }

   @FXML
    public void editForm(ActionEvent actionEvent)
    {
        patientRecordradioGenderMale.setDisable(false);
        patientRecordradioGenderFemale.setDisable(false);
        patientRecordLastname.setDisable(false);
        patientRecordFirstname.setDisable(false);
        patientRecordSVN.setDisable(false);
        patientRecordBday.setDisable(false);
        patientRecordStreet.setDisable(false);
        patientRecordPLZ.setDisable(false);
        patientRecordCity.setDisable(false);
        patientRecordCountryIsoCode.setDisable(false);
        patientRecordPhone.setDisable(false);
        patientRecordEmail.setDisable(false);
        patientRecordDoctor.setDisable(false);
        patientRecordAllergies.setDisable(false);
        patientRecordIntolerance.setDisable(false);
        patientRecordChildhood.setDisable(false);

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
    }

    @FXML
    public void onClose(ActionEvent actionEvent)
    {
        if(isFormEdited)
        {
            MessageBox mb1 = new MessageBox("Do you want to save changes?", MessageBoxType.YES_NO);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();

            if(mb1.getMessageBoxResult() == MessageBoxResult.OK)
            {
                saveChanges();
            }else{
                //TODO: close Tab;
            }
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


        //TODO: Bday speichern

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
    }
}
