

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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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


    private void buildQueueLists(){
        // get queue lists for doctors & orthoptists
        LinkedList<IUser> userlist = null;
        try {
            userlist = (LinkedList) _startupController.getAllDoctorsAndOrthoptists();
        } catch (BadConnectionException e) {
            e.printStackTrace();

        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        // set needed amount of lists for gui
        int usercount = 0;
        for (int i = 0; i < userlist.size(); i++) {
            if (userlist.get(i).getUserGroupId() != null){
                if (userlist.get(i).getUserGroupId() == 2 || userlist.get(i).getUserGroupId() == 3){
                    usercount++;
                }
            }
        }

        tps = new TitledPane[usercount];
        lists = new ListView[usercount];

        // setup listviews
        for (int i = 0; i < usercount; i++) {
            lists[i] = new ListView<>();
            lists[i].setPrefSize(200, 250);
            lists[i].minWidth(Region.USE_COMPUTED_SIZE);
            lists[i].minHeight(Region.USE_COMPUTED_SIZE);
            lists[i].maxWidth(Region.USE_COMPUTED_SIZE);
            lists[i].maxHeight(Region.USE_COMPUTED_SIZE);
            lists[i].setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        ListView source;
                        source = (ListView)event.getSource();

                        currPatient = (IPatient) source.getSelectionModel().getSelectedItem();
                        addPatientTab(currPatient);
                    }
                }
            });
        }

        // build
        String queuename = "";
        LinkedList<IQueueEntry> queueentries = null;
        IPatientQueue qe = null;
        int count = 0;
        for (int i = 0; i < userlist.size(); i++) {
            if (userlist.get(i).getUserGroupId() != null){
                if (userlist.get(i).getUserGroupId() == 2  || userlist.get(i).getUserGroupId() == 3){
                    if (userlist.get(i).getTitle() != null) {
                        queuename = userlist.get(i).getTitle() + " " + userlist.get(i).getFirstName() + " " + userlist.get(i).getLastName();
                    } else {
                        queuename = userlist.get(i).getFirstName() + " " + userlist.get(i).getLastName();
                    }
                    System.out.println("TEST" + userlist.get(i).getFirstName());

                    // needed get Queue From UserID
                    try {

                        qe = _startupController.getQueueByUserId(userlist.get(i));
                    } catch (BadConnectionException e) {
                        e.printStackTrace();
                    } catch (NoBrokerMappedException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (QueueEntry q: qe.getEntries()){
                            switch(count){
                                case 0:
                                    queuepatientlist1.add(q.getPatient());
                                    lists[0].setItems(queuepatientlist1);
                                    lists[0].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                                case 1:
                                    queuepatientlist2.add(q.getPatient());
                                    lists[1].setItems(queuepatientlist2);
                                    lists[1].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                                case 2:
                                    queuepatientlist3.add(q.getPatient());
                                    lists[2].setItems(queuepatientlist3);
                                    lists[2].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                                case 3:
                                    queuepatientlist4.add(q.getPatient());
                                    lists[3].setItems(queuepatientlist4);
                                    lists[3].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                                case 4:
                                    queuepatientlist5.add(q.getPatient());
                                    lists[4].setItems(queuepatientlist5);
                                    lists[4].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                                case 5:
                                    queuepatientlist6.add(q.getPatient());
                                    lists[5].setItems(queuepatientlist6);
                                    lists[5].setPrefHeight(queuepatientlist1.size() * 24);
                                    break;
                            }


                        }
                    } catch (NoBrokerMappedException e) {
                        e.printStackTrace();
                    } catch (BadConnectionException e) {
                        e.printStackTrace();
                    }



                    tps[count] = new TitledPane(queuename, lists[count]);
                    tps[count].setExpanded(false);
                    tps[count].setAnimated(true);
                    tps[count].setVisible(true);

                    count++;
                }
            }
        }

        tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);
    }


    public void refreshQueue(){
        tps = null;
        lists = null;
        vboxQueues.getChildren().removeAll();
        buildQueueLists();
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

