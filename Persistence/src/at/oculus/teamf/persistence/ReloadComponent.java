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
import at.oculus.teamf.persistence.entities.IEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;
import at.oculus.teamf.persistence.exceptions.NoBrokerMappedException;
import at.oculus.teamf.persistence.exceptions.NotAbleToLoadClassException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Simon Angerer
 * @version 1.0
 * @date 12.04.2015
 */
public class ReloadComponent {

	private Class _entityClazz;
	private Class _clazzToLoad;

	/**
	 *
	 * @param entityClazz entity class from which the reload needs to be extracted
	 * @param clazzToLoad domain class that needs to be reloaded
	 */
	public ReloadComponent(Class entityClazz, Class clazzToLoad) {
		_entityClazz = entityClazz;
		_clazzToLoad = clazzToLoad;
	}
	/**
	 * @param session
	 * 		session to use
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
	public Collection reloadCollection(ISession session, int id, CollectionLoader loader) throws FacadeException {
		Facade facade = Facade.getInstance();

		//load database CalendarEventEntity that has the collection that needs to be reloaded
		Object databaseEntity = null;
		try {
			databaseEntity = session.getByID(_entityClazz, id);
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		}
		if (databaseEntity == null) {
			throw new NotAbleToLoadClassException();
		}

		//load CalendarEventEntity collection from database CalendarEventEntity
		Collection entities = loader.load(databaseEntity);


		//get domain object broker
		EntityBroker toLoadClassDomainBroker = null;
		try {
			toLoadClassDomainBroker = facade.getBroker(_clazzToLoad);
		} catch (NoBrokerMappedException e) {
			//Todo: add Loging
			e.printStackTrace();

			throw new NotAbleToLoadClassException();
		}

		//convert database entity collection to domain entity collection
		Collection objects = new ArrayList();
		for (Object obj : entities) {
			objects.add(toLoadClassDomainBroker.persitentToDomain((IEntity) obj));
		}

		return objects;
	}
}
