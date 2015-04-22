package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.CheckinControllerException;
import at.oculus.teamf.application.facade.exceptions.PatientNotFoundException;
import at.oculus.teamf.application.facade.exceptions.QueueNotFoundException;
import at.oculus.teamf.application.facade.exceptions.UserNotFoundException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IUser;
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
    public void insertPatientIntoQueue(IPatient ipatient, IUser iuser) throws CheckinControllerException {
        Patient patient = (Patient) ipatient;
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
        PatientQueue queue = null;
        Doctor doctor = null;
        Orthoptist orthoptist = null;

        if(user instanceof Doctor){
            doctor = (Doctor) iuser;
            queue = doctor.getQueue();
            log.info("Queue belongs to doctor.");
        }else if(user instanceof Orthoptist){
            orthoptist = (Orthoptist) iuser;
            queue = orthoptist.getQueue();
            log.info("Queue belongs to orthoptist.");
        }

        if(queue != null){
            queue.addPatient(patient, doctor, orthoptist, tstamp);
            log.info("Patient has been added to correct queue.");
        }else{
            log.warn("Queue can not be null");
            throw new QueueNotFoundException();
        }
    }
}
