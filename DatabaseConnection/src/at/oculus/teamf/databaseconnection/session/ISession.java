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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Session interface to abstract sessions dealt by broker that implement the at.oculus.teamf.databaseconnection.session.ISessionBroker
 * interface. In addition your class should implement the at.oculus.teamf.databaseconnection.session.ISessionClosable
 * interface if it needs to be closed at some point.
 *
 * @author Simon Angerer
 * @date 30.3.2015
 * @version 1.0
 */
public interface ISession {

	/**
	 * Starts a transaction on the current {@code #ISesion}
	 *
	 * @return true if the transaction was successfully started
	 */
	boolean beginTransaction() throws BadSessionException, AlreadyInTransactionException;

	/**
	 * Commits the current session to the database.
	 *
	 * @return true if the current session was sucessfully commited
	 */
	boolean commit() throws BadSessionException, NoTransactionException;

	/**
	 * Resets the current transaction to the state before the transaction was started.
	 *
	 * @return true if the rollback was successful
	 */
	boolean rollback() throws BadSessionException, NoTransactionException;

	/**
	 * Deletes a object from the database
	 *
	 * @param obj
	 * 		object to delete
	 *
	 * @return true if the delete was sucsessful
	 */
	boolean delete(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException;

	/**
	 * Loads an object form the database from type clazz and with the specified id
	 *
	 * @param clazz
	 * 		of the object that needs to be loaded
	 * @param id
	 * 		of the object in the database
	 *
	 * @return a object
	 */
	Object getByID(Class clazz, Serializable id) throws BadSessionException, ClassNotMappedException;

	/**
	 * Loads a collection object form the database from type clazz
	 *
	 * @param clazz
	 * 		of the object in the collection that needs to be loaded
	 *
	 * @return a collection of objects from type clazz
	 */
	Collection<Object> getAll(Class clazz) throws BadSessionException, ClassNotMappedException;

	/**
	 * Saves a object in the database, can also override an entry
	 *
	 * @param obj
	 * 		that needs to be saved
	 *void search(Class<P> , String[] );
	 * @return the new id of the object in the database
	 */
	Serializable save(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException;

	/**
	 * Saves a object in the database if a object would be overriden it will update the object instead
	 *
	 * @param obj
	 * 		that needs to be saved
	 *
	 * @return the new id of the object in the database if it was saved an not updated
	 */
	Serializable saveOrUpdate(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException;

	List<Object> search(String queryName, String[] parameters) throws BadSessionException;
}
