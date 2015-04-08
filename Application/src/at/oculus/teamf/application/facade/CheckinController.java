package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.persistence.facade.Facade;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 02.04.2015.
 */
public class CheckinController {

    /* this method inserts a patient into the queue of the specified user. If the user doesn't have a queue,
    * TODO*/
    public PatientQueue insertPatientIntoQueue(Patient patient, User user){
        Facade facade = Facade.getInstance();

        PatientQueue queue = (PatientQueue) facade.getById(PatientQueue.class, user.getUserID());

        //if null ??
        queue.insert(patient);
        facade.save(queue);

        return queue;
    }

}
