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
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.exceptions.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Facade for requesting entities from hibernate. Singleton pattern.
 *
 * //TODO: docs, tests many tests
 * @author Simon Angerer
 * @version 0.1
 */
public class Facade {
	private static Facade _self;
	private HashMap<Class, EntityBroker> _entityBrokers;
	private ISessionBroker _sessionBroker;

	private Facade() {

		Collection<EntityBroker> entityBrokers = new LinkedList<EntityBroker>();
		entityBrokers.add(new CalendarBroker());
		entityBrokers.add(new CalendarEventBoker());
		entityBrokers.add(new DoctorBroker());
		entityBrokers.add(new PatientBroker());
		entityBrokers.add(new QueueBroker());
		entityBrokers.add(new ReceptionistBroker());
		entityBrokers.add(new OrthoptistBroker());
		entityBrokers.add(new CalendarEventTypeBroker());

		init(entityBrokers);
	}

	public static Facade getInstance() {
		if (_self == null) {
			_self = new Facade();
		}
		return _self;
	}

	//determine where to call
	private void init(Collection<EntityBroker> brokers){
		_entityBrokers = new HashMap<Class, EntityBroker>();
		Collection<Class> entityClazzes = new LinkedList<Class>();

		for(EntityBroker broker : brokers) {
			for(Object obj : broker.getDomainClasses()) {
				Class clazz = (Class) obj;
				_entityBrokers.put(clazz, broker);
			}
			entityClazzes.addAll(broker.getEntityClasses());
		}

		_sessionBroker = new HibernateSessionBroker(entityClazzes);
	}

	/**
	 * A worker class can implement the interface to be used in {@code #worker()}
	 */
	private abstract class Execute<T> {
		abstract  T execute(ISession session, EntityBroker broker) throws FacadeException;
	}

	/**
	 * @param clazz class to load
	 * @param execute individual implementation
	 * @param <T> type of the return value
	 * @return an object dependion on the implementation
	 * @throws FacadeException if an error occures
	 */
	private <T> T worker(Class clazz, Execute<T> execute) throws FacadeException {
		EntityBroker broker = getBroker(clazz);

		ISession session = _sessionBroker.getSession();

		T obj = execute.execute(session, broker);

		_sessionBroker.releaseSession(session);

		return obj;
	}

	
	private class Get extends Execute<Object> {

		private int _id;

		public Get(int id) {
			_id = id;
		}

		@Override
		public Object execute(ISession session, EntityBroker broker) throws FacadeException {
			return broker.getEntity(session, _id);
		}
	}

	public <T> T getById(Class clazz, int id) throws FacadeException {
		return (T)worker(clazz, new Get(id));
	}


	private class GetAll extends Execute<Collection<Object>> {

		@Override
		public Collection<Object> execute(ISession session, EntityBroker broker) throws FacadeException {
			return broker.getAll(session);
		}
	}

	public <T> Collection<T> getAll(Class clazz) throws FacadeException {
		return (Collection<T>)(Collection<?>)worker(clazz, new GetAll());
	}


	private class ReloadCollection extends Execute {

		private IDomain _obj;
		private Class _reloadClass;

		public ReloadCollection(IDomain obj, Class reloadClass) {
			_obj = obj;
			_reloadClass = reloadClass;
		}

		@Override
		public Object execute(ISession session, EntityBroker broker) throws FacadeException {
			if(!(broker instanceof ICollectionReload)) {
				throw new ReloadInterfaceNotImplementedException();
			}

			((ICollectionReload) broker).reload(session, _obj, _reloadClass);

			return null;
		}
	}

	public void reloadCollection(IDomain obj, Class clazz) throws FacadeException {
		worker(obj.getClass(), new ReloadCollection(obj, clazz));
	}


	private class Save extends Execute<Boolean> {

		private IDomain _toSave;

		public Save(IDomain toSave) {
			_toSave = toSave;
		}

		@Override
		public Boolean execute(ISession session, EntityBroker broker) {
			 return broker.saveEntity(session, _toSave);
		}
	}

	public boolean save(IDomain obj) throws FacadeException {
		return (boolean)worker(obj.getClass(), new Save(obj));
	}

	private class SaveAll extends Execute<Boolean> {

		private Collection<IDomain> _toSave;

		public SaveAll(Collection<IDomain> toSave) {
			_toSave = toSave;
		}

		@Override
		public Boolean execute(ISession session, EntityBroker broker) {
			return broker.saveAll(session, _toSave);
		}
	}

	public boolean saveAll(Collection<IDomain> obj) throws FacadeException {
		return (boolean)worker(obj.getClass(), new SaveAll(obj));
	}

	private class Delete extends Execute<Boolean> {

		private IDomain _toDelete;

		public Delete(IDomain toDelete) {
			_toDelete = toDelete;
		}

		@Override
		public Boolean execute(ISession session, EntityBroker broker) {
			return broker.deleteEntity(session, (IDomain) _toDelete);
		}
	}

	public boolean delete(IDomain obj) throws FacadeException {
		return (Boolean)worker(obj.getClass(), new Delete(obj));
	}

	private class DeleteAll extends Execute<Boolean> {

		private Collection<IDomain> _toDelete;

		public DeleteAll(Collection<IDomain> toDelete) {
			_toDelete = toDelete;
		}

		@Override
		public Boolean execute(ISession session, EntityBroker broker) {
			return broker.deleteAll(session, _toDelete);
		}
	}

	public boolean deleteAll(Collection<IDomain> obj) throws FacadeException {
		return (Boolean)worker(obj.getClass(), new DeleteAll(obj));
	}

	protected EntityBroker getBroker(Class clazz) throws NoBrokerMappedException {
		EntityBroker broker = _entityBrokers.get(clazz);

		if(broker == null) {
			throw new NoBrokerMappedException();
		}

		return broker;
	}
}