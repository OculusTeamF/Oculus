/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamE.domain.readonly.IRDiagnosisTb2;
import at.oculus.teamE.presentation.ViewLoaderTb2;
import at.oculus.teamE.presentation.controllers.ExaminationDataWidgetController;
import at.oculus.teamE.presentation.controllers.MedicineAttachDialog;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.presentation.view.DialogBoxController;
import at.oculus.teamf.technical.loggin.ILogger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Fabian on 10.05.2015.
 */
public class TabModel implements ILogger {

    private static TabModel _tabmodel = new TabModel();
    private Model _model;

    private HashMap<Tab, IPatient> _tabmap;
    private Tab _selectedTab;
    private TabPane _tabPanel;

    private IPatient _tabinitpatient;

    private TabModel(){
        _tabmap = new HashMap();
    }

    public static TabModel getInstance() {
        if(_tabmodel == null) {
            _tabmodel = new TabModel();
        }

        return _tabmodel;
    }

    // team E integration
    // private ViewLoaderTb2<MedicineEditFormViewController> exMedicationTeamE = new ViewLoaderTb2<>(MedicineEditFormViewController.class);
    // private ViewLoaderTb2<ExaminationCreationFormViewController> exCreateDetailTeamE = new ViewLoaderTb2<>(ExaminationCreationFormViewController.class);
    private ViewLoaderTb2<ExaminationDataWidgetController> exDetailTeamE = new ViewLoaderTb2<>(ExaminationDataWidgetController.class);
    // private ViewLoaderTb2<ExaminationsListViewController> exDetailTeamE = new ViewLoaderTb2<>(ExaminationsListViewController.class);

    /* set the Tabs for the TabPanel */
    public void setTabPanel(TabPane tabpanel){ _tabPanel = tabpanel;}

    // *****************************************************************************************************************
    //
    // LOAD AND ADD NEW TABS
    //
    // *****************************************************************************************************************
    public void loadTab(String tabTitle, String tabFXML, String ID)
    {
        _model = Model.getInstance();

        // check if tab is already opened
        Tab checktab = checkIfTabIsAlreadyOpened(ID);

        if (checktab == null) {
            try {
                // add new tab
                Tab tab = new Tab(tabTitle);

                // tab management
                _selectedTab = tab;
                tab.setId(ID);

                // load tab fxml
                String pathTabFXML = "/at/oculus/teamf/presentation/view/" + tabFXML;
                AnchorPane ap = FXMLLoader.load(this.getClass().getResource(pathTabFXML));
                tab.setContent(ap);


                // setup tab tab close event
                tab.setOnCloseRequest(new EventHandler<Event>() {
                    @Override
                    public void handle(Event t) {
                        if (DialogBoxController.getInstance().showYesNoDialog("Close tab ?", "Do you want to cancel the current progress ?") == false) {
                            t.consume();
                        } else {
                            removeTabMapEntry(_selectedTab);
                        }
                    }
                });

                setTabMapEntry(tab, _tabinitpatient);
                _tabPanel.getTabs().add(tab);               // add tab to pane
                _tabPanel.getSelectionModel().select(tab);  // switch to new tab
                log.debug("New Tab added: " + getSelectedTab().getText() + " to TabPanel " + _tabPanel.getId());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                DialogBoxController.getInstance().showErrorDialog("IOException", "Please contact support");
            }
        } else {
            // switch to already opened tab
            switchToTab(checktab);
        }
    }

    public IPatient getInitPatient(){ return _tabinitpatient; }


    // *****************************************************************************************************************
    //
    // TAB HASHMAP OPERATIONS (FOR TAB IDENTIFICATION & MANEGMENT)
    //
    // *****************************************************************************************************************

    // check if tab exits with given id
    public Tab getTabFromPatientAndID(String id, IPatient patient){
        id = id + patient.getSocialInsuranceNr();
        for (Tab key : _tabmap.keySet()) {
            if (key.getId().equals(id)){
                IPatient pat =  _tabmap.get(key);
                if (patient.equals(pat)){
                    return key;
                }
            }
        }
        return null;
    }

    // check if same tabtype is already openened (for same patient)
    private Tab checkIfTabIsAlreadyOpened(String ID){
        for (Tab key : _tabmap.keySet()) {
            if (key.getId().equals(ID)){
                return key;
            }
        }
        return null;
    }

    // return patient from current selected tab
    public IPatient getPatientFromSelectedTab(Tab tab){
        IPatient pat = _tabmap.get(tab);
        return pat;
    }

