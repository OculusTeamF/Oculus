/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.HibernateSessionBroker;
import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.ISessionBroker;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Facade for requesting entity from hibernate. Singleton pattern.
 *
 * @author Simon Angerer
 * @version 0.1
 */
public class Facade {
    private static Facade _self;
    private HashMap<Class, EntityBroker> _entityBrokers;
    private ISessionBroker _sessionBroker;

    private Facade() {

        Collection<EntityBroker> entityBrokers = new LinkedList<>();
        entityBrokers.add(new CalendarBroker());
        entityBrokers.add(new CalendarEventBroker());
        entityBrokers.add(new DoctorBroker());
        entityBrokers.add(new PatientBroker());
        entityBrokers.add(new QueueBroker());
        entityBrokers.add(new ReceptionistBroker());
        entityBrokers.add(new OrthoptistBroker());
        entityBrokers.add(new CalendarEventTypeBroker());
        entityBrokers.add(new DiagnosisBroker());
        entityBrokers.add(new ExaminationProtocolBroker());

        init(entityBrokers);
    }

    public static Facade getInstance() {
        if (_self == null) {
            _self = new Facade();
        }
        return _self;
    }

    //determine where to call
    private void init(Collection<EntityBroker> brokers) {
        _entityBrokers = new HashMap<>();
        Collection<Class> entityClazzes = new LinkedList<>();

        for (EntityBroker broker : brokers) {
            for (Object obj : broker.getDomainClasses()) {
                Class clazz = (Class) obj;
                _entityBrokers.put(clazz, broker);
            }
            entityClazzes.addAll(broker.getEntityClasses());
        }

        _sessionBroker = new HibernateSessionBroker(entityClazzes);
    }

    /**
     * @param clazz   class to load
     * @param execute individual implementation
     * @param <T>     type of the return value
     * @return an object dependion on the implementation
     * @throws NoBrokerMappedException
     * @throws SearchInterfaceNotImplementedException
     * @throws BadConnectionException
     * @throws ReloadInterfaceNotImplementedException
     * @throws InvalidSearchParameterException
     * @throws InvalidReloadClassException
     */
    private <T> T worker(Class clazz, Execute<T> execute) throws NoBrokerMappedException, SearchInterfaceNotImplementedException, BadConnectionException, ReloadInterfaceNotImplementedException, InvalidSearchParameterException, InvalidReloadClassException {
        EntityBroker broker = getBroker(clazz);

        ISession session = _sessionBroker.getSession();

        T obj = execute.execute(session, broker);

        _sessionBroker.releaseSession(session);

        return obj;
    }

    /**
     * get an entity of a specific domain class by id
     *
     * @param clazz domain class
     * @param id    id of the entity
     * @param <T>   type of the return value
     * @return requested domain class
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public <T> T getById(Class clazz, int id) throws BadConnectionException, NoBrokerMappedException {
        T object = null;

        try {
            object = (T) worker(clazz, new Get(id));
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return object;
    }

    /**
     * get all entities of a domain class
     *
     * @param clazz domain class
     * @param <T>   type of the return value
     * @return all entities of this domain class
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public <T> Collection<T> getAll(Class clazz) throws BadConnectionException, NoBrokerMappedException {
        Collection<T> objects = null;

        try {
            objects = (Collection<T>) (Collection<?>) worker(clazz, new GetAll());
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return objects;
    }

    /**
     * reload a collection in a domain object
     *
     * @param obj   domain object
     * @param clazz class of collection to be reloaded
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws ReloadInterfaceNotImplementedException
     * @throws InvalidReloadClassException
     */
    public void reloadCollection(IDomain obj, Class clazz) throws BadConnectionException, NoBrokerMappedException, ReloadInterfaceNotImplementedException, InvalidReloadClassException {
        try {
            worker(obj.getClass(), new ReloadCollection(obj, clazz));
        } catch (SearchException e) {
            //eat up
        }
    }

