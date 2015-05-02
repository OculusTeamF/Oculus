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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Menu menuChangeUser;
    @FXML private MenuItem openPatientsearch, menuUser;
    @FXML private RadioMenuItem defaultTheme, darkTheme, customTheme;
    @FXML private TabPane displayPane;
    @FXML private SplitPane splitter;
    @FXML private BorderPane borderPane;
    @FXML private Button buttonAddPatient;
    @FXML private AnchorPane splitLeftSide;

    private Model _model = Model.getInstance();

    /**
     * Initialize the waiting queue
     *
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        // setup model controller
        _model.setTabPanel(displayPane);

        // add queuefxml to mainwindow
        try {
            splitLeftSide.getChildren().addAll((VBox) FXMLLoader.load(this.getClass().getResource("fxml/QueueSide.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // search button & list init
        buttonAddPatient.setVisible(false);
        Image imageAddPatientButton = new Image(getClass().getResourceAsStream("/res/icon_addpatient.png"));
        buttonAddPatient.setGraphic(new ImageView(imageAddPatientButton));
        buttonAddPatient.setVisible(true);

        // statusbar setup
        borderPane.setBottom(StatusBarController.getInstance());
        StatusBarController.getInstance().setText("Welcome to Oculus [Logged in: " + _model.getLoggedInUser().getFirstName() + " " + _model.getLoggedInUser().getLastName() + "]");

        // menuitems init
        ToggleGroup menuThemeGroup = new ToggleGroup();
        defaultTheme.setToggleGroup(menuThemeGroup);
        darkTheme.setToggleGroup(menuThemeGroup);
        customTheme.setToggleGroup(menuThemeGroup);
        menuUser.setText("Current User: " + _model.getLoggedInUser().getFirstName() + " " + _model.getLoggedInUser().getLastName());

        // menuitems add user
        ToggleGroup userMenuGroup = new ToggleGroup();
        ComboBox cboUser = new ComboBox();
        cboUser.getItems().addAll("Paul Tavolato","Mister X");
        cboUser.setPromptText("Choose User");
        CustomMenuItem menuUserList = new CustomMenuItem(cboUser);
        menuUserList.setHideOnClick(false);

        RadioMenuItem x1 = new RadioMenuItem("Doctor");
        x1.setToggleGroup(userMenuGroup);
        RadioMenuItem x2 = new RadioMenuItem("Orthoptist");
        x2.setToggleGroup(userMenuGroup);
        RadioMenuItem x3 = new RadioMenuItem("Receptionist");
        x3.setToggleGroup(userMenuGroup);
        menuChangeUser.getItems().addAll(x1, x2, x3,menuUserList);
        x1.setSelected(true);
    }


    /*Tab: opens new tab for patient search (detailled search)*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        _model.loadTab( "Search patient", "fxml/SearchPatientTab.fxml");
    }

    /*Tab: opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        _model.loadTab("Add new patient", "fxml/NewPatientTab.fxml");
    }

    /*Tab: Opens the agenda calendar (unused)*/
    @FXML
    public void openCal(ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/Agenda.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    /*Tab: opens the patient property (unused)*/
    @FXML
    public void openPatientProperty(ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/PatientProperty.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    // *******************************************************************
    // Menu & Button Items Actions
    // *******************************************************************

    /*Menu item: opens help window*/
    @FXML
    public void clickMenuItemShowHelp(ActionEvent actionEvent) {
        DialogBoxController.getInstance().showInformationDialog("Oculus Help", "User manual for Oculus");
    }

    /*Menu item: opens about dialog*/
    @FXML
    public void clickMenuItemShowAbout(ActionEvent actionEvent) {
        DialogBoxController.getInstance().showAboutDialog();
    }

    /*Button: opens test action*/
    @FXML
    public void openPatient(ActionEvent actionEvent) {
        System.out.println(DialogBoxController.getInstance().showYesNoDialog("Frage", "ist es OK?"));
    }

    /* MenuItem for the selection of the Theme*/
    @FXML
    public void clickMenuItemThemeSelection(ActionEvent actionEvent) {
        MenuItem src = (MenuItem) actionEvent.getSource();
        switch(src.getId()) {
            case "defaultTheme":
                Main.scene.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_default.css").toExternalForm());
                break;
            case "darkTheme":
                Main.scene.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_dark.css").toExternalForm());
                break;
            case "customTheme":
                DialogBoxController.getInstance().showInformationDialog("theme","show custom theme");
                break;
        }

    }

    /*Close the application by clicking the Menuitem 'Exit'*/
    @FXML
    public void clickMenuItemExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    // *******************************************************************
    // Getter & Setter
    // *******************************************************************

    public SplitPane getSplitter() {
        return this.splitter;
    }
}

