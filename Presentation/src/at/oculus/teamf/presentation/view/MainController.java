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
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IOrthoptist;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
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
    private ListView<String> lists[];
    private IPatient currPatient;

    /**
     * Initialize the waiting queue
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        // search button init
        Image imageDecline = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchButton.setGraphic(new ImageView(imageDecline));
        listSearchResults.setPrefHeight(0);

        ObservableList<IDoctor> doctors = FXCollections.observableArrayList(_startupController.getAllDoctors());
        ObservableList<IOrthoptist> orthoptists = FXCollections.observableArrayList(_startupController.getAllOrthoptists());

        int userAmount =  doctors.size() + orthoptists.size(); // only user with permission 2 & 3 (usergroup)
        tps = new TitledPane[userAmount];
        lists = new ListView[userAmount];

        // setup listviews
        for (int i = 0; i < userAmount; i++) {
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

                        //addPatientTab(null);
                    }
                }
            });
        }


        // setup titlepane (doctors & orthoptists)
        // todo: add unassigned patients to list
        String userQueueName;
        int orth = 0;
        for (int i = 0; i < userAmount; i++) {
            if (i < doctors.size()) {
                userQueueName = doctors.get(i).getTitle() + " " + doctors.get(i).getFirstName() + " " + doctors.get(i).getLastName();
            } else {
                orth = i - doctors.size();
                if (orthoptists.get(orth).getTitle() != null) {
                    userQueueName = orthoptists.get(orth).getTitle() + " " + orthoptists.get(orth).getFirstName() + " " + orthoptists.get(orth).getLastName();
                } else {
                    userQueueName = orthoptists.get(orth).getFirstName() + " " + orthoptists.get(orth).getLastName();
                }
            }

            tps[i] = new TitledPane(userQueueName, lists[i]);
            tps[i].setExpanded(false);
            tps[i].setAnimated(true);
            tps[i].setVisible(false);

        }

        // add to stage
        tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);

        // fill queuelists with available patients
        // TODO: dynamic code
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        ObservableList<String> userQList1 = FXCollections.observableList(list1);
        ObservableList<String> userQList2 = FXCollections.observableList(list2);

        LinkedList<ObservableList<String>> uuq = new LinkedList<ObservableList<String>>();

        uuq.add(userQList1);
        uuq.add(userQList2);

        for (QueueEntry qe : doctors.get(0).getQueue().getEntries()) {
            userQList1.add(qe.getPatient().getFirstName() + " " + qe.getPatient().getLastName() + " / " + qe.getPatient().getSocialInsuranceNr());
        }
        for (QueueEntry qe : doctors.get(1).getQueue().getEntries()) {
            userQList2.add(qe.getPatient().getFirstName() + " " + qe.getPatient().getLastName() + " / " + qe.getPatient().getSocialInsuranceNr());
        }

        lists[0].setItems(uuq.get(0));
        lists[0].setPrefHeight(uuq.get(0).size() * 24 + 2);
        lists[1].setItems(uuq.get(1));
        tps[0].setVisible(true);
        tps[1].setVisible(true);
        tps[2].setVisible(true);
        tps[3].setVisible(true);
        tps[4].setVisible(true);
        tps[5].setVisible(true);

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

    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doSearch();
        }
    }

    @FXML
    public void doSearch() {
        ObservableList<IPatient> patientlist = FXCollections.observableList((List)_searchPatientController.searchPatients(textSearch.getText()));
        if(patientlist.size() > 0) {
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 24 + 2);
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

    public SplitPane getSplitter(){
        return this.splitter;
    }

    public TabPane getTabPane(){
        return this.displayPane;
    }

    @FXML
    public void openPatient(ActionEvent actionEvent) {
       //TODO:
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
}
