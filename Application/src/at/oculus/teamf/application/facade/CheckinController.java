package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

/**
 * Created by Norskan on 02.04.2015.
 */
public class CheckinController {

    /* this method inserts a patient into the queue of the specified user. If the user doesn't have a queue,
    * TODO*/
    public PatientQueue insertPatientIntoQueue(Patient patient, User user){
        Facade facade = Facade.getInstance();

        PatientQueue queue = null;
        try {
            queue = facade.getById(PatientQueue.class, user.getUserId());
        } catch (FacadeException e) {
            e.printStackTrace();
            //if null ?? was machma denn do??
        }

        //queue.insert(patient);//was mach ma denn do?

        try {
            facade.save(queue);
        } catch (FacadeException e) {
            e.printStackTrace();
        }

        return queue;
    }

}
