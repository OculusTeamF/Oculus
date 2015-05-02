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
 ~ Copyright (c) 2015 Team F
 ~
 ~ This file is part of Oculus.
 ~ Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 ~ Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 ~ You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.interfaces.IPatient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Karo on 11.04.2015.
 */

public class PatientSearchController implements Initializable{


    @FXML private TextField searchPatientLastname;
    @FXML private TextField searchPatientFirstname;;
    @FXML private TextField searchPatientSVN;
    @FXML private ListView searchPatientList;
    @FXML private Button searchPatientButton;

    private Model _model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchPatientLastname.requestFocus();
            }
        });

        // load search button image
        Image imageSearchIcon = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchPatientButton.setGraphic(new ImageView(imageSearchIcon));

        // add click handler for searchresult list (opens selected patient)
        searchPatientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 2) {
                    _model.setPatient((IPatient) searchPatientList.getSelectionModel().getSelectedItem());
                    _model.loadTab("PATIENT RECORD: " + _model.getPatient().getLastName(), "fxml/PatientRecordTab.fxml");
                }
            }
        });
    }

    // thread for search
    public Task<Void> doSearchPatients() {
        return new Task<Void>() {
            @Override
            protected Void call() {

                searchPatientButton.setDisable(true);

                String lastName = searchPatientLastname.getText();
                String firstName = searchPatientFirstname.getText();
                String svn = searchPatientSVN.getText();

                ObservableList<IPatient> patientlist = FXCollections.observableList((List)_model.searchPatients(svn, firstName, lastName));

                if (patientlist.size() > 0) {
                    searchPatientList.setItems(patientlist);
                } else {
                    DialogBoxController.getInstance().showInformationDialog("Information", "No matches found");
                    searchPatientLastname.clear();
                    searchPatientFirstname.clear();
                    searchPatientSVN.clear();
                    searchPatientLastname.requestFocus();
                }

                return null;
            }
        };
    }

    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        final Task<Void> search = doSearchPatients();
        _model.showStatusBarProgressBarIdle("Searching patients");
        search.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                _model.hideStatusBarProgressBarIdle();
                searchPatientButton.setDisable(false);
            }
        });
        new Thread(search).start();
    }
}
