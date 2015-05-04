/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Fabian on 29.04.2015.
 */
public class QueueController implements Initializable {
    @FXML private Button searchButton;
    @FXML private CustomTextField textSearch;
    @FXML private ListView listSearchResults;
    @FXML private TitledPane searchResults;
    @FXML private VBox vboxQueues;

    private Model _model = Model.getInstance();
    private ObservableList<IPatient> patientlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // search button & searchresultslist init
        Image imageSearchIcon = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchButton.setGraphic(new ImageView(imageSearchIcon));
        listSearchResults.setPrefHeight(0);
        searchResults.setDisable(true);
        textSearch.setRight(searchButton);

        // tooltip
        Tooltip tp = new Tooltip();
        tp.setText("Search for Firstname, Lastname or SVN number");
        textSearch.setTooltip(tp);

        // search results listview event (opens selected patient)
        listSearchResults.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    _model.setPatient((IPatient) listSearchResults.getSelectionModel().getSelectedItem());
                    _model.loadTab("PATIENT RECORD: " + _model.getPatient().getLastName(), "fxml/PatientRecordTab.fxml");
                }
            }
        });

        // add toolbar as seperator: searchlist <-> queuelists
        ToolBar tbQueue = new ToolBar();
        tbQueue.setMinHeight(30);
        vboxQueues.getChildren().add(tbQueue);

        // build queue
        _model.setVboxQueues(vboxQueues);
        _model.buildQueueLists();

        // setup logged in User
        //ArrayList<IDoctor> docs = (ArrayList) _model.getAllDoctors();
        //_model.setLoggedInUser(docs.get(0));
    }

    // *******************************************************************
    // Searchbox
    // *******************************************************************

    /*Key pressed: do search for patients*/
    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doPatientSearch();
        }
    }

    /* search and list patients with used keywords */
    @FXML
    public void doPatientSearch() {

        final Task<Void> search = doSearchPatients();
        StatusBarController.showProgressBarIdle("Searching patients");
        search.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                // end of search thread
                StatusBarController.hideStatusBarProgressBarIdle();
                textSearch.setDisable(false);

                // list results and setup controls
                if (patientlist.size() > 0) {
                    listSearchResults.setItems(patientlist);
                    listSearchResults.setPrefHeight((patientlist.size() * 30) + 8);
                    searchResults.setDisable(false);
                    searchResults.setExpanded(true);
                    searchResults.setText("Search Results (" + patientlist.size() + "):");
                    StatusBarController.getInstance().setText("Found patients: " + patientlist.size());
                } else {
                    searchResults.setText("Search Results (None)");
                    searchResults.setExpanded(false);
                    searchResults.setDisable(true);
                    listSearchResults.setItems(patientlist);
                    listSearchResults.setPrefHeight(0);
                    StatusBarController.getInstance().setText("No patients found");
                }
            }
        });
        new Thread(search).start();
    }

    // *******************************************************************
    // Search Thread
    // *******************************************************************

    /* thread for search */
    public Task<Void> doSearchPatients() {return new Task<Void>() {
            @Override
            protected Void call() {
                // disable texfield while searching
                textSearch.setDisable(true);

                // application layer acces for search
                patientlist = FXCollections.observableList((List)_model.searchPatients(textSearch.getText()));
                return null;
            }
        };
    }
}
