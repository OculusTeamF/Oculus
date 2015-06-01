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
 * Created by Karo on 09.04.2015.
 */

import at.oculus.teamf.application.controller.PrescriptionController;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.controller.exceptions.PrescriptionControllerExceptions.NoPatientException;
import at.oculus.teamf.domain.entity.ICalendarEvent;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.user.Gender;
import at.oculus.teamf.application.controller.exceptions.CreatePatientControllerExceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.controller.exceptions.CreatePatientControllerExceptions.RequirementsNotMetException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetPrescriptionException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.presentation.view.models.PatientRecordModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class PatientRecordController implements Initializable {

    @FXML private TextField patientRecordLastname;
    @FXML private TextField patientRecordFirstname;
    @FXML private TextField patientRecordSVN;
    @FXML private TextField patientRecordCountryIsoCode;
    @FXML private TextField patientRecordPhone;
    @FXML private TextField patientRecordEmail;
    @FXML private TextField patientRecordPLZ;
    @FXML private TextField patientRecordStreet;
    @FXML private TextField patientRecordCity;
    @FXML private ComboBox<IDoctor> patientRecordDoctor;
    @FXML private RadioButton patientRecordradioGenderFemale;
    @FXML private RadioButton patientRecordradioGenderMale;
    @FXML private TextArea patientRecordAllergies;
    @FXML private TextArea patientRecordIntolerance;
    @FXML private TextArea patientRecordChildhood;
    @FXML private DatePicker patientRecordBday;
    @FXML private Button notPrintedPrescriptions;
    @FXML private ComboBox<IUser> addToQueueBox;
    @FXML private Button addPatientToQueueButton;
    @FXML private Button examinationProtocolButton;
    @FXML private Button patientRecordSaveButton;
    @FXML private Button patientRecordEditButton;
    @FXML private ListView patientRecordListDiagnoses;
    @FXML private ListView patientRecordAppointmentList;
    @FXML private Accordion medicalHistory;
    @FXML private TitledPane mh4, mh3, mh2, mh1;
    @FXML private AnchorPane patientRecordPane;


    private Model _model = Model.getInstance();
    private boolean _isFormEdited = false;
    private ToggleGroup _group = new ToggleGroup();
    private PatientRecordModel _patientRecordModel = PatientRecordModel.getInstance();
    private PrescriptionController _prescriptionController;
    private IPatient _initpatient;

    private ObservableList<IDiagnosis> _diagnosislist;
    private ObservableList<ICalendarEvent> _calendareventlist;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        /**
         * if changes are detected in patientform, then the tab cannot be closed without answer the dialogbox
         * if you press no to "Do you want to save changes?" Tab is closing without saving changes.
         */

        // set patient to init only
        _initpatient = _model.getTabModel().getInitPatient();

        // load button images
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        patientRecordSaveButton.setGraphic(new ImageView(imageSaveIcon));
        Image imageEditIcon = new Image(getClass().getResourceAsStream("/res/icon_edit.png"));
        patientRecordEditButton.setGraphic(new ImageView(imageEditIcon));
        Image imageQueueIcon = new Image(getClass().getResourceAsStream("/res/icon_enqueue.png"));
        addPatientToQueueButton.setGraphic(new ImageView(imageQueueIcon));
        Image imageExaminationIcon = new Image(getClass().getResourceAsStream("/res/icon_examination.png"));
        examinationProtocolButton.setGraphic(new ImageView(imageExaminationIcon));
        Image imageAddForm = new Image(getClass().getResourceAsStream("/res/icon_forms.png"));
        notPrintedPrescriptions.setGraphic(new ImageView(imageAddForm));

        // load data: doctorlist, appointmentlist, diagnoseslist
        loadPatientData();


        //disable all Patientfields and the save button, sets the text from IPatient into fields
        patientRecordSaveButton.setDisable(true);
        patientRecordradioGenderFemale.setToggleGroup(_group);
        patientRecordradioGenderMale.setToggleGroup(_group);


        patientRecordLastname.setText(_initpatient.getLastName());
        patientRecordFirstname.setText(_initpatient.getFirstName());

        if(_initpatient.getGender().equals("female"))
        {
            patientRecordradioGenderFemale.setSelected(true);
        }else{
            patientRecordradioGenderMale.setSelected(true);
        }

        patientRecordSVN.setText(_initpatient.getSocialInsuranceNr());


        if(_initpatient.getBirthDay() != null)
        {
            Date input = _initpatient.getBirthDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            patientRecordBday.setValue(date);
        }
        patientRecordStreet.setText(_initpatient.getStreet());
        patientRecordPLZ.setText(_initpatient.getPostalCode());
        patientRecordCity.setText(_initpatient.getCity());
        patientRecordCountryIsoCode.setText(_initpatient.getCountryIsoCode());
        patientRecordPhone.setText(_initpatient.getPhone());
        patientRecordEmail.setText(_initpatient.getEmail());
        patientRecordDoctor.setValue(_initpatient.getIDoctor());

        disableFields();

        if(_initpatient.getAllergy() == null || _initpatient.getAllergy().length() < 1) {
            patientRecordAllergies.setText("No Allergies known");
        }else{
            patientRecordAllergies.setText(_initpatient.getAllergy());
        }
        if(_initpatient.getMedicineIntolerance() == null || _initpatient.getMedicineIntolerance().length() < 1) {
            patientRecordIntolerance.setText("No Intolerance known");
        }else{
            patientRecordIntolerance.setText(_initpatient.getMedicineIntolerance());
        }
        if(_initpatient.getChildhoodAilments() == null || _initpatient.getChildhoodAilments().length() < 1) {
            patientRecordChildhood.setText("No childhood Ailments");
        }else{
            patientRecordChildhood.setText(_initpatient.getChildhoodAilments());
        }

        patientRecordAllergies.setDisable(false);
        patientRecordIntolerance.setDisable(false);
        patientRecordChildhood.setDisable(false);
        patientRecordAllergies.setEditable(false);
        patientRecordIntolerance.setEditable(false);
        patientRecordChildhood.setEditable(false);

        addToQueueBox.setValue(_initpatient.getIDoctor());

        if(_model.isPatientInQueue(_initpatient.toString()))
        {
            addToQueueBox.setDisable(true);
            addPatientToQueueButton.setDisable(true);

        }

        //check if something has changed, if changes detected --> saveChanges()
        addListener(patientRecordLastname);
        addListener(patientRecordFirstname);
        addListener(patientRecordSVN);
        addListener(patientRecordStreet);
        addListener(patientRecordPLZ);
        addListener(patientRecordCity);
        addListener(patientRecordCountryIsoCode);
        addListener(patientRecordPhone);
        addListener(patientRecordEmail);


        patientRecordBday.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                try {
                    if (oldValue != newValue) {
                        _isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("Exception", "Please contact support");
                }
            }
        });

        patientRecordDoctor.valueProperty().addListener(new ChangeListener<IDoctor>() {
            @Override
            public void changed(ObservableValue<? extends IDoctor> observable, IDoctor oldValue, IDoctor newValue) {
                _isFormEdited = true;
            }
        });
        patientRecordAllergies.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!oldValue.equals(newValue)) {
                        _isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("Exception", "Please contact support");
                }
            }
        });
        patientRecordIntolerance.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!oldValue.equals(newValue)) {
                        _isFormEdited = true;
                        System.out.println("Intolerances changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("Exception", "Please contact support");
                }
            }
        });
        patientRecordChildhood.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!oldValue.equals(newValue)) {
                        _isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("Exception", "Please contact support");
                }
            }
        });

        // textfield limiter
        addTextLimiter(patientRecordLastname, 50);
        addTextLimiter(patientRecordFirstname, 50);
        addTextLimiter(patientRecordSVN, 10);
        addTextLimiter(patientRecordStreet, 255);
        addTextLimiter(patientRecordPLZ, 20);
        addTextLimiter(patientRecordCity, 50);
        addTextLimiter(patientRecordCountryIsoCode, 2);
        addTextLimiter(patientRecordPhone, 50);
        addTextLimiter(patientRecordEmail, 255);

        medicalHistory.setExpandedPane(mh4);

        Collection<IPrescription> notPrintedprescriptionsList = null;
        try {
            _prescriptionController = PrescriptionController.createController(_initpatient);
            notPrintedprescriptionsList = _prescriptionController.getNotPrintedPrescriptions(_initpatient);
        } catch (CouldNotGetPrescriptionException couldNotGetPrescriptionException) {
            couldNotGetPrescriptionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotGetPrescriptionException", "Please contact support");
        } catch (NotInitiatedExceptions notInitiatedExceptions) {
            notInitiatedExceptions.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact support");
        } catch (NoPatientException noPatientException) {
            noPatientException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NoPatientException", "Please contact support");
        }

        if(notPrintedprescriptionsList.isEmpty()){
            notPrintedPrescriptions.setDisable(true);

        }
    }

    /**
     * save Changes in Patient Record Form if isFormEdited == true
     */
    private void saveChanges()
    {
        if(patientRecordradioGenderFemale.isSelected())
        {
            _model.getPatient().setGender(Gender.Female);
        }else{
            _model.getPatient().setGender(Gender.Male);
        }
        if(patientRecordLastname.getText()!=null) {
            _model.getPatient().setLastName(patientRecordLastname.getText());
        }
        if(patientRecordFirstname.getText()!=null) {
            _model.getPatient().setFirstName(patientRecordFirstname.getText());
        }
        if(patientRecordSVN.getText()!=null) {
            _model.getPatient().setSocialInsuranceNr(patientRecordSVN.getText());
        }
        if(patientRecordBday.getValue()!=null) {
            LocalDate localDate = patientRecordBday.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date bday = Date.from(instant);
            _model.getPatient().setBirthDay(bday);
        }
        if(patientRecordStreet.getText()!=null) {
            _model.getPatient().setStreet(patientRecordStreet.getText());
        }
        if(patientRecordPLZ.getText()!=null) {
            _model.getPatient().setPostalCode(patientRecordPLZ.getText());
        }
        if(patientRecordCity.getText()!=null) {
            _model.getPatient().setCity(patientRecordCity.getText());
        }
        if(patientRecordCountryIsoCode.getText()!=null) {
            _model.getPatient().setCountryIsoCode(patientRecordCountryIsoCode.getText());
        }
        if(patientRecordPhone.getText()!=null){
            _model.getPatient().setPhone(patientRecordPhone.getText());
        }
        if(patientRecordEmail.getText()!=null) {
            _model.getPatient().setEmail(patientRecordEmail.getText());
        }
        if(patientRecordDoctor.getValue()!=null) {
            _model.getPatient().setIDoctor(patientRecordDoctor.getValue());
        }
        if(patientRecordAllergies.getText()!=null) {
            _model.getPatient().setAllergy(patientRecordAllergies.getText());
        }
        if(patientRecordIntolerance.getText()!=null) {
            _model.getPatient().setMedicineIntolerance(patientRecordIntolerance.getText());
        }
        if(patientRecordChildhood.getText()!=null) {
            _model.getPatient().setChildhoodAilments(patientRecordChildhood.getText());
        }

        disableFields();

        try {
            _model.getPatientModel().savePatient(_model.getPatient());
        } catch (RequirementsNotMetException requirementsNotMetException) {
            requirementsNotMetException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("RequirementsNotMetException", "Please contact support");
        } catch (PatientCouldNotBeSavedException patientCouldNotBeSavedException) {
            patientCouldNotBeSavedException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("PatientCouldNotBeSavedException", "Please contact support");
        } catch (BadConnectionException badConnectionException) {
            badConnectionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Please contact support");
        } catch (CriticalDatabaseException criticalDatabaseException) {
            criticalDatabaseException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CriticalDatabaseException", "Please contact support");
        } catch (CriticalClassException criticalClassException) {
            criticalClassException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CriticalClassException", "Please contact support");
        }
        StatusBarController.getInstance().setText("Changes saved...");
        _isFormEdited = false;


    }

    // *****************************************************************************************************************
    //
    // CONTROLS HANDLING
    //
    // *****************************************************************************************************************

    /**
     * add textlimiter to textfield
     *
     * @param tf    set textfield
     * @param maxLength max length of input chars
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        try {
            tf.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                    if (tf.getText().length() > maxLength) {
                        String s = tf.getText().substring(0, maxLength);
                        tf.setText(s);
                    }
                }
            });
        } catch(NullPointerException e) {
            //eat up
        }

    }

    /* add change listener to inputfields */
    private void addListener(TextField textfield)
    {
        textfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        _isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showErrorDialog("Exception", "Please contact support");
                }
            }
        });
    }

    private void disableFields() {

        //disables every field, radiobutton or choicebox
        patientRecordradioGenderMale.setDisable(true);
        patientRecordradioGenderFemale.setDisable(true);
        patientRecordLastname.setDisable(true);
        patientRecordFirstname.setDisable(true);
        patientRecordSVN.setDisable(true);
        patientRecordBday.setDisable(true);
        patientRecordStreet.setDisable(true);
        patientRecordPLZ.setDisable(true);
        patientRecordCity.setDisable(true);
        patientRecordCountryIsoCode.setDisable(true);
        patientRecordPhone.setDisable(true);
        patientRecordEmail.setDisable(true);
        patientRecordDoctor.setDisable(true);
        patientRecordAllergies.setDisable(true);
        patientRecordIntolerance.setDisable(true);
        patientRecordChildhood.setDisable(true);
    }

    private void enableFields() {
        patientRecordradioGenderMale.setDisable(false);
        patientRecordradioGenderFemale.setDisable(false);
        patientRecordLastname.setDisable(false);
        patientRecordLastname.setEditable(true);
        patientRecordFirstname.setDisable(false);
        patientRecordFirstname.setEditable(true);
        patientRecordSVN.setDisable(false);
        patientRecordSVN.setEditable(true);
        patientRecordBday.setDisable(false);
        patientRecordBday.setEditable(true);
        patientRecordStreet.setDisable(false);
        patientRecordStreet.setEditable(true);
        patientRecordPLZ.setDisable(false);
        patientRecordPLZ.setEditable(true);
        patientRecordCity.setDisable(false);
        patientRecordCity.setEditable(true);
        patientRecordCountryIsoCode.setDisable(false);
        patientRecordCountryIsoCode.setEditable(true);
        patientRecordPhone.setDisable(false);
        patientRecordPhone.setEditable(true);
        patientRecordEmail.setDisable(false);
        patientRecordEmail.setEditable(true);
        patientRecordDoctor.setDisable(false);
        patientRecordAllergies.setDisable(false);
        patientRecordAllergies.setEditable(true);
        patientRecordIntolerance.setDisable(false);
        patientRecordIntolerance.setEditable(true);
        patientRecordChildhood.setDisable(false);
        patientRecordChildhood.setEditable(true);
        patientRecordSaveButton.setDisable(false);
    }

    // *****************************************************************************************************************
    //
    // BUTTON HANDLERS
    //
    // *****************************************************************************************************************

    @FXML
    public void openPrescriptionsToPrintButtonHandler(ActionEvent actionEvent) {

        if(_initpatient.getBirthDay() != null){
            _patientRecordModel.openPrescriptionsToPrint(_prescriptionController, _initpatient);
        }else{
            DialogBoxController.getInstance().showInformationDialog("Cannot print Prescription", "Please make sure that the Patient has a Birth date set");
        }
    }

    @FXML
    public void addPatientToQueueButtonHandler()
    {
        if(addToQueueBox.getSelectionModel().getSelectedItem() != null)
        {
            IUser user = addToQueueBox.getSelectionModel().getSelectedItem();
            _model.getQueueModel().insertPatientIntoQueue(user);
            StatusBarController.getInstance().setText("Added _patient '" + _model.getPatient().getFirstName() + " " + _model.getPatient().getLastName() + "' to queue for: " + user.getLastName());
            addPatientToQueueButton.setDisable(true);
            addToQueueBox.setDisable(true);
        }else{
            DialogBoxController.getInstance().showInformationDialog("Information", "Please choose a Waiting List");
        }
    }

    @FXML
    public void openExaminationButtonHandler(ActionEvent actionEvent) {
        _model.getTabModel().addExaminationTab(_model.getPatient());
    }

    @FXML
    public void editButtonHandler(ActionEvent actionEvent)
    {
        enableFields();
    }

    @FXML
    public void saveButtonHandler(ActionEvent actionEvent) {

        if (patientRecordFirstname.getLength() == 0 || patientRecordLastname.getLength() == 0 || patientRecordSVN.getLength() == 0 || patientRecordBday == null){
            DialogBoxController.getInstance().showInformationDialog("Missing _patient data requirements", "Please fill the following fields: Firstname / Lastname / SVN / Birthdate");
        } else {
            if (_isFormEdited) {
                saveChanges();
            } else {
                DialogBoxController.getInstance().showInformationDialog("Information", "No changes detected");
            }
            disableFields();
        }
    }

    // *****************************************************************************************************************
    //
    // LOADING THREAD
    //
    // *****************************************************************************************************************

    /* thread: load examination protocols */
    public Task<Void> loadPatientDataThread() {return new Task<Void>() {
        @Override
        protected Void call() {

            patientRecordDoctor.setItems(FXCollections.observableArrayList(_model.getAllDoctors()));
            try {
                _calendareventlist = FXCollections.observableArrayList(_initpatient.getCalendarEvents());

            } catch (CouldNotGetCalendarEventsException couldNotGetCalendarEventsException) {
                couldNotGetCalendarEventsException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("CouldNotGetCalendarEventsException", "Please contact support");
            }
            addToQueueBox.setItems(FXCollections.observableArrayList(_model.getAllDoctorsAndOrthoptists()));
            try {
                _diagnosislist = FXCollections.observableArrayList(_initpatient.getDiagnoses());

            } catch (CouldNotGetDiagnoseException couldNotGetDiagnoseException) {
                couldNotGetDiagnoseException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("CouldNotGetDiagnoseException", "Please contact support");
            }

           /* try {
                _prescriptionController = PrescriptionController.createController(_initpatient);
            } catch (NoPatientException noPatientException) {
                noPatientException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NoPatientException", "Please contact support");
            } catch (NotInitiatedExceptions notInitatedExceptions) {
                notInitatedExceptions.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact support");
            }*/
           /* try {
                notPrintedPrescriptionsEntries = FXCollections.observableArrayList();
                _prescriptionController = PrescriptionController.createController(initpatient);
                notPrintedPrescriptionsEntries.addAll(_prescriptionController.getNotPrintedPrescriptions(initpatient));
            } catch (CouldNotGetPrescriptionException e) {
                e.printStackTrace();
            } catch (NotInitiatedExceptions notInitatedExceptions) {
                notInitatedExceptions.printStackTrace();
            } catch (NoPatientException e) {
                e.printStackTrace();
            }

            try {
                notPrintedVisualAidEntries = FXCollections.observableArrayList();
                Collection<IDiagnosis> diagnosises = initpatient.getDiagnoses();
                IDiagnosis patientsDiagnose = diagnosises.iterator().next();
                _visualAidController = VisualAidController.createController(patientsDiagnose);
                notPrintedVisualAidEntries.addAll(_visualAidController.getNotPrintedPrescriptions(initpatient));
            } catch (CouldNotGetDiagnoseException e) {
                e.printStackTrace();
            } catch (NotInitiatedExceptions notInitatedExceptions) {
                notInitatedExceptions.printStackTrace();
            } catch (NoPatientException e) {
                e.printStackTrace();
            } catch (CouldNotGetVisualAidException e) {
                e.printStackTrace();
            }*/

            return null;
        }
    };
    }

    private void loadPatientData(){

        // setup task: load patient data
        patientRecordAppointmentList.setDisable(true);
        patientRecordListDiagnoses.setDisable(true);
        String loadtext = "Loading...";
        patientRecordListDiagnoses.getItems().add(loadtext);
        patientRecordAppointmentList.getItems().add(loadtext);

        final Task<Void> loaddata = loadPatientDataThread();

        StatusBarController.showProgressBarIdle("Loading patient data");

        // setup: when task is done then refresh all controls
        loaddata.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                patientRecordAppointmentList.setItems(_calendareventlist);
                patientRecordListDiagnoses.setItems(_diagnosislist);
                patientRecordAppointmentList.setDisable(false);
                patientRecordListDiagnoses.setDisable(false);
                StatusBarController.hideStatusBarProgressBarIdle();

/*                if(!notPrintedPrescriptionsEntries.isEmpty() || !notPrintedvVisualAidMap.isEmpty()){
                    notPrintedPrescriptions.setVisible(true);
                }*/
            }
        });

        // start loading task
        new Thread(loaddata).start();
    }
}
