/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Fabian on 01.05.2015.
 */
public class DiagnosisController implements Initializable {
    //private IPatient patient;

    @FXML private Button saveDiagnosisButton;
    @FXML private TextField textDiagnosisTitle;
    @FXML private TextArea textDiagnosisDescription;

    private Model _model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveDiagnosisButton.setGraphic(new ImageView(imageSaveIcon));
    }

    @FXML
    public void saveDiagnosisButtonHandler (ActionEvent actionEvent){
        //TODO save new examination entry / get correct logged in user
        _model.addNewPatientDiagnosis(textDiagnosisTitle.getText(),textDiagnosisDescription.getText(), (IDoctor) _model.getLoggedInUser());
        _model.closeSelectedTab();
    }
}
