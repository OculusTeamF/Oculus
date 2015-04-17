/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.RequirementsNotMetException;
import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Date;


/**<h1>$CreatePatientController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $02.04.15
 *
 * Description: TODO
 * A short description of the File content
 **/

/**
 * <h2>$CreatePatientController</h2>
 *
 * <b>Description:</b>
 * With this controller a new patient can be created.
 * All the information given in the method createPatient is used to set up a new patient object,
 * but before the object ist created or saved, some important fields get checked if they are not empty,
 * afterwards this object is saved in the database
 **/
public class CreatePatientController implements ILogger{

    /**
     *<h3>$createPatient</h3>
     *
     * <b>Description:</b>
     *  this method creates a new Patient-Object, sets all given data und saves the new Patient in the database.
     *  If some Information is missing, then an exception is thrown.
     *
     *<b>Parameter</b>
     * @param gender description
     * @param svn description
     * @param lastName description
     * @param firstName description
     * @param bday description
     * @param street description
     * @param postalCode description
     * @param city description
     * @param email description
     * @param phone description
     */

    /*this method creates a new Patient-Object, sets all given data und saves the new Patient in the database.
    If some Information is missing, then an exception is thrown*/
    public void createPatient(String gender, String lastName, String firstName, String svn, Date bday , String street, String postalCode, String city, String phone, String email) throws RequirementsNotMetException {
        Patient patient = new Patient();
        if(gender.equals("female")){
            patient.setGender(Gender.Female);
        }else if(gender.equals(Gender.Male)){
            patient.setGender(Gender.Male);
        }
        patient.setLastName(lastName);
        patient.setFirstName(firstName);
        patient.setSocialInsuranceNr(svn);
        patient.setBirthDay(bday);
        patient.setStreet(street);
        patient.setPostalCode(postalCode);
        patient.setCity(city);
        patient.setPhone(phone);
        patient.setEmail(email);

        try {
            savePatient(patient);
        } catch (RequirementsNotMetException e) {
            log.warn("RequirementsNotMetException catched");
            throw new RequirementsNotMetException();
        }
    }

    /*with this method the Patient Collection gets up to date*/
    private void savePatient(Patient patient)throws RequirementsNotMetException{

        if(checkRequirements(patient)){
            Facade facade = Facade.getInstance();
            try {
                facade.save(patient);
            } catch (FacadeException e) {
                log.warn("FacadeException catched");
                e.printStackTrace();
            }
        }else{
            log.warn("Requirements unfulfilled");
            throw new RequirementsNotMetException();
        }

    }

    /*in this method the data gets checked, if all fields are complete everything is alright. Some requirements are missing TODO */
    private boolean checkRequirements(Patient patient) {
        if(patient.getSocialInsuranceNr() == "" || patient.getLastName() == "" || patient.getFirstName() == "" || patient.getGender() == null ||
                patient.getBirthDay() == null){
            return false;
        }else{
            return true;
        }
    }

}
