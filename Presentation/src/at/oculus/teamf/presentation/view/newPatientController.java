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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;

public class newPatientController implements Initializable {

    

    @FXML public GridPane newPatientPane;
    @FXML public Button newPatientSaveButton;


    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {}


    /*Saves the form in a new Patient-Object*/
    public void saveForm(ActionEvent actionEvent)
    {
        //TODO: connect with application

    }
}
