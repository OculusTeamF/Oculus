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

import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.presentation.view.models.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientRecordController implements Initializable {

    //<editor-fold desc="FXML Fields">
    @FXML public TextField patientRecordLastname;
    @FXML public TextField patientRecordFirstname;
    @FXML public TextField patientRecordSVN;
    @FXML public TextField patientRecordCountryIsoCode;
    @FXML public TextField patientRecordPhone;
    @FXML public TextField patientRecordEmail;
    @FXML public TextField patientRecordPLZ;
    @FXML public TextField patientRecordStreet;
    @FXML public TextField patientRecordCity;
    @FXML public ComboBox<IDoctor> patientRecordDoctor;
    @FXML public RadioButton patientRecordradioGenderFemale;
    @FXML public RadioButton patientRecordradioGenderMale;
    @FXML public TextArea patientRecordAllergies;
    @FXML public TextArea patientRecordIntolerance;
    @FXML public TextArea patientRecordChildhood;
    @FXML public DatePicker patientRecordBday;
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
    //</editor-fold>


    private Model _model = Model.getInstance();
    private boolean isFormEdited = false;
    private ToggleGroup group = new ToggleGroup();


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        /**
         * if changes are detected in patientform, then the tab cannot be closed without answer the dialogbox
         * if you press no to "Do you want to save changes?" Tab is closing without saving changes.
         */
        patientRecordAllergies.setWrapText(true);
        patientRecordChildhood.setWrapText(true);
        patientRecordIntolerance.setWrapText(true);


        // button images
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        patientRecordSaveButton.setGraphic(new ImageView(imageSaveIcon));
        Image imageEditIcon = new Image(getClass().getResourceAsStream("/res/icon_edit.png"));
        patientRecordEditButton.setGraphic(new ImageView(imageEditIcon));
        Image imageQueueIcon = new Image(getClass().getResourceAsStream("/res/icon_enqueue.png"));
        addPatientToQueueButton.setGraphic(new ImageView(imageQueueIcon));
        Image imageExaminationIcon = new Image(getClass().getResourceAsStream("/res/icon_examination.png"));
        examinationProtocolButton.setGraphic(new ImageView(imageExaminationIcon));

        patientRecordDoctor.setItems(FXCollections.observableArrayList(_model.getAllDoctors()));
        patientRecordAppointmentList.setItems(FXCollections.observableArrayList(_model.getCalendarEvents()));

        if (_model.getAllDiagnoses() != null) {
            patientRecordListDiagnoses.setDisable(false);
            try {
                // TODO diagnoses not shown with toString
                ObservableList<IDiagnosis> diagnosislist = FXCollections.observableArrayList(_model.getPatient().getDiagnoses());
                patientRecordListDiagnoses.setItems(diagnosislist);
            } catch (CouldNotGetDiagnoseException e) {
                e.printStackTrace();
            }
        } else {
            patientRecordListDiagnoses.setDisable(true);
        }


        //<editor-fold desc="Set Default Values">
        //disable all Patientfields and the save button, sets the text from IPatient into fields
        patientRecordSaveButton.setDisable(true);

        patientRecordradioGenderFemale.setToggleGroup(group);
        patientRecordradioGenderMale.setToggleGroup(group);

        patientRecordLastname.setText(_model.getPatient().getLastName());
        patientRecordFirstname.setText(_model.getPatient().getFirstName());

        if(_model.getPatient().getGender().equals("female"))
        {
            patientRecordradioGenderFemale.setSelected(true);
        }else{
            patientRecordradioGenderMale.setSelected(true);
        }

        patientRecordSVN.setText(_model.getPatient().getSocialInsuranceNr());


        if(_model.getPatient().getBirthDay() != null)
        {
            Date input = _model.getPatient().getBirthDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            patientRecordBday.setValue(date);
        }
        patientRecordStreet.setText(_model.getPatient().getStreet());
        patientRecordPLZ.setText(_model.getPatient().getPostalCode());
        patientRecordCity.setText(_model.getPatient().getCity());
        patientRecordCountryIsoCode.setText(_model.getPatient().getCountryIsoCode());
        patientRecordPhone.setText(_model.getPatient().getPhone());
        patientRecordEmail.setText(_model.getPatient().getEmail());
        patientRecordDoctor.setValue(_model.getPatient().getIDoctor());

        disableFields();

        if(_model.getPatient().getAllergy() == null || _model.getPatient().getAllergy().length() < 1)
        {
            patientRecordAllergies.setText("No Allergies known");
        }else{
            patientRecordAllergies.setText(_model.getPatient().getAllergy());
        }
        if(_model.getPatient().getMedicineIntolerance() == null || _model.getPatient().getMedicineIntolerance().length() < 1)
        {
            patientRecordIntolerance.setText("No Intolerance known");
        }else{
            patientRecordIntolerance.setText(_model.getPatient().getMedicineIntolerance());
        }
        if(_model.getPatient().getChildhoodAilments() == null || _model.getPatient().getChildhoodAilments().length() < 1)
        {
            patientRecordChildhood.setText("No childhood Ailments");
        }else{
            patientRecordChildhood.setText(_model.getPatient().getChildhoodAilments());
        }

        addToQueueBox.setItems(FXCollections.observableArrayList(_model.getAllDoctorsAndOrthoptists()));

        patientRecordAllergies.setDisable(false);
        patientRecordIntolerance.setDisable(false);
        patientRecordChildhood.setDisable(false);
        patientRecordAllergies.setEditable(false);
        patientRecordIntolerance.setEditable(false);
        patientRecordChildhood.setEditable(false);

        addToQueueBox.setValue(_model.getPatient().getIDoctor());

        //</editor-fold>

        //<editor-fold desc="Set Field Listener">
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
                        isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Birthday");
                }
            }
        });

        patientRecordDoctor.valueProperty().addListener(new ChangeListener<IDoctor>() {
            @Override
            public void changed(ObservableValue<? extends IDoctor> observable, IDoctor oldValue, IDoctor newValue) {
                isFormEdited = true;
            }
        });
        patientRecordAllergies.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textarea Allergies");
                }
            }
        });
        patientRecordIntolerance.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Intolerances changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textarea Intolerances");
                }
            }
        });
        patientRecordChildhood.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textarea Childhood");
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

    }

    /* add change listener to inputfields */
    private void addListener(TextField textfield){
        textfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Lastname");
                }
            }
        });
    }

    //<editor-fold desc="Event Handler">
    /**
     * when press edit button all patientfield are disable(false) and editable
     * savebutton is disable(false)
     * @param actionEvent
     */
    @FXML
    public void editButtonHandler(ActionEvent actionEvent)
    {
        enableFields();
    }

    /**
     * saves the changes in the _patient record after press Button 'Save'
     */
    @FXML
    public void saveButtonHandler(ActionEvent actionEvent) {

        if (patientRecordFirstname.getLength() == 0 || patientRecordLastname.getLength() == 0 || patientRecordSVN.getLength() == 0 || patientRecordBday == null){
            DialogBoxController.getInstance().showInformationDialog("Missing _patient data requirements", "Please fill the following fields: Firstname / Lastname / SVN / Birthdate");
        } else {
            if (isFormEdited) {
                saveChanges();
            } else {
                DialogBoxController.getInstance().showInformationDialog("Information", "No changes detected");
            }
            disableFields();
        }
    }

    @FXML
    public void addPatientToQueueButtonHandler(){
        if(addToQueueBox.getSelectionModel().getSelectedItem() != null){
            IUser user = addToQueueBox.getSelectionModel().getSelectedItem();
            _model.getQueueModel().insertPatientIntoQueue(user);
            StatusBarController.getInstance().setText("Added _patient '" + _model.getPatient().getFirstName() + " " + _model.getPatient().getLastName() + "' to queue for: " + user.getLastName());
            addPatientToQueueButton.setDisable(true);
            addToQueueBox.setDisable(true);
        }else{
            DialogBoxController.getInstance().showInformationDialog("Information", "Please choose a Waiting List");
        }
    }

    /**
     * opens a new Examination protocol
     * @param actionEvent
     */
    @FXML
    public void openExaminationButtonHandler(ActionEvent actionEvent) {
        _model.getTabModel().addExaminationTab(_model.getPatient());
    }

    /**
     * save Changes in Patient Record Form if isFormEdited == true
     */
    private void saveChanges()
    {
        if(patientRecordradioGenderFemale.isSelected())
        {
            _model.getPatient().setGender("female");
        }else{
            _model.getPatient().setGender("male");
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
            Date bday = java.sql.Date.from(instant);
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

        //createPatientController.saveIPatient(_model.getPatient());
        _model.getPatientModel().savePatient(_model.getPatient());
        StatusBarController.getInstance().setText("Changes saved...");
        isFormEdited = false;


    }

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

    private void disableFields() {
        /*for (Node node : patientRecordPane.getChildren()) {
            if (node instanceof TextField) {
                node.setDisable(true);
                ((TextField)node).setEditable(false);
            }
        }*/

        //disables every field, radiobutton or choicebox
        patientRecordradioGenderMale.setDisable(true);
        patientRecordradioGenderFemale.setDisable(true);
        patientRecordLastname.setDisable(true);
        patientRecordLastname.setEditable(false);
        patientRecordFirstname.setDisable(true);
        patientRecordFirstname.setEditable(false);
        patientRecordSVN.setDisable(true);
        patientRecordSVN.setEditable(false);
        patientRecordBday.setDisable(true);
        patientRecordBday.setEditable(false);
        patientRecordStreet.setDisable(true);
        patientRecordStreet.setEditable(false);
        patientRecordPLZ.setDisable(true);
        patientRecordPLZ.setEditable(false);
        patientRecordCity.setDisable(true);
        patientRecordCity.setEditable(false);
        patientRecordCountryIsoCode.setDisable(true);
        patientRecordCountryIsoCode.setEditable(false);
        patientRecordPhone.setDisable(true);
        patientRecordPhone.setEditable(false);
        patientRecordEmail.setDisable(true);
        patientRecordEmail.setEditable(false);
        patientRecordDoctor.setDisable(true);
        patientRecordAllergies.setDisable(true);
        patientRecordAllergies.setEditable(false);
        patientRecordIntolerance.setDisable(true);
        patientRecordIntolerance.setEditable(false);
        patientRecordChildhood.setDisable(true);
        patientRecordChildhood.setEditable(false);
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
}
