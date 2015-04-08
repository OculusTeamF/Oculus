/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.facade;

import at.oculus.teamf.databaseconnection.session.HibernateSessionBroker;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.ISessionBroker;
import at.oculus.teamf.persistence.broker.EntityBroker;
import at.oculus.teamf.persistence.broker.ICollectionReload;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Facade for requesting entities from hibernate. Singleton pattern.
 *
 * //TODO: add Exception, docs
 * @author san
 * @version 0.1
 */
public class Facade {
	private static Facade _self;
	private HashMap<Class, EntityBroker> _entityBrokers;
	private ISessionBroker _sessionBroker;

	private Facade() { }

	public static Facade getInstance() { //}, Collection<EntityBroker> broker) {
		if (_self == null) {
			_self = new Facade();
		}
		return _self;
	}

	public void init(Collection<EntityBroker> brokers) {
		if(_entityBrokers != null) {
			//TODO: throw exception
		}

		_entityBrokers = new HashMap<Class, EntityBroker>();
		Collection<Class> entityClazzes = new LinkedList<Class>();

		for(EntityBroker broker : brokers) {
			_entityBrokers.put(broker.getDomainClass(), broker);
			entityClazzes.add(broker.getEntityClass());
		}

		_sessionBroker = new HibernateSessionBroker(entityClazzes);
	}

	public Object getById(Class clazz, int id) {
		ISession session = _sessionBroker.getSession();
		Object obj = _entityBrokers.get(clazz).getEntity(session, id);
		_sessionBroker.releaseSession(session);
		return obj;
	}

	public Collection<Object> getAll(Class clazz) {
		ISession session = _sessionBroker.getSession();
		Collection<Object> objs = _entityBrokers.get(clazz).getAll(session);
		_sessionBroker.releaseSession(session);
		return objs;
	}

	public void reloadCollection(Object entity, Class clazz) throws CantReloadException {
		EntityBroker broker = _entityBrokers.get(clazz);

		if(!(broker instanceof ICollectionReload)) {
			throw new CantReloadException();
		}

		ISession session = _sessionBroker.getSession();
		((ICollectionReload) broker).reload(session, entity, clazz);
		_sessionBroker.releaseSession(session);
	}

	public boolean save(Object entity) {
		ISession session = _sessionBroker.getSession();
		Boolean result = _entityBrokers.get(entity.getClass()).saveEntity(session, entity);
		_sessionBroker.releaseSession(session);
		return result;
	}
}