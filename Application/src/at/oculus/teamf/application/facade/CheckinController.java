package at.oculus.teamf.application.facade;

import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.Facade;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 02.04.2015.
 */
public class CheckinController {

    public PatientEntity getPatient(int svn){
        Collection <Class> collection = new LinkedList<Class>();
        collection.add(PatientEntity.class);
        Facade facade = Facade.getInstance(collection);
        PatientEntity patient = (PatientEntity) facade.getEnity(PatientEntity.class);

        Collection <PatientEntity> patients;

        // = get all, search patient (svn), patient = found patient; return

        return patient;
    }


}
