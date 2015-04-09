/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.NoBrokerMappedException;
import at.oculus.teamf.persistence.exceptions.NotAbleToLoadClassException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Reload Component for domain class brokers
 *
 * @param <D>
 * 		Domain class of which the collection needs to be reloaded
 * @param <R>
 * 		The reload database entity
 */
public class ReloadComponent<D, R> {

	private Class<D> _entityClazz;
	private Class<R> _clazzToLoad;
	/**
	 * @param session
	 * 		session to use
	 * @param entityClazz
	 * 		entity that needs a collection reload
	 * @param clazzToLoad
	 * 		domain class that needs to be loaded
	 * @param id
	 * 		of the object to reload
	 * @param loader
	 * 		that will load the collection
	 *
	 * @return collection of entityClazz
	 *
	 * @throws FacadeException
	 * 		gets thrown if an error occures
	 */
	public Collection<D> reloadCollection(ISession session, Class entityClazz, Class clazzToLoad, int id,
	                                      CollectionLoader loader) throws FacadeException {
		Facade facade = Facade.getInstance();

		//load database CalendarEventEntity that has the collection that needs to be reloaded
		Object databaseEntity = null;
		try {
			databaseEntity = session.getByID(entityClazz, id);
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		}
		if (databaseEntity == null) {
			throw new NotAbleToLoadClassException();
		}

		//load CalendarEventEntity collection from database CalendarEventEntity
		Collection<R> entities = loader.load(databaseEntity);


		//convert to domain object broker
		EntityBroker toLoadClassDomainBroker = null;
		try {
			toLoadClassDomainBroker = facade.getBroker(clazzToLoad);
		} catch (NoBrokerMappedException e) {
			//Todo: add Loging
			e.printStackTrace();

			throw new NotAbleToLoadClassException();
		}

		//convert database entity collection to domain entity collection
		Collection<D> objects = new ArrayList<D>();
		for (R r : entities) {
			objects.add((D) toLoadClassDomainBroker.persitentToDomain(r));
		}

		return objects;
	}
}
