package at.oculus.teamf.application.facade;

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
    public PatientEntity getPatientBySocialInsuranceNumber(String socialInsuranceNumber){
        Collection <Class> collection = new LinkedList<Class>();
        collection.add(PatientEntity.class);
        Facade facade = Facade.getInstance(collection);
        PatientEntity patient = (PatientEntity) facade.getEnity(PatientEntity.class, 1);


        /*Collection <PatientEntity> patients = getAll(PatientEntity.class);

        if ((patient = searchPatientBySocialInsuranceNumber(patients, socialInsuranceNumber)) != null){
            return patient;
        } else {
            return null;
        }*/

        return null;

    }

    /*
    this method will search in a given collection for the patient with the given social insurance number and return it
    if found, else it will return null
     */
    private PatientEntity searchPatientBySocialInsuranceNumber(Collection<PatientEntity> patients, String socialInsuranceNumber){
        for (PatientEntity patient : patients){
            if (patient.getSocialInsuranceNr().equals(socialInsuranceNumber)){
                return patient;
            }
        }
        return null;
    }

    public Collection <QueueEntity> getQueues(){
        return null;
    }

}
