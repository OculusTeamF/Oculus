/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.persistence.entities.PatientEntity;

/**
 * Created by Norskan on 08.04.2015.
 */
public class PatientBroker extends EntityBroker<Patient, PatientEntity> {

	public PatientBroker() {
		super(Patient.class, PatientEntity.class);
	}

	@Override
	protected Patient persitentToDomain(PatientEntity entity) {
		Patient patient = new Patient();
		patient.setFirstName(entity.getFirstName());
		patient.setLastName(entity.getLastName());

		return patient;
	}

	@Override
	protected PatientEntity domainToPersitent(Patient entity) {
		return null;
	}
}
