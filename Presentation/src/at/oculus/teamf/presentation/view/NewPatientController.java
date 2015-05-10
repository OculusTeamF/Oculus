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

import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.presentation.view.models.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


public class NewPatientController implements Initializable{

    @FXML private RadioButton radioGenderFemale;
    @FXML private RadioButton radioGenderMale;
    @FXML private TextField newPatientLastname;
    @FXML private TextField newPatientFirstname;
    @FXML private TextField newPatientStreet;
    @FXML private TextField newPatientPhone;
    @FXML private TextField newPatientEmail;
    @FXML private TextField newPatientSVN;
    @FXML private DatePicker newPatientBday;
    @FXML private TextField newPatientPLZ;
    @FXML private TextField newPatientCity;
    @FXML private ChoiceBox newPatientDoctor;
    @FXML private Button newPatientSaveButton;
    @FXML private TextField newPatientCountryIsoCode;

    private Model _model = Model.getInstance();
    private ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addTextLimiter(newPatientSVN, 10);
        addTextLimiter(newPatientLastname, 50);
        addTextLimiter(newPatientFirstname, 50);
        addTextLimiter(newPatientStreet, 255);
        addTextLimiter(newPatientPLZ, 20);
        addTextLimiter(newPatientCity, 50);
        addTextLimiter(newPatientCountryIsoCode, 2);
        addTextLimiter(newPatientPhone, 50);
        addTextLimiter(newPatientEmail, 255);

        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        newPatientSaveButton.setGraphic(new ImageView(imageSaveIcon));

        newPatientDoctor.setItems(FXCollections.observableArrayList(_model.getAllDoctors()));

        radioGenderFemale.setToggleGroup(group);
        radioGenderMale.setToggleGroup(group);
        radioGenderFemale.setSelected(true);
    }

    /*Triggers the saveNewPatient() method from Model when 'save' button is pressed*/
    @FXML
    public void saveForm(ActionEvent actionEvent) {

        String gender = null;
        String lastname = newPatientLastname.getText();
        String firstname = newPatientFirstname.getText();
        String svn = newPatientSVN.getText();
        String street = newPatientStreet.getText();
        String postalcode = newPatientPLZ.getText();
        String city = newPatientCity.getText();
        String phone = newPatientPhone.getText();
        String email = newPatientEmail.getText();
        IDoctor doctor = (IDoctor)newPatientDoctor.getValue();
        String countryIsoCode = newPatientCountryIsoCode.getText();

        //check if mandatory fields are filled with data
        boolean filled = true;
        if(radioGenderFemale.isSelected()){
            gender = "female";
        }else if(radioGenderMale.isSelected()){
            gender = "male";
        }else{
            filled = false;
        }
        if(lastname.isEmpty()){
            filled = false;
            newPatientLastname.requestFocus();
            newPatientLastname.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
        }
        if(firstname.isEmpty()){
            filled = false;
            newPatientFirstname.requestFocus();
            newPatientFirstname.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
        }
        if(svn.isEmpty()){
            filled = false;
            newPatientSVN.requestFocus();
            newPatientSVN.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
        }
        if(newPatientBday.getValue() == null) {
            filled = false;
            newPatientBday.requestFocus();
            newPatientBday.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
        }


        boolean isSaved = false;

        if(!filled){
            DialogBoxController.getInstance().showInformationDialog("Information", "Please fill in the mandatory fields.");
        }else{

            LocalDate localDate = newPatientBday.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date bday = Date.from(instant);
            StatusBarController.getInstance().setText("Save New Patient...");
            isSaved = _model.getPatientModel().saveNewPatient(gender, lastname,firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
        }

        if(isSaved){
            StatusBarController.getInstance().setText("Patient " + newPatientLastname.getText() + ", " + newPatientFirstname.getText() + " added");
            _model.getTabModel().closeSelectedTab();
        }
    }


    /**
     * add textlimiter to textfield
     *
     * @param tf    set textfield
     * @param maxLength max length of input chars
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
