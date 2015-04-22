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
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.IDoctor;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Date;


/**<h1>$CreatePatientController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $02.04.15
 *
 * <b>Description:</b>
 * This File contains the CreatePatientController class,
 * which is responsible for the creation of a new patient object and to save it into the database.
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
     *  This method creates a new patient-object, sets all given data und saves the new Patient in the database.
     *  If some Information is missing, then an exception is thrown.
     *
     *<b>Parameter</b>
     * @param gender the gender of the patient which should be created
     * @param svn the social insurance number of the patient which should be created
     * @param lastName the last name of the patient which should be created
     * @param firstName the first name of the patient which should be created
     * @param bday the birthday of the patient which should be created
     * @param street the street of the new patients address
     * @param postalCode the postal code of the new patients address
     * @param city the city of the new patients address
     * @param email the email address of the patient which should be created
     * @param phone the phone-number of the patient which should be created
     * @param doctor this is the doctor, who is referred to the new patient
     * @param countryIsoCode this is the country iso code of the patient, like AT, DE ...
     */

    public void createPatient(String gender, String lastName, String firstName, String svn, Date bday , String street, String postalCode, String city, String phone, String email, IDoctor doctor, String countryIsoCode) throws RequirementsNotMetException, FacadeException {
        Patient patient = new Patient();
        if(gender.equals("female")){
            patient.setGender(Gender.Female);
        }else if(gender.equals("male")){
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
        patient.setCountryIsoCode(countryIsoCode);
        patient.setDoctor((Doctor) doctor);

        try {
            savePatient(patient);
        } catch (FacadeException facadeException) {
            log.warn("FacadeException caught! Patient cannot be saved!");
            throw facadeException;
        }
    }

    /**
     *<h3>$savePatient</h3>
     *
     * <b>Description:</b>
     * This method gets the patient object which should be saved. The requirements were checked with another method
     * and if everything is alright, the object is given to the facade to save it into the database. Afterwards
     * the patient collection in the database should be up to date again.
     *
     *<b>Parameter</b>
     * @param patient this is the Patient-object, which should be saved in the database
     */
    private void savePatient(Patient patient) throws RequirementsNotMetException, FacadeException {

        if(checkRequirements(patient)){
            Facade facade = Facade.getInstance();
            try {
                facade.save(patient);
            } catch (FacadeException facadeException) {
                log.warn("FacadeException caught! Patient cannot be saved!");
                throw facadeException;

            }
        }else{
            log.warn("Requirements unfulfilled!");
            throw new RequirementsNotMetException();
        }

    }

    /**
     *<h3>$checkRequirements</h3>
     *
     * <b>Description:</b>
     * In this method the data gets checked. If all fields are complete - everything is alright and the method returns true.
     * If some required data is missing the method returns false.
     *
     *<b>Parameter</b>
     * @param patient this is the Patient-object, which should be checked before it is saved
     */
    private boolean checkRequirements(Patient patient) {
        if(patient.getSocialInsuranceNr().equals("") || patient.getLastName().equals("") || patient.getFirstName().equals("") ||
                patient.getBirthDay().equals(null)){
            return false;
        }else{
            return true;
        }
    }
}