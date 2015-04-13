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
 * A short description of the File content
 * TODO
 **/
package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$SearchPatientController</h2>
 *
 * <b>Description:</b>
 * A short description of the class
 * TODO
 **/

public class SearchPatientController{

    /**
     *<h3>$searchPatients</h3>
     *
     * <b>Description:</b>
     *  A short description of the function
     *  TODO
     *
     *<b>Parameter</b>
     * @param svn description
     * @param lastName description
     * @param firstName description
     */

    public Collection <Patient> searchPatients (String svn, String lastName, String firstName){

        Facade facade = Facade.getInstance();

        Collection <Patient> patients = new LinkedList<Patient>();

        if (svn != null){
            patients = getPatientBySocialInsuranceNumber(facade, svn);
            if (patients.size() <= 1) {
                return patients;
            }
        } else if (lastName != null) {
            patients = getPatientByLastName(facade, lastName);
            if (patients.size() <= 1){
                return patients;
            } else {
                patients = getPatientByFirstName(facade, firstName, patients);
            }
        }
        return patients;
    }

    /**
     *<h3>$getPatientBySocialInsuranceNumber</h3>
     *
     * <b>Description:</b>
     *  A short description of the function
     *  TODO
     *
     *<b>Parameter</b>
     * @param facade description
     * @param svn description
     */

    private Collection <Patient> getPatientBySocialInsuranceNumber(Facade facade, String svn){

        Collection<Patient> patients = new LinkedList<Patient>();
        try {
            patients = facade.getAll(Patient.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

        Collection <Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (svn.equals(patient.getSvn())){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }

    /**
     *<h3>$getPatientByLastName</h3>
     *
     * <b>Description:</b>
     *  A short description of the function
     *  TODO
     *
     *<b>Parameter</b>
     * @param facade description
     * @param lastName description
     */

    private Collection <Patient> getPatientByLastName(Facade facade, String lastName){

        Collection<Patient> patients = new LinkedList<Patient>();
        try {
            patients = facade.getAll(Patient.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

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
     *  A short description of the function
     *  TODO
     *
     *<b>Parameter</b>
     * @param facade description
     * @param firstName description
     * @param patients description
     */

    private Collection<Patient> getPatientByFirstName(Facade facade, String firstName, Collection<Patient> patients) {

        Collection<Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (patient.getFirstName().equals(firstName)){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }

}
