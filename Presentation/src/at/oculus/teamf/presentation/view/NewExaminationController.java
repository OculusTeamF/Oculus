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
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.resourcebundel.SingleResourceBundle;
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

    @FXML public TextArea examinationDocumentation;
    @FXML private Button saveProtocolButton;
    @FXML private Button addDiagnosisButton;
    @FXML private Text examinationLnameFnameSvn;
    @FXML private Text examinationCurrDate;
    @FXML private Text examinationCurrTime;
    @FXML private Label diagnosisIdentity;

    private Timeline timeline;
    private Integer timeSeconds = 0;
    //private IPatient patient;
    //private ReceivePatientController receivePatientController = new ReceivePatientController();
    private Model _model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date date = new Date();

        // get patient object
        //patient =  (IPatient)resources.getObject(null);

        // setup controls
        examinationLnameFnameSvn.setText("NEW PROTOCOL: " + _model.getPatient().getFirstName() + " " + _model.getPatient().getLastName()+ ", "+_model.getPatient().getBirthDay().toString());
        examinationCurrDate.setText("START: " + date.toString());
        examinationCurrTime.setText("TIMECOUNTER: 00:00:00");
        diagnosisIdentity.setText("Diagnosis details: [docname] - [start] - [end]");

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveProtocolButton.setGraphic(new ImageView(imageSaveIcon));
        Image imageAddIcon = new Image(getClass().getResourceAsStream("/res/icon_enqueue.png"));
        addDiagnosisButton.setGraphic(new ImageView(imageAddIcon));

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
        //examinationCurrTime.setText("TIMECOUNTER: " + timeSeconds.toString());

        // load data
       /* if(_model.getPatient().getAllergy() == null || _model.getPatient().getAllergy().length() < 1)
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
        //TODO save new examination entry
        //ReceivePatientController receivePatientController = new ReceivePatientController();
        //receivePatientController.createNewExaminationProtocol(date, examinationDocumentation.getText(), patient, patient.getIDoctor(), null);
        timeline.stop();
        DialogBoxController.getInstance().showInformationDialog("saving","saving not added yet");
    }

    @FXML
    public void addDiagnosisButtonHandler (ActionEvent actionEvent){
        // add diagnosis for patient
        _model.addDiagnosisTab(_model.getPatient());
    }
}
