package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.facade.Facade;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 02.04.2015.
 */
public class CheckinController {

    /*
    first usecase controller method is to get a patient by his social insurance number
    for that we need the persistence layer to give us a list of all patients and then we search the correct one
    inside the application layer. afterwards the application layer returns the found patient (if found) or null (if not
    found).
     */
    public Patient getPatientBySocialInsuranceNumber(String socialInsuranceNumber){

        Facade facade = Facade.getInstance();

        Collection<Object> patients = new LinkedList<Object>();
        patients = facade.getAll(Patient.class);

        for(Object patient: patients){
            if (((Patient) patient).getSvn().equals(socialInsuranceNumber))
                return (Patient) patient;
            else {
                return null;
            }
        }
        return null;

    }

}
