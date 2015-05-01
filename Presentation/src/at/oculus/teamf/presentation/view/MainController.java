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

import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.resourcebundel.HashResourceBundle;
import javafx.collections.ObservableList;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Menu menuChangeUser;
    @FXML
    public MenuItem openPatientsearch, menuUser;
    @FXML
    public RadioMenuItem defaultTheme, darkTheme, customTheme;
    @FXML
    public TabPane displayPane;
    @FXML
    private SplitPane splitter;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button buttonAddPatient;
    @FXML
    private AnchorPane splitLeftSide;

    private StartupController _startupController;
    private SearchPatientController _searchPatientController;

    private HashMap<IUser, ObservableList> _userOListMap;
    private HashMap<ObservableList, IUser> _OListUserMap;
    private HashMap<IUser, ListView> _listViewMap;

    private Collection<IDoctor> _doctors;
    private Collection<IUser> _userlist;

    /**
     * Initialize the waiting queue
     *
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        _startupController = new StartupController();
        _searchPatientController = new SearchPatientController();

        try {
            _doctors = _startupController.getAllDoctors();
            _userlist = _startupController.getAllDoctorsAndOrthoptists();
        } catch (NoBrokerMappedException e) {
            //Todo: add DialogBox
            e.printStackTrace();
        } catch (BadConnectionException e) {
            //Todo: add DialogBox
            e.printStackTrace();
        }

        // search button & list init
        buttonAddPatient.setVisible(false);
        Image imageAddPatientButton = new Image(getClass().getResourceAsStream("/res/icon_addpatient.png"));
        buttonAddPatient.setGraphic(new ImageView(imageAddPatientButton));
        buttonAddPatient.setVisible(true);

        // statusbar setup
        borderPane.setBottom(StatusBarController.getInstance());
        StatusBarController.getInstance().setText("Welcome to Oculus");

        // menuitems init
        ToggleGroup menuThemeGroup = new ToggleGroup();
        defaultTheme.setToggleGroup(menuThemeGroup);
        darkTheme.setToggleGroup(menuThemeGroup);
        customTheme.setToggleGroup(menuThemeGroup);
        menuUser.setText("Current User: [user]" );

        // menuitems add user
        ToggleGroup userMenuGroup = new ToggleGroup();
        /*for (IUser u : _userlist){
            RadioMenuItem x1 = new RadioMenuItem(u.getLastName());
            x1.setToggleGroup(userMenuGroup);
            menuChangeUser.getItems().add(x1);
        }*/
        RadioMenuItem x1 = new RadioMenuItem("Doctor");
        x1.setToggleGroup(userMenuGroup);
        RadioMenuItem x2 = new RadioMenuItem("Orthoptist");
        x2.setToggleGroup(userMenuGroup);
        RadioMenuItem x3 = new RadioMenuItem("Receptionist");
        x3.setToggleGroup(userMenuGroup);
        menuChangeUser.getItems().addAll(x1, x2, x3);
        x1.setSelected(true);


        // add queuefxml to mainwindow
        try {
            splitLeftSide.getChildren().addAll((VBox) FXMLLoader.load(this.getClass().getResource("fxml/QueueSide.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // *******************************************************************
    // New Tabs Methods
    // *******************************************************************

    /*Tabhandler*/
    public void loadTab(String tabTitle, String tabFXML, ResourceBundle resourceMap){
        try {
            Tab tab = new Tab(tabTitle);
            AnchorPane ap = (AnchorPane) FXMLLoader.load(this.getClass().getResource(tabFXML),resourceMap);
            tab.setContent(ap);
            displayPane.getTabs().add(tab);
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - (Tab loading error) Please contact support");
        }
    }

    /*Tab: opens new tab for patient search (detailled search)*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        HashMap<String, Object> resourceMap = new HashMap<>();
        loadTab("Search patient", "fxml/SearchPatientTab.fxml",new HashResourceBundle(resourceMap));
    }

    /*Tab: opens patient record for selected patient*/
    public void addPatientTab(IPatient patient) {
        HashMap<String, Object> resourceMap = new HashMap<>();
        resourceMap.put("Doctors", _doctors);
        resourceMap.put("Patient", patient);
        resourceMap.put("UserList", _userlist);

        loadTab("Patient: " + patient.getFirstName() + " " + patient.getLastName(), "fxml/PatientRecordTab.fxml",new HashResourceBundle(resourceMap));

        //Tab tab = (Tab) FXMLLoader.load(this.getClass().getResource("fxml/PatientRecordTab.fxml"), new HashResourceBundle(resourceMap));
        //displayPane.getTabs().addAll(tab);
        //displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        //displayPane.getTabs().get(displayPane.getTabs().size() - 1).setText("Patient: " + patient.getFirstName() + " " + patient.getLastName());
        StatusBarController.getInstance().setText("Opened Patient Record: " + patient.getFirstName() + " " + patient.getLastName());
    }

    /*Tab: opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        HashMap<String, Object> resourceMap = new HashMap<>();
        resourceMap.put("Doctors", _doctors);
        loadTab("Add new patient", "fxml/NewPatientTab.fxml",new HashResourceBundle(resourceMap));
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

    public TabPane getTabPane() {
        return this.displayPane;
    }
}

