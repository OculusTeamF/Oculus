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
import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationProtolException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotRemovePatientFromQueueException;
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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karo on 01.05.2015.
 */
public class Model implements Serializable, ILogger{

    private Stage _primaryStage = null;

    private static Model _modelInstance;
    private StartupController _startupController;
    private SearchPatientController _searchPatientController = new SearchPatientController();
    private CreatePatientController _createPatientController = new CreatePatientController();
    private CreateDiagnosisController _createDiagnosisController;
    private CheckinController _checkinController = new CheckinController();
    private ReceivePatientController _recievePatientController = new ReceivePatientController();
    private Collection<IDoctor> _doctors;
    private Collection<IUser> _userlist;
    private IPatient _patient;
    private IExaminationProtocol _eximationprotocol;
    //private IPatientQueue _queue = null;
    private TabPane _tabPanel = null;
    //private TitledPane _queueTitledPane[];
    private VBox _vBoxQueues;
    private Task<Void> task;

    private HashMap<IUser, ObservableList> _userWaitingList;
    private HashMap<IUser, ListView> _listViewMap;
    private HashMap<IUser, TitledPane> _queueTitledPaneFromUser;
    private HashMap<String, IPatient> _patientsInQueue;

    // user management
    private IUser _loggedInUser;
    private Tab _selectedTab;
    private HashMap<Tab, IPatient> _tabmap;

