/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.facade;

import at.oculus.teamf.databaseconnection.session.*;
import at.oculus.teamf.domain.entity.DomainEntity;
import at.oculus.teamf.persistence.broker.Broker;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.broker.IEntityBroker;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Facade for requesting entities from hibernate. Singleton pattern.
 *
 * @author san
 * @version 0.1
 */
public class Facade {
	private static Facade _self;
	private Collection<IEntityBroker> _entityBroker;
	private ISessionBroker _sessionBroker;

	/**
	 * Constructor setting the hibernate classes.
	 *
	 * @param clazzes
	 * 		hibernate classes
	 */
	private Facade(Collection<Class> clazzes) {
		_sessionBroker = new HibernateSessionBroker(clazzes);
	}

	/**
	 * Constructor setting the hibernate classes and the entity broker. not in use now
	 *
	 * @param clazzes
	 * 		hibernate classes
	 * @param broker
	 * 		entity broker
	 */
	private Facade(Collection<Class> clazzes, Collection<IEntityBroker> broker) {
		this(clazzes);
		_entityBroker = broker;
	}

	/**
	 * Return facade singleton.
	 *
	 * @param clazzes
	 * 		hibernate classes
	 *
	 * @return facade singleton
	 */
	public static Facade getInstance(Collection<Class> clazzes) { //}, Collection<IEntityBroker> broker) {
		if (_self == null) {
			_self = new Facade(clazzes);
		}
		return _self;
	}

	/**
	 * returns object with requested id
	 *
	 * @param clazz
	 * 		requested object class
	 * @param id
	 * 		primary key in database of the entity
	 *
	 * @return object of class
	 *
	 * @throws ClassNotFoundException
	 * @throws BadSessionException
	 * @throws ClassNotMappedException
	 */
	public DomainEntity getObjectById(Class clazz, int id)
			throws ClassNotFoundException, BadSessionException, ClassNotMappedException, IllegalAccessException,
			       InstantiationException {
		// read annotation with entity class name
		at.oculus.teamf.domain.entity.EntityClass entityClass = (at.oculus.teamf.domain.entity.EntityClass) clazz
				.getAnnotation(at.oculus.teamf.domain.entity.EntityClass.class);
		// request and return entity from broker
		Broker broker = new Broker();
		DomainEntity entity = (DomainEntity) clazz.newInstance();
		entity.set(broker.getEntity(Class.forName(entityClass.value()), id));
		return entity;
	}

	public Collection getObjectCollection(DomainEntity entity, Class collectionClazz)
			throws ClassNotFoundException, BadSessionException, ClassNotMappedException {
		// read annotation with entity class name
		at.oculus.teamf.domain.entity.EntityClass entityClass =
				(at.oculus.teamf.domain.entity.EntityClass) entity.getClass().getAnnotation(
						at.oculus.teamf.domain.entity.EntityClass.class);
		// request and return entity from broker
		Broker broker = new Broker();
		return broker.getCollection(Class.forName(entityClass.value()), entity.getId(), collectionClazz);
	}

	public void save(DomainEntity entity)
			throws ClassNotFoundException, IllegalAccessException, ClassNotMappedException, InstantiationException,
			       AlreadyInTransactionException, BadSessionException, NoTransactionException {
		// read annotation with entity class name
		at.oculus.teamf.domain.entity.EntityClass entityClass =
				(at.oculus.teamf.domain.entity.EntityClass) entity.getClass().getAnnotation(
						at.oculus.teamf.domain.entity.EntityClass.class);
		// request and return entity from broker
		Broker broker = new Broker();
		broker.setEntity(Class.forName(entityClass.value()), entity);
	}
}