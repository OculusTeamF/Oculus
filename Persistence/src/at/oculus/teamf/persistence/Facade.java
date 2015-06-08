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
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.queue.IQueueEntry;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Facade for requesting entity from hibernate. Singleton pattern.
 *
 * @author Simon Angerer
 * @version 0.1
 */
public class Facade implements ILogger, IFacade{
    private static Facade _self;
    private HashMap<Class, EntityBroker> _entityBrokers;
    private ISessionBroker _sessionBroker;

    private Facade() {
        log.debug("creating new facade");

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
        entityBrokers.add(new ExaminationResultBroker());
	    entityBrokers.add(new MedicineBroker());
	    entityBrokers.add(new PrescriptionEntryBroker());
	    entityBrokers.add(new PrescriptionBroker());
	    entityBrokers.add(new VisualAidBroker());
	    entityBrokers.add(new WorkingHoursBroker());
	    entityBrokers.add(new CalendarWorkingHoursBroker());
        entityBrokers.add(new PatientQueueBroker());
	    entityBrokers.add(new EventTypeBroker());

	    init(entityBrokers);
    }

    private void init(Collection<EntityBroker> brokers) {
        log.debug("init brokers in facade");

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

	public static Facade getInstance() {
		if (_self == null) {
			_self = new Facade();
		}
		return _self;
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
    public <T> T getById(Class clazz, int id)
		    throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        log.debug("loading object with class " + clazz.getName() + " with id " + id);

	    T object = null;

	    try {
		    object = (T) worker(clazz, new Get(id));
	    } catch (SearchException | ReloadException e) {
		    //eat up
	    }

	    return object;
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
    private <T> T worker(Class clazz, Execute<T> execute) throws NoBrokerMappedException, SearchInterfaceNotImplementedException, BadConnectionException, ReloadInterfaceNotImplementedException, InvalidSearchParameterException, InvalidReloadClassException, DatabaseOperationException {
        EntityBroker broker = getBroker(clazz);

        ISession session = _sessionBroker.getSession();

        T obj;
        try {
            obj = execute.execute(session, broker);
        } catch (BadSessionException e) {
            log.error(e.getMessage());
            throw new BadConnectionException();
        } catch (ClassNotMappedException e) {
            log.error(e.getMessage());
            throw new NoBrokerMappedException();
        }

        _sessionBroker.releaseSession(session);

        return obj;
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
     * get all entities of a domain class
     *
     * @param clazz domain class
     * @param <T>   type of the return value
     * @return all entities of this domain class
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public <T> Collection<T> getAll(Class clazz) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        Collection<T> objects = null;

        try {
            objects = (Collection<T>) (Collection<?>) worker(clazz, new GetAll());
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return objects;
    }

    /**
     * save a domain object
     *
     * @param obj domain object
     * @return true = saved / false = not saved
     * @throws BadConnectionException
     * @throws NoBrokerMappedException
     */
    public boolean save(IDomain obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
        log.debug("saving " + obj.getClass());
        boolean isSaved = false;

        try {
            isSaved = worker(DomainWrapperFactory.getRealClass(obj), new Save(obj));
        } catch (SearchException | ReloadException e) {
            //eat up
        }

        return isSaved;
    }

    public boolean saveAll(Collection<IDomain> obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
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
    public boolean delete(IDomain obj) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, DatabaseOperationException {
        log.debug("deleting " + obj.getClass());
        boolean isDeleted = false;

        try {
            isDeleted = worker(DomainWrapperFactory.getRealClass(obj), new Delete(obj));
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
    public boolean deleteAll(Collection<IDomain> obj) throws FacadeException, ClassNotMappedException {
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
    public <T> Collection<T> search(Class clazz, String... search) throws SearchInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, DatabaseOperationException {
        log.debug("searching " + clazz.getName());
        Collection<T> searchResult = null;

        try {
            searchResult = (Collection<T>) worker(clazz, new Search(search));
        } catch (ReloadException  e) {
            log.error(e.getMessage());
        }

        return searchResult;
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
    public void reloadCollection(IDomain obj, Class clazz)
		    throws BadConnectionException, NoBrokerMappedException, ReloadInterfaceNotImplementedException,
		           InvalidReloadClassException, DatabaseOperationException {
	    try {
		    worker(DomainWrapperFactory.getRealClass(obj) , new ReloadCollection(obj, clazz));
	    } catch (SearchException e) {
		    //eat up
        }
    }

    /**
     * A worker class can implement the interface to be used in {@code #worker()}
     */
    private abstract class Execute<T> {
        abstract T execute(ISession session, EntityBroker broker) throws SearchInterfaceNotImplementedException, BadConnectionException, ReloadInterfaceNotImplementedException, NoBrokerMappedException, InvalidSearchParameterException, InvalidReloadClassException, DatabaseOperationException, ClassNotMappedException, BadSessionException;
    }

    private class Get extends Execute<Object> {

        private int _id;

        public Get(int id) {
            _id = id;
        }

        @Override
        public Object execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, ClassNotMappedException, DatabaseOperationException, InvalidSearchParameterException, SearchInterfaceNotImplementedException {

            try {
                return broker.getEntity(session, _id);
            } catch (DatabaseOperationException e) {
                log.error(e.getMessage());
                throw  new DatabaseOperationException(e);
            }

        }
    }

    private class GetAll extends Execute<Collection> {

        @Override
        public Collection execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, BadSessionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
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
        public Object execute(ISession session, EntityBroker broker) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, BadSessionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
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
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, BadSessionException {
            broker.saveEntity(session, _toSave);
            return true;
        }
    }

    private class SaveAll extends Execute<Boolean> {

        private Collection<IDomain> _toSave;

        public SaveAll(Collection<IDomain> toSave) {
            _toSave = toSave;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, BadSessionException, ClassNotMappedException {
            broker.saveAll(session, _toSave);
            return true;
        }
    }

    private class Delete extends Execute<Boolean> {

        private IDomain _toDelete;

        public Delete(IDomain toDelete) {
            _toDelete = toDelete;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, BadSessionException, DatabaseOperationException, ClassNotMappedException {
            broker.deleteEntity(session, _toDelete);
            return true;
        }
    }

    private class DeleteAll extends Execute<Boolean> {

        private Collection<IDomain> _toDelete;

        public DeleteAll(Collection<IDomain> toDelete) {
            _toDelete = toDelete;
        }

        @Override
        public Boolean execute(ISession session, EntityBroker broker) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
            return broker.deleteAll(session, _toDelete);
        }
    }

    private class Search extends Execute<Collection<Object>> {

        private String[] _params;

        public Search(String... params) {
            _params = params;
        }

        @Override
        public Collection<Object> execute(ISession session, EntityBroker broker) throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException, BadSessionException, DatabaseOperationException, ClassNotMappedException {
            if (!(broker instanceof ISearch)) {
                throw new SearchInterfaceNotImplementedException();
            }

            return ((ISearch) broker).search(session, _params);
        }
    }
}