/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

import java.io.Serializable;
import java.util.Collection;

/**
 * Session interface to abstract sessions dealt by broker that implement the at.oculus.teamf.databaseconnection.session.ISessionBroker
 * interface. In addition your class should implement the at.oculus.teamf.databaseconnection.session.ISessionClosable
 * interface if it needs to be closed at some point.
 * <p/>
 *
 * Todo: Methode Documentation
 *
 * @author Simon Angerer
 * @date 30.3.2015
 */
public interface ISession {


	boolean beginTransaction() throws BadSessionException, AlreadyInTransactionException;

	boolean commit() throws BadSessionException, NoTransactionException;

	boolean rollback() throws BadSessionException, NoTransactionException;

	boolean delete(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException;

	Object getByID(Class clazz, Serializable id) throws BadSessionException, ClassNotMappedException;

	Collection<Object> getAll(Class clazz) throws BadSessionException, ClassNotMappedException;

	Serializable save(Object obj) throws BadSessionException, NoTransactionException, ClassNotMappedException;
}
