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
import at.oculus.teamf.persistence.EntityBroker;
import at.oculus.teamf.persistence.entities.ReceptionistEntity;
import at.oculus.teamf.persistence.entities.UserEntity;

/**
 * Created by Norskan on 08.04.2015.
 */
public class ReceptionistBroker extends EntityBroker<Receptionist, ReceptionistEntity> {
	public ReceptionistBroker() {
		super(Receptionist.class, ReceptionistEntity.class);
	}

	@Override
	protected Receptionist persitentToDomain(ReceptionistEntity entity) {
		Receptionist receptionist = new Receptionist();
		receptionist.setId(entity.getId());
		// TODO Kalender und Queues laden
		// user data
		UserEntity userEntity = entity.getUser();
		receptionist.setUserGroupId(userEntity.getUserGroupId());
		receptionist.setUserName(userEntity.getUserName());
		receptionist.setPassword(userEntity.getPassword());
		receptionist.setTitle(userEntity.getTitle());
		receptionist.setFirstName(userEntity.getFirstName());
		receptionist.setLastName(userEntity.getLastName());
		receptionist.setEmail(userEntity.getEmail());
		receptionist.setCreateDate(userEntity.getCreateDate());
		receptionist.setIdleDate(userEntity.getIdleDate());
		//orthoptist.setUserGroup(userEntity.getUserGroup());
		return receptionist;
	}

	@Override
	protected ReceptionistEntity domainToPersitent(Receptionist entity) {
		ReceptionistEntity receptionistEntity = new ReceptionistEntity();
		receptionistEntity.setId(entity.getId());
		// TODO Kalender und Queues laden
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
		userEntity.setCreateDate(entity.getCreateDate());
		userEntity.setIdleDate(entity.getIdleDate());
		//userEntity.setUserGroup(entity.getUserGroup());
		receptionistEntity.setUser(userEntity);
		return null;
	}
}
