/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.facade.*;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IQueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import at.oculus.teamf.technical.loggin.ILogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karo on 01.05.2015.
 */
public class Model implements Serializable, ILogger{

    private Stage _primaryStage = null;

    private static Model _modelInstance;
    private TabModel _tabmodel;
    private PatientRecordModel _patientmodel;
    private QueueModel _queuemodel;
    private ExaminationModel _examinationmodel;
    private SearchModel _searchmodel;

    private StartupController _startupController;
    private SearchPatientController _searchPatientController = new SearchPatientController();

    private CreatePatientController _createPatientController = new CreatePatientController();
    private CheckinController _checkinController = new CheckinController();
    private ReceivePatientController _recievePatientController = new ReceivePatientController();
    private Collection<IDoctor> _doctors;
    private Collection<IUser> _userlist;
    public IPatient _patient;
    private VBox _vBoxQueues;
    private Task<Void> task;

    private HashMap<IUser, ObservableList> _userWaitingList;
    private HashMap<IUser, ListView> _listViewMap;
    private HashMap<IUser, TitledPane> _queueTitledPaneFromUser;
    private HashMap<String, IPatient> _patientsInQueue;

    // user management
    private IUser _loggedInUser;


    /**
     * Singelton of Model
     */
    private Model(){
        _startupController = new StartupController();
        _tabmodel = TabModel.getInstance();
        _patientmodel = PatientRecordModel.getInstance();
        _queuemodel = QueueModel.getInstance();
        _examinationmodel = ExaminationModel.getInstance();
        _searchmodel = SearchModel.getInstance();

        try {
            _doctors = _startupController.getAllDoctors();
            _userlist = _startupController.getAllDoctorsAndOrthoptists();
            _queueTitledPaneFromUser = new HashMap<>();
            _patientsInQueue = new HashMap<>();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - (Initialization error) Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - (Initialization error) Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - (Initialization error) Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - (Initialization error) Please contact support");
        }
    }

    public static Model getInstance(){
        if(_modelInstance == null) {
            _modelInstance = new Model();
        }
        return _modelInstance;
    }

    // *******************************************************************
    // Return Models
    // *******************************************************************

    public TabModel getTabModel(){ return _tabmodel; }
    public PatientRecordModel getPatientModel(){ return _patientmodel; }
    public QueueModel getQueueModel(){ return _queuemodel; }
    public ExaminationModel getExaminationModel(){ return _examinationmodel; }
    public SearchModel getSearchModel() { return _searchmodel; }
    public void setPrimaryStage(Stage primaryStage){ _primaryStage = primaryStage; }

    // *******************************************************************
    // Return Application Controllers
    // *******************************************************************

    public CreatePatientController getCreatePatientController() { return _createPatientController; }
    public StartupController getStartupController() { return _startupController; }
    public CheckinController getCheckinController() { return _checkinController; }
    public ReceivePatientController getReceivePatientController() { return _recievePatientController; }
    public SearchPatientController getSearchPatientController() { return _searchPatientController; }

    // *******************************************************************
    // User methods - should not be used by the controllers
    // *******************************************************************

    public Collection<IDoctor> getAllDoctors(){ return _doctors; }
    public Collection<IUser> getAllDoctorsAndOrthoptists(){ return _userlist; }

    // *******************************************************************
    // user management
    // *******************************************************************

    public void setLoggedInUser(IUser user){
        _loggedInUser = user;
    }
    public IUser getLoggedInUser(){
        return _loggedInUser;
    }

    // *******************************************************************
    // Patient methods
    // TODO: remove getpatient & setpatient (handled by tabmodel)
    // *******************************************************************

    public IPatient getPatient(){ return _patient; }
    public void setPatient(IPatient setPatient) { this._patient = setPatient; }

