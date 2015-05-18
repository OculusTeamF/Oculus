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
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.technical.loggin.ILogger;
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
public class ExaminationController implements Initializable, ILogger {

    @FXML public ListView examinationResults;
    @FXML private Button newExaminationButton;
    @FXML private Button refreshButton;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private TextArea textExaminationDetails;
    @FXML private ListView examinationList;
    @FXML private TextArea examinationDocumentation;

    private Model _model = Model.getInstance();
    private Date date = new Date();
    private ObservableList<IExaminationProtocol> _protocolist;
    private ObservableList<IExaminationResult> _results;
    private IExaminationProtocol selectedProtocol;
    private IPatient initPatient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPatient =  _model.getTabModel().getInitPatient();

        // setup controls
        examinationCurrDate.setText(date.toString());
        examinationLnameFnameSvn.setText(" PATIENT: " + initPatient.getLastName() + ", " + initPatient.getFirstName() + ", " + initPatient.getSocialInsuranceNr());

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
        Image imageRefresh = new Image(getClass().getResourceAsStream("/res/icon_refresh.png"));
        refreshButton.setGraphic(new ImageView(imageRefresh));

        // load all examination protocols
        getExaminationList();

        // add mouse event handler
        examinationList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    loadSelectedExaminationData((IExaminationProtocol) examinationList.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    // *******************************************************************
    // EXAMINATION PROTOCOLS
    // *******************************************************************

    /* loads all examination protocols for selected patient */
    private void getExaminationList(){
        // setup task: load all examination protocols
        final Task<Void> loadlist = loadExaminationListsThread();

        StatusBarController.showProgressBarIdle("Loading examination protocols");

        // setup: when task is done then refresh all controls
        loadlist.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                examinationList.setDisable(false);
                examinationList.setItems(_protocolist);
                StatusBarController.hideStatusBarProgressBarIdle();
            }
        });

        // start loading task
        new Thread(loadlist).start();
    }

    // *******************************************************************
    // SELECTED PROTOCOL ACTIONS
    // *******************************************************************

    /* load selected examination data and set data to forms */
    private void loadSelectedExaminationData (IExaminationProtocol exp){
        examinationDocumentation.setText(exp.getDescription());

        // load examination results
        selectedProtocol = exp;
        examinationResults.setDisable(true);
        examinationResults.setItems(FXCollections.observableArrayList("Loading examination results..."));
        final Task<Void> loadresults = loadExaminationResultsThread();
        StatusBarController.showProgressBarIdle("Loading examination results");

        // setup: when task is done then refresh all controls
        loadresults.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                StatusBarController.hideStatusBarProgressBarIdle();
                examinationResults.setItems(_results);
                examinationResults.setDisable(false);
            }
        });
        // start loading task
        new Thread(loadresults).start();


        // return loaded data as stringbuilder
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
        if (exp.getTeamFDiagnosis() != null) {
            result.append("DIAGNOSIS: " + exp.getTeamFDiagnosis().getTitle());
            result.append(System.getProperty("line.separator"));
        } else {
            result.append("DIAGNOSIS: none");
            result.append(System.getProperty("line.separator"));
        }

        textExaminationDetails.setText(result.toString());
    }

    // *******************************************************************
    // BUTTON EVENTS
    // *******************************************************************

    @FXML
    public void addNewExaminationProtocol(ActionEvent actionEvent) {
        IPatient selectedpatient =  _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab());
        _model.getTabModel().addNewExaminationTab(selectedpatient);
    }

    @FXML
    public void refreshTab(ActionEvent actionEvent) {
        String loadtext = "Loading examinations protocols....";
        examinationList.getItems().add(loadtext);
        getExaminationList();
    }

    // *****************************************************************************************************************
    //
    // LOADING THREADS
    //
    // *****************************************************************************************************************

    /* thread: load examination protocols */
    public Task<Void> loadExaminationListsThread() {return new Task<Void>() {
        @Override
        protected Void call() {
            //IPatient selectedPatient =  _model.getTabModel().getInitPatient();
            log.debug("Loading examination protocols for patient: " + initPatient.getLastName());
            _protocolist = FXCollections.observableArrayList(_model.getExaminationModel().getAllExaminationProtcols(initPatient));
            return null;
        }
    };
    }

    /* thread: load examination results */
    public Task<Void> loadExaminationResultsThread() {return new Task<Void>() {
        @Override
        protected Void call() {
            IPatient selectedpatient =  _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab());
            log.debug("Loading examination results for patient: " + selectedpatient.getLastName());
            try {
                _results = FXCollections.observableArrayList(selectedProtocol.getExaminationResults());
            } catch (CouldNotGetExaminationResultException e) {
                e.printStackTrace();
            }
            return null;
        }
    };
    }
}
