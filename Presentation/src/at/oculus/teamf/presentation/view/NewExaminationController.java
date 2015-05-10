/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

    @FXML public Button prescriptionButton;
    @FXML private Button saveProtocolButton;
    @FXML private Button addDiagnosisButton;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationCurrTime;
    @FXML private Label diagnosisIdentity;
    @FXML private TextArea examinationDocumentation;

    private Timeline timeline;
    private Integer timeSeconds = 0;
    private Model _model = Model.getInstance();
    private Date _startDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // setup controls
        _startDate = new Date();
        examinationLnameFnameSvn.setText("NEW PROTOCOL: " + _model.getPatient().getFirstName() + " " + _model.getPatient().getLastName());
        examinationCurrDate.setText("START: " + _startDate.toString());
        examinationCurrTime.setText("TIMECOUNTER: 00:00:00");
        diagnosisIdentity.setText("Diagnosis details: [docname] - [start] - [end]");

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveProtocolButton.setGraphic(new ImageView(imageSaveIcon));
        Image imageAddIcon = new Image(getClass().getResourceAsStream("/res/icon_enqueue.png"));
        addDiagnosisButton.setGraphic(new ImageView(imageAddIcon));

        // enable addDiagnosis only ig protocol is created
        addDiagnosisButton.setDisable(true);

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

        // load data
        /*if(patient.getAllergy() == null || patient.getAllergy().length() < 1)
        {
            examinationAllergies.setText("No Allergies known");
        }else{
            examinationAllergies.setText(patient.getAllergy());
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
        });*/
    }

    private String convertSecondToHHMMString(int secondtTime) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime*1000L));
        return time;
    }

    @FXML
    public void saveExaminationButtonHandler (ActionEvent actionEvent){
        //TODO startdate & enddate missing !
        timeline.stop();
        Date date = new Date();
        //TODO check if orthoptist or doctor
        _model.newExaminationProtocol(date, examinationDocumentation.getText(), _model.getPatient(), (IDoctor) _model.getLoggedInUser(), null);
        addDiagnosisButton.setDisable(false);
    }

    @FXML
    public void addDiagnosisButtonHandler (ActionEvent actionEvent){
        // add diagnosis for patient
        _model.addDiagnosisTab(_model.getPatient());
    }

    @FXML
    public void addPrescriptionButtonHandler(ActionEvent actionEvent) {
        //opens a new PrescriptionTab
        _model.addPrescriptionTab(_model.getPatient());
    }
}
