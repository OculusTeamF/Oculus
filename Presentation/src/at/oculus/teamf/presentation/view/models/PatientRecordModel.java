/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.facade.exceptions.PatientCouldNotBeSavedException;
import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetDiagnoseException;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.presentation.view.DialogBoxController;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Fabian on 10.05.2015.
 */
public class PatientRecordModel {

    private static PatientRecordModel _patientmodel = new PatientRecordModel();
    private Model _model;

    public static PatientRecordModel getInstance() {
        if(_patientmodel == null) {
            _patientmodel = new PatientRecordModel();
        }

        return _patientmodel;
    }

    /**
     * saves a updated Patient object
     */
    public void savePatient(IPatient patient){
        _model = Model.getInstance();
        try {
            _model.getCreatePatientController().saveIPatient(patient);
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
     * creates & saves a new Patient object
     */
    public boolean saveNewPatient(String gender, String lastname, String firstname, String svn, Date bday, String street, String postalcode, String city, String phone, String email, IDoctor doctor, String countryIsoCode){
        _model = Model.getInstance();
        try {
            _model.getCreatePatientController().createPatient(gender, lastname, firstname, svn, bday, street, postalcode, city, phone, email, doctor, countryIsoCode);
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
     * Returns Calendar Events of a specific patient
     */
    public Collection<ICalendarEvent> getCalendarEvents(){

        Collection<ICalendarEvent> events = null;
        try {
            System.out.println(_model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab()).getLastName());
            events = _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab()).getCalendarEvents();
        } catch (CouldNotGetCalendarEventsException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CouldNotGetCalendarEventsException - Please contact support");
        }
        return events;
    }

    /**
     * returns all Diagnoses Titles from a given Patient
     * @return
     */
    public Collection<IDiagnosis> getAllDiagnoses(){
        Collection<IDiagnosis> allDiagnoses = null;
        try {
            allDiagnoses = _model.getTabModel().getPatientFromSelectedTab(_model.getTabModel().getSelectedTab()).getDiagnoses();
        } catch (CouldNotGetDiagnoseException e) {
            e.printStackTrace();
        }

        return allDiagnoses;
    }

}
