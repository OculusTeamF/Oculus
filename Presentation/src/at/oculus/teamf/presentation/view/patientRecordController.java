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
/*
import at.oculus.teamf.application.facade;
*/

import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.domain.entity.IDoctor;
import at.oculus.teamf.domain.entity.IPatient;
import at.oculus.teamf.domain.entity.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class patientRecordController implements Initializable {


    @FXML public TextField patientRecordLastname;
    @FXML public TextField patientRecordFirstname;
    @FXML public TextField patientRecordSVN;
    @FXML public DatePicker patientRecordBday;
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
    @FXML public TextArea patientRecordMedicineIntolerance;
    @FXML public Tab patientRecordTab;
    @FXML public Button addToQueue;

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

                Boolean closing = false;

                if (closing == false){

                    System.out.println("Closing tab.....");
                } else {
                    t.consume(); // eat event
                    System.out.println("Do not close tab.....");
                }
            }
        });

        patientRecordDoctor.setItems(FXCollections.observableArrayList(startupController.getAllDoctors()));

        patientRecordSaveButton.setDisable(true);

        patientRecordradioGenderFemale.setToggleGroup(group);
        patientRecordradioGenderMale.setToggleGroup(group);

        patientRecordLastname.setText(patient.getLastName());
        patientRecordFirstname.setText(patient.getFirstName());

        if(patient.getGender().equals("female"))
        {
            patientRecordradioGenderFemale.setSelected(true);
            patientRecordradioGenderFemale.setDisable(true);
        }else{
            patientRecordradioGenderMale.setSelected(true);
            patientRecordradioGenderMale.setDisable(true);
        }

        patientRecordSVN.setText(patient.getSocialInsuranceNr());

        Date bday = (Date) patient.getBirthDay();
        patientRecordBday.setAccessibleText(String.valueOf(bday));

        patientRecordStreet.setText(patient.getStreet());
        patientRecordPLZ.setText(patient.getPostalCode());
        patientRecordCity.setText(patient.getCity());
        patientRecordCountryIsoCode.setText(patient.getCountryIsoCode());
        patientRecordPhone.setText(patient.getPhone());
        patientRecordEmail.setText(patient.getEmail());

        patientRecordDoctor.setValue(patient.getDoctor());
        patientRecordDoctor.setDisable(true);

        patientRecordAllergies.setText(patient.getAllergy());
        patientRecordMedicineIntolerance.setText(patient.getMedicineIntolerance());

    }

   @FXML
    public void editForm(ActionEvent actionEvent)
    {
        patientRecordradioGenderMale.setDisable(false);
        patientRecordradioGenderFemale.setDisable(false);
        patientRecordLastname.setEditable(true);
        patientRecordFirstname.setEditable(true);
        patientRecordSVN.setEditable(true);
        patientRecordBday.setEditable(true);
        patientRecordStreet.setEditable(true);
        patientRecordPLZ.setEditable(true);
        patientRecordCity.setEditable(true);
        patientRecordCountryIsoCode.setEditable(true);
        patientRecordPhone.setEditable(true);
        patientRecordEmail.setEditable(true);
        patientRecordDoctor.setDisable(false);
        patientRecordAllergies.setEditable(true);

        isFormEdited = true;

        patientRecordSaveButton.setDisable(false);
    }
    @FXML
    public void saveChangedForm(ActionEvent actionEvent)
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
        java.util.Date utildate = java.sql.Date.from(instant);
        java.sql.Date bday = new java.sql.Date(utildate.getTime());
        patient.setBirthDay(bday);

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

            }
        }
    }

    /**
     * saves the changes in the patient record
     */
    private void saveChanges()
    {

    }
}
