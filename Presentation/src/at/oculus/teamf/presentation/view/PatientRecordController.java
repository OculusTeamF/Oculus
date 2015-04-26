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

import at.oculus.teamf.application.facade.CheckinController;
import at.oculus.teamf.application.facade.CreatePatientController;
import at.oculus.teamf.application.facade.ReceivePatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.presentation.view.resourcebundel.SingleResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

public class PatientRecordController implements Initializable {


    @FXML public TextField patientRecordLastname;
    @FXML public TextField patientRecordFirstname;
    @FXML public TextField patientRecordSVN;
    @FXML public TextField patientRecordCountryIsoCode;
    @FXML public TextField patientRecordPhone;
    @FXML public TextField patientRecordEmail;
    @FXML public TextField patientRecordPLZ;
    @FXML public TextField patientRecordStreet;
    @FXML public ComboBox<IDoctor> patientRecordDoctor;
    @FXML public RadioButton patientRecordradioGenderFemale;
    @FXML public RadioButton patientRecordradioGenderMale;
    @FXML public ListView patientRecordAppointmentList;
    @FXML public Button patientRecordSaveButton;
    @FXML public Button patientRecordEditButton;
    @FXML public TextField patientRecordCity;
    @FXML public TextArea patientRecordAllergies;
    @FXML public Tab patientRecordTab;
    @FXML public TextArea patientRecordIntolerance;
    @FXML public TextArea patientRecordChildhood;
    @FXML public DatePicker patientRecordBday;
    @FXML public ComboBox<IUser> addToQueueBox;
    @FXML public Button addPatientToQueueButton;
    @FXML public Button examinationProtocolButton;

    private boolean isFormEdited = false;
    private ToggleGroup group = new ToggleGroup();
    private IPatient patient;
    private StartupController startupController = new StartupController();
    private CreatePatientController createPatientController = new CreatePatientController();
    public CheckinController checkinController = new CheckinController();
    private ReceivePatientController receivePatientController = new ReceivePatientController();
    private IUser _user = null;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        patient =  (IPatient)resources.getObject(null);
        Integer userID = Main.controller.userID;

