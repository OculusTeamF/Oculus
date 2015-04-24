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

import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.FacadeException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import at.oculus.teamf.application.facade.SearchPatientController;
/**
 * Created by Karo on 11.04.2015.
 */

public class PatientSearchController implements Initializable{


    @FXML public TextField searchPatientLastname;
    @FXML public TextField searchPatientFirstname;;
    @FXML public TextField searchPatientSVN;
    @FXML public ListView searchPatientList;
    @FXML public Button searchPatientButton;
    @FXML public Tab searchPatientTab;
    @FXML public AnchorPane searchAnchor;

    private SearchPatientController _searchPatientController = new SearchPatientController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchPatientLastname.requestFocus();
            }
        });
    }

    private void openPatientRecord(IPatient currPatient)
     {
         Main.controller.addPatientTab(currPatient);
     }

    @FXML
    public void searchPatient(ActionEvent actionEvent) {


        String lastName = searchPatientLastname.getText();
        String firstName = searchPatientFirstname.getText();
        String svn = searchPatientSVN.getText();


        ObservableList<IPatient> patientlist = null;
        try {
            patientlist = FXCollections.observableList((List) _searchPatientController.searchPatients(svn, firstName, lastName));
        } catch (FacadeException e) {
            e.printStackTrace();
            MessageBox mb = new MessageBox("Error!!! Please contact your Support", MessageBoxType.OK_ONLY);
            mb.setHeight(150);
            mb.centerOnScreen();
            mb.showAndWait();
        }

            if (patientlist.size() > 0) {
                searchPatientList.setItems(patientlist);
            } else {
                MessageBox mb1 = new MessageBox("No matches found", MessageBoxType.OK_ONLY);
                mb1.setHeight(150);
                mb1.centerOnScreen();
                mb1.showAndWait();

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
