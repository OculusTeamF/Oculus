package at.oculus.teamf.databaseconnection.session;

import java.io.Serializable;
import java.util.Collection;

/**
 * Session interface to abstract sessions dealt by broker that implement the at.oculus.teamf.databaseconnection.session.ISessionBroker interface.
 * In addition your class should implement the at.oculus.teamf.databaseconnection.session.ISessionClosable interface if it needs to be closed at
 * some point.
 * @author Simon Angerer
 * @date 30.3.2015
 */
public interface ISession {

    boolean beginTransaktion() throws BadSessionException, AlreadyInTransaktionException;

    boolean commit() throws BadSessionException, NoTransaktionException;

    boolean rollback() throws BadSessionException, NoTransaktionException;

    boolean delete(Object obj) throws BadSessionException, NoTransaktionException;

    Object getByID(Class clazz, Serializable id) throws BadSessionException;

    Collection<Object> getAll(Class clazz) throws BadSessionException;

    Serializable save(Object obj) throws BadSessionException, NoTransaktionException;
}
