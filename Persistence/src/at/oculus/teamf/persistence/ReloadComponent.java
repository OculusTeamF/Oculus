/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * ReloadComponent provides an abstract implementation for reloading collections that belong to an entity. It only needs
 * to know which entity class to load and witch domain class to cast the loaded collection from the entity.
 *
 * The user of this component only needs to implement an object that implements the {@code ICollectionLoader} interface and
 * pass the object to the {@code reloadCollection()} method.
 *
 * @author Simon Angerer
 * @version 1.0
 */
 class ReloadComponent implements ILogger{

    private Class _entityClazz;
    private Class _clazzToLoad;

    /**
     * @param entityClazz entity class from which the reload needs to be extracted
     * @param clazzToLoad domain class that needs to be reloaded
     */
    public ReloadComponent(Class entityClazz, Class clazzToLoad) {
        _entityClazz = entityClazz;
        _clazzToLoad = clazzToLoad;
    }

    /**
     * @param session session to use
     * @param id      of the object to reload
     * @param loader  that will load the collection
     * @return collection of domain objects
     */
    public Collection reloadCollection(ISession session, int id, ICollectionLoader loader) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        Facade facade = Facade.getInstance();

        //load database entity that has the collection that needs to be reloaded
        Object databaseEntity;

        try {
            databaseEntity = session.getByID(_entityClazz, id);
        } catch (BadSessionException e) {
            log.error("Session is no longer valide! Original Message: " + e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error("Class not mapped: " + e.getMessage());
            throw new NoBrokerMappedException();
        }

        //load CalendarEventEntity collection from database CalendarEventEntity
        Collection entities = loader.load(databaseEntity);


        //get domain object broker
        EntityBroker toLoadClassDomainBroker;
        toLoadClassDomainBroker = facade.getBroker(_clazzToLoad);


        //convert database entity collection to domain entity collection
        Collection calendarEvents = new ArrayList();
        for (Object obj : entities) {
            calendarEvents.add(toLoadClassDomainBroker.persistentToDomain((IEntity) obj));
        }

        return calendarEvents;
    }
}
