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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class newPatientController implements Initializable {

    @FXML public ChoiceBox<String> cBoxGender;
    @FXML public ComboBox<String> cBoxInsurance;
    @FXML public ChoiceBox cBoxGender1;
    @FXML public ComboBox cBoxInsurance1;
    @FXML public Button closeButton;
    @FXML public Button saveButton;
    @FXML public GridPane newPatientPane;
    @FXML public ComboBox statusPatient;
    @FXML public ComboBox statusInsured;


    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        ObservableList<String> gender = FXCollections.observableArrayList("Mister","Ms");
        cBoxGender.setItems(gender);
        cBoxGender1.setItems(gender);

        ObservableList<String> insurance = FXCollections.observableArrayList("GKK","VA für Eisenbahn und Bergbau", "VA öffentlich Bediensteter", "SVA der gewerblichen Wirtschaft", "SVA der Bauern");
        cBoxInsurance.setItems(insurance);
        cBoxInsurance1.setItems(insurance);

        ObservableList<String> status = FXCollections.observableArrayList("employed","non-working","self-employed");
        statusPatient.setItems(status);
        statusInsured.setItems(status);
    }

    @FXML
    /*Close the 'new Patient' form without saving'*/
    public void onClose(ActionEvent actionEvent)
    {
        newPatientPane.setVisible(false);
    }
    /*Saves the form in a new Patient-Object*/
    public void saveForm(ActionEvent actionEvent)
    {
        //TODO: connect with application
    }
}
