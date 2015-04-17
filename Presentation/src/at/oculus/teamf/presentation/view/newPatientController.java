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

import at.oculus.teamf.application.facade.CreatePatientController;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;



public class newPatientController{

    @FXML public RadioButton radioGenderFemale;
    @FXML public RadioButton radioGenderMale;
    @FXML public TextField PatientRecordLastname;
    @FXML public TextField PatientRecordFirstname;
    @FXML public TextField PatientRecordStreet;
    @FXML public TextField PatientRecordPhone;
    @FXML public TextField PatientRecordEmail;
    @FXML public TextField PatientRecordSVN;
    @FXML public DatePicker PatientRecordBday;
    @FXML public TextField PatientRecordPLZ;
    @FXML public TextField PatientRecordCity;
    @FXML private GridPane newPatientPane;
    @FXML private Button newPatientSaveButton;

    CreatePatientController createPatientController = new CreatePatientController();

    /*Saves the form in a new Patient-Object*/
    public void saveForm(ActionEvent actionEvent)
    {
        String gender = null;
        String lastname = PatientRecordLastname.getText();
        String firstname = PatientRecordFirstname.getText();
        String svn = PatientRecordSVN.getText();

        LocalDate localDate = PatientRecordBday.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date utildate = Date.from(instant);
        Date bday = new Date(utildate.getTime());

        String street = PatientRecordStreet.getText();
        String postalcode = PatientRecordPLZ.getText();
        String city = PatientRecordCity.getText();
        String phone = PatientRecordPhone.getText();
        String email = PatientRecordEmail.getText();

        if(radioGenderFemale.isSelected()){
            gender = "female";
        }else if(radioGenderMale.isSelected()){
            gender = "male";
        }else{
            MessageBox mb1 = new MessageBox("Please choose gender.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
        if(lastname.isEmpty()){
            MessageBox mb1 = new MessageBox("Please enter Lastname.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
        if(firstname.isEmpty()){
            MessageBox mb1 = new MessageBox("Please enter Firstname.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
        if(svn.isEmpty()){
            MessageBox mb1 = new MessageBox("Please enter Social security number.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
        if(bday.toString().isEmpty()) {
            MessageBox mb1 = new MessageBox("Please enter Birthday.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        }
        try {
            createPatientController.createPatient(gender, lastname,firstname, svn, bday, street, postalcode, city, phone, email);
            MessageBox mb1 = new MessageBox("New Patient saved.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
        }

    }
}
