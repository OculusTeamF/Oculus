/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by jpo2433 on 08.04.15.
 */
public class SearchPatientController{

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

    private Collection <Patient> getPatientBySocialInsuranceNumber(Facade facade, String socialInsuranceNumber){

        Collection<Patient> patients = new LinkedList<Patient>();
        try {
            patients = facade.getAll(Patient.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

        Collection <Patient> selectedPatients = new LinkedList<Patient>();

        for(Patient patient: patients){
            if (socialInsuranceNumber.equals(patient.getSvn())){
                selectedPatients.add(patient);
            }
        }
        return selectedPatients;
    }

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
