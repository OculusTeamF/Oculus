/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.persistence.entities.QueueEntity;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<PatientQueue,QueueEntity> {

	public QueueBroker() {

		super(PatientQueue.class, QueueEntity.class);
	}

	@Override
	protected PatientQueue persitentToDomain(QueueEntity entity) {

		return null;
	}

	@Override
	protected QueueEntity domainToPersitent(PatientQueue entity) {
		return null;
	}
}