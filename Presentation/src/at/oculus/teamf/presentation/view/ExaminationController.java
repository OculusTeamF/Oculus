/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Karo on 20.04.2015.
 */
public class ExaminationController implements Initializable {

    @FXML private Button newExaminationButton;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private TextArea textExaminationDetails;
    @FXML private ListView examinationList;
    @FXML private TextArea examinationDocumentation;

    private Model _model = Model.getInstance();
    private Date date = new Date();
    private Boolean isFormEdited = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setup controls
        examinationCurrDate.setText(date.toString());
        examinationLnameFnameSvn.setText("PATIENT: " + _model.getPatient().getLastName() + ", " + _model.getPatient().getFirstName() + ", " + _model.getPatient().getSocialInsuranceNr());

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_newexamination.png"));
        newExaminationButton.setGraphic(new ImageView(imageSaveIcon));

        // fetch data (loading & setup)
        getExaminationList();
        //_model.hideStatusBarloader();
    }

    private void getExaminationList(){
        // loads all examination protocols for selected patient
        //TODO reduce loading times
        //try {
            examinationList.setItems(FXCollections.observableArrayList(_model.getAllExaminationProtcols(_model.getPatient())));
       /* } catch (InvalidReloadClassException e) {
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
        }*/

        //TODO sort list
        /*java.util.Collections.sort(examinationList.getItems(), new java.util.Comparator<TYPE>() {
            @Override
            public int compare(TYPE o1, TYPE o2) {
                // Implement your comparator here.
            }
        });*/

        // add mouse event handler
        examinationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    // TODO load examination details
                    loadSelectedExaminationData((IExaminationProtocol) examinationList.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    /* load selected examination data and set data to forms */
    private void loadSelectedExaminationData (IExaminationProtocol exp){
        examinationDocumentation.setText(exp.getDescription());

        // TODO fill into controls
        textExaminationDetails.setText("");
        StringBuilder result = new StringBuilder();
        result.append("START TIME: " + exp.getStartTime());
        result.append(System.getProperty("line.separator"));
        result.append("END TIME: " + exp.getEndTime());
        result.append(System.getProperty("line.separator"));
        if (exp.getDoctor() != null) {
            result.append("DOCTOR: " + exp.getDoctor().getLastName());
            result.append(System.getProperty("line.separator"));
        }
        if (exp.getOrthoptist() != null) {
            result.append("ORTHOPTIST: " + exp.getOrthoptist().getLastName());
            result.append(System.getProperty("line.separator"));
        }
        if (exp.getDiagnosis() != null) {
            result.append("DIAGNOSIS: " + exp.getDiagnosis().toString());
            result.append(System.getProperty("line.separator"));
        } else {
            result.append("DIAGNOSIS: none");
            result.append(System.getProperty("line.separator"));
        }

        textExaminationDetails.setText(result.toString());
    }

    /**
     * Save the Protocoll, if allergies have changed they will saved too
     * @param actionEvent
     */
    @FXML
    public void addNewExaminationProtocol(ActionEvent actionEvent) {
        Date date = new Date();
        //_model.newExaminationProtocol(date, examinationDocumentation.getText(), _model.getPatient(), _model.getPatient().getIDoctor(), null);
        _model.loadTab("NEW EXAMINATION: " + _model.getPatient().getLastName() + " [" + date.toString()+"]" ,"fxml/NewExaminationTab.fxml");
        //DialogBoxController.getInstance().showInformationDialog("Save examination protocol", "Examination Protocol: " + _model.getPatient().getLastName() + ", " + _model.getPatient().getFirstName() + " saved.");
    }
}
