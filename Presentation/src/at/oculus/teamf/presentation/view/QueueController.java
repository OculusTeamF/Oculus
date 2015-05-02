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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private Button searchButton;
    @FXML
    private CustomTextField textSearch;
    @FXML
    private ListView listSearchResults;
    @FXML
    private TitledPane searchResults;
    @FXML
    private VBox vboxQueues;

    private Model _model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // search button & searchresultslist init
        Image imageSearchIcon = new Image(getClass().getResourceAsStream("/res/icon_search.png"));
        searchButton.setGraphic(new ImageView(imageSearchIcon));
        listSearchResults.setPrefHeight(0);
        searchResults.setDisable(true);
        textSearch.setRight(searchButton);

        // tooltip test
        Tooltip tp = new Tooltip();
        tp.setText("Search for Firstname, Lastname or SVN number");
        textSearch.setTooltip(tp);

        // search results listview event (opens selected patient)
        listSearchResults.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    _model.addPatientTab((IPatient) listSearchResults.getSelectionModel().getSelectedItem());
                }
            }
        });

        ToolBar tbQueue = new ToolBar();
        tbQueue.setMinHeight(30);
        vboxQueues.getChildren().add(tbQueue);

        // build queue
        //buildQueueLists();
        _model.setVboxQueues(vboxQueues);
        _model.buildQueueLists();

        //Main.controlQueue = this;
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

    /*search and list patients with used keywords*/
    @FXML
    public void doPatientSearch()
    {
        ObservableList<IPatient>patientlist = FXCollections.observableList((List)_model.searchPatients(textSearch.getText()));

        if (patientlist.size() > 0) {
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 30);
            searchResults.setDisable(false);
            searchResults.setExpanded(true);
            searchResults.setText("Search Results (" + patientlist.size() + "):");
            StatusBarController.getInstance().setText("Found patients: " + patientlist.size());
        } else {
            searchResults.setText("Search Results (None)");
            searchResults.setExpanded(false);
            searchResults.setDisable(true);
            listSearchResults.setItems(patientlist);
            listSearchResults.setPrefHeight(patientlist.size() * 30);
            StatusBarController.getInstance().setText("No patients found");
        }
    }

    // *******************************************************************
    // Queuelist
    // *******************************************************************

    /*load and setup queuelist for all users (on application load)*/
    public void buildQueueLists() {

       // TitledPane[] titledPanes = _model.buildQueueLists();

        //TitledPane[] titledPanes = new TitledPane[_model.getAllDoctorsAndOrhtoptists().size()];

       /* // setup listviews
        int i = 0;
        for (IUser u : _model.getAllDoctorsAndOrhtoptists()) {
            ListView<IPatient> listView = new ListView<>();
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
                        _model.addPatientTab((IPatient) source.getSelectionModel().getSelectedItem());
                    }
                }
            });

            //put the entries of the Queue from User u into the List
            ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List)_model.getEntriesFromQueue(u));
            ObservableList<IPatient> olist = FXCollections.observableArrayList();

            for(QueueEntry entry : entries){
                olist.add(entry.getPatient());
            }

            // Queue titlepane string - Header of the Titled panel
            String queuename;
            if (u.getTitle() == null || u.getTitle().equals("null") || u.getTitle().equals("")) {
                queuename = u.getFirstName() + " " + u.getLastName();
            } else {
                queuename = u.getTitle() + " " + u.getFirstName() + " " + u.getLastName();
            }
            queuename = queuename + " (" + olist.size()+")";

            // bind listview to titledpanes
            listView.setItems(olist);
            listView.setPrefHeight(olist.size() * 30);

            titledPanes[i] = new TitledPane(queuename, listView);
            titledPanes[i].setExpanded(false);
            titledPanes[i].setAnimated(true);
            titledPanes[i].setVisible(true);

            i++;
        }*/
        //titledPanes[0].setExpanded(true);
       // vboxQueues.getChildren().addAll(titledPanes);

    }



    /*refresh queue after adding or removing patient*/
   /* public void refreshQueue(IUser user) {
        //ObservableList observableList = _userOListMap.get(user);
        //the entries of the Queue from the given user
        ObservableList observableList = FXCollections.observableList((List) _model.getQueueFromUser(user));

        if (observableList != null) {
            observableList.remove(0, observableList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }

        IPatientQueue  queue = _model.getQueueFromUser(user);

        /*try {
            if (user instanceof IDoctor) {
                queue = ((IDoctor) user).getQueue();
            } else {
                queue = ((IOrthoptist) user).getQueue();
            }

        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }*/

        //warum doppelt?
       /* observableList.remove(0, observableList.size());

        try {
            for(IQueueEntry iQueueEntry : queue.getEntries()) {
                observableList.add(iQueueEntry.getPatient());
            }
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }*/

    /*    _model.refreshQueue(user);


        //ListView list = _listViewMap.get(user);
        ListView list = (ListView) FXCollections.observableList((List) _model.getEntriesFromQueue(user));
        list.setPrefHeight(observableList.size() * 24);
    }*/
}
