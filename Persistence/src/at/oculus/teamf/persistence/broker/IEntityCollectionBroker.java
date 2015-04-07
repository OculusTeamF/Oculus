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
 * IEntityCollectionBroker.java
 * Created by oculus on 06.04.15.
 */
public interface IEntityCollectionBroker {
    /**
     * Load a Collection into a Entity
     *
     * @param entity Object where the data has to be loaded into
     * @param clazz  Objecttype of collection which has to be loaded
     */
    void getCollection(IEntity entity, Class clazz);

    /**
     * Save a Collection into a Entity
     *
     * @param entity     Object where the data has to be saved into
     * @param collection ollection which has to be loaded
     */
    void setCollection(IEntity entity, Collection<IEntity> collection);
}

// TODO delete?