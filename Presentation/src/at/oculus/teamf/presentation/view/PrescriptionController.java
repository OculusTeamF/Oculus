/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitatedExceptions;
import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.presentation.view.models.PrescriptionModel;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

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
    @FXML public TextField medicinTextfield;
    @FXML public TextField dosageTextfield;
    @FXML public TextField informationTextfield;
    @FXML public Button removeEntryFromTable;
    @FXML public Button addNewEntryToTable;
    @FXML public TextField dioptersRight;
    @FXML public TextField dioptersLeft;
    @FXML private TableView prescriptionItems;
    @FXML private TableColumn medicamentationCol;
    @FXML private TableColumn dosageCol;
    @FXML private TableColumn Informationcol;

    private Model _model = Model.getInstance();
    private ObservableList<String> _prescriptionType;
    private PrescriptionModel _prescriptionModel = PrescriptionModel.getInstance();
    private ObservableList<MedicineTableEntry> _medicinList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // load image resources for buttons
        Image imagePrintIcon = new Image(getClass().getResourceAsStream("/res/icon_print.png"));
        printButton.setGraphic(new ImageView(imagePrintIcon));


        _prescriptionType = FXCollections.observableArrayList("Medicin", "Visual Aid");
        choosePrescriptionBox.setItems(_prescriptionType);
        choosePrescriptionBox.getSelectionModel().select(0);

        //Stackpane on index 1 is the visualAidPrescription, on index 0 is the medicalPrescription
        prescriptionStackPane.getChildren().get(0).setVisible(false);
        prescriptionStackPane.getChildren().get(1).setVisible(true);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveButton.setGraphic(new ImageView(imageSaveIcon));
        printButton.setDisable(true);
        printButton.setTooltip(new Tooltip("Save prescription before using print"));

        //Medicin box
        String text = "choose medicin ...";
        chooseMedicinBox.setPromptText(text);


        //Prescription controller for this Patient
        try {
            _prescriptionModel.addNewPrescription(_model.getPatient());
        } catch (NotInitatedExceptions notInitatedExceptions) {
            notInitatedExceptions.printStackTrace();
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

        //TableView
        medicamentationCol.setCellValueFactory(
                new PropertyValueFactory<MedicineTableEntry,String>("medicin")
        );
        dosageCol.setCellValueFactory(
                new PropertyValueFactory<MedicineTableEntry,String>("dosage")
        );
        Informationcol.setCellValueFactory(
                new PropertyValueFactory<MedicineTableEntry,String>("information")
        );

        _medicinList = FXCollections.observableArrayList();

        //medicinTextfield.setEditable(false);
    }

    // *****************************************************************************************************************
    //
    // BUTTON HANDLERS
    //
    // *****************************************************************************************************************

    /**
     * Saves a prescription or visualAid without printing out, and put it into the HashMap for not printed Prescriptions
     */
    @FXML
    public void savePrescriptionButtonActionHandler(){

        IPatient patient = _model.getPatient();
        Collection<IMedicine> medicinList = new LinkedList<IMedicine>();
        Collection<IDiagnosis> allDiagnoses = new LinkedList<IDiagnosis>();


        if(choosePrescriptionBox.getSelectionModel().getSelectedItem().toString().equals("Medicin")) {

            for (MedicineTableEntry med : _medicinList){
                med.getMedicin().setDose(med.getDosage());
                medicinList.add(med.getMedicin());
            }

            try {
                //_prescriptionModel.addNewPrescription(patient);
                _prescriptionModel.addPrescriptionEntries(medicinList);
                printButton.setDisable(false);
                log.info("Medicine Prescription saved for " + patient.getLastName());
            } catch (NotInitatedExceptions notInitatedExceptions) {
                notInitatedExceptions.printStackTrace();
                //Todo: handle
            }
        }else if(choosePrescriptionBox.getSelectionModel().getSelectedItem().toString().equals("Visual Aid"))
        {
            try {
                allDiagnoses = _model.getPatient().getDiagnoses();
            } catch (CouldNotGetDiagnoseException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showInformationDialog("CouldNotGetDiagnoseException", "Cannot Save Prescription - No Diagnose ");
            }

            IDiagnosis diagnosis = allDiagnoses.iterator().next();
            _prescriptionModel.addNewVisualAidPrescription(diagnosis);
            _prescriptionModel.addVisualAidPrescriptionEntries(visualAidInformation.getText(),dioptersLeft.getText(), dioptersRight.getText());
            printButton.setDisable(false);
            log.info("Visual aid prescription saved for " + patient.getLastName());
        }
    }


    @FXML
    public void printPrescriptionButtonActionHandler() {

        //TODO: print and save prescription
        _prescriptionModel.printPrescription();

        //save prescription
        IPatient patient = _model.getPatient();

        Collection<IMedicine> medicinList = new LinkedList<IMedicine>();

        try {
            //_prescriptionModel.addNewPrescription(patient);
            _prescriptionModel.addPrescriptionEntries(medicinList);
        } catch (NotInitatedExceptions notInitatedExceptions) {
            notInitatedExceptions.printStackTrace();
            //Todo: handle
        }
    }


    // *****************************************************************************************************************
    //
    // TABLEVIEW METHODS
    //
    // *****************************************************************************************************************

    @FXML
    public void addNewPrescriptionEntryToTable(ActionEvent actionEvent) {

        if (dosageTextfield.getText().length() > 0 && chooseMedicinBox.getSelectionModel().getSelectedItem() != null) {

            IMedicine itemToAdd = (IMedicine) chooseMedicinBox.getSelectionModel().getSelectedItem();

            MedicineTableEntry newEntry = new MedicineTableEntry(itemToAdd, dosageTextfield.getText(), informationTextfield.getText());
            _medicinList.add(newEntry);
            prescriptionItems.setItems(_medicinList);
            clearFields();
        }
    }

    //remove the Medicin from the PrescriptionList
    @FXML
    public void removeMedicinButtonActionHandler(ActionEvent actionEvent) {
        if (_medicinList.size() > 0 ) {
            MedicineTableEntry itemToRemove = (MedicineTableEntry) prescriptionItems.getSelectionModel().getSelectedItem();
            _medicinList.remove(itemToRemove);
        }
    }

    @FXML
    public void clearFields() {
        dosageTextfield.clear();
        informationTextfield.clear();
        chooseMedicinBox.getSelectionModel().clearSelection();
    }

    // *****************************************************************************************************************
    //
    // TABLEVIEW DATAENTRY BEAN
    //
    // *****************************************************************************************************************

    public static class MedicineTableEntry {
        private final ObjectProperty<IMedicine> medicin;
        private final SimpleStringProperty dosage;
        private final SimpleStringProperty information;

        public MedicineTableEntry(IMedicine medicin, String dosage, String information){
            this.medicin = new SimpleObjectProperty(medicin);
            this.dosage = new SimpleStringProperty(dosage);
            this.information = new SimpleStringProperty(information);
        }

        public IMedicine getMedicin() {
            return medicin.get();
        }
        public void setMedicin(IMedicine med) {
            medicin.set(med);
        }

        public String getDosage() {
            return dosage.get();
        }
        public void setDosage(String dos) {
            dosage.set(dos);
        }

        public String getInformation() {
            return information.get();
        }
        public void setInformation(String inf) {
            information.set(inf);
        }
    }
}

