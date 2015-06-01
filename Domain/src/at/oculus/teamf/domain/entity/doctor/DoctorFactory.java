package at.oculus.teamf.domain.entity.doctor;

import at.oculus.teamf.domain.entity.factory.DomainFactory;
import at.oculus.teamf.domain.entity.interfaces.IDomain;

/**
 * Created by Simon Angerer on 01.06.2015.
 */
public class DoctorFactory extends DomainFactory<IDoctor> {
    public DoctorFactory() {
        super(IDoctor.class);
    }

    @Override
    public IDoctor create() {
        return new Doctor();
    }
}
