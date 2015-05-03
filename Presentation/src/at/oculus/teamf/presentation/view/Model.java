/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.application.facade.*;
import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.application.facade.exceptions.RequirementsUnfulfilledException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.exception.CouldNotAddExaminationProtocol;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationProtolException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Karo on 01.05.2015.
 */
public class Model implements Serializable, ILogger{

    private Stage _primaryStage = null;


    private static Model _modelInstance;
    private StartupController _startupController = new StartupController();
    private SearchPatientController _searchPatientController = new SearchPatientController();
    private CreatePatientController _createPatientController = new CreatePatientController();
    private CreateDiagnosisController _createDiagnosisController;
    private CheckinController _checkinController = new CheckinController();
    private ReceivePatientController _recievePatientController = new ReceivePatientController();
    private Collection<IDoctor> _doctors;
    private Collection<IUser> _userlist;
    private IPatient _patient;
    private IExaminationProtocol _eximationprotocol;
    private IPatientQueue _queue = null;
    private TabPane _tabPanel = null;
    private TitledPane _queueTitledPane[];
    private VBox _vBoxQueues;
    private Task<Void> task;
    private HashMap<IUser, ObservableList> _userWaitingList;
    private HashMap<IUser, ListView> _listViewMap;
    private HashMap<IUser, TitledPane> _queueTitledPaneFromUser = new HashMap<>();
    private HashMap<String, IPatient> _patientsInQueue = new HashMap<>();

    // user management
    private IUser _loggedInUser;
    private Tab _selectedTab;
    private HashMap<Tab, IPatient> _tabmap;

    /**
     * Singelton of Model
     * The Collection of Doctors and Orthoptist is fetched from DB only once
     */
    private Model(){
        try {
            _doctors = _startupController.getAllDoctors();
            _userlist = _startupController.getAllDoctorsAndOrthoptists();
            _tabmap = new HashMap();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
        } catch (CriticalClassException e) {
            e.printStackTrace();
        }
    }

    public static Model getInstance(){
        if(_modelInstance == null)
        {
            _modelInstance = new Model();
        }
        return _modelInstance;
    }

    /**
     * set the primaryStage
     */
    public void setPrimaryStage(Stage primaryStage){
        this._primaryStage = primaryStage;
    }

    // *******************************************************************
    // Tab methods
    // *******************************************************************

