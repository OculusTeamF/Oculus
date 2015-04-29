/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
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

    private SearchPatientController _searchPatientController;
    private StartupController _startupController;
    private Collection<IDoctor> _doctors;
    private Collection<IUser> _userlist;

    private HashMap<IUser, ObservableList> _userOListMap;
    private HashMap<ObservableList, IUser> _OListUserMap;
    private HashMap<IUser, ListView> _listViewMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // controller connection
        _searchPatientController = new SearchPatientController();
        _startupController = new StartupController();

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
                    Main.controller.addPatientTab((IPatient) listSearchResults.getSelectionModel().getSelectedItem());
                }
            }
        });

        ToolBar tbQueue = new ToolBar();
        tbQueue.setMinHeight(30);
        vboxQueues.getChildren().add(tbQueue);

        // build queue
        buildQueueLists();
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
    public void doPatientSearch() {
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
    // Queuelist
    // *******************************************************************

    /*load and setup queuelist for all users (on application load)*/
    public void buildQueueLists() {
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
                        Main.controller.addPatientTab((IPatient) source.getSelectionModel().getSelectedItem());
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
            } catch (final NullPointerException e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        DialogBoxController.getInstance().showExceptionDialog(e, "NullPointerException (Userqueue) - Please contact support");
                    }
                });
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
}
