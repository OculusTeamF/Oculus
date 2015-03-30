package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.domain.entity.IEntity;

/**
 * Created by Norskan on 30.03.2015.
 */
public interface IEntityBroker {
    IEntity getEnity(Class clazz, int id);

    boolean setEntity(IEntity entity);
}
