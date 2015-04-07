/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.databaseconnection.session.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Norskan on 30.03.2015.
 * //Todo: add loging, comments docs etz
 */
public abstract class EntityBroker<D, P> {

    protected Class<D> _domainClass;
    protected Class<P> _entityClass;

    public EntityBroker(Class domainClass, Class entityClass) {
        _domainClass = domainClass;
        _entityClass = entityClass;
    }

    //<editor-fold desc="Abstract Methode">
    public D getEntity(ISession session, int id) {
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

    public Collection<D> getAll(ISession session) {
        Collection<P> entities = null;
        try {
            entities = (Collection<P>) session.getAll(_entityClass);
        } catch (BadSessionException e) {
            e.printStackTrace();
        } catch (ClassNotMappedException e) {
            e.printStackTrace();
        }

        Collection<D> domainObjects = new ArrayList<D>();
        for(P entity : entities) {
            domainObjects.add(persitentToDomain(entity));
        }

        return domainObjects;
    }

    public boolean saveEntity(ISession session, D domainObj) {
        P entity = domainToPersitent(domainObj);

        try {
            session.beginTransaction();

            session.save(entity);

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

    public boolean saveAll(ISession session, Collection<D> domainObjs) {
        Collection<P> entities = new ArrayList<P>();
        for(D obj : domainObjs) {
            entities.add(domainToPersitent(obj));
        }

        try {
            session.beginTransaction();
            for(P entity : entities){
                session.save(entity);
            }

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

	protected abstract D persitentToDomain(P entity);

	protected abstract P domainToPersitent(D entity);

    //</editor-fold>

    //<editor-fold desc="Getter">
    public Class<D> getDomainClass() {
        return _domainClass;
    }

    public Class<P> getEntityClass() {
        return _entityClass;
    }
    //</editor-fold>

}