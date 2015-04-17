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

import at.oculus.teamf.domain.entity.IPatient;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
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

    public Collection <IPatient> searchPatients (String svn, String lastName, String firstName){

        Facade facade = Facade.getInstance();
        Collection<Patient> patients = new LinkedList<Patient>();
        try {
            patients = facade.searchPatient(svn, firstName, lastName);
        } catch (FacadeException e) {
            log.warn("Facade Exception caught!");
            e.printStackTrace();
        }

        Collection<IPatient> selectedPatients = new LinkedList<IPatient>();
        for(Patient patient : patients){
            selectedPatients.add(patient);
        }

        return selectedPatients;
    }
}
