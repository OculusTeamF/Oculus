/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.application.controller.exceptions.CheckinControllerExceptions.PatientNotFoundException;
import at.oculus.teamf.application.controller.exceptions.CheckinControllerExceptions.QueueNotFoundException;
import at.oculus.teamf.application.controller.exceptions.CheckinControllerExceptions.UserNotFoundException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.doctor.Doctor;
import at.oculus.teamf.domain.entity.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Date;

/**<h1>$CheckinController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $02.04.15
 *
 * Description:
 * This file contains the CheckinController. It implements the fist Usecase - "Check in at the reception".
 **/

/**
 * <h2>$CheckinController</h2>
 *
 * <b>Description:</b>
 * The Checkin-Controller is to realise the first UseCase. It implements the method insertPatientIntoQueue.
 **/
public class CheckinController implements ILogger {

    /**
     *<h3>$insertPatientIntoQueue</h3>
     *
     * <b>Description:</b>
     * This method inserts a patient into the queue of the specified user. If the user doesn't have a queue,
     * a QueueNotFoundException is thrown. If there is a queue, the patient and the user is given to the queue to save.
     *
     *<b>Parameter</b>
     * @param ipatient this parameter shows the interface of the patient which should be added to the queue of the specified user
     * @param iuser the patient should be added to the queue of this user
     *
     */
    public void insertPatientIntoQueue(IPatient ipatient, IUser iuser) throws CouldNotAddPatientToQueueException, UserNotFoundException, PatientNotFoundException, CriticalClassException, BadConnectionException, QueueNotFoundException {
        IPatient patient =ipatient;
        log.info("Patient object has been created and assigned from interface.");
        User user = (User) iuser;
        log.info("User object has been created and assigned from interface.");

        if(user == null){
            log.warn("User can not be null!");
            throw new UserNotFoundException();
        }
        if(patient == null){
            log.warn("Patient can not be null!");
            throw new PatientNotFoundException();
        }

        Timestamp tstamp = new Timestamp(new Date().getTime());
        IPatientQueue queue = null;
        IDoctor doctor = null;
        IOrthoptist orthoptist = null;

        if(user instanceof Doctor){
            doctor = (Doctor) iuser;

            queue = (PatientQueue) doctor.getQueue();

            log.info("Queue belongs to doctor.");
        }else if(user instanceof Orthoptist){
            orthoptist = (Orthoptist) iuser;
            try {
                queue = orthoptist.getQueue();
            } catch (NoBrokerMappedException e) {
                log.error("Major implementation error was found! " + e.getMessage());
                throw new CriticalClassException();
            }
            log.info("Queue belongs to orthoptist.");
        }

        if(queue != null){
            queue.addPatient(patient, tstamp);
            log.info("Patient has been added to correct queue.");
        }else{
            log.warn("Queue can not be null");
            throw new QueueNotFoundException();
        }
    }
}
