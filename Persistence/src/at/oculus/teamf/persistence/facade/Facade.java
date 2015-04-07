/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.facade;

import at.oculus.teamf.databaseconnection.session.ISessionBroker;
import at.oculus.teamf.persistence.broker.EntityBroker;
import at.oculus.teamf.persistence.broker.ICollectionReload;

import java.util.Collection;
import java.util.HashMap;

/**
 * Facade for requesting entities from hibernate. Singleton pattern.
 *
 * @author san
 * @version 0.1
 */
public class Facade {
	private static Facade _self;
	private HashMap<Class, EntityBroker> _entityBrokers;
	private ISessionBroker _sessionBroker;

	private Facade(Collection<EntityBroker> broker) {

	}

	public static Facade getInstance(Collection<EntityBroker> brokers) { //}, Collection<EntityBroker> broker) {
		if (_self == null) {
			_self = new Facade(brokers);
		}
		return _self;
	}

	public Object getById(Class clazz, int id) {
		return _entityBrokers.get(clazz).getEnity(id);
	}

	public Collection getAll(Class clazz) {
		return _entityBrokers.get(clazz).getAll(clazz);
	}

	public Collection reloadCollection(Object entity, Class clazz) throws CantReloadException {
		EntityBroker broker = _entityBrokers.get(clazz);

		if(!(broker instanceof ICollectionReload)) {
			throw new CantReloadException();
		}

		return ((ICollectionReload) broker).reload();
	}

	public boolean save(Object entity) {
		return _entityBrokers.get(entity.getClass()).saveEntity(entity);
	}
}