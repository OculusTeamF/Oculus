/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.persistence.entity.IEntity;

import java.util.Collection;

/**
 * Created by Norskan on 30.03.2015.
 */
public abstract class EntityBroker<T> {

    private Class _domainClass;
    private Class<T> _entityClass;

    public EntityBroker(Class entityClass) {
        _entityClass = entityClass;
    }

    //<editor-fold desc="Abstract Methode">
    public abstract IEntity getEnity(Class<T> clazz, int id);

    public abstract Collection<IEntity> getAll(Class<T> clazz);

    public abstract boolean saveEntity(T entity);

    public abstract boolean saveAll(Collection<T> collection);
    //</editor-fold>

    //<editor-fold desc="Getter">
    public Class getDomainClass() {
        return _domainClass;
    }

    public Class getEntityClass() {
        return _entityClass;
    }
    //</editor-fold>

}