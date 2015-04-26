/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade.additional;

/**<h1>$RemovePatientController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $22.04.15
 *
 * Description:
 * This file contains the removePatientController. It implements the posibility to remove a patient from the database.
 **/


import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.technical.loggin.ILogger;

/**
 * <h2>$RemovePatientController</h2>
 *
 * <b>Description:</b>
 * The Checkin-Controller is to realise the first UseCase. It implements the method insertPatientIntoQueue.
 **/
public class RemovePatientController implements ILogger{

    /**
     *<h3>$insertPatientIntoQueue</h3>
     *
     * <b>Description:</b>
     * This method inserts a patient into the queue of the specified user. If the user doesn't have a queue,
     * a QueueNotFoundException is thrown. If there is a queue, the patient and the user is given to the queue to save.
     *
     *<b>Parameter</b>
     * @param iPatient this parameter shows the interface of the patient which should be removed from the database
     *
     */
    public void removePatientFromDatabase (IPatient iPatient) throws FacadeException {
        Facade facade = Facade.getInstance();
        try {
            facade.delete((Patient) iPatient);
            log.info("Patient has been deleted.");
        } catch (FacadeException facadeException) {
            log.warn("FacadeException caught! Facade could not delete patient!");
            throw facadeException;
        }
    }
}
