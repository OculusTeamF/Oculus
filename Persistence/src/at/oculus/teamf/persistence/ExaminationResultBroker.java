/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.ExaminationResult;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.ExaminationResultEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * ExaminationResultBroker.java Created by oculus on 30.04.15.
 */
public class ExaminationResultBroker extends EntityBroker {
	public ExaminationResultBroker() {
		super(ExaminationResult.class, ExaminationResultEntity.class);
	}

	@Override
	protected ExaminationResult persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException {
		ExaminationResultEntity examinationResultEntity = (ExaminationResultEntity) entity;

		ExaminationProtocol examinationProtocol = null;
		if ((examinationResultEntity.getExaminationProtocolEntity() != null)) {
			examinationProtocol = (ExaminationProtocol) Facade.getInstance().getBroker(ExaminationProtocol.class)
			                              .persistentToDomain(examinationResultEntity.getExaminationProtocolEntity());
		}

		User user = null;
		if (examinationResultEntity.getUserEntity() != null) {
			user = (User) Facade.getInstance().getBroker(User.class)
			                                                  .persistentToDomain(examinationResultEntity.getUserEntity());
		}

		return new ExaminationResult(examinationResultEntity.getId(), examinationProtocol, user,
		                             examinationResultEntity.getResult(), examinationResultEntity.getCreateDate(),
		                             examinationResultEntity.getDevice(), examinationResultEntity.getDeviceData());
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj) throws NoBrokerMappedException, BadConnectionException {
		return null;
	}
}