    /**
     * set the Tabs for the TabPanel
     */
    public void setTabPanel(TabPane tabpanel){
        this._tabPanel = tabpanel;
    }
    /**
     * Controlls the Tabs of the panels
     */
    public void loadTab(String tabTitle, String tabFXML){
        try {
            Tab tab = new Tab(tabTitle);
            AnchorPane ap = FXMLLoader.load(this.getClass().getResource(tabFXML));
            tab.setContent(ap);

            // setup tab hashmap
            _selectedTab = tab;
            //Main.controller.updateStatusbar();
            setTabMapEntry(tab, _patient);

            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event t) {
                    removeTabMapEntry(_selectedTab);
                }
            });

            _tabPanel.getTabs().add(tab);               // add tab to pane
            _tabPanel.getSelectionModel().select(tab);  // switch to new tab
            log.debug("New Tab added: " + getSelectedTab().getText());
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - (Tab loading error) Please contact support");
        }
    }

    public void closeSelectedTab(){
        _tabPanel.getTabs().removeAll(_selectedTab);
        removeTabMapEntry(_selectedTab);
    }

    public void setSelectedTab(Tab tab){
        _selectedTab = tab;
        log.debug("Switched Tab to: " + getSelectedTab().getText());
    }
    public Tab getSelectedTab(){ return _selectedTab; }

    public void setTabMapEntry(Tab tab, IPatient pat){ _tabmap.put(tab,pat); }

    public void removeTabMapEntry(Tab tab){ _tabmap.remove(tab); }

    public IPatient getPatientFromSelectedTab(Tab tab){
        IPatient pat = _tabmap.get(tab);
        return pat;
    }

    /**
     * Tab: opens DiagnosisTab for selected patient
     */
    public void addPatientTab(IPatient patient){
        this._patient = patient;
        loadTab("PATIENT: " + patient.getFirstName() + " " + patient.getLastName(), "fxml/PatientRecordTab.fxml");
    }

    /**
     * Tab: opens patient record for selected patient
     */
    public void addDiagnosisTab(IPatient patient){

        this._patient = patient;
        loadTab("NEW DIAGNOSIS: " + patient.getFirstName() + " " + patient.getLastName() ,"fxml/DiagnosisTab.fxml");
    }


    // *******************************************************************
    // User methods - should not be used by the controllers
    // *******************************************************************

    public Collection<IDoctor> getAllDoctors(){ return _doctors; }
    public Collection<IUser> getAllDoctorsAndOrthoptists(){ return _userlist; }

    // *******************************************************************
    // Patient methods
    // *******************************************************************

    public IPatient getPatient(){
        return this._patient;
    }
    public void setPatient(IPatient setPatient) { this._patient = setPatient; }

    /**
     * Returns Calendar Events of a specific patient
     */
    public Collection<CalendarEvent> getCalendarEvents(){

        Collection<CalendarEvent> events = null;
        try {
            events = _patient.getCalendarEvents();
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "InvalidReloadClassException - Please contact support");
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "ReloadInterfaceNotImplementedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (CouldNotGetCalendarEventsException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CouldNotGetCalendarEventsException - Please contact support");
        }
        return events;
    }

    /**
     * search Patients by given Text
     */
    public Collection<IPatient> searchPatients(String text){

        Collection<IPatient> searchresults = null;
        try {
            searchresults = _searchPatientController.searchPatients(text);
        }  catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        } catch (InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "InvalidSearchParameterException - Please contact support");
        }

        return searchresults;
    }
    public Collection<IPatient> searchPatients(String svn, String fname, String lname){

        Collection<IPatient> searchresults = null;

        try {
            searchresults = _searchPatientController.searchPatients(svn, fname, lname);
        }  catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }

        return searchresults;
    }

    /**
     * save changes in the PatientRecord of the Patient
     * @param patient
     */
    public void savePatient(IPatient patient){

        try {
            _createPatientController.saveIPatient(patient);
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequrementsNotMetException - Please contact support");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }
    }

    /**
     * saves a new Patient object
     * @param gender
     * @param lastname
     * @param firstname
     * @param svn
     * @param bday
     * @param street
     * @param postalcode
     * @param city
     * @param phone
     * @param email
     * @param doctor
     * @param countryIsoCode
     * @return
     */
    public boolean saveNewPatient(String gender, String lastname, String firstname, String svn, Date bday, String street, String postalcode, String city, String phone, String email, IDoctor doctor, String countryIsoCode){

        try {
            _createPatientController.createPatient(gender, lastname, firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
            return true;
        } catch (RequirementsNotMetException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequirementsNotMetException - Please contact support.");
        } catch (PatientCouldNotBeSavedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "PatientCouldNotBeSavedException - Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }
        return false;
    }

    /**
     * returns all Diagnoses Titles from a given Patient
     * @return
     */
    public Collection getDiagnoses(){

        //TODO: Method from application
        Collection allDiagnoses = null;

        /*try {
           allDiagnoses = _recievePatientController.getAllDiagnoses(_patient);
        } catch (CouldNotGetExaminationProtolException e) {
            e.printStackTrace();
        }*/

        return allDiagnoses;
    }

    // *******************************************************************
    // Queue methods
    // *******************************************************************


    public void buildQueueLists(){

       // _queueTitledPane = new TitledPane[_userlist.size()];
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
                        setPatient((IPatient) source.getSelectionModel().getSelectedItem());
                        loadTab("PATIENT RECORD: " + getPatient().getLastName(), "fxml/PatientRecordTab.fxml");
                        //addPatientTab((IPatient) source.getSelectionModel().getSelectedItem());
                    }
                }
            });

            //put the entries of the Queue from User u into the List
            ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List) getEntriesFromQueue(u));
            //ObservableList<IPatient> olist = FXCollections.observableArrayList();

            ObservableList<HBoxCell> olist = FXCollections.observableArrayList();
            for(QueueEntry entry : entries){

                _patientsInQueue.put(entry.getPatient().toString(), entry.getPatient());
                olist.add(new HBoxCell(entry));
            }

            // Queue titlepane string - Header of the Titled panel
            String queuename = buildTitledPaneHeader(u, olist.size());

            // bind listview to titledpanes
            listView.setItems(olist);
            listView.setPrefHeight((olist.size() * 24) + 8);

            _userWaitingList.put(u, olist);
            _listViewMap.put(u, listView);

            // Queue titlepane string - Header of the Titled panel
            String queuename = buildTitledPaneHeader(u, olist.size());

            TitledPane queueTitledPane = new TitledPane(queuename, listView);
            queueTitledPane.setExpanded(false);
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
        ObservableList<IPatient> observableList = _userWaitingList.get(user);

        if (observableList != null) {
            observableList.remove(0, observableList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }

        //the new, actual Queue from the given user
        IPatientQueue queue = getQueueFromUser(user);

        observableList.remove(0, observableList.size());

        //fill Waitinglist with the actual data
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

        //New Header of Titledpane
        TitledPane userTitledPane = _queueTitledPaneFromUser.get(user);
        String header = buildTitledPaneHeader(user, observableList.size());
        userTitledPane.setText(header);
        userTitledPane.setExpanded(true);
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
     * returns the Queue from the given User
     * @param user
     * @return IPatientQueue
     */
    public IPatientQueue getQueueFromUser(IUser user){

        try {
            _queue = _startupController.getQueueByUser(user);
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
        return _queue;
    }

    /**
     * returns a Collection of the QueueEntries of the UserQueue
     * @param user
     * @return Collection<QueueEntry>
     */
    public Collection<QueueEntry> getEntriesFromQueue(IUser user) {

        Collection<QueueEntry> entries = null;

        IPatientQueue queue = getQueueFromUser(user);
        try {
            entries = queue.getEntries();
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }
        return entries;
    }

    /**
     * insert the Patient into the right Queue from given User
     * @param user
     */
    public void insertPatientIntoQueue(IUser user){

        try {
            _checkinController.insertPatientIntoQueue(_patient,user);
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showInformationDialog("Error", "Patient already in Waitinglist.");
        } catch (CheckinControllerException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CheckinControllerException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
        } catch (CouldNotAddPatientToQueueException e) {
            e.printStackTrace();
        }
        refreshQueue(user);
    }



    public void setQueueTitledPane(TitledPane[] pane)
    {
        _queueTitledPane = new TitledPane[_userlist.size()];
        this._queueTitledPane = pane;
    }

    public void setVboxQueues(VBox vboxQueues){
        this._vBoxQueues = vboxQueues;
    }

    // *******************************************************************
    // Examinationprotocol methods
    // *******************************************************************

    /**
     * returns a List with all Examinationprotcols from the given patient
     */
    public Collection<IExaminationProtocol> getAllExaminationProtcols(IPatient patient){

        Collection<IExaminationProtocol> protocols = null;

        try {
            protocols = _recievePatientController.getAllExaminationProtocols(patient);
        } catch (CouldNotGetExaminationProtolException e) {
            e.printStackTrace();
        }

        return protocols;
    }

    /**
     * creates a new examinationprotocol
     */
    public void newExaminationProtocol(Date date, String examinationDocumentation,IPatient patient, IDoctor doctor, IOrthoptist orthoptist) {

        try {
            _recievePatientController.createNewExaminationProtocol(date, examinationDocumentation, patient, doctor, orthoptist);
        }  catch (CouldNotAddExaminationProtocol couldNotAddExaminationProtocol) {
            couldNotAddExaminationProtocol.printStackTrace();
        }
    }

    /**
     * creates a new examinationprotocol
     */
    public void addNewPatientDiagnosis(String title, String description, IDoctor doc){
        //TODO IExaminationProtocol getter setter
        _createDiagnosisController = new CreateDiagnosisController(_eximationprotocol);

        try {
            _createDiagnosisController.createDiagnosis(title,description, doc);
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (RequirementsUnfulfilledException e) {
            e.printStackTrace();
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
        } catch (CriticalClassException e) {
            e.printStackTrace();
        }
    }

    public IExaminationProtocol getCurrentExaminationProtocol(){
        return _eximationprotocol;
    }

    public void setCurrentExaminationProtocol(IExaminationProtocol ep){
        _eximationprotocol = ep;
    }


    // *******************************************************************
    // user management
    // *******************************************************************

    public void setLoggedInUser(IUser user){
        _loggedInUser = user;
    }

    public IUser getLoggedInUser(){
        return _loggedInUser;
    }


    /**
     * produces the content of the ListView items
     */
    public static class HBoxCell extends HBox{

        QueueEntry entry = null;
        Button _button = new Button();
        Label _label;

        HBoxCell(QueueEntry entry ){
            super();
            Image imageEnqueue = new Image(getClass().getResourceAsStream("/res/arrow1_klein.png"));
            _button.setGraphic(new ImageView(imageEnqueue));
            _button.setTooltip(new Tooltip("remove Patient from Waiting List"));
            _label = new Label(entry.getPatient().toString());
            _label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(_label, Priority.ALWAYS);
            this.getChildren().addAll(_label, _button);
        }


    }
}

