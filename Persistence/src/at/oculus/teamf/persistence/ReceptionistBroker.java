/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.Receptionist;
import at.oculus.teamf.persistence.entity.ReceptionistEntity;
import at.oculus.teamf.persistence.entity.UserEntity;

import java.sql.Timestamp;

/**
 * patient broker translating domain objects to persistence entities
 */
public class ReceptionistBroker extends EntityBroker<Receptionist, ReceptionistEntity> {
	public ReceptionistBroker() {
		super(Receptionist.class, ReceptionistEntity.class);
		addClassMapping(UserEntity.class);
	}

    /**
     * converts a persitency entity to a domain object
     *
     * @param entity that needs to be converted
     * @return domain object that is created from entity
     */
    @Override
	protected Receptionist persistentToDomain(ReceptionistEntity entity) {
        log.debug("converting persistence entity " + _entityClass.getClass() + " to domain object " + _domainClass.getClass());
        Receptionist receptionist = new Receptionist();
        receptionist.setId(entity.getId());
		// user data
		UserEntity userEntity = entity.getUser();
		if (userEntity != null){
			receptionist.setUserGroupId(userEntity.getUserGroupId());
			receptionist.setUserName(userEntity.getUserName());
			receptionist.setPassword(userEntity.getPassword());
			receptionist.setTitle(userEntity.getTitle());
			receptionist.setFirstName(userEntity.getFirstName());
			receptionist.setLastName(userEntity.getLastName());
			receptionist.setEmail(userEntity.getEmail());
			receptionist.setCreateDate(userEntity.getCreateDate());
			receptionist.setIdleDate(userEntity.getIdleDate());
		}
		//orthoptist.setUserGroup(userEntity.getUserGroup());
		return receptionist;
    }

    /**
     * Converts a domain object to persitency entity
     * @param entity that needs to be converted
     * @return return a persitency entity
     */
    @Override
	protected ReceptionistEntity domainToPersistent(Receptionist entity) {
        log.debug("converting domain object " + _domainClass.getClass() + " to persistence entity " + _entityClass.getClass());
        ReceptionistEntity receptionistEntity = new ReceptionistEntity();
        receptionistEntity.setId(entity.getId());
		// user data
		UserEntity userEntity = new UserEntity();
		userEntity.setId(entity.getUserId());
		userEntity.setUserGroupId(entity.getUserGroupId());
		userEntity.setUserName(entity.getUserName());
		userEntity.setPassword(entity.getPassword());
		userEntity.setTitle(entity.getTitle());
		userEntity.setFirstName(entity.getFirstName());
		userEntity.setLastName(entity.getLastName());
		userEntity.setEmail(entity.getEmail());
		userEntity.setCreateDate((Timestamp) entity.getCreateDate());
		userEntity.setIdleDate((Timestamp) entity.getIdleDate());
		//userEntity.setUserGroup(entity.getUserGroup());
		receptionistEntity.setUser(userEntity);
		return null;
	}
}
