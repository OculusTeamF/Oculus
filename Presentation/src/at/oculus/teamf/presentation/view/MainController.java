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
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.persistence.exception.FacadeException;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.dialog.Dialogs;

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
        LinkedList<QueueEntry> queueentry = new LinkedList();
        ObservableList<IPatientQueue> queues = null;

        try {
            queues = FXCollections.observableArrayList(_startupController.getAllQueues());
        } catch (FacadeException e) {
            e.printStackTrace();
        }


        // set needed amount of lists for gui
        int userAmount = queues.size();//doctors.size() + orthoptists.size();
        tps = new TitledPane[userAmount];
        lists = new ListView[userAmount];


        // setup listviews
        for (int i = 0; i < queues.size(); i++) {
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

        // create queues
        ObservableList<IPatient> queuepatientlist = FXCollections.observableArrayList();  // need array from it ! (problem: generic arrays not allowed)
        ObservableList<IPatient> queuepatientlist2 = FXCollections.observableArrayList();
        ObservableList<IPatient> queuepatientlist3 = FXCollections.observableArrayList();
        ObservableList<IPatient> queuepatientlist4 = FXCollections.observableArrayList();

        String queuename = "";
        for (int i = 0; i < queues.size(); i++) {
            queueentry = (LinkedList) queues.get(i).getEntries();

            if (queueentry.size() != 0) {
                if (queueentry.get(0).getDoctor() != null) {
                    queuename = queueentry.get(0).getDoctor().getTitle() + " " + queueentry.get(0).getDoctor().getFirstName() + " " + queueentry.get(0).getDoctor().getLastName();
                }else{
                    queuename = queueentry.get(0).getOrthoptist().getTitle() + " " + queueentry.get(0).getOrthoptist().getFirstName() + " " + queueentry.get(0).getOrthoptist().getLastName();
                }
                for  (int j = 0; j < queueentry.size(); j++){
                    switch(i){
                        case 0:
                            queuepatientlist.add(queueentry.get(j).getPatient());
                            break;
                        case 1:
                            queuepatientlist2.add(queueentry.get(j).getPatient());
                            break;
                        case 2:
                            queuepatientlist3.add(queueentry.get(j).getPatient());
                            break;
                        case 3:
                            queuepatientlist4.add(queueentry.get(j).getPatient());
                            break;
                    }
                }
            } else {
                queuename = "no patients for queue available";
            }

            tps[i] = new TitledPane(queuename, lists[i]);
            tps[i].setExpanded(false);
            tps[i].setAnimated(true);
            tps[i].setVisible(true);
        }

        // add to stage
        tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);

        lists[0].setItems(queuepatientlist);
        lists[0].setPrefHeight(queuepatientlist.size() * 24);
        lists[1].setItems(queuepatientlist2);
        lists[1].setPrefHeight(queuepatientlist2.size() * 24);
        lists[2].setItems(queuepatientlist3);
        lists[2].setPrefHeight(queuepatientlist3.size() * 24);
        lists[3].setItems(queuepatientlist4);
        lists[3].setPrefHeight(queuepatientlist4.size() * 24);
    }


    public void refreshQueue(){
        tps = null;
        lists = null;
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
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("agenda2.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("newPatientTab.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPatientTab(IPatient patient){
        try {
            currPatient = patient;
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("patientRecordTab.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
            displayPane.getTabs().get(displayPane.getTabs().size() - 1).setText("Patient: " + currPatient.getFirstName() + " " + currPatient.getLastName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openPatient(ActionEvent actionEvent) {
        Dialogs.create()
                .owner(Main.stage)
                .title("Error Dialog")
                .masthead("EIN FEHLER !!!")
                .message("Soeben hat ein Fehler stattgefunden!")
                .showError();
    }

    /*Opens a patient search tab*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("searchPatientTab.fxml")));
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
