/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.Collection;
import java.lang.Class;

/**
 * Encapsulated hibernate session that can be simple exchanged thought the {@code #ISession} implementation.
 * <p/>
 *
 * TODO: Test and docs
 * @author Simon Angerer
 * @date 30.03.2015
 */
class HibernateSession implements ISession, ISessionClosable {

    private Session _session;
    private Transaction _transaction;

    public HibernateSession(Session session) {
        _session = session;
    }

    /**
     * Begins the transaction to use [@link #save()} and  [@link #delete()}.
     * Use {@link #rollback()} to rollback the current transaction and {@link #commit()}
     * to commit the current session to the database.
     *
     * @return {@code true} if the connection was started, else {@code false}
     * @throws BadSessionException if there is an Connection error
     * @throws AlreadyInTransactionException if a transaktion is already running
     */
    @Override
    public boolean beginTransaction() throws BadSessionException, AlreadyInTransactionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaction != null) {
            throw new AlreadyInTransactionException();
        }

        try {
            _transaction = _session.getTransaction();
            _transaction.begin();
        } catch (HibernateException e) {
            //Todo: add Logging
            System.out.println("Can not start transaction! OriginalMessage: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean commit() throws BadSessionException, NoTransactionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _transaction.commit();
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("Can not commit the transaction! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean rollback() throws BadSessionException, NoTransactionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _transaction.rollback();
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("A error occurred when rolling back the transaction! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Object obj) throws BadSessionException, NoTransactionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaction == null) {
            throw new NoTransactionException();
        }

        try {
           _session.delete(obj);
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("A error ocured during the deletion process!: " + e.getMessage());

            rollback();
            return false;
        }

        return true;
    }

    @Override
    public Object getByID(Class clazz, Serializable id) throws BadSessionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        return  _session.get(clazz, id);
    }

    @Override
    public Collection<Object> getAll(Class clazz)throws BadSessionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        return  _session.createCriteria(clazz).list();
    }

    @Override
    public Serializable save(Object obj) throws BadSessionException, NoTransactionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaction == null) {
            throw new NoTransactionException();
        }

        try {
            _session.save(obj);
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("A error occurred when rolling back the transaction! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return false;
    }

    @Override
    public void close() {
        _session.close();
    }
}
