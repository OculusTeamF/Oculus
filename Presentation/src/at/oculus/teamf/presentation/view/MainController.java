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
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.resourcebundel.HashResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    public MenuItem openPatientsearch;
    @FXML
    public RadioMenuItem defaultTheme, darkTheme, customTheme;
    @FXML
    private VBox vboxQueues;
    @FXML
    private TabPane displayPane;
    @FXML
    private SplitPane splitter;
    @FXML
    private Button searchButton;
    @FXML
    private TextField textSearch;
    @FXML
    private ListView listSearchResults;
    @FXML
    private TitledPane searchResults;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button buttonAddPatient;

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
        Image imageDecline = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        Image imageAddPatientButton = new Image(getClass().getResourceAsStream("/res/icon_addpatient.png"));
        searchButton.setGraphic(new ImageView(imageDecline));
        listSearchResults.setPrefHeight(0);
        searchResults.setDisable(true);
        buttonAddPatient.setGraphic(new ImageView(imageAddPatientButton));
        buttonAddPatient.setVisible(true);

        // statusbar setup
        borderPane.setBottom(StatusBarController.getInstance());
        StatusBarController.getInstance().setText("Welcome to Oculus");

        // tooltip test
        Tooltip tp = new Tooltip();
        tp.setText("Search for Firstname, Lastname or SVN number");
        textSearch.setTooltip(tp);

        // menuitems init
        ToggleGroup mGroup = new ToggleGroup();
        defaultTheme.setToggleGroup(mGroup);
        darkTheme.setToggleGroup(mGroup);
        customTheme.setToggleGroup(mGroup);

        // build queuelist
        buildQueueLists();

        // search results listview event (opens selected patient)
        listSearchResults.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    addPatientTab((IPatient) listSearchResults.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    // *******************************************************************
    // Queuelist
    // *******************************************************************

    /*load and setup queuelist for all users (on application load)*/
    private void buildQueueLists() {
        _userOListMap = new HashMap<>();
        _OListUserMap = new HashMap<>();
        _listViewMap = new HashMap<>();

        TitledPane[] titledPanes;

        titledPanes = new TitledPane[_userlist.size()];

        // setup listviews
        int i = 0;
        for (IUser u : _userlist) {
            ListView<IPatient> listView = new ListView<>();
            listView = new ListView<>();
            listView.setPrefSize(200, 250);
            listView.minWidth(Region.USE_COMPUTED_SIZE);
            listView.minHeight(Region.USE_COMPUTED_SIZE);
            listView.maxWidth(Region.USE_COMPUTED_SIZE);
            listView.maxHeight(Region.USE_COMPUTED_SIZE);
            listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        ListView source;
                        source = (ListView) event.getSource();
                        ObservableList<IPatient> observableList = source.getItems();
                        final IUser user = _OListUserMap.get(observableList);
                        addPatientTab((IPatient) source.getSelectionModel().getSelectedItem());
                    }
                }
            });
            String queuename = null;
            if (u.getTitle() == null || u.getTitle().equals("null") || u.getTitle().equals("")) {
                queuename = u.getFirstName() + " " + u.getLastName();
            } else {
                queuename = u.getTitle() + " " + u.getFirstName() + " " + u.getLastName();

            }

            // needed get Queue From UserID
            IPatientQueue qe = null;
            try {
                qe = _startupController.getQueueByUser(u);
            } catch (BadConnectionException | NoBrokerMappedException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException, NoBrokerMappedException - Please contact support");
            }

            ObservableList<IPatient> olist = FXCollections.observableArrayList();

            try {
                for (QueueEntry entry : qe.getEntries()) {
                    olist.add(entry.getPatient());
                }
            } catch (NoBrokerMappedException | BadConnectionException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
            }

            listView.setItems(olist);
            listView.setPrefHeight(olist.size() * 24);

            _userOListMap.put(u, olist);
            _OListUserMap.put(olist, u);
            _listViewMap.put(u, listView);

            titledPanes[i] = new TitledPane(queuename, listView);
            titledPanes[i].setExpanded(false);
            titledPanes[i].setAnimated(true);
            titledPanes[i].setVisible(true);

            i++;
        }

        //titledPanes[0].setExpanded(true);
        vboxQueues.getChildren().addAll(titledPanes);
    }

    /*refresh queue after adding or removing patient*/
    public void refreshQueue(IUser user) {
        ObservableList observableList = _userOListMap.get(user);
        if (observableList != null) {
            observableList.remove(0, observableList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }

        IPatientQueue queue = null;
        try {
            if (user instanceof IDoctor) {
                queue = ((IDoctor) user).getQueue();
            } else {
                queue = ((IOrthoptist) user).getQueue();
            }

        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }

        observableList.remove(0, observableList.size());

        try {
            for(IQueueEntry iQueueEntry : queue.getEntries()) {
                observableList.add(iQueueEntry.getPatient());
            }
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }


        ListView list = _listViewMap.get(user);
        list.setPrefHeight(observableList.size() * 24);
    }

    // *******************************************************************
    // Searchbox
    // *******************************************************************

    /*Key pressed: do search for patients*/
    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doSearch();
        }
    }

    /*search and list patients with used keywords*/
    @FXML
    public void doSearch() {
        ObservableList<IPatient> patientlist = null;
        try {
            patientlist = FXCollections.observableList((List) _searchPatientController.searchPatients(textSearch.getText()));
        } catch (FacadeException | InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException, InvalidSearchParameterException - Please contact support");
        }
        if (patientlist.size() > 0) {
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 24);
            searchResults.setDisable(false);
            searchResults.setExpanded(true);
            searchResults.setText("Search Results (" + patientlist.size() + "):");
            StatusBarController.getInstance().setText("Found patients: " + patientlist.size());
        } else {
            searchResults.setText("Search Results (None)");
            searchResults.setExpanded(false);
            searchResults.setDisable(true);
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 24);
            StatusBarController.getInstance().setText("No patients found");
        }
    }

    // *******************************************************************
    // New Tabs Methods
    // *******************************************************************

    /*Tab: opens new tab for patient search (detailled search)*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/SearchPatientTab.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    /*Tab: opens patient record for selected patient*/
    public void addPatientTab(IPatient patient) {
        try {
            HashMap<String, Object> resourceMap = new HashMap<>();
            resourceMap.put("Doctors", _doctors);
            resourceMap.put("Patient", patient);
            resourceMap.put("UserList", _userlist);

            Tab tab = (Tab) FXMLLoader.load(this.getClass().getResource("fxml/PatientRecordTab.fxml"), new HashResourceBundle(resourceMap));

            displayPane.getTabs().addAll(tab);
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
            displayPane.getTabs().get(displayPane.getTabs().size() - 1).setText("Patient: " + patient.getFirstName() + " " + patient.getLastName());
            StatusBarController.getInstance().setText("Opened Patient Record: " + patient.getFirstName() + " " + patient.getLastName());
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    /*Tab: opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        try {
            HashMap<String, Object> resourceMap = new HashMap<>();
            resourceMap.put("Doctors", _doctors);
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/NewPatientTab.fxml"), new HashResourceBundle(resourceMap)));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
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