    // *******************************************************************
    // Queue methods
    // TODO: move 'buildQueueLists' & 'refreshQueue' to QueueController
    // *******************************************************************
    public void buildQueueLists(){

        _userWaitingList = new HashMap<>();
        _listViewMap = new HashMap<>();

        // setup listviews
        int i = 0;
        for (IUser u : _userlist) {
            ListView<HBoxCell> listView = new ListView<>();
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
                        HBoxCell cell = (HBoxCell) source.getSelectionModel().getSelectedItem();
                        setPatient(cell.getQueueEntry().getPatient());
                        _tabmodel.addPatientRecordTab(_patient);
                    }
                }
            });

            //put the entries of the Queue from User u into the List
            ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List) QueueModel.getInstance().getEntriesFromQueue(u));

            ObservableList<HBoxCell> olist = FXCollections.observableArrayList();
            for(QueueEntry entry : entries){

                _patientsInQueue.put(entry.getPatient().toString(), entry.getPatient());
                olist.add(new HBoxCell(entry, u));
            }

            // bind listview to titledpanes
            listView.setItems(olist);
            listView.setPrefHeight((olist.size() * 38));

            _userWaitingList.put(u, olist);
            _listViewMap.put(u, listView);

            // Queue titlepane string - Header of the Titled panel
             String queueName = buildTitledPaneHeader(u, olist.size());

            TitledPane queueTitledPane = new TitledPane(queueName, listView);
            if (olist.size() > 0 ){
                queueTitledPane.setExpanded(true);
            } else {
                queueTitledPane.setExpanded(false);
            }
            queueTitledPane.setAnimated(true);
            queueTitledPane.setVisible(true);

            _queueTitledPaneFromUser.put(u,queueTitledPane);

            i++;

            //adds the titledPanes to the Vbox, index 3 in params means that the titledPanes will be added
            //after the search elements
            _vBoxQueues.getChildren().add(3, queueTitledPane);
        }
    }

    /**
     *
     * @return
     */
    public void refreshQueue(IUser user) {

        //the entries of the Queue from the given user with is not actual
        ListView<HBoxCell> list = _listViewMap.get(user);
        ObservableList<HBoxCell> entryList = list.getItems();


        if (entryList != null) {
            entryList.remove(0, entryList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }

        _patientsInQueue.clear();
        _userWaitingList.clear();
        //_queueTitledPaneFromUser.remove(user);

        //actual list from the given user //hier ist problem!!!
        ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List) QueueModel.getInstance().getEntriesFromQueue(user));

        //ObservableList<HBoxCell> olist = FXCollections.observableArrayList();
        for(QueueEntry entry : entries){

            _patientsInQueue.put(entry.getPatient().toString(), entry.getPatient());
            entryList.add(new HBoxCell(entry, user));
        }

        ListView newList = _listViewMap.get(user);
        newList.setPrefHeight(entries.size() * 38);

        _userWaitingList.put(user, entryList);
        _listViewMap.put(user, newList);

        //New Header of Titledpane
        TitledPane userTitledPane = _queueTitledPaneFromUser.get(user);
        String header = buildTitledPaneHeader(user, entries.size());
        userTitledPane.setText(header);
        userTitledPane.setExpanded(true);


        /*//the entries of the Queue from the given user which is not actual
        ObservableList<IPatient> observableList = _userWaitingList.get(user);

        if (observableList != null) {
            observableList.remove(0, observableList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }*/


    }

    /**
     * returns the title of the TitledPanes in the Waiting List
     * @param user
     * @param sizeOfQueue
     * @return String titleOfTitledPanes
     */
    private String buildTitledPaneHeader(IUser user, int sizeOfQueue)
    {
        String queuename;
        if (user.getTitle() == null || user.getTitle().equals("null") || user.getTitle().equals("")) {
            queuename = user.getFirstName() + " " + user.getLastName();
        } else {
            queuename = user.getTitle() + " " + user.getFirstName() + " " + user.getLastName();
        }
        queuename = queuename + " (" + sizeOfQueue+")";

        return queuename;
    }

    /**
     * unused
     * @param
     */
    /*public void setQueueTitledPane(TitledPane[] pane)
    {
        _queueTitledPane = new TitledPane[_userlist.size()];
        this._queueTitledPane = pane;
    }*/

    /**
     * is used by QueueController to set the vBoxes. VBoxes are nessecary for the buildQueueLists method
     * @param vboxQueues
     */
    public void setVboxQueues(VBox vboxQueues){
        this._vBoxQueues = vboxQueues;
    }


    // *******************************************************************
    // produces the content of the ListView items for Waiting List
    // *******************************************************************

    public static class HBoxCell extends HBox {

        IQueueEntry _entry = null;
        Button _startexaminationbutton = new Button();
        Button _deletebutton = new Button();
        Button _openbutton = new Button();
        Label _label;
        IUser _user;

        HBoxCell(IQueueEntry entry, IUser user ){
            super();

            _entry = entry;
            _user = user;
            Image imageEnqueue = new Image(getClass().getResourceAsStream("/res/icon_start.png"));
            Image imageDelete = new Image(getClass().getResourceAsStream("/res/icon_delete.png"));
            Image imageOpen = new Image(getClass().getResourceAsStream("/res/icon_open.png"));

            _startexaminationbutton.setGraphic(new ImageView(imageEnqueue));
            _startexaminationbutton.setTooltip(new Tooltip("Start Examination and remove patient from queue"));
            _startexaminationbutton.setCursor(Cursor.HAND);
            _startexaminationbutton.setId("queueButton");

            _deletebutton.setGraphic(new ImageView(imageDelete));
            _deletebutton.setTooltip(new Tooltip("Delete patient from queue without examination"));
            _deletebutton.setCursor(Cursor.HAND);
            _deletebutton.setId("queueDeleteButton");

            _openbutton.setGraphic(new ImageView(imageOpen));
            _openbutton.setTooltip(new Tooltip("Open and view patient record"));
            _openbutton.setCursor(Cursor.HAND);
            _openbutton.setId("queueOpenButton");

            /**
             * removes the patient from the Waiting List
             */
            _startexaminationbutton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    QueueModel.getInstance().removePatientFromQueue(_entry.getPatient(), _user);
                }
            });

            _deletebutton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (DialogBoxController.getInstance().showYesNoDialog("Delete queue entry ?", "Remove patient from queue without examination ?") == true){
                        QueueModel.getInstance().deletePatientFromQueue(_entry.getPatient(), _user);
                    }
                }
            });

            _openbutton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    TabModel.getInstance().addPatientRecordTab(_entry.getPatient());
                }
            });

            // button hover
            _startexaminationbutton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _startexaminationbutton.setText("Start Examination");
                    _startexaminationbutton.setContentDisplay(ContentDisplay.RIGHT);
                    _startexaminationbutton.setTextAlignment(TextAlignment.LEFT);
                }
            });
            _startexaminationbutton.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _startexaminationbutton.setText("");
                }
            });

            _deletebutton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _deletebutton.setText("Delete from queue");
                    _deletebutton.setContentDisplay(ContentDisplay.RIGHT);
                    _deletebutton.setTextAlignment(TextAlignment.LEFT);
                }
            });
            _deletebutton.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _deletebutton.setText("");
                }
            });

            _openbutton.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _openbutton.setText("Open patient record");
                    _openbutton.setContentDisplay(ContentDisplay.RIGHT);
                    _openbutton.setTextAlignment(TextAlignment.LEFT);
                }
            });
            _openbutton.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    _openbutton.setText("");
                }
            });

            _label = new Label(entry.getPatient().toString());
            _label.setMaxWidth(Double.MAX_VALUE);
            _label.setMinHeight(30);

            HBox.setHgrow(_label, Priority.ALWAYS);
            this.getChildren().addAll(_label,_openbutton, _deletebutton , _startexaminationbutton);
        }

        /**
         * saves the QueueEntry to the HBoxCell
         * @return the QueueEntry of the HBoxCell which is doubleclicked in WaitingList
         */
        public IQueueEntry getQueueEntry(){
            return this._entry;
        }

        public IUser getUserFromQueueEntry(){
            return this._user;
        }
    }
}

