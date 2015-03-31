package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.IEntity;
import at.oculus.teamf.persistence.broker.IEntityBroker;

import java.util.Collection;

/**
 * Created by Norskan on 30.03.2015.
 */
public class Facade {

    public Facade(Collection<Class> clazzes, Collection<IEntityBroker> broker) {

    }

    IEntity getEnity(Class clazz, int id) {
        return null;
    }

    boolean setEntity(IEntity entity) {
        return false;
    }
}
