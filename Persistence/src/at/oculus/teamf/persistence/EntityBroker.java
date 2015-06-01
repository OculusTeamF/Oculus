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
import at.oculus.teamf.databaseconnection.session.exception.*;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Abstract EntityBroker from witch each Broker needs to extend, to be used in the facade. {@code EntityBroker} implements
 * {@code getByID}, {@code getAll}, {@code save}, {@code saveAll}. Each broker that extends from {@code EntityBroker} only needs
 * to implement {@code persistentToDomain} and {@code domainToPersistent}.
 *
 * Extended Broker can implement {@code ICollectionReload} to add a reload functionality to the broker.
 *
 * @param <D> Domain class
 * @param <P> Entity class
 * @author Simon Angerer
 * @version 1.0
 */
abstract class EntityBroker<D extends IDomain, P extends IEntity> implements ILogger {

    protected Class<D> _domainClass;
    protected Class<P> _entityClass;

    private Collection<Class> _entityClasses;
    private Collection<Class> _domainClasses;

    public EntityBroker(Class domainClass, Class entityClass) {
        _entityClasses = new LinkedList<>();
        _domainClasses = new LinkedList<>();

        _domainClass = domainClass;
        _entityClass = entityClass;

        _entityClasses.add(_entityClass);
        _domainClasses.add(_domainClass);

    }

    /**
     * Gets an object from the database that is of type {@code D} and has {@code id} as primary key in the database.
     *
     * @param session that should be use
     * @param id      of the table entry
     * @return an instance of {@code D}
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public D getEntity(ISession session, int id) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, InvalidSearchParameterException, SearchInterfaceNotImplementedException {
        log.debug("get " + _domainClass.toString() + " with ID " + id);

        P entity = null;

        try {
            entity = (P) session.getByID(_entityClass, id);
        } catch (BadSessionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        }

        D result = null;
        result = persistentToDomain(entity);

        return result;
    }

    /**
     * Gets an colletion of D object from the database.
     *
     * @param session that should be use
     * @return a collection of objects that are instance of D
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public Collection<D> getAll(ISession session) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        log.debug("get all " + _domainClass.toString());

        Collection<Object> entities = null;
        try {
            entities = session.getAll(_entityClass);
        } catch (BadSessionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        }

        Collection<D> domainObjects = new ArrayList<D>();
        for (Object obj : entities) {
            P entity = (P) obj;

            domainObjects.add(persistentToDomain(entity));
        }

        return domainObjects;
    }

    /**
     * Saves an object in the database
     *
     * @param session that should be use
     * @param domainObj that needs to be saved
     * @return {@code true} if the object was saved, {@code false} if the object could not be saved
     */
    public void saveEntity(ISession session, D domainObj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        log.debug("save " + _domainClass.toString() + " with ID " + domainObj.getId());

        P entity = null;
        entity = domainToPersistent(domainObj);


        try {
            session.beginTransaction();

            session.saveOrUpdate(entity);
            domainObj.setId(entity.getId());
            session.commit();

        } catch (BadSessionException | AlreadyInTransactionException | NoTransactionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        } catch (CanNotStartTransactionException | CanNotCommitTransactionException e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException(e);
        }

        log.debug(_domainClass.toString() + " with ID " + domainObj.getId() + " saved");
    }

    /**
     * Saves a collection of object to the database.
     *
     * @param session that should be use
     * @param domainObjs that need to be saved
     * @return {@code true} if the objects was saved, {@code false} if the objects could not be saved
     */
    public void saveAll(ISession session, Collection<D> domainObjs) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        log.debug("save collection of " + _domainClass.toString());

        Collection<P> entities = new ArrayList<P>();
        for (D obj : domainObjs) {
            entities.add(domainToPersistent(obj));
        }

        try {
            session.beginTransaction();
            for (P entity : entities) {
                session.saveOrUpdate(entity);
            }
            session.commit();

        } catch (AlreadyInTransactionException | BadSessionException  | NoTransactionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        } catch (CanNotStartTransactionException | CanNotCommitTransactionException e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException(e);
        }

        log.debug("collection of " + _domainClass.toString() + " saved");
    }

    /**
     * Deletes an object from the database.
     * @param session that should be use
     * @param domainObj that needs to be deleted from the database
     * @return {@code true} if the object was deleted from the database, {@code false} if the object was not deletet
     */
    public boolean deleteEntity(ISession session, D domainObj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        log.debug("delete " + _domainClass.toString() + " with id " + domainObj.getId());

        P entity = null;

        entity = domainToPersistent(domainObj);

        try {
            session.beginTransaction();

            session.delete(entity);

            session.commit();

        } catch (BadSessionException | AlreadyInTransactionException | ClassNotMappedException | NoTransactionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (CanNotRollbackTheTransaction | CanNotStartTransactionException | CanNotCommitTransactionException e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException(e);
        }

        log.debug(_domainClass.toString() + " with id " + domainObj.getId() + " deleted");

        return true;
    }

    public boolean deleteAll(ISession session, Collection<IDomain> domainObjs) throws NoBrokerMappedException, DatabaseOperationException, BadConnectionException {
        log.debug("deleting collection of " + _domainClass.toString());

        P entity = null;

        try {
            session.beginTransaction();
            for(Object obj : domainObjs) {
                entity = domainToPersistent((D) obj);
                session.delete(entity);
            }
            session.commit();

        } catch (BadSessionException | AlreadyInTransactionException | NoTransactionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        } catch (CanNotStartTransactionException | CanNotCommitTransactionException | CanNotRollbackTheTransaction e) {
            log.error(e.getMessage());
            throw new DatabaseOperationException(e);
        }

        log.debug("collection of " + _domainClass.toString() + " deleted");

        return true;
    }

    /**
     * Adds additional entity class mappings to the broker.
     * @param clazz that needs to map
     */
    protected void addEntityClassMapping(Class clazz) {
        _entityClasses.add(clazz);
    }

    /**
     * Adds additional domain class mappings to the broker only. Can be used to map muliple domain classes to one broker.
     * @param clazz that needs to map
     */
    protected void addDomainClassMapping(Class clazz) {
        _domainClasses.add(clazz);
    }

    //<editor-fold desc="Abstract Methode">

    /**
     * Converts a persitency entity to a domain object
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    protected abstract D persistentToDomain(P entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException;

    /**
     * Converts a domain object to persitency entity
     * @param obj that needs to be converted
     * @return return a persitency entity
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    protected abstract P domainToPersistent(D obj) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException;

    //</editor-fold>

    //<editor-fold desc="Getter">
    public Class<D> getDomainClass() {
        return _domainClass;
    }

    public Collection<Class> getEntityClasses() {
        return _entityClasses;
    }

    public Collection<Class> getDomainClasses() {
        return _domainClasses;
    }
    //</editor-fold>

}