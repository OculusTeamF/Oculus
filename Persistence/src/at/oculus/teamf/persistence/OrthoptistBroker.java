/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.persistence.entity.OrthoptistEntity;
import at.oculus.teamf.persistence.entity.UserEntity;
import at.oculus.teamf.persistence.exception.FacadeException;

/**
 * Created by Norskan on 10.04.2015.
 */
public class OrthoptistBroker extends EntityBroker<Orthoptist, OrthoptistEntity>{

	public OrthoptistBroker() {
		super(Orthoptist.class, OrthoptistEntity.class);
		addClassMapping(UserEntity.class);
	}

	@Override
	protected Orthoptist persistentToDomain(OrthoptistEntity entity) throws FacadeException {
		Orthoptist orthoptist = new Orthoptist();
		orthoptist.setId(entity.getId());
		try {
			orthoptist.setCalendar((Calendar) Facade.getInstance().getBroker(Calendar.class).persistentToDomain(
					entity.getCalendar()));
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		orthoptist.setQueue(null); // reload when needed!
		// user data
		UserEntity userEntity = entity.getUser();
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

	@Override
	protected OrthoptistEntity domainToPersistent(Orthoptist entity) {
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
		orthoptistEntity.setUser(userEntity);
		return orthoptistEntity;
	}
}