        /**
         * to get the IUser from userID
         */
        try {
            Collection<IUser> users = startupController.getAllDoctorsAndOrthoptists();
            for(IUser user : users){
                if(user.getUserId() == userID) {
                    _user = user;
                }
            }
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        /**
         * if changes are detected in patientform, then the tab cannot be closed without answer the dialogbox
         * if you press no to "Do you want to save changes?" Tab is closing without saving changes.
         */
        patientRecordAllergies.setWrapText(true);
        patientRecordChildhood.setWrapText(true);
        patientRecordIntolerance.setWrapText(true);

        /**
         * if changes are detected in patientform, then the tab cannot be closed without answer the dialogbox
         * if you press no to "Do you want to save changes?" Tab is closing without saving changes.
         */
        patientRecordTab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (isFormEdited) {
                    if (DialogBoxController.getInstance().showYesNoDialog("Save patient", "Do you want to save changes?") == true){
                        saveChanges();
                    } else{
                        isFormEdited = false;
                    }
                }
            }
        });

        try {
            patientRecordDoctor.setItems(FXCollections.observableArrayList(startupController.getAllDoctors()));
        } catch (FacadeException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        }

        //disable all Patientfields and the save button, sets the text from IPatient into fields
        patientRecordSaveButton.setDisable(true);

        patientRecordradioGenderFemale.setToggleGroup(group);
        patientRecordradioGenderMale.setToggleGroup(group);

        patientRecordLastname.setText(patient.getLastName());
        patientRecordLastname.setDisable(true);
        patientRecordFirstname.setText(patient.getFirstName());
        patientRecordFirstname.setDisable(true);
        addTextLimiter(patientRecordLastname, 50);
        addTextLimiter(patientRecordFirstname, 50);

        if(patient.getGender().equals("female"))
        {
            patientRecordradioGenderFemale.setSelected(true);
            patientRecordradioGenderFemale.setDisable(true);
        }else{
            patientRecordradioGenderMale.setSelected(true);
            patientRecordradioGenderMale.setDisable(true);
        }

        patientRecordSVN.setText(patient.getSocialInsuranceNr());
        patientRecordSVN.setDisable(true);
        addTextLimiter(patientRecordSVN, 10);

        if(patient.getBirthDay() != null)
        {
            Date input = patient.getBirthDay();
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));

            patientRecordBday.setValue(date);
        }
        patientRecordBday.setDisable(true);
        patientRecordStreet.setText(patient.getStreet());
        patientRecordStreet.setDisable(true);
        addTextLimiter(patientRecordStreet, 255);
        patientRecordPLZ.setText(patient.getPostalCode());
        patientRecordPLZ.setDisable(true);
        addTextLimiter(patientRecordPLZ, 20);
        patientRecordCity.setText(patient.getCity());
        patientRecordCity.setDisable(true);
        addTextLimiter(patientRecordCity, 50);
        patientRecordCountryIsoCode.setText(patient.getCountryIsoCode());
        patientRecordCountryIsoCode.setDisable(true);
        addTextLimiter(patientRecordCountryIsoCode, 2);
        patientRecordPhone.setText(patient.getPhone());
        patientRecordPhone.setDisable(true);
        addTextLimiter(patientRecordPhone, 50);
        patientRecordEmail.setText(patient.getEmail());
        patientRecordEmail.setDisable(true);
        addTextLimiter(patientRecordEmail, 255);
        patientRecordDoctor.setValue(patient.getIDoctor());
        patientRecordDoctor.setDisable(true);

        try {
            patientRecordAppointmentList.setItems(FXCollections.observableArrayList(patient.getCalendarEvents()));
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        if(patient.getAllergy() == null || patient.getAllergy().length() < 1)
        {
            patientRecordAllergies.setText("No Allergies known");
        }else{
            patientRecordAllergies.setText(patient.getAllergy());
        }
        if(patient.getMedicineIntolerance() == null || patient.getMedicineIntolerance().length() < 1)
        {
            patientRecordIntolerance.setText("No Intolerance known");
        }else{
            patientRecordIntolerance.setText(patient.getMedicineIntolerance());
        }
        if(patient.getChildhoodAilments() == null || patient.getChildhoodAilments().length() < 1)
        {
            patientRecordChildhood.setText("No childhood Ailments");
        }else{
            patientRecordChildhood.setText(patient.getChildhoodAilments());
        }
        try {
            addToQueueBox.setItems(FXCollections.observableArrayList(startupController.getAllDoctorsAndOrthoptists()));
        } catch (FacadeException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        }
        patientRecordAllergies.setDisable(false);
        patientRecordIntolerance.setDisable(false);
        patientRecordChildhood.setDisable(false);

        patientRecordAllergies.setEditable(false);
        patientRecordIntolerance.setEditable(false);
        patientRecordChildhood.setEditable(false);

        if(_user != null){
            addToQueueBox.setValue(_user);
        }else if(patient.getIDoctor() != null){
            addToQueueBox.setValue(patient.getIDoctor());
        }

        //check if something has changed, if changes detected --> saveChanges()
        patientRecordLastname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Lastname changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Lastname");
                }
            }
        });
        patientRecordFirstname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Firstname changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Firstname");
                }
            }
        });
        patientRecordSVN.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("SVN changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield SVN");
                }
            }
        });
        patientRecordBday.accessibleTextProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Bday changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Birthday");
                }
            }
        });
        patientRecordStreet.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Street changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Street");
                }
            }
        });
        patientRecordPLZ.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("PLZ changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Postal code");
                }
            }
        });
        patientRecordCity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("City changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield City");
                }
            }
        });
        patientRecordCountryIsoCode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Countryisocode changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield country iso code");
                }
            }
        });
        patientRecordPhone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Phone changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Phone");
                }
            }
        });
        patientRecordEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (oldValue != newValue) {
                        isFormEdited = true;
                        System.out.println("Email changed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogBoxController.getInstance().showExceptionDialog(e, "Exception - Problems with detecting changes in Textfield Email");
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
    }

    /**
     * when press edit button all patientfield are disable(false) and editable
     * savebutton is disable(false)
     * @param actionEvent
     */
    @FXML
    public void editForm(ActionEvent actionEvent)
    {
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

    /**
     * saves the changes in the patient record after press Button 'Save'
     */
    @FXML
    public void saveChangedForm(ActionEvent actionEvent)
    {
        if(isFormEdited){
            saveChanges();
        }else{
            DialogBoxController.getInstance().showInformationDialog("Information", "No changes detected");
        }
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

    @FXML
    public void addPatientToQueue(){

        if(addToQueueBox.getSelectionModel().getSelectedItem() != null){
            try {
                /*Task<Void> task = new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        done();
                        return null;
                    }
                };
                StatusBarController.getInstance().progressProperty().bind(task.progressProperty());*/

                DialogBoxController.getInstance().showInformationDialog("Adding patient...." , "Adding to waiting list: " + System.getProperty("line.separator")
                        + patient.getFirstName() + " " + patient.getLastName() + System.getProperty("line.separator")
                        + "To queue:" + System.getProperty("line.separator")+ _user.getFirstName() + " " + _user.getLastName()  + System.getProperty("line.separator")
                        + System.getProperty("line.separator") + "Please wait");
                _user = addToQueueBox.getSelectionModel().getSelectedItem();
                StatusBarController.getInstance().setText("Adding patient '" + patient.getFirstName() + " " + patient.getLastName() + "' to queue for: " + _user.getLastName());
                checkinController.insertPatientIntoQueue(patient, _user);
                IPatientQueue queue = startupController.getQueueByUserId(_user);
                Main.controller.refreshQueue(queue, _user);
            //TODO saubere Exceptions!
            } catch (BadConnectionException e) {
                e.printStackTrace();
                //DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
                DialogBoxController.getInstance().showInformationDialog("Error", "Patient already in Waitinglist.");
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
            } catch (CheckinControllerException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "CheckinControllerException - Please contact support");
            }
        }else{
            DialogBoxController.getInstance().showInformationDialog("Information", "Please choose a Waitinglist");
        }
    }

    /**
     * save Changes in Patient Record Form if isFormEdited == true
     */
    private void saveChanges()
    {
        if(patientRecordradioGenderFemale.isSelected())
        {
            patient.setGender("female");
        }else{
            patient.setGender("male");
        }
        if(patientRecordLastname.getText()!=null) {
            patient.setLastName(patientRecordLastname.getText());
        }
        if(patientRecordFirstname.getText()!=null) {
            patient.setFirstName(patientRecordFirstname.getText());
        }
        if(patientRecordSVN.getText()!=null) {
            patient.setSocialInsuranceNr(patientRecordSVN.getText());
        }
        if(patientRecordBday.getValue()!=null) {
            LocalDate localDate = patientRecordBday.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date bday = java.sql.Date.from(instant);
            patient.setBirthDay(bday);
        }
        if(patientRecordStreet.getText()!=null) {
            patient.setStreet(patientRecordStreet.getText());
        }
        if(patientRecordPLZ.getText()!=null) {
            patient.setPostalCode(patientRecordPLZ.getText());
        }
        if(patientRecordCity.getText()!=null) {
            patient.setCity(patientRecordCity.getText());
        }
        if(patientRecordCountryIsoCode.getText()!=null) {
            patient.setCountryIsoCode(patientRecordCountryIsoCode.getText());
        }
        if(patientRecordPhone.getText()!=null){
            patient.setPhone(patientRecordPhone.getText());
        }
        if(patientRecordEmail.getText()!=null) {
            patient.setEmail(patientRecordEmail.getText());
        }
        if(patientRecordDoctor.getValue()!=null) {
            patient.setIDoctor(patientRecordDoctor.getValue());
        }
        if(patientRecordAllergies.getText()!=null) {
            patient.setAllergy(patientRecordAllergies.getText());
        }
        if(patientRecordIntolerance.getText()!=null) {
            patient.setMedicineIntolerance(patientRecordIntolerance.getText());
        }
        if(patientRecordChildhood.getText()!=null) {
            patient.setChildhoodAilments(patientRecordChildhood.getText());
        }

        try {
            createPatientController.saveIPatient(patient);
            //DialogBoxController.getInstance().showInformationDialog("Patient record edited", "Changes saved");
            StatusBarController.getInstance().setText("Changes saved...");
            isFormEdited = false;
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequrementsNotMetException - Please contact support");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        }

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

    /**
     * opens a new Examination protocol
     * @param actionEvent
     */
    public void openExamination(ActionEvent actionEvent) {

        if(addToQueueBox.getSelectionModel().getSelectedItem() != null) {
            try {
                Main.controller.getTabPane().getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/ExaminationTab.fxml"), new SingleResourceBundle(patient)));
                Main.controller.getTabPane().getSelectionModel().select(Main.controller.getTabPane().getTabs().size() - 1);
                StatusBarController.getInstance().setText("Open examination...");
            } catch (IOException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
            }
            try {
                IUser user = addToQueueBox.getSelectionModel().getSelectedItem();
                IPatientQueue patientqueue = startupController.getQueueByUserId(user);
                receivePatientController.removePatientFromQueue(patient, patientqueue);
                Main.controller.refreshQueue(patientqueue, user);
                DialogBoxController.getInstance().showInformationDialog("Information", patient.getLastName() + ", " + patient.getFirstName() + " removed from Waitinglist");
            } catch (BadConnectionException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
            } catch (InvalidSearchParameterException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "InvalidSearchParameterException - Please contact support");
            }
        }else{
            DialogBoxController.getInstance().showInformationDialog("Cannot open examination protocoll", "Please make sure, that the patient was in Waitinglist and a Doctor is selected");
        }
    }

    /**
     * add textlimiter to textfield
     *
     * @param tf    set textfield
     * @param maxLength max length of input chars
     */
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        //if (!tf.getText().isEmpty()) {
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
            //
        }
        //}

    }
}
