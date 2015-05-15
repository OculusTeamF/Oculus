/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.presentation.view.models.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Created by Fabian on 01.05.2015.
 */
public class NewExaminationController implements Initializable {

    @FXML private Button prescriptionButton;
    @FXML private Button saveProtocolButton;
    @FXML private Button addDiagnosisButton;
    @FXML private Button refreshButton;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationCurrTime;
    @FXML private TextField diagnosisTitle;
    @FXML private Label diagnosisIdentity;
    @FXML private TextArea examinationDocumentation;
    @FXML private TextArea diagnosisDetails;

    private Timeline timeline;
    private Integer timeSeconds = 0;
    private Model _model = Model.getInstance();
    private Date _startDate;
    private IPatient initPatient;

    private IExaminationProtocol newexam;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPatient =  _model.getTabModel().getInitPatient();

        // setup controls
        _startDate = new Date();
        examinationLnameFnameSvn.setText(" EXAMINATION: " + initPatient.getFirstName() + " " + initPatient.getLastName());
        examinationCurrDate.setText("START: " + _startDate.toString());
        examinationCurrTime.setText("TIMECOUNTER: 00:00:00");
        diagnosisIdentity.setText("Diagnosis details");
        diagnosisDetails.setDisable(true);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveProtocolButton.setGraphic(new ImageView(imageSaveIcon));
        Image imageAddIcon = new Image(getClass().getResourceAsStream("/res/icon_enqueue.png"));
        addDiagnosisButton.setGraphic(new ImageView(imageAddIcon));
        Image imageAddForm = new Image(getClass().getResourceAsStream("/res/icon_forms.png"));
        prescriptionButton.setGraphic(new ImageView(imageAddForm));
        Image imageRefresh = new Image(getClass().getResourceAsStream("/res/icon_refresh.png"));
        refreshButton.setGraphic(new ImageView(imageRefresh));

        // enable addDiagnosis only ig protocol is created
        addDiagnosisButton.setDisable(true);

        //enable PrescriptionButton only when diagnose ist created
        prescriptionButton.setDisable(true);

        // start stopwatch
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler() {
            @Override
            public void handle(Event event) {
                timeSeconds++;
                // update timerLabel
                examinationCurrTime.setText("TIMECOUNTER: " + convertSecondToHHMMString(timeSeconds));
            }
        }));
        timeline.playFromStart();
    }

    /* for stopwatch: converts seconds to HHMM format */
    private String convertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime*1000L));
        return time;
    }

    @FXML
    public void saveExaminationButtonHandler (ActionEvent actionEvent){
        if (examinationDocumentation.getText().length() != 0){
            IPatient selectedPatient =  _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab());
            timeline.stop();
            examinationCurrTime.setText("TIMECOUNTER: " + convertSecondToHHMMString(timeSeconds) + " [Examination done]");
            Date enddate = new Date();

            if (_model.getLoggedInUser() instanceof Doctor) {
                newexam = _model.getExaminationModel().newExaminationProtocol(_startDate, enddate, examinationDocumentation.getText(), selectedPatient, (IDoctor) _model.getLoggedInUser(), null);
            } else{
                newexam = _model.getExaminationModel().newExaminationProtocol(_startDate, enddate, examinationDocumentation.getText(), selectedPatient, null, (IOrthoptist) _model.getLoggedInUser());
            }

            saveProtocolButton.setDisable(true);
            addDiagnosisButton.setDisable(false);
        }
    }

    @FXML
    public void addDiagnosisButtonHandler (ActionEvent actionEvent){
        // add diagnosis for selected patient
        _model.getExaminationModel().setCurrentExaminationProtocol(newexam);
        IPatient selectedPatient =  _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab());
        _model.getTabModel().addDiagnosisTab(selectedPatient);
        addDiagnosisButton.setDisable(true);
        prescriptionButton.setDisable(false);
    }

    @FXML
    public void addPrescriptionButtonHandler(ActionEvent actionEvent) {
        //opens a new PrescriptionTab
        Tab selectedTab = _model.getTabModel().getSelectedTab();
        IPatient selectedPatient =  _model.getTabModel().getPatientFromSelectedTab(selectedTab);
        _model.getTabModel().addPrescriptionTab(selectedPatient);
    }

    @FXML
    public void refreshTab(ActionEvent actionEvent) {
        IDiagnosis diag = newexam.getDiagnosis();
        if (diag != null) {
            diagnosisTitle.setText(diag.getTitle());
            diagnosisDetails.setText(diag.getDescription());
        }
    }
}
