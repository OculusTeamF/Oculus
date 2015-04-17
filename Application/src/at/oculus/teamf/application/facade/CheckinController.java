package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.PatientNotFoundException;
import at.oculus.teamf.application.facade.exceptions.QueueNotFoundException;
import at.oculus.teamf.application.facade.exceptions.UserNotFoundException;
import at.oculus.teamf.domain.entity.*;
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
 * The CheckinController is to realise the first usecase. It implements the method insertPatientIntoQueue.
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
     * @param patient this parameter shows the patient which should be added to the queue of the specified user
     * @param user the patient should be added to the queue of this user
     * @param queue this is the queue, to which the patient should be added
     *
     */
    public void insertPatientIntoQueue(Patient patient, User user, PatientQueue queue) throws CheckinControllerException {

        if(queue == null) {
            log.warn("Queue can not be null!");
            throw new QueueNotFoundException();
        }
        if(user == null){
            log.warn("User can not be null!");
            throw new UserNotFoundException();
        }
        if(patient == null){
            log.warn("Patient can not be null!");
            throw new PatientNotFoundException();
        }

        Timestamp tstamp = new Timestamp(new Date().getTime());
        if(user instanceof Doctor){
            queue.addPatient(patient, (Doctor)user, null, tstamp);
        }else if(user instanceof Orthoptist){
            queue.addPatient(patient, null, (Orthoptist)user, tstamp);
        }
    }
}
