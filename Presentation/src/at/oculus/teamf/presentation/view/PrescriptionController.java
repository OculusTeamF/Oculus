/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.presentation.view.models.PrescriptionModel;
import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;
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
import javafx.scene.layout.StackPane;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Karo on 04.05.2015.
 */
public class PrescriptionController implements Initializable, IPrinter, ILogger {


    @FXML public ComboBox choosePrescriptionBox;
    @FXML public Button printButton;
    @FXML public Button saveButton;
    @FXML public StackPane prescriptionStackPane;
    @FXML public Label lastnameVA;
    @FXML public Label firstnameVA;
    @FXML public Label svnVA;
    @FXML public Label bdayVA;
    @FXML public Label addressVA;
    @FXML public Label stateVA;
    @FXML public Label zipVA;
    @FXML public Label cityVA;
    @FXML public ComboBox chooseMedicinBox;
    @FXML public Label lastname;
    @FXML public Label firstname;
    @FXML public Label svn;
    @FXML public Label bday;
    @FXML public Label address;
    @FXML public Label state;
    @FXML public Label zip;
    @FXML public Label city;
    @FXML public TextArea visualAidInformation;
    @FXML public ChoiceBox visualAidChoiceBox;
    @FXML public Button removeEntryFromTable;
    @FXML public Button addNewEntryToTable;
    @FXML public TextField dioptersRight;
    @FXML public TextField dioptersLeft;
    @FXML private ListView prescriptionItems;

    private Model _model = Model.getInstance();
    private ObservableList<String> _prescriptionType;
    private PrescriptionModel _prescriptionModel = PrescriptionModel.getInstance();
    private ObservableList<IMedicine> _medicinList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // load image resources for buttons
        Image imagePrintIcon = new Image(getClass().getResourceAsStream("/res/icon_print.png"));
        printButton.setGraphic(new ImageView(imagePrintIcon));


        _prescriptionType = FXCollections.observableArrayList("Medicine", "Visual Aid");
        choosePrescriptionBox.setItems(_prescriptionType);
        choosePrescriptionBox.getSelectionModel().select(0);

        //Stackpane on index 1 is the visualAidPrescription, on index 0 is the medicalPrescription
        prescriptionStackPane.getChildren().get(0).setVisible(false);
        prescriptionStackPane.getChildren().get(1).setVisible(true);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveButton.setGraphic(new ImageView(imageSaveIcon));

        //Medicin box
        String text = "choose medicine ...";
        chooseMedicinBox.setPromptText(text);


        //Prescription controller for this Patient
        try {
            _prescriptionModel.addNewPrescription(_model.getPatient());
        } catch (NotInitiatedExceptions notInitiatedExceptions) {
            notInitiatedExceptions.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact support");
        } catch (NoPatientException noPatientException) {
            noPatientException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NoPatientException", "Please contact support");
        }
        ObservableList<IMedicine> prescribedMedicin = FXCollections.observableArrayList((List)_prescriptionModel.getPrescribedMedicin());
        chooseMedicinBox.setItems(prescribedMedicin);

        //VisualAidChoiceBox
        ObservableList<String> visualAids = FXCollections.observableArrayList("Contact Lenses", "Glasses");
        visualAidChoiceBox.setItems(visualAids);

        //fill in data
        //Prescription
        lastname.setText(_model.getPatient().getLastName());
        firstname.setText(_model.getPatient().getFirstName());
        svn.setText(_model.getPatient().getSocialInsuranceNr());
        if (_model.getPatient().getBirthDay() != null) {
            bday.setText(_model.getPatient().getBirthDay().toString());
        }
        address.setText(_model.getPatient().getStreet());
        zip.setText(_model.getPatient().getPostalCode());
        city.setText(_model.getPatient().getCity());
        state.setText(_model.getPatient().getCountryIsoCode());
        //Visual Aid
        lastnameVA.setText(_model.getPatient().getLastName());
        firstnameVA.setText(_model.getPatient().getFirstName());
        svnVA.setText(_model.getPatient().getSocialInsuranceNr());
        if (_model.getPatient().getBirthDay() != null) {
            bdayVA.setText(_model.getPatient().getBirthDay().toString());
        }
        addressVA.setText(_model.getPatient().getStreet());
        zipVA.setText(_model.getPatient().getPostalCode());
        cityVA.setText(_model.getPatient().getCity());
        stateVA.setText(_model.getPatient().getCountryIsoCode());


        choosePrescriptionBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    switch (newValue.toString()) {
                        case "Medicin":
                            prescriptionStackPane.getChildren().get(0).setVisible(false);
                            prescriptionStackPane.getChildren().get(1).setVisible(true);
                            printButton.setVisible(true);
                            break;
                        case "Visual Aid":
                            prescriptionStackPane.getChildren().get(1).setVisible(false);
                            prescriptionStackPane.getChildren().get(0).setVisible(true);
                            printButton.setVisible(false);
                            break;
                    }

                }
            }
        });

        _medicinList = FXCollections.observableArrayList();

    }

    // *****************************************************************************************************************
    //
    // BUTTON HANDLERS
    //
    // *****************************************************************************************************************

    /**
     * Saves a prescription or visualAid without printing out
     */
    @FXML
    public void savePrescriptionButtonActionHandler(){

        IPatient patient = _model.getPatient();
        Collection<IMedicine> medicinList = new LinkedList<IMedicine>();
        Collection<IDiagnosis> allDiagnoses = new LinkedList<IDiagnosis>();


        if(choosePrescriptionBox.getSelectionModel().getSelectedItem().toString().equals("Medicine")) {

           ObservableList<IMedicine> prescribedMedicine = prescriptionItems.getItems();

            for(IMedicine med : prescribedMedicine){
                medicinList.add(med);
            }

            try {
                _prescriptionModel.addPrescriptionEntries(medicinList);
                printButton.setDisable(false);

                log.info("Medicine Prescription saved for " + patient.getLastName());
                StatusBarController.getInstance().setText("Medical Prescription saved for "+patient.getLastName()+", "+patient.getFirstName());
            } catch (NotInitiatedExceptions notInitiatedExceptions) {
                notInitiatedExceptions.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact Support ");
            } catch (DatabaseOperationException databaseOperationException) {
                databaseOperationException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("DatabaseOperationException", "Please contact Support ");
            } catch (BadConnectionException badConnectionException) {
                badConnectionException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Please contact Support ");
            } catch (CouldNotAddPrescriptionEntryException CouldNotAddPrescriptionEntryException) {
                CouldNotAddPrescriptionEntryException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("CouldNotAddPrescriptionEntryException", "Please contact Support ");
            } catch (NoBrokerMappedException noBrokerMappedException) {
                noBrokerMappedException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NoBrokerMappedException", "Please contact Support ");
            }
            saveButton.setDisable(true);


        }else if(choosePrescriptionBox.getSelectionModel().getSelectedItem().toString().equals("Visual Aid"))
        {
            try {
                allDiagnoses = _model.getPatient().getDiagnoses();

            } catch (CouldNotGetDiagnoseException couldNotGetDiagnoseException) {
                couldNotGetDiagnoseException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("CouldNotGetDiagnoseException", "Cannot Save Prescription - No Diagnose ");
            }
            IDiagnosis diagnosis = allDiagnoses.iterator().next();
            try {
                _prescriptionModel.addNewVisualAidPrescription(diagnosis);

            } catch (NoPatientException noPatientException) {
                noPatientException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NoPatientException", "Cannot Save Prescription - No Diagnose ");
            } catch (NotInitiatedExceptions notInitiatedExceptions) {
                notInitiatedExceptions.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Cannot Save Prescription - No Diagnose ");
            }
            try{
            _prescriptionModel.addVisualAidPrescriptionEntries(visualAidInformation.getText(),dioptersLeft.getText(), dioptersRight.getText());
            } catch (DatabaseOperationException databaseOperationException) {
                databaseOperationException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("DatabaseOperationException", "Cannot Save Prescription - No Diagnose ");
            } catch (NoBrokerMappedException noBrokerMappedException) {
                noBrokerMappedException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NoBrokerMappedException", "Cannot Save Prescription - No Diagnose ");
            } catch (BadConnectionException badConnectionException) {
                badConnectionException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Cannot Save Prescription - No Diagnose ");
            } catch (NotInitiatedExceptions notInitiatedExceptions) {
                notInitiatedExceptions.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Cannot Save Prescription - No Diagnose ");
            }

            saveButton.setDisable(true);
            log.info("Visual aid prescription saved for " + patient.getLastName());
            StatusBarController.getInstance().setText("Visual Aid Prescription saved for " + patient.getLastName() + ", " + patient.getFirstName());
        }
    }

    /**
     * prints out a medical Prescription
     */
    @FXML
    public void printPrescriptionButtonActionHandler() {

        try {
            _prescriptionModel.printPrescription();

        } catch (DatabaseOperationException databaseOperationException) {
            databaseOperationException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("DatabaseOperationException", "Please contact Support ");
        } catch (NoBrokerMappedException noBrokerMappedException) {
            noBrokerMappedException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NoBrokerMappedException", "Please contact Support ");
        } catch (BadConnectionException badConnectionException) {
            badConnectionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Please contact Support ");
        } catch (COSVisitorException cosVisitorException) {
            cosVisitorException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("COSVisitorException", "Please contact Support ");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("IOException", "Please contact Support ");
        } catch (NotInitiatedExceptions notInitiatedExceptions) {
            notInitiatedExceptions.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NotInitiatedExceptions", "Please contact Support ");
        } catch (NoPrescriptionToPrintException noPrescriptionToPrintException) {
            noPrescriptionToPrintException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("NoPrescriptionToPrintException", "Please contact Support ");
        } catch (CantGetPresciptionEntriesException cantGetPrescriptionEntriesException) {
            cantGetPrescriptionEntriesException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CantGetPresciptionEntriesException", "Please contact Support ");
        }
        saveButton.setDisable(true);
        log.info("Print Prescription");
        StatusBarController.getInstance().setText("Print Prescription...");
    }


    // *****************************************************************************************************************
    //
    // LISTVIEW METHODS
    //
    // *****************************************************************************************************************

    @FXML
    public void addNewPrescriptionEntryToTable(ActionEvent actionEvent) {

        IMedicine itemToAdd = (IMedicine) chooseMedicinBox.getSelectionModel().getSelectedItem();

        if(itemToAdd != null){
            _medicinList.add(itemToAdd);
            prescriptionItems.setItems(_medicinList);
            clearFields();
            log.info("Medicin: "+itemToAdd+" was added to the Prescription");
        }
    }

    //remove the Medicin from the PrescriptionList
    @FXML
    public void removeMedicinButtonActionHandler(ActionEvent actionEvent)
    {
        if (_medicinList.size() > 0 ) {
            IMedicine itemToRemove = (IMedicine) prescriptionItems.getSelectionModel().getSelectedItem();
            _medicinList.remove(itemToRemove);
            log.info("Medicin: " + itemToRemove + " was removed from the Prescription");
        }
    }

    @FXML
    public void clearFields() {

        chooseMedicinBox.getSelectionModel().clearSelection();
    }
}

