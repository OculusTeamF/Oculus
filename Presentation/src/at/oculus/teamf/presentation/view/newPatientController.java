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
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.persistence.exception.FacadeException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;


public class newPatientController implements Initializable{

    @FXML public RadioButton radioGenderFemale;
    @FXML public RadioButton radioGenderMale;
    @FXML public TextField newPatientLastname;
    @FXML public TextField newPatientFirstname;
    @FXML public TextField newPatientStreet;
    @FXML public TextField newPatientPhone;
    @FXML public TextField newPatientEmail;
    @FXML public TextField newPatientSVN;
    @FXML public DatePicker newPatientBday;
    @FXML public TextField newPatientPLZ;
    @FXML public TextField newPatientCity;
    @FXML public ChoiceBox newPatientDoctor;
    @FXML public Button newPatientSaveButton;
    @FXML public TextField newPatientCountryIsoCode;


    private CreatePatientController createPatientController = new CreatePatientController();
    private StartupController startupController = new StartupController();
    private ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            newPatientDoctor.setItems(FXCollections.observableArrayList(startupController.getAllDoctors()));
        } catch (FacadeException e) {
            e.printStackTrace();
        }

        radioGenderFemale.setToggleGroup(group);
        radioGenderMale.setToggleGroup(group);
        radioGenderFemale.setSelected(true);
    }

    /*Saves the form in a new Patient-Object*/
    public void saveForm(ActionEvent actionEvent)
    {
        String gender = null;
        String lastname = newPatientLastname.getText();
        String firstname = newPatientFirstname.getText();
        String svn = newPatientSVN.getText();

        /*LocalDate localDate = newPatientBday.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date utildate = Date.from(instant);
        Date bday = new Date(utildate.getTime());*/

        LocalDate localDate = newPatientBday.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date bday = Date.from(instant);

        String street = newPatientStreet.getText();
        String postalcode = newPatientPLZ.getText();
        String city = newPatientCity.getText();
        String phone = newPatientPhone.getText();
        String email = newPatientEmail.getText();
        IDoctor doctor = (IDoctor)newPatientDoctor.getValue();
        String countryIsoCode = newPatientCountryIsoCode.getText();



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
            try {
                createPatientController.createPatient(gender, lastname,firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
            } catch (FacadeException e) {
                e.printStackTrace();
            } catch (PatientCouldNotBeSavedException e) {
                e.printStackTrace();
            }
            MessageBox mb1 = new MessageBox("New Patient saved.", MessageBoxType.OK_ONLY);
            mb1.setHeight(150);
            mb1.centerOnScreen();
            mb1.showAndWait();
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
        }
    }
}
