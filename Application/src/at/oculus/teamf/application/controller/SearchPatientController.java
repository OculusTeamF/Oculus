/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

/**<h1>$SearchPatientController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $08.04.15
 *
 * Description:
 * This file contains all the methods which are necessary for the UseCase SearchPatient. It also contains the class
 * SearchPatientController.
 **/

package at.oculus.teamf.application.controller;

import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$SearchPatientController</h2>
 *
 * <b>Description:</b>
 * This class contains all the necessary methods for the UseCase SearchPatient.
 * This are only two methods, which are both called searchPatient(). The only difference are the given parameters.
 * In the first method only one parameter is necessary (with full text search) and in the other method
 * are three parameters available.
 **/

public class SearchPatientController implements ILogger{

    /**
     *<h3>$searchPatients</h3>
     *
     * <b>Description:</b>
     * This method gets one  parameter data. With the help of the facade,
     * we get a collection of selected Patients.
     * If no patient is found, an empty list is returned.
     * This collection is transformed into a Collection of Patient-Interfaces,
     * which can be returned to the presentation layer.
     *
     *<b>Parameter</b>
     * @param data this is all given information to search the patient (with full-text-search)
     */

    public Collection <IPatient> searchPatients (String data) throws BadConnectionException, CriticalClassException, InvalidSearchParameterException, CriticalDatabaseException {

        Facade facade = Facade.getInstance();
        Collection<IPatient> patients = new LinkedList<>();

        try {
            patients = facade.search(IPatient.class, "%" + data.replace(" ", "%") + "%");
        } catch (SearchInterfaceNotImplementedException | NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }

        Collection<IPatient> selectedPatients = new LinkedList<IPatient>();
        for(IPatient patient : patients){
            selectedPatients.add(patient);
        }
        log.info("All patients have been added to IPatient Collection.");

        return selectedPatients;
    }

    /**
     *<h3>$searchPatients</h3>
     *
     * <b>Description:</b>
     * This method gets the three parameters svn, lastName and firstName. With the help of the facade,
     * we get a collection of selected Patients. If no patient is found, an empty list is returned.
     * This collection is transformed into a Collection of Patient-Interfaces,
     * which can be returned to the presentation layer.
     *
     *<b>Parameter</b>
     * @param svn this is the social insurance number of the searched patient
     * @param firstName this is the first name of the searched patient
     * @param lastName this is the last name of the searched patient
     */

    public Collection<IPatient> searchPatients(String svn, String firstName, String lastName) throws InvalidSearchParameterException, BadConnectionException, CriticalClassException, CriticalDatabaseException {

        Facade facade = Facade.getInstance();
        Collection<IPatient> patients = new LinkedList<>();

        try {
            patients = facade.search(IPatient.class, "%" + svn + "%", "%" + firstName + "%", "%" + lastName + "%"); //Todo: remove %
        } catch (SearchInterfaceNotImplementedException | NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }

        Collection<IPatient> selectedPatients = new LinkedList<IPatient>();
        for(IPatient patient : patients){
            selectedPatients.add(patient);
        }
        log.info("All patients have been added to IPatient Collection.");

        return selectedPatients;
    }
}