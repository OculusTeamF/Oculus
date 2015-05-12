/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.models.Model;
import at.oculus.teamf.presentation.view.models.PrescriptionModel;
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

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by Karo on 04.05.2015.
 */
public class PrescriptionController implements Initializable, IPrinter {


    @FXML public ComboBox choosePrescriptionBox;
    @FXML public Button printButton;
    @FXML public Button saveButton;
    @FXML public StackPane prescriptionStackPane;
    @FXML public ListView prescriptionItemsVA;
    @FXML public ComboBox chooseMedicinBoxVA;
    @FXML public Button addMedicinButtonVA;
    @FXML public Button removeMedicinButtonVA;
    @FXML public Label lastnameVA;
    @FXML public Label firstnameVA;
    @FXML public Label svnVA;
    @FXML public Label bdayVA;
    @FXML public Label addressVA;
    @FXML public Label stateVA;
    @FXML public Label zipVA;
    @FXML public Label cityVA;
    @FXML public ListView prescriptionItems;
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


    private Model _model = Model.getInstance();
    private ObservableList<String> _prescriptionType;
    private PrescriptionModel _prescriptionModel = PrescriptionModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // load image resources for buttons
        Image imagePrintIcon = new Image(getClass().getResourceAsStream("/res/icon_print.png"));
        printButton.setGraphic(new ImageView(imagePrintIcon));


        _prescriptionType = FXCollections.observableArrayList("Medicin", "Visual Aid");
        choosePrescriptionBox.setItems(_prescriptionType);
        choosePrescriptionBox.getSelectionModel().select(0);

        addMedicinButton.setTooltip(new Tooltip("add Medicin to Prescription"));
        removeMedicinButton.setTooltip(new Tooltip("remove Medicin from Prescription"));

        //Stackpane on index 0 is the visualAidPrescription, on index 1 is the medicalPrescription
        prescriptionStackPane.getChildren().get(0).setVisible(false);
        prescriptionStackPane.getChildren().get(1).setVisible(true);

        // load image resources for buttons
        Image imageSaveIcon = new Image(getClass().getResourceAsStream("/res/icon_save.png"));
        saveButton.setGraphic(new ImageView(imageSaveIcon));

        //Medicin box
        String text = "choose medicin ...";
        chooseMedicinBox.setPromptText(text);
        chooseMedicinBoxVA.setPromptText(text);

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
                            prescriptionStackPane.getChildren().get(0).setVisible(false);
                            prescriptionStackPane.getChildren().get(1).setVisible(true);
                            break;
                        case "Visual Aid":
                            prescriptionStackPane.getChildren().get(1).setVisible(false);
                            prescriptionStackPane.getChildren().get(0).setVisible(true);
                            break;
                    }
                    printButton.setVisible(true);
                }
            }
        });
    }

    //add the selected MedicinItems to the PrescriptionList
    @FXML
    public void addMedicinButtonActionHandler(ActionEvent actionEvent) {

        ObservableList<IMedicine> medicinList = null;

        medicinList.add((IMedicine)chooseMedicinBox.getSelectionModel().getSelectedItem());

        prescriptionItems.setItems(medicinList);

    }

    //remove the Medicin from the PrescriptionList
    @FXML
    public void removeMedicinButtonActionHandler(ActionEvent actionEvent) {

        prescriptionItems.getSelectionModel().clearSelection();
    }

    @FXML
    public void savePrescriptionButtonActionHandler(ActionEvent actionEvent){

        //TODO: save prescription
        IPatient patient = _model.getPatient();
        Collection<IMedicine> medicinList = prescriptionItems.getItems();

        _prescriptionModel.addNewPrescription(patient);
        _prescriptionModel.addPrescriptionEntries(medicinList);
    }

    @FXML
    public void printPrescriptionButtonActionHandler(ActionEvent actionEvent){

        //TODO: print and save prescription
        _prescriptionModel.printPrescription();

        //save prescription
        IPatient patient = _model.getPatient();
        Collection<IMedicine> medicinList = prescriptionItems.getItems();

        _prescriptionModel.addNewPrescription(patient);
        _prescriptionModel.addPrescriptionEntries(medicinList);

        }
}
