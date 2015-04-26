/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.application.facade.ReceivePatientController;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Karo on 20.04.2015.
 */
public class examinationController implements Initializable {

    @FXML public Button saveButton;
    @FXML private Tab examinationTab;
    @FXML public Text examinationCurrDate;
    @FXML public Text examinationLnameFnameSvn;
    @FXML public TextArea examinationAllergies;
    @FXML public ListView examinationList;
    @FXML public TextArea examinationDocumentation;

    private IPatient patient;
    private Date date = new Date();
    private ReceivePatientController receivePatientController = new ReceivePatientController();
    private Boolean isFormEdited = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        patient =  (IPatient)resources.getObject(null);

        examinationAllergies.setWrapText(true);
        examinationDocumentation.setWrapText(true);

        examinationTab.setText(patient.getLastName() + ", " + patient.getFirstName() + ", " + date.toString());
        examinationCurrDate.setText(date.toString());
        examinationLnameFnameSvn.setText(patient.getLastName()+", "+patient.getFirstName()+", "+patient.getSocialInsuranceNr());

        if(patient.getAllergy() == null || patient.getAllergy().length() < 1)
        {
            examinationAllergies.setText("No Allergies known");
        }else{
            examinationAllergies.setText(patient.getAllergy());
        }

        try {
            examinationList.setItems(FXCollections.observableArrayList(receivePatientController.getAllExaminationProtocols(patient)));
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "InvalidReloadClassException - Please contact your support");
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "ReloadInterfaceNotImplementedException - Please contact your support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact your support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact your support");
        }

        //checks if Allergies have changed
        examinationAllergies.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!oldValue.equals(newValue)) {
                        isFormEdited = true;
                        System.out.println("Allergies changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textarea Allergies");
                }
            }
        });
    }

    /**
     * Save the Protocoll, if allergies have changed the will saved too
     * @param actionEvent
     */
    @FXML
    public void saveProtocol(ActionEvent actionEvent)
    {
        ReceivePatientController receivePatientController = new ReceivePatientController();
        try {
            receivePatientController.createNewExaminationProtocol(date, examinationDocumentation.getText(), patient, patient.getIDoctor(), null);
            DialogBoxController.getInstance().showInformationDialog("Save examination protocol", "Examination Protocol: " + patient.getLastName() + ", " + patient.getFirstName() + " saved.");

            if(isFormEdited){
                patient.setAllergy(examinationAllergies.getText());
            }
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact your support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact your support");
        }
    }
}