    public void closeSelectedTab(){
        _tabPanel.getTabs().removeAll(_selectedTab);
    }

    public void closeSelectedAndSwitchTab(Tab switchtab){
        // close and remove current tab
        removeTabMapEntry(_selectedTab);
        _tabPanel.getTabs().removeAll(_selectedTab);
        // switch to given tab
        _selectedTab = switchtab;
        _tabPanel.getSelectionModel().select(switchtab);
    }

    public void switchToTab(Tab tab){
        _tabPanel.getSelectionModel().select(tab);
    }

    public void setSelectedTab(Tab tab){
        _selectedTab = tab;
        log.debug("SET SELECTED TAB: " + getSelectedTab().getText() + " / ID: " + getSelectedTab().getId());
    }

    public void setTabMapEntry(Tab tab, IPatient pat){  _tabmap.put(tab,pat); }

    public void removeTabMapEntry(Tab tab){
        _tabmap.remove(tab);
        log.debug("REMOVE TAB ENTRY: " + tab.getId());
    }

    public Tab getSelectedTab(){ return  _selectedTab; }

    // *****************************************************************************************************************
    //
    // ADD NEW TABS METHODS
    //
    // opens:   PatientRecord
    //          Diagnosis (for NewExamination)
    //          Examination (Archived protocols)
    //          NewExamination
    //          Prescription
    //          Search (for detail patient search)
    //          NewPatient (adding new patient to database)
    //
    // *****************************************************************************************************************

    public void addPatientRecordTab(IPatient patient)
    {
        _model = Model.getInstance();
        _model._patient = patient;
        _tabinitpatient = patient;
        String tabid = "patientrecord" + patient.getSocialInsuranceNr();
        loadTab("RECORD: " + patient.getLastName(), "fxml/PatientRecordTab.fxml", tabid);
    }

    public void addDiagnosisTab(IPatient patient)
    {
        _model = Model.getInstance();
        _model._patient = patient;
        _tabinitpatient = patient;
        loadTab("DIAGNOSIS: " + patient.getFirstName() + " " + patient.getLastName() ,"fxml/DiagnosisTab.fxml", "diagnosis" + patient.getSocialInsuranceNr());
    }

    public void addExaminationTab(IPatient patient){
        _model = Model.getInstance();
        _model._patient = patient;
        _tabinitpatient = patient;
        String tabid = "protocols" + patient.getSocialInsuranceNr();
        loadTab("PROTOCOLS: " +  patient.getLastName(), "fxml/ExaminationTab.fxml", tabid);
    }

    public void addPrescriptionTab(IPatient patient)
    {
        _model = Model.getInstance();
        _model._patient = patient;
        _tabinitpatient = patient;
        String tabid = "prescription" + patient.getSocialInsuranceNr();
        loadTab("PRESCRIPTIONS: " +  patient.getLastName(), "fxml/PrescriptionTab.fxml", tabid);
    }

    public void addNewExaminationTab(IPatient patient)
    {
        _model = Model.getInstance();
        _model._patient = patient;
        _tabinitpatient = patient;
        String tabid = "newexamination" + patient.getSocialInsuranceNr();
        loadTab("EXAMINATION: " + patient.getLastName(), "fxml/NewExaminationTab.fxml", tabid);
    }

    public void addSearchTab(){
        loadTab("Search patient", "fxml/SearchPatientTab.fxml", "search");
    }

    public void addNewPatientTab(){
        loadTab("Add new patient", "fxml/NewPatientTab.fxml", "newpatient");
    }

    // *****************************************************************************************************************
    //
    // TEAM E INTEGRATION
    //
    // *****************************************************************************************************************


    public void addNewExaminationEntryTab(IPatient patient)
    {
        _model._patient = patient;
        _tabinitpatient = patient;
        Tab tab = new Tab("EXAMINATION DETAILS: " + patient.getLastName());
        // tab management
        _selectedTab = tab;
        tab.setId("examinationdetails" + patient.getSocialInsuranceNr());

        Node newnode = exDetailTeamE.loadNode();
        tab.setContent(newnode);

        setTabMapEntry(tab, _tabinitpatient);
        _tabPanel.getTabs().add(tab);               // add tab to pane
        _tabPanel.getSelectionModel().select(tab);  // switch to new tab
    }

    public void showMedicineAttachDialog(IDiagnosis diag){
        new MedicineAttachDialog<>((IRDiagnosisTb2)diag).showAndWait();
    }
}