    /**
     * Singelton of Model
     * The Collection of Doctors and Orthoptist is fetched from DB only once
     */
    private Model(){

        _startupController = new StartupController();

        try {
            _doctors = _startupController.getAllDoctors();
            _userlist = _startupController.getAllDoctorsAndOrthoptists();
            _tabmap = new HashMap();
            _queueTitledPaneFromUser = new HashMap<>();
            _patientsInQueue = new HashMap<>();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - (Tab loading error) Please contact support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - (Tab loading error) Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - (Tab loading error) Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - (Tab loading error) Please contact support");
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
     * Tab: opensPatientRecordTab for selected patient
     * @param patient
     */
    public void addPatientTab(IPatient patient){

        this._patient = patient;
        loadTab("PATIENT: " + patient.getFirstName() + " " + patient.getLastName(), "fxml/PatientRecordTab.fxml");
    }

    /**
     * Tab: opens DiagnosisTab for selected patient
     * @param patient
     */
    public void addDiagnosisTab(IPatient patient){

        this._patient = patient;
        loadTab("NEW DIAGNOSIS: " + patient.getFirstName() + " " + patient.getLastName() ,"fxml/DiagnosisTab.fxml");
    }

    /**
     * Tab: opens ExaminationTab for selected patient
     * @param patient
     */
    public void addExaminationTab(IPatient patient){

        this._patient = patient;
        loadTab("PATIENT: " + getPatient().getLastName(), "fxml/ExaminationTab.fxml");
    }

    /**
     * Tab: opens PrescriptionTab for selected patient
     * @return
     */
    public void addPrescriptionTab(IPatient patient){
        this._patient = patient;
        loadTab("PATIENT: " + getPatient().getLastName(), "fxml/PrescriptionTab.fxml");
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

    /**
     * search Patients by lastname, firstname or svn, detailed search
     * @param svn
     * @param fname
     * @param lname
     * @return Collection<IPatient>
     */
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
    public Collection<IDiagnosis> getAllDiagnoses(){
        Collection<IDiagnosis> allDiagnoses = null;
        try {
            allDiagnoses = getPatient().getDiagnoses();
        } catch (CouldNotGetDiagnoseException e) {
            e.printStackTrace();
        }

        return allDiagnoses;
    }

    // *******************************************************************
    // Queue methods
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
                        loadTab("PATIENT RECORD: " + _patient.getLastName(), "fxml/PatientRecordTab.fxml");
                    }
                }
            });

            //put the entries of the Queue from User u into the List
            ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List) getEntriesFromQueue(u));

            ObservableList<HBoxCell> olist = FXCollections.observableArrayList();
            for(QueueEntry entry : entries){

                _patientsInQueue.put(entry.getPatient().toString(), entry.getPatient());
                olist.add(new HBoxCell(entry, u));
            }

            // bind listview to titledpanes
            listView.setItems(olist);
            listView.setPrefHeight((olist.size() * 35));


            _userWaitingList.put(u, olist);
            _listViewMap.put(u, listView);

            // Queue titlepane string - Header of the Titled panel
             String queueName = buildTitledPaneHeader(u, olist.size());

            TitledPane queueTitledPane = new TitledPane(queueName, listView);
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
        ObservableList<QueueEntry> entries = FXCollections.observableArrayList((List) getEntriesFromQueue(user));

        //ObservableList<HBoxCell> olist = FXCollections.observableArrayList();
        for(QueueEntry entry : entries){

            _patientsInQueue.put(entry.getPatient().toString(), entry.getPatient());
            entryList.add(new HBoxCell(entry, user));
        }

        ListView newList = _listViewMap.get(user);
        newList.setPrefHeight(entries.size() * 24);

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
     * returns the Queue from the given User
     * @param user
     * @return IPatientQueue
     */
    public IPatientQueue getQueueFromUser(IUser user){

        IPatientQueue queue = null;

        try {
            queue = _startupController.getQueueByUser(user);
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
        return queue;
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
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        } catch (CouldNotAddPatientToQueueException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CouldNotAddPatientToQueueException - Please contact support");
        }
        refreshQueue(user);
    }

    /**
     * removes the patient from the queue and opens a new examinationTab
     * @param patient
     */
    public void removePatientFromQueue(IPatient patient, IUser user) {

        IPatientQueue queue = getQueueFromUser(user);

        try {
            _recievePatientController.removePatientFromQueue(patient, queue);
        } catch (CouldNotRemovePatientFromQueueException couldNotRemovePatientFromQueueException) {
            couldNotRemovePatientFromQueueException.printStackTrace();
        }

        addExaminationTab(patient);
        refreshQueue(user);
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
    // Examinationprotocol methods
    // *******************************************************************


    /**
     * returns a List with all Examinationprotcols from the given patient
     * @param patient
     * @return Collection<IExaminationProtocol>
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
    public void newExaminationProtocol(Date startdate, Date enddate, String examinationDocumentation,IPatient patient, IDoctor doctor, IOrthoptist orthoptist) {
        try {
            _recievePatientController.createNewExaminationProtocol(startdate, enddate, examinationDocumentation,patient,doctor,orthoptist);
        } catch (CouldNotAddExaminationProtocol couldNotAddExaminationProtocol) {
            couldNotAddExaminationProtocol.printStackTrace();
        }
    }


    /**
     * creates a new examinationprotocol
     */
    public void addNewPatientDiagnosis(String title, String description, IDoctor doc){
        _createDiagnosisController = CreateDiagnosisController.CreateController(_eximationprotocol);

        try {
            _createDiagnosisController.createDiagnosis(title,description, doc);
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (RequirementsUnfulfilledException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequirementsUnfulfilledException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
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



    // *******************************************************************
    // produces the content of the ListView items for Waiting List
    // *******************************************************************

    public static class HBoxCell extends HBox{

        IQueueEntry _entry = null;
        Button _button = new Button();
        Label _label;
        IUser _user;

        HBoxCell(IQueueEntry entry, IUser user ){
            super();

            _entry = entry;
            _user = user;
            Image imageEnqueue = new Image(getClass().getResourceAsStream("/res/arrow1_klein.png"));

            _button.setGraphic(new ImageView(imageEnqueue));
            _button.setTooltip(new Tooltip("remove Patient from Waiting List"));

            /**
             * removes the patient from the Waiting List
             */
            _button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Model.getInstance().removePatientFromQueue(_entry.getPatient(), _user);
                }
            });

            _label = new Label(entry.getPatient().toString());
            _label.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(_label, Priority.ALWAYS);
            this.getChildren().addAll(_label, _button);
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