    /**
     * save a domain object
     *
     * @param obj domain object
     * @return true = saved / false = not saved
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public boolean save(IDomain obj) throws BadConnectionException, NoBrokerMappedException {
        boolean isSaved = false;

        try {
            isSaved = worker(obj.getClass(), new Save(obj));
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return isSaved;
    }

    public boolean saveAll(Collection<IDomain> obj) throws BadConnectionException, NoBrokerMappedException {
        boolean isSaved = false;

        try {
            isSaved = worker(obj.getClass(), new SaveAll(obj));
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return isSaved;
    }

    /**
     * delete a specific entity of a doamin object
     *
     * @param obj domain object to be deleted
     * @return true = deleted / false = not deleted
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidSearchParameterException
     */
    public boolean delete(IDomain obj) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException {
        boolean isDeleted = false;

        try {
            isDeleted = worker(obj.getClass(), new Delete(obj));
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return isDeleted;
    }

    /**
     * delete a collection of entities of domain objects
     *
     * @param obj domain object
     * @return true = deleted / false = not deleted
     * @throws FacadeException
     */
    public boolean deleteAll(Collection<IDomain> obj) throws FacadeException {
        return worker(obj.getClass(), new DeleteAll(obj));
    }

    /**
     * search a entity with specific parameters
     *
     * @param clazz  entity to be searched
     * @param search search parameters
     * @param <T>    type of return value
     * @return collection of search results
     * @throws SearchInterfaceNotImplementedException
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     * @throws InvalidSearchParameterException
     */
    public <T> Collection<T> search(Class clazz, String... search) throws SearchInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException {
        Collection<T> searchResult = null;

        try {
            searchResult = (Collection<T>) worker(clazz, new Search(search));
        } catch (ReloadException e) {
            //eat up
        }

        return searchResult;
    }

    /**
     * get broker of a specific domain class
     *
     * @param clazz domain class of broker
     * @return requested broker
     * @throws NoBrokerMappedException
     */
    protected EntityBroker getBroker(Class clazz) throws NoBrokerMappedException {
        EntityBroker broker = _entityBrokers.get(clazz);

        if (broker == null) {
            throw new NoBrokerMappedException();
        }

        return broker;
    }

    /**
     * A worker class can implement the interface to be used in {@code #worker()}
     */
    private abstract class Execute<T> {
        abstract T execute(ISession session, EntityBroker broker) throws SearchInterfaceNotImplementedException, BadConnectionException, ReloadInterfaceNotImplementedException, NoBrokerMappedException, InvalidSearchParameterException, InvalidReloadClassException;
    }

    private class Get extends Execute<Object> {

        private int _id;

        public Get(int id) {
            _id = id;
        }

        @Override
        public Object execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.getEntity(session, _id);
        }
    }

    private class GetAll extends Execute<Collection> {

        @Override
        public Collection execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.getAll(session);
        }
    }

    private class ReloadCollection extends Execute {

        private IDomain _obj;
        private Class _reloadClass;

        public ReloadCollection(IDomain obj, Class reloadClass) {
            _obj = obj;
            _reloadClass = reloadClass;
        }

        @Override
        public Object execute(ISession session, EntityBroker broker) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException {
            if (!(broker instanceof ICollectionReload)) {
                throw new ReloadInterfaceNotImplementedException();
            }

            ((ICollectionReload) broker).reload(session, _obj, _reloadClass);

            return null;
        }
    }

    private class Save extends Execute<Boolean> {

        private IDomain _toSave;

        public Save(IDomain toSave) {
            _toSave = toSave;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.saveEntity(session, _toSave);
        }
    }

    private class SaveAll extends Execute<Boolean> {

        private Collection<IDomain> _toSave;

        public SaveAll(Collection<IDomain> toSave) {
            _toSave = toSave;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.saveAll(session, _toSave);
        }
    }

    private class Delete extends Execute<Boolean> {

        private IDomain _toDelete;

        public Delete(IDomain toDelete) {
            _toDelete = toDelete;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.deleteEntity(session, _toDelete);
        }
    }

    private class DeleteAll extends Execute<Boolean> {

        private Collection<IDomain> _toDelete;

        public DeleteAll(Collection<IDomain> toDelete) {
            _toDelete = toDelete;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException {
            return broker.deleteAll(session, _toDelete);
        }
    }

    private class Search extends Execute<Collection<Object>> {

        private String[] _params;

        public Search(String... params) {
            _params = params;
        }

        @Override
        public Collection<Object> execute(ISession session, EntityBroker broker) throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException {
            if (!(broker instanceof ISearch)) {
                throw new SearchInterfaceNotImplementedException();
            }

            Collection<Object> collection = new LinkedList<>();

            for (Object obj : ((ISearch) broker).search(session, _params)) {
                collection.add(obj);
            }

            return collection;
        }
    }
}