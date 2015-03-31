package at.oculus.teamf.databaseconnection.session;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.Collection;
import java.lang.Class;

/**
 * Encapsulated hibernate session that can be simple exchanged thought the at.oculus.teamf.databaseconnection.session.ISession implementation.
 *
 * TODO: Implement, Test and docs
 * @author Simon Angerer
 * @date 30.3.2015
 */
class HibernateSession implements ISession, ISessionClosable {

    private Session _session;
    private Transaction _transaktion;

    public HibernateSession(Session session) {
        _session = session;
    }

    /**
     * Begins the transaktion to use [@link #save()} and  [@link #delete()}.
     * Use {@link #rollback()} to rollback the current transaktion and {@link #commit()}
     * to commit the current session to the database.
     *
     * @return {@code true} if the connection was started, else {@code false}
     * @throws BadSessionException if there is an Connection error
     * @throws AlreadyInTransaktionException if a transaktion is already running
     */
    @Override
    public boolean beginTransaktion() throws BadSessionException, AlreadyInTransaktionException{
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaktion == null) {
            throw new AlreadyInTransaktionException();
        }

        try {
            _transaktion = _session.getTransaction();
            _transaktion.begin();
        } catch (HibernateException e) {
            //Todo: add Logging
            System.out.println("Can not start transaction! OriginalMessage: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean commit() throws BadSessionException, NoTransaktionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaktion == null) {
            throw new NoTransaktionException();
        }

        try {
            _transaktion.commit();
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("Can not commit the transaction! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean rollback() throws BadSessionException, NoTransaktionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaktion == null) {
            throw new NoTransaktionException();
        }

        try {
            _transaktion.rollback();
        } catch(HibernateException e) {
            //Todo: add Logging
            System.out.println("A error occurred when rolling back the transaction! OriginalMessage: " + e.getMessage());

            rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Object obj) throws BadSessionException, NoTransaktionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaktion == null) {
            throw new NoTransaktionException();
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
    public Serializable save(Object obj) throws BadSessionException, NoTransaktionException {
        if(_session.isConnected() || _session.isOpen()) {
            throw new BadSessionException();
        }

        if(_transaktion == null) {
            throw new NoTransaktionException();
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

    }
}
