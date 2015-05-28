/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.interfaces.IWorkingHours;
import at.oculus.teamf.domain.entity.WorkingHours;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.WorkingHoursEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Time;

/**
 * WorkingHoursBroker.java Created by oculus on 27.05.15.
 */
class WorkingHoursBroker extends EntityBroker {
	public WorkingHoursBroker() {
		super(WorkingHours.class, WorkingHoursEntity.class);
		addDomainClassMapping(IWorkingHours.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		WorkingHours workingHours = new WorkingHours();
		WorkingHoursEntity workingHoursEntity = (WorkingHoursEntity) entity;

		workingHours.setId(workingHoursEntity.getId());
		if(workingHoursEntity.getMorningFrom()!=null) {
			workingHours.setMorningFrom(workingHoursEntity.getMorningFrom().toLocalTime());
		}
		if(workingHoursEntity.getMorningTo()!=null){
			workingHours.setMorningTo(workingHoursEntity.getMorningTo().toLocalTime());
		}
		if(workingHoursEntity.getAfternoonFrom()!=null){
			workingHours.setAfternoonFrom(workingHoursEntity.getAfternoonFrom().toLocalTime());
		}
		if(workingHoursEntity.getAfternoonTo()!=null){
			workingHours.setAfternoonTo(workingHoursEntity.getAfternoonTo().toLocalTime());
		}

		return workingHours;
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		WorkingHoursEntity workingHoursEntity = new WorkingHoursEntity();
		WorkingHours workingHours = (WorkingHours) obj;

		workingHoursEntity.setId(workingHours.getId());
		if(workingHours.getMorningFrom()!=null){
			workingHoursEntity.setMorningFrom(new Time(workingHours.getMorningFrom().toSecondOfDay()));
		}
		if(workingHours.getMorningTo()!=null){
			workingHoursEntity.setMorningTo(new Time(workingHours.getMorningTo().toSecondOfDay()));
		}
		if(workingHours.getAfternoonFrom()!=null){
			workingHoursEntity.setAfternoonFrom(new Time(workingHours.getAfternoonFrom().toSecondOfDay()));
		}
		if(workingHours.getAfternoonTo()!=null){
			workingHoursEntity.setAfternoonTo(new Time(workingHours.getAfternoonTo().toSecondOfDay()));
		}

		return workingHoursEntity;
	}
}
