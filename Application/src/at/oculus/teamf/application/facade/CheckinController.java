package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.QueueNotFoundException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Norskan on 02.04.2015.
 */
public class CheckinController {

    /* this method inserts a patient into the queue of the specified user. If the user doesn't have a queue,
    * TODO*/
    public PatientQueue insertPatientIntoQueue(Patient patient, User user) throws QueueNotFoundException {
        Facade facade = Facade.getInstance();

        PatientQueue queue = null;
        try {
            queue = facade.getById(PatientQueue.class, user.getUserId());
        } catch (FacadeException e) {
            e.printStackTrace();
            throw new QueueNotFoundException();
            //TODO
        }

        //if queue is not null ...
        Timestamp tstamp = new Timestamp(new Date().getTime());

        if(user instanceof Doctor){
            queue.addPatient(patient, (Doctor)user, null, tstamp);
        }else if(user instanceof Orthoptist){
            queue.addPatient(patient, null, (Orthoptist)user, tstamp);
        }

        try {
            facade.save(queue);
        } catch (FacadeException e) {
            e.printStackTrace();
        }

        return queue;
    }

}
