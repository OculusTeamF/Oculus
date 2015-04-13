package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.QueueNotFoundException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.sql.Timestamp;
import java.util.Date;
/**<h1>$CheckinController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $02.04.15
 *
 * Description: TODO
 * A short description of the File content
 **/

/**
 * <h2>$CheckinController</h2>
 *
 * <b>Description:</b>
 * A short description of the class TODO
 **/
public class CheckinController {

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
     *
     */
    public void insertPatientIntoQueue(Patient patient, User user, PatientQueue queue) throws QueueNotFoundException {

        //if queue is not null ...
        Timestamp tstamp = new Timestamp(new Date().getTime());

        if(user instanceof Doctor){
            queue.addPatient(patient, (Doctor)user, null, tstamp);
        }else if(user instanceof Orthoptist){
            queue.addPatient(patient, null, (Orthoptist)user, tstamp);
        }

    }

}
