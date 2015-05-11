/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.presentation.view.DialogBoxController;
import at.oculus.teamf.technical.loggin.ILogger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Date;
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

    private TabModel(){
        _tabmap = new HashMap();
    }

    public static TabModel getInstance() {
        if(_tabmodel == null) {
            _tabmodel = new TabModel();
        }

        return _tabmodel;
    }

    /**
     * set the Tabs for the TabPanel
     */
    public void setTabPanel(TabPane tabpanel){ _tabPanel = tabpanel;}
    /**
     * Controlls the Tabs of the panels
     */
    public void loadTab(String tabTitle, String tabFXML, String ID){
        _model = Model.getInstance();
        try {
            Tab tab = new Tab(tabTitle);
            _selectedTab = tab;
            tab.setId(ID);

            String pathTabFXML = "../" + tabFXML;
            AnchorPane ap = FXMLLoader.load(this.getClass().getResource(pathTabFXML));
            tab.setContent(ap);


            // setup tab hashmap
            setTabMapEntry(tab, _model._patient);

            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event t) {
                    if (DialogBoxController.getInstance().showYesNoDialog("Close tab ?", "Do you want to cancel the current progress ?") == false) {
                        t.consume();
                    } else {
                        removeTabMapEntry( _selectedTab);
                    }
                }
            });

            _tabPanel.getTabs().add(tab);               // add tab to pane
            _tabPanel.getSelectionModel().select(tab);  // switch to new tab
            log.debug("New Tab added: " + getSelectedTab().getText() + " to TabPanel " + _tabPanel.getId());
        } catch (IOException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "IOException - (Tab loading error) Please contact support");
        }
    }

    public void closeSelectedTab(){
        _tabPanel.getTabs().removeAll(_selectedTab);
        removeTabMapEntry( _selectedTab);
    }

    public void setSelectedTab(Tab tab){
        _selectedTab = tab;
        log.debug("Switched Tab to: " + getSelectedTab().getText());
    }

    public Tab getSelectedTab(){ return  _selectedTab; }

    public void setTabMapEntry(Tab tab, IPatient pat){  _tabmap.put(tab,pat); }

    public void removeTabMapEntry(Tab tab){  _tabmap.remove(tab); }

    public IPatient getPatientFromSelectedTab(Tab tab){
        IPatient pat =  _tabmap.get(tab);
        return pat;
    }

    /**
     * Tab: opensPatientRecordTab for selected patient
     * @param patient
     */
    public void addPatientRecordTab(IPatient patient){
        _model = Model.getInstance();
        _model._patient = patient;
        loadTab("RECORD: " + patient.getLastName(), "fxml/PatientRecordTab.fxml", "patientrecord" + patient.getSocialInsuranceNr());
    }

    /**
     * Tab: opens DiagnosisTab for selected patient
     * @param patient
     */
    public void addDiagnosisTab(IPatient patient){
        _model = Model.getInstance();
        _model._patient = patient;
        loadTab("DIAGNOSIS: " + patient.getFirstName() + " " + patient.getLastName() ,"fxml/DiagnosisTab.fxml", "diagnosis" + patient.getSocialInsuranceNr());
    }

    /**
     * Tab: opens ExaminationTab for selected patient
     * @param patient
     */
    public void addExaminationTab(IPatient patient){
        _model = Model.getInstance();
        _model._patient = patient;
        loadTab("PROTOCOLS: " +  _model.getPatient().getLastName(), "fxml/ExaminationTab.fxml", "examination" + patient.getSocialInsuranceNr());
    }

    /**
     * Tab: opens PrescriptionTab for selected patient
     * @return
     */
    public void addPrescriptionTab(IPatient patient){
        _model = Model.getInstance();
        _model._patient = patient;
        loadTab("PRESCRIPTIONS: " +  _model.getPatient().getLastName(), "fxml/PrescriptionTab.fxml", "prescription" + patient.getSocialInsuranceNr());
    }

    public void addNewExaminationTab(IPatient patient){
        Date date = new Date();
        _model = Model.getInstance();
        _model._patient = patient;
        loadTab("EXAMINATION: " + _model.getPatient().getLastName(), "fxml/NewExaminationTab.fxml","newexamination" + patient.getSocialInsuranceNr() );
    }

    public void addSearchTab(){
        loadTab("Search patient", "fxml/SearchPatientTab.fxml", "search");
    }

    public void addNewPatientTab(){
        loadTab("Add new patient", "fxml/NewPatientTab.fxml", "newpatient");
    }
}
