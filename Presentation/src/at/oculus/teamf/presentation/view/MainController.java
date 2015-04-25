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
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

    @FXML public MenuItem openPatientsearch;
    @FXML private VBox vboxQueues;
    @FXML private TabPane displayPane;
    @FXML private SplitPane splitter;
    @FXML private Button searchButton;
    @FXML private TextField textSearch;
    @FXML private ListView listSearchResults;
    @FXML private TitledPane searchResults;
    @FXML private BorderPane borderPane;
    @FXML private Button buttonTest;

    private StartupController _startupController = new StartupController();
    private SearchPatientController _searchPatientController = new SearchPatientController();

    private HashMap<IUser, ObservableList> _listMap;
    public IUser user;
    public boolean loadingDone;


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
        buttonTest.setVisible(true);
        borderPane.setBottom(StatusBarController.getInstance());
        StatusBarController.getInstance().setText("Welcome to Oculus");

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
                    addPatientTab((IPatient) listSearchResults.getSelectionModel().getSelectedItem());
                }
            }
        });
        loadingDone = true;
    }

    private void buildQueueLists() {
        _listMap = new HashMap<>();

        TitledPane[] titledPanes;
        LinkedList<IUser> userlist = null;

        try {
            userlist = (LinkedList) _startupController.getAllDoctorsAndOrthoptists();
        } catch (BadConnectionException | NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException, NoBrokerMappedException - Please contact support");

        }

        titledPanes = new TitledPane[userlist.size()];

        // setup listviews
        int i = 0;
        for(IUser u : userlist) {
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
                        ObservableList<IPatient> ql = FXCollections.observableArrayList();
                        ql = source.getItems();
                        user = getKeyByValue(_listMap, ql);
                        addPatientTab((IPatient) source.getSelectionModel().getSelectedItem());
                    }
                }
            });

            String queuename = u.getTitle() + " " + u.getFirstName() + " " + u.getLastName();

            // needed get Queue From UserID
            IPatientQueue qe = null;
            try {
                qe = _startupController.getQueueByUserId(u);
            } catch (BadConnectionException | NoBrokerMappedException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException, NoBrokerMappedException - Please contact support");
            }

            ObservableList<IPatient> olist = FXCollections.observableArrayList();

            try {
                for(QueueEntry entry : qe.getEntries()) {
                    olist.add(entry.getPatient());
                }
            } catch (NoBrokerMappedException | BadConnectionException e) {
                e.printStackTrace();
                DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
            }

            listView.setItems(olist);
            listView.setPrefHeight(olist.size() * 24);
            _listMap.put(u, olist);

            titledPanes[i] = new TitledPane(queuename, listView);
            titledPanes[i].setExpanded(false);
            titledPanes[i].setAnimated(true);
            titledPanes[i].setVisible(true);

            i++;
        }

        //titledPanes[0].setExpanded(true);
        vboxQueues.getChildren().addAll(titledPanes);
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
        } catch (FacadeException | InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException, InvalidSearchParameterException - Please contact support");
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
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    @FXML
    public void openPatientProperty (ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/PatientProperty.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/NewPatientTab.fxml")));
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    public void addPatientTab(final IPatient patient){
        try {
            Tab tab = (Tab) FXMLLoader.load(this.getClass().getResource("fxml/patientRecordTab.fxml"), new SingleResourceBundle(patient));

            displayPane.getTabs().addAll(tab);
            displayPane.getSelectionModel().select(displayPane.getTabs().size() - 1);
            displayPane.getTabs().get(displayPane.getTabs().size() - 1).setText("Patient: " + patient.getFirstName() + " " + patient.getLastName());
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }


    @FXML
    public void openPatient(ActionEvent actionEvent) {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                updateMessage("First we sleep ....");

                Thread.sleep(2500);

                int max = 1000;
                for (int i = 0; i < max; i++) {
                    updateMessage("Message " + i);
                    updateProgress(i, max);
                }

                updateProgress(0, 0);
                done();
                return null;
            }
        };
        StatusBarController.getInstance().progressProperty().bind(task.progressProperty());
    }

    /*Opens a patient search tab*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("fxml/SearchPatientTab.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - Please contact support");
        }
    }

    public SplitPane getSplitter(){
        return this.splitter;
    }

    public TabPane getTabPane(){
        return this.displayPane;
    }

    public void refreshQueue(IPatientQueue queue, IUser user)
    {
        ObservableList observableList = _listMap.get(user.getUserId());
        observableList.remove(0, observableList.size());

        try {
            for(QueueEntry entry : queue.getEntries()) {
                observableList.add(entry.getPatient());
            }
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    /*Opens a new Patient record to add a patient*/
    @FXML
    public void showMenuHelp(ActionEvent actionEvent) {
        DialogBoxController.getInstance().showInformationDialog("Help","Shows Help");
    }

    public void showMenuAbout(ActionEvent actionEvent) {
        DialogBoxController.getInstance().showInformationDialog("About","Shows About");
    }
}

