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
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IQueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.StatusBar;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML public MenuItem openPatientsearch;
    @FXML private VBox vboxQueues;
    @FXML private TabPane displayPane;
    @FXML private SplitPane splitter;
    @FXML private Button searchButton;
    @FXML private TextField textSearch;
    @FXML private ListView listSearchResults;
    @FXML private TitledPane searchResults;
    @FXML private Tab patientRecordTab;
    @FXML private BorderPane borderPane;
    @FXML private HBox statusHBox;

    private Collection<PatientQueue> _allQueues;
    private Tab _newPatientTab;
    private Tab _calendarTab;
    private Tab _searchPatientTab;
    private User _user;
    private StartupController _startupController = new StartupController();
    private SearchPatientController _searchPatientController = new SearchPatientController();

    private TitledPane[] tps;
    private ListView<IPatient> lists[];
    private IPatient currPatient;
    private  DialogBoxController box = new DialogBoxController();

    private ObservableList<IPatient> queuepatientlist1 = FXCollections.observableArrayList();  // need array ! (problem: generic arrays not allowed)
    private ObservableList<IPatient> queuepatientlist2 = FXCollections.observableArrayList();
    private ObservableList<IPatient> queuepatientlist3 = FXCollections.observableArrayList();
    private ObservableList<IPatient> queuepatientlist4 = FXCollections.observableArrayList();
    private ObservableList<IPatient> queuepatientlist5 = FXCollections.observableArrayList();
    private ObservableList<IPatient> queuepatientlist6 = FXCollections.observableArrayList();
    private LinkedList<IUser> userlist = null;


    /**
     * Initialize the waiting queue
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // search button & list init
        Image imageDecline = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchButton.setGraphic(new ImageView(imageDecline));
        listSearchResults.setPrefHeight(0);

        // statusbar test
        StatusBar statusBar = new StatusBar();
        borderPane.setBottom(statusBar);
        statusBar.setText("Welcome to Oculus");

        // tooltip test
        Tooltip tp = new Tooltip();
        tp.setText("Search for Firstname, Lastname or SVN number");
        textSearch.setTooltip(tp);

        // build queuelist
        buildQueueLists();

        // search results listview event (opens selected patient)
        listSearchResults.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    currPatient = (IPatient) listSearchResults.getSelectionModel().getSelectedItem();
                    addPatientTab(currPatient);
                }
            }
        });
    }

    private HashMap<Integer, ObservableList> _listMap;

    private void buildQueueLists() {
        // get queue lists for doctors & orthoptists

        _listMap = new HashMap<>();


        try {
            userlist = (LinkedList) _startupController.getAllDoctorsAndOrthoptists();
        } catch (BadConnectionException e) {
            e.printStackTrace();

        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        LinkedList<IUser> user = new LinkedList<>();
        //Todo: deligate down
        // set needed amount of lists for gui

        for(IUser u : userlist) {
            if (u.getUserGroupId() != null) {
                if (u.getUserGroupId() == 2 || u.getUserGroupId() == 3) {
                    user.add(u);
                }
            }
        }

        tps = new TitledPane[user.size()];
        lists = new ListView[user.size()];

        // setup listviews
        int i = 0;
        for(IUser u : user) {
            ListView<IPatient>  listView = new ListView<>();
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

                        currPatient = (IPatient) source.getSelectionModel().getSelectedItem();
                        addPatientTab(currPatient);
                    }
                }
            });

            String queuename = u.getTitle() + " " + u.getFirstName() + " " + u.getLastName();

            // needed get Queue From UserID
            IPatientQueue qe = null;
            try {
                qe = _startupController.getQueueByUserId(u);
            } catch (BadConnectionException e) {
                e.printStackTrace();
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            }

            ObservableList<IPatient> olist = FXCollections.observableArrayList();

            try {
                for(QueueEntry entry : qe.getEntries()) {
                    olist.add(entry.getPatient());
                }
            } catch (NoBrokerMappedException e) {
                e.printStackTrace();
            } catch (BadConnectionException e) {
                e.printStackTrace();
            }

            listView.setItems(olist);
            _listMap.put(u.getUserId(), olist);

            tps[i] = new TitledPane(queuename, listView);
            tps[i].setExpanded(false);
            tps[i].setAnimated(true);
            tps[i].setVisible(true);

            i++;
        }

        //tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);
    }

    public void refreshQueue(IPatientQueue queue, IUser user)
    {
        ObservableList observableList = _listMap.get(user.getUserId());
        observableList.remove(0, observableList.size());

        try {
            for(QueueEntry entry : queue.getEntries()) {
                observableList.add(entry.getPatient());
            }
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doSearch();
        }
    }

    @FXML
    public void doSearch()  {
        ObservableList<IPatient> patientlist = null;
        try {
            patientlist = FXCollections.observableList((List) _searchPatientController.searchPatients(textSearch.getText()));
        } catch (FacadeException e) {
            e.printStackTrace();
        } catch (InvalidSearchParameterException e) {
            e.printStackTrace();
        }
        if(patientlist.size() > 0) {
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 24);
            searchResults.setExpanded(true);
        }
    }


    /*Close the application by clicking the Menuitem 'Exit'*/
    @FXML
    public void onClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    /*Opens the calendar view by clicking Menuitem 'Calendar'*/
    @FXML
    public void openCal(ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/Agenda.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openPatientProperty (ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/PatientProperty.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/newPatientTab.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPatientTab(IPatient patient){
        try {
            currPatient = patient;
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/patientRecordTab.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
            displayPane.getTabs().get(displayPane.getTabs().size() - 1).setText("Patient: " + currPatient.getFirstName() + " " + currPatient.getLastName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openPatient(ActionEvent actionEvent) {
        DialogBoxController dial = new DialogBoxController();
        dial.showLoginDialog("a","b");
    }

    /*Opens a patient search tab*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/SearchPatientTab.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public IPatient getPatient(){
        return currPatient;
    }

    public SplitPane getSplitter(){
        return this.splitter;
    }

    public TabPane getTabPane(){
        return this.displayPane;
    }
}

