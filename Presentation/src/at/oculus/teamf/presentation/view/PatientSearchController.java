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


    @FXML public TextField searchPatientLastname;
    @FXML public TextField searchPatientFirstname;;
    @FXML public TextField searchPatientSVN;
    @FXML public ListView searchPatientList;
    @FXML public Button searchPatientButton;


    //private SearchPatientController _searchPatientController = new SearchPatientController();
    private Model _model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchPatientLastname.requestFocus();
            }
        });

        Image imageSearchIcon = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchPatientButton.setGraphic(new ImageView(imageSearchIcon));
    }

    private void openPatientRecord(IPatient currPatient) {
        //Todo: rework
        _model.addPatientTab(currPatient);
    }

    // thread for search
    public Task<Void> doSearch() {
        return new Task<Void>() {
            @Override
            protected Void call() {

                return null;
            }
        };
    }

    public Task<ObservableList<IPatient>> doSearchPatient() {
        return new Task<ObservableList<IPatient>>() {
            @Override
            protected ObservableList<IPatient> call() {
                String lastName = searchPatientLastname.getText();
                String firstName = searchPatientFirstname.getText();
                String svn = searchPatientSVN.getText();
                ObservableList<IPatient> patientlist = FXCollections.observableList((List)_model.searchPatients(svn, firstName, lastName));
                return patientlist;
            }
        };
    }

    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        _model.showStatusBarProgressBarIdle("Searching patients");
        String lastName = searchPatientLastname.getText();
        String firstName = searchPatientFirstname.getText();
        String svn = searchPatientSVN.getText();

        //ObservableList<IPatient> patientlist = FXCollections.observableList((List)_model.searchPatients(svn, firstName, lastName));

        ObservableList<IPatient> patientlist = doSearchPatient().getValue();

        if (patientlist.size() > 0) {
            searchPatientList.setItems(patientlist);
        } else {
            DialogBoxController.getInstance().showInformationDialog("Information", "No matches found");
            searchPatientLastname.clear();
            searchPatientFirstname.clear();
            searchPatientSVN.clear();
            searchPatientLastname.requestFocus();
        }

        searchPatientList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getClickCount() == 2) {
                    IPatient currPatientItem = (IPatient) searchPatientList.getSelectionModel().getSelectedItem();
                    //open Patient record
                    openPatientRecord(currPatientItem);
                }
            }
        });
    }
}
