package at.oculus.teamf.databaseconnection.session;

import java.util.Collection;

/**
 * Session interface to abstract sessions dealt by broker that implement the ISessionBroker interface.
 * In addition your class should implement the ISessionClosable interface if it needs to be closed at
 * some point.
 * @author Simon Angerer
 * @date 30.3.2015
 */
public interface ISession {

    boolean startTransaktion();

    void flush();

    boolean commit();

    boolean rollback();

    boolean delete(Object obj);

    Object getByID(Class clazz, int id);

    Collection<Object> getAll(Class clazz);

    boolean save(Object obj);

    boolean saveAll(Collection<Object> obj);
}
