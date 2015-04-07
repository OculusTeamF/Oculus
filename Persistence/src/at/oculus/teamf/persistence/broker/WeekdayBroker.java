/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.domain.entity.Weekday;
import at.oculus.teamf.persistence.entities.WeekdayEntity;

import java.util.Collection;

/**
 * WeekdayBroker.java
 * Created by dgr on 07.04.15.
 */
public class WeekdayBroker extends EntityBroker<Weekday,WeekdayEntity> {
    public WeekdayBroker() {
        super(Weekday.class, WeekdayEntity.class);
    }

	@Override
	public Collection<Weekday> getAll(ISession session) {
		return null;
	}

	@Override
	public boolean saveEntity(ISession session, Weekday entity) {
		return false;
	}

	@Override
	public boolean saveAll(ISession session, Collection<Weekday> collection) {
		return false;
	}

	@Override
	protected Weekday persitentToDomain(WeekdayEntity entity) {
		Weekday wd = new Weekday();
		wd.setKey(entity.getWeekDayKey());
		wd.setName(entity.getName());

		return wd;
	}

	@Override
	protected WeekdayEntity domainToPersitent(Weekday entity) {
		return null;
	}
}
