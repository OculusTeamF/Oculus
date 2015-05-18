/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.persistence.entity.OrthoptistEntity;
import at.oculus.teamf.persistence.entity.UserEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * orthoptist broker translating domain objects to persistence entities
 */
public class OrthoptistBroker extends EntityBroker<Orthoptist, OrthoptistEntity> implements ISearch{

	public OrthoptistBroker() {
		super(Orthoptist.class, OrthoptistEntity.class);
		addClassMapping(UserEntity.class);
	}

    /**
     * converts a persitency entity to a domain object
     *
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    @Override
	protected Orthoptist persistentToDomain(OrthoptistEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        Orthoptist orthoptist = new Orthoptist();
        orthoptist.setId(entity.getId());

		orthoptist.setCalendar((Calendar) Facade.getInstance().getBroker(Calendar.class).persistentToDomain(
					entity.getCalendar()));

		orthoptist.setQueue(null); // reload when needed!
		// user data
		UserEntity userEntity = entity.getUser();
        orthoptist.setUserId(entity.getUserId());
        orthoptist.setUserGroupId(userEntity.getUserGroupId());
		orthoptist.setUserName(userEntity.getUserName());
		orthoptist.setPassword(userEntity.getPassword());
		orthoptist.setTitle(userEntity.getTitle());
		orthoptist.setFirstName(userEntity.getFirstName());
		orthoptist.setLastName(userEntity.getLastName());
		orthoptist.setEmail(userEntity.getEmail());
		orthoptist.setCreateDate(userEntity.getCreateDate());
		orthoptist.setIdleDate(userEntity.getIdleDate());
		//orthoptist.setUserGroup(userEntity.getUserGroup());
		return orthoptist;
    }

    /**
     * Converts a domain object to persitency entity
     * @param entity that needs to be converted
     * @return return a persitency entity
     */
    @Override
	protected OrthoptistEntity domainToPersistent(Orthoptist entity) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        OrthoptistEntity orthoptistEntity = new OrthoptistEntity();
        orthoptistEntity.setId(entity.getId());
		/*try {
			orthoptistEntity.setCalendar((CalendarEntity) Facade.getInstance().getBroker(Calendar.class)
			                                                    .persistentToDomain((IEntity) entity.getCalendar()));
		} catch (FacadeException e) {
			e.printStackTrace();
		}*/
		orthoptistEntity.setCalendarId(entity.getCalendar().getId());
		// user data
		UserEntity userEntity = new UserEntity();
		userEntity.setId(entity.getTeamFUserId());
		userEntity.setUserGroupId(entity.getUserGroupId());
		userEntity.setUserName(entity.getUserName());
		userEntity.setPassword(entity.getPassword());
		userEntity.setTitle(entity.getTitle());
		userEntity.setFirstName(entity.getFirstName());
		userEntity.setLastName(entity.getLastName());
		userEntity.setEmail(entity.getEmail());
		userEntity.setCreateDate((Timestamp) entity.getCreateDate());
		userEntity.setIdleDate((Timestamp) entity.getTeamFIdleDate());
		//userEntity.setUserGroup(entity.getUserGroup());
		orthoptistEntity.setUser(userEntity);
		return orthoptistEntity;
	}

    @Override
    public Collection<Orthoptist> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException {
        if (params.length == 1) {
            Collection<OrthoptistEntity> result = (Collection<OrthoptistEntity>)(Collection<?>)session.search("getOrthoptistByUserId", params[0]);

	        Collection<Orthoptist> domainResult = new LinkedList<>();
	        for(OrthoptistEntity ort : result) {
		        domainResult.add(persistentToDomain(ort));
	        }

	        return domainResult;
        } else {
            return null;
        }
    }
}
