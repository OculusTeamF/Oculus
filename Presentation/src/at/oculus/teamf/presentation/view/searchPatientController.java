/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;/*package sample;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Karo on 11.04.2015.
 */

public class searchPatientController implements Initializable{

    @FXML  public TextField searchPatientLastname;
    @FXML public TextField searchPatientFirstname;;
    @FXML public TextField searchPatientSVN;
    @FXML public ListView searchPatientList;
    @FXML public Button searchPatientButton;

    private SearchPatientController _searchPatientController = new SearchPatientController();
    private Patient _patient;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchPatientLastname.requestFocus();
    }

    public void openPatientRecord(Event event)
     {
         /* searchPatient.setOnMouseClicked(new EventHandler<MouseEvent>()
          {
           @Override
           public void handle(javafx.scene.input.MouseEvent event)
           {
               ObservableList<Patient> patientlist;
               _patient = new Patient();

                  if(event.getClickCount() == 2)
                  {
                      patientlist = (ObservableList<Patient>) searchPatient.getSelectionModel().getSelectedItem();
                  }
           }
          });*/

     }

    public void searchPatient(ActionEvent actionEvent) {

        //TODO: Focus on Lastneme at the beginning
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
            mb.showAndWait();
            searchPatientLastname.clear();
            searchPatientFirstname.clear();
            searchPatientSVN.clear();

        }

    }


}
