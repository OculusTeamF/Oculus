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
import at.oculus.teamf.application.facade.exceptions.InvalidSearchParameterException;
import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.domain.entity.CalendarEvent;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Karo on 01.05.2015.
 */
public class Model implements Serializable{

    private Stage _primaryStage = null;


    private static Model _modelInstance;
    private StartupController _startupController = new StartupController();
    private SearchPatientController _searchPatientController = new SearchPatientController();
    private CreatePatientController _createPatientController = new CreatePatientController();
    private CheckinController _checkinController = new CheckinController();
    private ReceivePatientController _recievePatientController = new ReceivePatientController();

    private Collection<IDoctor > _doctors;
    private Collection<IUser> _userlist;
    private IPatient _patient;
    private IPatientQueue _queue = null;
    private TabPane _tabPanel = null;



    /**
     * Singelton of Model
     */
    private Model(){}

    public static Model getInstance(){
        if(_modelInstance == null) {
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
            _tabPanel.getTabs().add(tab);
            _tabPanel.getSelectionModel().select(_tabPanel.getTabs().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - (Tab loading error) Please contact support");
        }
    }

    /**
     * Tab: opens patient record for selected patient
     */
    public void addPatientTab(IPatient patient){

        this._patient = patient;
        loadTab("Patient: " + patient.getFirstName() + " " + patient.getLastName(), "fxml/PatientRecordTab.fxml");
    }


    // *******************************************************************
    // User methods
    // *******************************************************************

    /**
     * Returns the List of all Doctors in a Collection of IDoctors
     * @return
     */
    public Collection<IDoctor> getAllDoctors(){

        try {
            _doctors =  _startupController.getAllDoctors();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        }
        return _doctors;
    }

    /**
     * Returns a Collection of all IDoctors and IOrthoptists
     * @return
     */
    public Collection<IUser> getAllDoctorsAndOrhtoptists(){

        try {
            _userlist = _startupController.getAllDoctorsAndOrthoptists();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        return _userlist;
    }

    // *******************************************************************
    // Patient methods
    // *******************************************************************

    /**
     * Returns an Object of the Patient
     */
    public IPatient getPatient(){
        return this._patient;
    }


    /**
     * Returns Calendar Events of a specific patient
     */
    public Collection<CalendarEvent> getCalendarEvents(){

        Collection<CalendarEvent> events = null;
        try {
            events = _patient.getCalendarEvents();
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
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
        } catch (InvalidSearchParameterException e) {
            e.printStackTrace();
        } catch (SearchInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        }

        return searchresults;
    }
    public Collection<IPatient> searchPatients(String svn, String fname, String lname){

        Collection<IPatient> searchresults = null;

        try {
            searchresults = _searchPatientController.searchPatients(svn, fname, lname);
        } catch (SearchInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        }

        return searchresults;
    }

    /**
     * saves changes in Patient
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
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        }
    }


    // *******************************************************************
    // Queue methods
    // *******************************************************************
    /**
     * gets the Queue from the given User
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
     * returns a Collection of QueueEntries of the Queue from the given User
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
     * insert Patient into Queue
     */
    public void insertPatientIntoQueue(IUser user){

        try {
            _checkinController.insertPatientIntoQueue(_patient,user);
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showInformationDialog("Error", "Patient already in Waitinglist.");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact support");
        } catch (CheckinControllerException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CheckinControllerException - Please contact support");
        }
    }

    /**
     *
     * @param user, which queue should be updated
     * @return
     */
    public ObservableList refreshQueue(IUser user) {

        //the entries of the Queue from the given user
        ObservableList observableList = FXCollections.observableList((List) getQueueFromUser(user));

        if (observableList != null) {
            observableList.remove(0, observableList.size());
        } else {
            DialogBoxController.getInstance().showErrorDialog("Error", "ObservableList == null");
        }

        IPatientQueue  queue = getQueueFromUser(user);

        observableList.remove(0, observableList.size());

        try {
            for(IQueueEntry iQueueEntry : queue.getEntries()) {
                observableList.add(iQueueEntry.getPatient());
            }
        } catch (NoBrokerMappedException | BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException, BadConnectionException - Please contact support");
        }

        return observableList;
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
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "InvalidReloadClassException - Please contact your support");
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "ReloadInterfaceNotImplementedException - Please contact your support");
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "NoBrokerMappedException - Please contact your support");
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact your support");
        }

        return protocols;
    }

    /**
     * creates a new examinationprotocol
     */
    public void newExaminationProtocol(Date date, String examinationDocumentation,IPatient patient, IDoctor doctor, IOrthoptist orthoptist){

        try {
            _recievePatientController.createNewExaminationProtocol(date, examinationDocumentation, patient, doctor, orthoptist);
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        }
    }

}
