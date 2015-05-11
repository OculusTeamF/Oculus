/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

/**<h1>$OpticalAidPrescriptionController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $30.04.2015
 *
 * <b>Description:</b>
 * This File contains the OpticalAidPrescriptionController class,
 * which is responsible for the creation of a new optical aid prescription and to save it into the database
 **/

import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;

/**
 * <h2>$OpticalAidPrescriptionController</h2>
 *
 * <b>Description:</b>
 * With this controller a new optical aid prescription can be created.
 **/
public class OpticalAidPrescriptionController implements ILogger{

    private IPatient _iPatient;

    private OpticalAidPrescriptionController(IPatient iPatient){
            _iPatient = iPatient;
    }

    public static OpticalAidPrescriptionController createController(IPatient iPatient) throws NoPatientException {
        if(iPatient == null){
            throw new NoPatientException();
        }
        return new OpticalAidPrescriptionController(iPatient);
    }
}
