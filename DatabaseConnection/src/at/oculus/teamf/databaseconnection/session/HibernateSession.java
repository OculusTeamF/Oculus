/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

import at.oculus.teamf.databaseconnection.session.exception.AlreadyInTransactionException;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.databaseconnection.session.exception.NoTransactionException;
import at.oculus.teamf.technical.loggin.ILogger;
import org.hibernate.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Encapsulated hibernate session that can be simple exchanged thought the {@code #ISession} implementation.
 *
 * @author Simon Angerer
 * @date 30.03.2015
 * @version 1.0
 */
class HibernateSession implements ISession, ISessionClosable, ILogger {

    //hibernate session
    private Session _session;

    //transaction that is currently running
    private Transaction _transaction;

    //classes that can be serialised and deserialised
    private HashSet<Class> _clazzes;


	private EntityManagerFactory _entityManagerFactory;
	private EntityManager _entityManager;

    public HibernateSession(Session session, Collection<Class> classes) {
        _session = session;

        _clazzes = new HashSet<Class>();
        for(Class clazz : classes) {
            _clazzes.add(clazz);
        }

        _entityManagerFactory = Persistence.createEntityManagerFactory("oculus_f");

        _entityManager = _entityManagerFactory.createEntityManager();
    }

    /**
     * Begins the transaction to use [@link #save()} and  [@link #delete()}.
     * Use {@link #rollback()} to rollback the current transaction and {@link #commit()}
     * to commit the current session to the database.
     *
     * @return {@code true} if the connection was started, else {@code false}
     * @throws BadSessionException           if the session is corrupted
     * @throws AlreadyInTransactionException if a transaction is already running
     */
    @Override
    public boolean beginTransaction() throws BadSessionException, AlreadyInTransactionException {
        validateSession();

        if (_transaction != null) {
            throw new AlreadyInTransactionException();
        }

        try {
            _transaction = _session.getTransaction();
            _transaction.begin();
        } catch (HibernateException e) {
            log.error("Can not start transaction! OriginalMessage: " + e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * Checks if the current session is sill valide
     * @throws BadSessionException if the session is invalide
     */
    private void validateSession() throws BadSessionException {
        if ((_session == null) || !_session.isConnected() || !_session.isOpen()) {
            throw new BadSessionException();
        }
    }

    /**
     * Commits a transaction to the database that was started before by calling {@code #beginTransaction()}.
     * The method will rollback the transaction if an error occurs.
     *
     * @return {@code true} if the commit was successful and {@code false} if an error occurred
     * @throws BadSessionException           if the session is corrupted
     * @throws AlreadyInTransactionException if no transaction is runnning
     */
    @Override
    public boolean commit() throws BadSessionException, NoTransactionException {
        validateSession();

        if (_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _transaction.commit();
        } catch (HibernateException e) {
            log.error("Can not commit the transaction! OriginalMessage: " + e.getMessage());
            rollback();
            return false;
        }

        _transaction = null;
        return true;
    }

    /**
     * Will rollback the database to the state before the transaction was started, when calling {@code #beginTransaction()}
     *
     * @return {@code true} if the transaction was rolled back successfully, false if something went wrong
     * @throws BadSessionException           if the session is corrupted
     * @throws AlreadyInTransactionException if no transaction is runnning
     */
    @Override
    public boolean rollback() throws BadSessionException, NoTransactionException {
        validateSession();

        if (_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _transaction.rollback();
        } catch (HibernateException e) {
            log.error("A error occurred when rolling back the transaction! OriginalMessage: " + e.getMessage());
            return false;
        }

        _transaction = null;
        return true;
    }

    /**
     * Deletes a object from the database during a transaction
     *
     * @param obj object to delete from the database
     * @return {@code true} if obj was delete sucessfully from the database. {@code }
     * @throws BadSessionException           if the session is corrupted
     * @throws AlreadyInTransactionException if no transaction is runnning
     */
    @Override
    public boolean delete(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException {
        validateSession();

        if (_transaction == null) {
            throw new NoTransactionException();
        }

        validateClass(obj.getClass());

        try {
            _session.delete(obj);
        } catch (HibernateException e) {
            log.error("A error ocured during the deletion process!: " + e.getMessage());
            rollback();
            return false;
        }

        return true;
    }

    /**
     * Checks if the class was mapped
     * @param clazz tha need to be validated
     * @throws ClassNotMappedException gets thrown if the class was not mapped
     */
    private void validateClass(Class clazz) throws ClassNotMappedException {
        if(!_clazzes.contains(clazz)) {
            throw new ClassNotMappedException(clazz.toString());
        }
    }

    /**
     * Deserializes a object from the database.
     *
     * @param clazz the class of the object that is needed from the database
     * @param id    identifier of the object in the database
     * @return a object form the clazz, or null if the id was not found
     * @throws BadSessionException if the session is corrupted
     */
    @Override
    public Object getByID(Class clazz, Serializable id) throws BadSessionException, ClassNotMappedException {
        validateSession();

        validateClass(clazz);

        return _session.get(clazz, id);
    }

    /**
     * Deserializes a entire table an returns it in form of a collection.
     *
     * @param clazz class of the objects that are needed from the database
     * @return a collection of objects from type clazz
     * @throws BadSessionException if the session is corrupted
     */
    @Override
    public Collection<Object> getAll(Class clazz) throws BadSessionException, ClassNotMappedException {
        validateSession();

        validateClass(clazz);

        return _session.createCriteria(clazz).list();
    }

    /**
     * Saves a object in the database
     *
     * @return the unique id of the object in the database
     * @throws BadSessionException           if there is an Connection error
     * @throws AlreadyInTransactionException if no transaction is runnning
     */
    @Override
    public Serializable save(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException  {
        validateSession();

        validateClass(obj.getClass());

        if (_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _session.save(obj);
        } catch (HibernateException e) {
            log.error("A error occurred when trying to save " + obj + "! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return false;
    }

	/**
	 * Saves or updates a object in the database
	 *
	 * @return the unique id of the object in the database
	 * @throws BadSessionException           if there is an Connection error
	 * @throws AlreadyInTransactionException if no transaction is runnning
	 */
	@Override
	public Serializable saveOrUpdate(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException  {
		validateSession();

		validateClass(obj.getClass());

		if (_transaction == null) {
			throw new NoTransactionException();
		}

		try {
			_session.saveOrUpdate(obj);
		} catch (HibernateException e) {
            log.error("A error occurred when trying to save " + obj +"! OriginalMessage: " + e.getMessage());
			rollback();
			return false;
		}

		return false;
	}

    /**
     * Searches for an database entry by using named queries and parameters.
     *
     * @param queryName namedquery name that is spezivied in the entity
     * @param parameters to use in the query if needed
     * @return a collection of objects or null if no entry was found
     */
	@Override
	public Collection<Object> search(String queryName, String[] parameters) throws BadSessionException {
		validateSession();
		Query query = _entityManager.createNamedQuery(queryName);
		for(Integer i = 0; i < parameters.length; i++){
            try{
                query.setParameter(i.toString(), "%"+parameters[i].replace(" ","%")+"%");
            } catch (Exception e) {
                log.error("A error occurred when trying to search for an object through named queries! OriginalMessage: " + e.getMessage());
            }
		}
		return query.getResultList();
	}

	/**
     * Closess the current session
     */
    @Override
    public void close() {
        _session.close();
    }
}
