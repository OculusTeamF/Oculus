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
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.presentation.view.models.PrescriptionModel;
import at.oculus.teamf.technical.printing.IPrinter;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import javafx.beans.Observable;
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
import javafx.util.Callback;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Karo on 04.05.2015.
 */
public class PrescriptionController implements Initializable, IPrinter {


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
    @FXML public Button addMedicinButton;
    @FXML public Button removeMedicinButton;
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
    @FXML public TableView prescriptionItems;
    @FXML public TextField medicinTextfield;
    @FXML public TextField dosageTextfield;
    @FXML public TextField informationTextfield;
    @FXML public Button removeEntryFromTable;
    @FXML public Button addNewEntryToTable;

    private Model _model = Model.getInstance();
    private ObservableList<String> _prescriptionType;
    private PrescriptionModel _prescriptionModel = PrescriptionModel.getInstance();
    private ObservableList<Entry> _medicinList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // load image resources for buttons
        Image imagePrintIcon = new Image(getClass().getResourceAsStream("/res/icon_print.png"));
        printButton.setGraphic(new ImageView(imagePrintIcon));


        _prescriptionType = FXCollections.observableArrayList("Medicin", "Visual Aid");
        choosePrescriptionBox.setItems(_prescriptionType);
        choosePrescriptionBox.getSelectionModel().select(0);

        addMedicinButton.setTooltip(new Tooltip("add Medicin"));
        removeMedicinButton.setTooltip(new Tooltip("clear Fields"));

        //Stackpane on index 1 is the visualAidPrescription, on index 0 is the medicalPrescription
        prescriptionStackPane.getChildren().get(1).setVisible(false);
        prescriptionStackPane.getChildren().get(0).setVisible(true);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveButton.setGraphic(new ImageView(imageSaveIcon));

        //Medicin box
        String text = "choose medicin ...";
        chooseMedicinBox.setPromptText(text);

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
        lastnameVA.setText(_model.getPatient().getLastName());
        firstnameVA.setText(_model.getPatient().getFirstName());
        svnVA.setText(_model.getPatient().getSocialInsuranceNr());
        bdayVA.setText(_model.getPatient().getBirthDay().toString());
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
                            prescriptionStackPane.getChildren().get(1).setVisible(false);
                            prescriptionStackPane.getChildren().get(0).setVisible(true);
                            break;
                        case "Visual Aid":
                            prescriptionStackPane.getChildren().get(0).setVisible(false);
                            prescriptionStackPane.getChildren().get(1).setVisible(true);
                            break;
                    }
                    printButton.setVisible(true);
                }
            }
        });

        //TableView
        TableColumn col = (TableColumn) prescriptionItems.getColumns().get(0);

        _medicinList = FXCollections.observableArrayList();
        prescriptionItems.setItems(_medicinList);
    }

    //add the selected MedicinItems to the Textfield
    @FXML
    public void addMedicinButtonActionHandler(ActionEvent actionEvent) {

        IMedicine itemToAdd = (IMedicine) chooseMedicinBox.getSelectionModel().getSelectedItem();
        medicinTextfield.setText(itemToAdd.toString());
    }

    //remove the Medicin from the PrescriptionList
    @FXML
    public void removeMedicinButtonActionHandler(ActionEvent actionEvent) {

        final int selectedIdx = prescriptionItems.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
            IMedicine itemToRemove = (IMedicine)prescriptionItems.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIdx == prescriptionItems.getItems().size() - 1)
                            ? selectedIdx - 1
                            : selectedIdx;

            prescriptionItems.getItems().remove(selectedIdx);
            StatusBarController.showProgressBarIdle("Removed " + itemToRemove);
            prescriptionItems.getSelectionModel().select(newSelectedIdx);
        }
    }


    @FXML
    public void savePrescriptionButtonActionHandler(ActionEvent actionEvent){


        if(choosePrescriptionBox.getSelectionModel().getSelectedItem().equals("Medicin"))
        {
            IPatient patient = _model.getPatient();
            Collection<IMedicine> medicinList = prescriptionItems.getItems();

            try {
                _prescriptionModel.addNewPrescription(patient);
                _prescriptionModel.addPrescriptionEntries(medicinList);
            } catch (NotInitatedExceptions notInitatedExceptions) {
                notInitatedExceptions.printStackTrace();
                //Todo: handle
            }
        }else if(choosePrescriptionBox.getSelectionModel().getSelectedItem().equals("Visual Aid"))
        {
            //TODO:
            // _prescriptionModel.addNewVisualAidPrescription(_model.getPatient());
            String text = visualAidInformation.getText();
             _prescriptionModel.addVisualAidPrescriptionEntries(text);
        }
    }

    @FXML
    public void printPrescriptionButtonActionHandler(ActionEvent actionEvent) {

        //TODO: print and save prescription
        _prescriptionModel.printPrescription();

        //save prescription
        IPatient patient = _model.getPatient();
        Collection<IMedicine> medicinList = prescriptionItems.getItems();

        try {
            _prescriptionModel.addNewPrescription(patient);
            _prescriptionModel.addPrescriptionEntries(medicinList);
        } catch (NotInitatedExceptions notInitatedExceptions) {
            notInitatedExceptions.printStackTrace();
            //Todo: handle
        }
    }

    @FXML
    public void addNewPrescriptionEntryToTable(ActionEvent actionEvent) {

        _medicinList.add(new Entry(medicinTextfield.getText(), dosageTextfield.getText(), informationTextfield.getText()));

    }
}

class Entry{

    private final SimpleStringProperty  medicin;
    private final SimpleStringProperty  dosage;
    private final SimpleStringProperty  information;

    public Entry(String medicin, String dosage, String information){

        this.medicin = new SimpleStringProperty( medicin.toString());
        this.dosage = new SimpleStringProperty(dosage);
        this.information = new SimpleStringProperty(information);
    }
}