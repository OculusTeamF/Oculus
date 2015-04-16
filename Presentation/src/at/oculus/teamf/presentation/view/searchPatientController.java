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
import at.oculus.teamf.domain.entity.Patient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Karo on 11.04.2015.
 */

public class searchPatientController implements Initializable  {

    @FXML  public TextField searchPatientLastname;
    @FXML public TextField searchPatientFirstname;;
    @FXML public TextField searchPatientSVN;
    @FXML public ListView searchPatientList;
    @FXML public Button searchPatientButton;

    private SearchPatientController _searchPatientController = new SearchPatientController();
    private Patient _patient;
    private Tab _patientRecordTab;
    private MainController mainController = MainController.getInstance();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchPatientLastname.requestFocus();
            }
        });
    }

    private void openPatientRecord(String currPatient)
     {
         MessageBox mb = new MessageBox("in Patient Record", MessageBoxType.OK_ONLY);
         mb.centerOnScreen();
         mb.showAndWait();
         mainController.generateTab("Patient Record");
     }

    public void searchPatient(ActionEvent actionEvent) {

        String lastName = searchPatientLastname.getText();
        String firstName = searchPatientFirstname.getText();
        String svn = searchPatientSVN.getText();

        if (lastName.length() == 0) {
            lastName = null;
        }
        if (firstName.length() == 0) {
            firstName = null;
        }
        if (svn.length() == 0) {
            svn = null;
        }

        ObservableList<Patient> patientlist = FXCollections.observableList((List<Patient>) _searchPatientController.searchPatients(svn,lastName,firstName));

        if(patientlist.size() > 0)
        {
            searchPatientList.setItems(patientlist);
        }else{
            MessageBox mb = new MessageBox("No matches found", MessageBoxType.OK_ONLY);
            mb.centerOnScreen();
            mb.showAndWait();
            searchPatientLastname.clear();
            searchPatientFirstname.clear();
            searchPatientSVN.clear();
            searchPatientLastname.requestFocus();
        }

        searchPatientList.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                ObservableList<Patient> patientlist;

                if(event.getClickCount() == 2)
                {
                    String currPatientItem = searchPatientList.getSelectionModel().getSelectedItem().toString();

                    MessageBox mb = new MessageBox("double clicked", MessageBoxType.OK_ONLY);
                    mb.centerOnScreen();
                    mb.showAndWait();

                    openPatientRecord(currPatientItem);
                    //TODO: open Patient record
                }
            }
        });
    }


}
