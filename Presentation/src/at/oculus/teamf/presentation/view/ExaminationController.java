/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationResultException;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IExaminationResult;
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

    @FXML public ListView examinationResults;
    @FXML private Button newExaminationButton;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private TextArea textExaminationDetails;
    @FXML private ListView examinationList;
    @FXML private TextArea examinationDocumentation;

    private Model _model = Model.getInstance();
    private Date date = new Date();
    private ObservableList<IExaminationProtocol> _protocolist;
    private ObservableList<IExaminationResult> _results;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setup controls
        examinationCurrDate.setText(date.toString());
        examinationLnameFnameSvn.setText("PATIENT: " + _model.getPatient().getLastName() + ", " + _model.getPatient().getFirstName() + ", " + _model.getPatient().getSocialInsuranceNr());
        examinationList.setDisable(true);
        examinationResults.setDisable(true);
        examinationDocumentation.setDisable(true);
        textExaminationDetails.setDisable(true);
        String loadtext = "Loading examinations protocols....";
        examinationList.getItems().add(loadtext);
        String loadtext2 = "Choose an examination protocol....";
        examinationResults.getItems().add(loadtext2);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_newexamination.png"));
        newExaminationButton.setGraphic(new ImageView(imageSaveIcon));

        // fetch data (loading & setup)
        getExaminationList();

        // add mouse event handler
        examinationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    loadSelectedExaminationData((IExaminationProtocol) examinationList.getSelectionModel().getSelectedItem());


                    IExaminationProtocol protocol = (IExaminationProtocol) examinationList.getSelectionModel().getSelectedItem();
                    try {
                        _results = FXCollections.observableArrayList(protocol.getExaminationResults());
                    } catch (CouldNotGetExaminationResultException e) {
                        e.printStackTrace();
                        DialogBoxController.getInstance().showExceptionDialog(e, "CouldNotGetExaminationResultException - Please contact support");
                    }
                    examinationResults.setItems(_results);
                }
            }
        });
    }

    private void getExaminationList(){
        //TODO reduce loading times
        // loads all examination protocols for selected patient
        final Task<Void> search = loadExaminationListsThread();
        StatusBarController.showProgressBarIdle("Loading examination protocols");
        search.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                // end of search thread
                StatusBarController.hideStatusBarProgressBarIdle();
                examinationList.setDisable(false);
                examinationDocumentation.setDisable(false);
                textExaminationDetails.setDisable(false);
                examinationList.setItems(_protocolist);
                //TODO sort list
                /*java.util.Collections.sort(examinationList.getItems(), new java.util.Comparator<TYPE>() {
                    @Override
                    public int compare(TYPE o1, TYPE o2) {
                    // implement date comparator
                    }
                });*/
            }
        });

        new Thread(search).start();

    }

    /* load selected examination data and set data to forms */
    private void loadSelectedExaminationData (IExaminationProtocol exp){
        examinationDocumentation.setText(exp.getDescription());

        ObservableList<IExaminationResult> results = null; try {
            results = FXCollections.observableArrayList(exp.getExaminationResults());
        } catch (CouldNotGetExaminationResultException e) {
            e.printStackTrace();
        }
        examinationResults.setItems(results);

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
            result.append("DIAGNOSIS: " + exp.getDiagnosis().getTitle());
            result.append(System.getProperty("line.separator"));
        } else {
            result.append("DIAGNOSIS: none");
            result.append(System.getProperty("line.separator"));
        }

        textExaminationDetails.setText(result.toString());
    }

    @FXML
    public void addNewExaminationProtocol(ActionEvent actionEvent) {
        Date date = new Date();
        _model.loadTab("NEW EXAMINATION: " + _model.getPatient().getLastName() + " [" + date.toString()+"]" ,"fxml/NewExaminationTab.fxml");
    }

    // *******************************************************************
    // Loading Thread
    // *******************************************************************

    /* thread for loading*/
    public Task<Void> loadExaminationListsThread() {return new Task<Void>() {
        @Override
        protected Void call() {
            // application layer acces for examination protocols loading
            _protocolist = FXCollections.observableArrayList(_model.getAllExaminationProtcols(_model.getPatient()));
            return null;
        }
    };
    }
}
