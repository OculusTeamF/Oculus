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
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

/**
 * Created by jpo2433 on 13.04.15.
 */
public class CreatePatientController {

    /*this method returns a new instance of a Patient, so that a new Patient could be created*/
    public Patient createPatient(){
        Patient patient = new Patient();
        return patient;
    }

    /*with this method the Patient Collection gets up to date*/
    public void savePatient(Patient patient)throws RequirementsNotMetException{

        if(checkRequirements(patient)){
            Facade facade = Facade.getInstance();
            try {
                facade.save(patient);
            } catch (FacadeException e) {
                e.printStackTrace();
                //TODO
            }
        }else{
            throw new RequirementsNotMetException();
        }

    }

    /*in this method the data gets checked, if all fields are complete everything is alright. Some requirements are missing TODO ;) */
    private boolean checkRequirements(Patient patient) {
        if(patient.getSvn() == null || patient.getLastName() == null || patient.getFirstName() == null || patient.getGender() == null){
            return false;
        }else{
            return true;
        }
    }

}
