/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.*;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.entities.IEntity;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Norskan on 30.03.2015.
 * //Todo: add loging, comments docs etz
 */
abstract class EntityBroker<D extends IDomain, P extends IEntity> {

    protected Class<D> _domainClass;
    protected Class<P> _entityClass;

    private Collection<Class> _entityClasses;
	private Collection<Class> _domainClasses;

    public EntityBroker(Class domainClass, Class entityClass) {
        _entityClasses = new LinkedList<Class>();
	    _domainClasses = new LinkedList<Class>();

        _domainClass = domainClass;
        _entityClass = entityClass;

        _entityClasses.add(_entityClass);
	    _domainClasses.add(_domainClass);
    }

    //<editor-fold desc="Abstract Methode">
    public D getEntity(ISession session, int id) throws FacadeException {
	    P entity = null;
	    try {
		    entity = (P)session.getByID(_entityClass, id);
	    } catch (BadSessionException e) {
		    e.printStackTrace();
	    } catch (ClassNotMappedException e) {
		    e.printStackTrace();
	    }

	    D result = persitentToDomain(entity);

	    return result;
    }

    public Collection<D> getAll(ISession session) throws FacadeException {
        Collection<Object> entities = null;
        try {
            entities = (Collection<Object>) session.getAll(_entityClass);
        } catch (BadSessionException e) {
            e.printStackTrace();
        } catch (ClassNotMappedException e) {
            e.printStackTrace();
        }

        Collection<D> domainObjects = new ArrayList<D>();
        for(Object obj : entities) {
	        P entity = (P) obj;
            domainObjects.add(persitentToDomain(entity));
        }

        return domainObjects;
    }

    public boolean saveEntity(ISession session, D domainObj) {
        P entity = domainToPersitent(domainObj);

        try {
            session.beginTransaction();

	        session.saveOrUpdate(entity);

            session.commit();
			//TODO make it work!
	        domainObj.setId(entity.getId());
        } catch (BadSessionException e) {
            e.printStackTrace();
            return false;
        } catch (AlreadyInTransactionException e) {
            e.printStackTrace();
            return false;
        } catch (NoTransactionException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotMappedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean saveAll(ISession session, Collection<D> domainObjs) {
        P entity = null;

        try {
            session.beginTransaction();
	        for(D obj : domainObjs) {
		        entity = domainToPersitent(obj);
		        session.saveOrUpdate(entity);
		        //TODO old IDs on rollback?
		        obj.setId(entity.getId());
	        }
	        session.commit();

        } catch (BadSessionException e) {
            e.printStackTrace();
            return false;
        } catch (AlreadyInTransactionException e) {
            e.printStackTrace();
            return false;
        } catch (NoTransactionException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotMappedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

	public boolean deleteEntity(ISession session, D domainObj){
		P entity = domainToPersitent(domainObj);

		try {
			session.beginTransaction();

			session.delete(entity);

			session.commit();

		} catch (BadSessionException e) {
			e.printStackTrace();
			return false;
		} catch (AlreadyInTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (NoTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean deleteAll(ISession session, Collection<IDomain> domainObjs){
		P entity = null;

		try {
			session.beginTransaction();
			for(Object obj : domainObjs) {
				entity = domainToPersitent((D) obj);
				session.delete(entity);
			}
			session.commit();

		} catch (BadSessionException e) {
			e.printStackTrace();
			return false;
		} catch (AlreadyInTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (NoTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

    protected void addClassMapping(Class clazz) {
        _entityClasses.add(clazz);
    }

	protected void addDomainClasses(Collection<Class> clazzes){
		for(Class clazz : clazzes){
			_domainClasses.add(clazz);
		}
	}

	protected abstract D persitentToDomain(P entity) throws FacadeException;

	protected abstract P domainToPersitent(D obj);

    //</editor-fold>

    //<editor-fold desc="Getter">
    public Class<D> getDomainClass() {
        return _domainClass;
    }

    public Collection<Class> getEntityClasses() {
        return _entityClasses;
    }

	public Collection<Class> getDomainClasses() {
		return _domainClasses;
	}
	//</editor-fold>

}