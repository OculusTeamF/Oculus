package at.oculus.teamf.databaseconnection.session;

import org.hibernate.Session;

import java.util.Collection;

/**
 * Encapsulated hibernate session that can be simple exchanged thought the ISession implementation.
 *
 * TODO: Implement and docs
 * @author Simon Angerer
 * @date 30.3.2015
 */
class HibernateSession implements ISession, ISessionClosable {

    public HibernateSession(Session session) {

    }

    @Override
    public boolean startTransaktion() {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public boolean rollback() {
        return false;
    }

    @Override
    public boolean delete(Object obj) {
        return false;
    }

    @Override
    public Object getByID(Class clazz, int id) {
        return null;
    }

    @Override
    public Collection<Object> getAll(Class clazz) {
        return null;
    }

    @Override
    public boolean save(Object obj) {
        return false;
    }

    @Override
    public boolean saveAll(Collection<Object> obj) {
        return false;
    }

    @Override
    public void close() {

    }
}
