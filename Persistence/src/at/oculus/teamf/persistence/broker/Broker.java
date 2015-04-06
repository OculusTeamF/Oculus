/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.databaseconnection.session.*;
import at.oculus.teamf.domain.entity.DomainEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.IEntityCollection;

import java.util.Collection;

/**
 * Broker.java Created by oculus on 06.04.15.
 */
public class Broker {
	private ISessionBroker _sessionBroker;

	/**
	 * @param clazz
	 * 		hibernate class
	 * @param id
	 * 		primary key in database of the entity
	 *
	 * @return
	 *
	 * @throws BadSessionException
	 * @throws ClassNotMappedException
	 */
	public IEntity getEntity(Class clazz, int id) throws BadSessionException, ClassNotMappedException {
		ISession session = _sessionBroker.getSession();
		IEntity entity = (IEntity) session.getByID(clazz, id);
		_sessionBroker.releaseSession(session);
		return entity;
	}

	public Collection getCollection(Class clazzEntity, int id, Class clazzCollection)
			throws BadSessionException, ClassNotMappedException {
		ISession session = _sessionBroker.getSession();
		IEntityCollection entity = (IEntityCollection) session.getByID(clazzEntity, id);
		Collection collection = entity.getCollection(clazzCollection);
		_sessionBroker.releaseSession(session);
		return collection;
	}

	public void setEntity(Class clazz, DomainEntity entity)
			throws IllegalAccessException, InstantiationException, BadSessionException, AlreadyInTransactionException,
			       NoTransactionException, ClassNotMappedException {
		IEntity saveEntity = (IEntity) clazz.newInstance();
		saveEntity.set(entity);
		ISession session = _sessionBroker.getSession();
		session.beginTransaction();
		session.save(saveEntity);
		session.commit();
		_sessionBroker.releaseSession(session);
	}
}
