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
 * This file contains all the methods which are necessary for the usecase SearchPatient. It also contains the class
 * SearchPatientController.
 **/

package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$SearchPatientController</h2>
 *
 * <b>Description:</b>
 * This class contains all the necessary methods for the usecase SearchPatient. These methods are searchPatient by
 * itself which will call the other methods. The other methods are being called depending on the different input
 * in the parameters.
 **/

public class SearchPatientController implements ILogger{

    /**
     *<h3>$searchPatients</h3>
     *
     * <b>Description:</b>
     *  The method searchPatients will at first fetch all available patients from the persistence layer and then search
     *  inside the collection with the other methods depending on its different input in the parameters. At first the
     *  method will search for patients by their social insurance number, afterwards by their last name and finally
     *  by their first name. The complete list will be returned if the list is bigger than 1. Else the list will be
     *  returned as soon as the list is <= 1;
     *
     *<b>Parameter</b>
     * @param svn description
     * @param lastName description
     * @param firstName description
     */

    public Collection <Patient> searchPatients (String svn, String lastName, String firstName){

        Facade facade = Facade.getInstance();

        Collection<Patient> patients = new LinkedList<Patient>();

        try {
            patients = facade.getAll(Patient.class);
        } catch (FacadeException e) {
            log.warn("Facade Exception caught!");
            e.printStackTrace();
            //TODO
        }

        Collection<Patient> selectedPatients = new LinkedList<Patient>();

        if (svn != null){
            selectedPatients = getPatientBySocialInsuranceNumber(patients, svn);
            if (selectedPatients.size() <= 1) {
                return selectedPatients;
            }
        }
        if (lastName != null) {
            selectedPatients = getPatientByLastName(patients, lastName);
            if (selectedPatients.size() <= 1){
                return selectedPatients;
            } else if (firstName != null){
                selectedPatients = getPatientByFirstName(selectedPatients, firstName);
                return selectedPatients;
            }
            return selectedPatients;
        }
        if (firstName != null){
            selectedPatients = getPatientByFirstName(patients, firstName);
            return selectedPatients;
        }
        return patients;
    }

    /**
     *<h3>$getPatientBySocialInsuranceNumber</h3>
     *
     * <b>Description:</b>
     * This method will fetch all the patients from the facade and then search for patients with a matching social
     * insurance number. If a match occurs the patient is added to a collection of patients and finally returned.
     *
     *<b>Parameter</b>
     * @param patients description
     * @param svn description
     */

    private Collection <Patient> getPatientBySocialInsuranceNumber(Collection <Patient> patients, String svn){

        Collection <Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (svn.equals(patient.getSocialInsuranceNr())){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }

    /**
     *<h3>$getPatientByLastName</h3>
     *
     * <b>Description:</b>
     * This method will most likely be called when there are no patients being found by the first method (social
     * insurance number). Again the facade will fetch all patients and the list will be iterated. Every time a patients
     * last name matches the search string the patient will be added to a new list which will be returned at the end.
     *
     *<b>Parameter</b>
     * @param patients description
     * @param lastName description
     */

    private Collection <Patient> getPatientByLastName(Collection<Patient> patients, String lastName){

        Collection<Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (patient.getLastName().equals(lastName)){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }

    /**
     *<h3>$getPatientByFirstName</h3>
     *
     * <b>Description:</b>
     *  This method is normally called when the previous method returns a list with more than 1 result. Also the
     *  parameter for the first name cannot be null. Then the collection of patients which was returned by the
     *  searchByLastName method will be searched for matching first names and added to a new collection. The collection
     *  will be returned.
     *
     *<b>Parameter</b>
     * @param patients description
     * @param firstName description
     */

    private Collection<Patient> getPatientByFirstName(Collection<Patient> patients, String firstName) {

        Collection<Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (patient.getFirstName().equals(firstName)){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }
}
