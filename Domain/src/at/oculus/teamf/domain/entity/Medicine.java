/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IMedicine;

/**
 * Created by Simon Angerer on 08.05.2015.
 */
public class Medicine implements IMedicine {
	private int _id;
	private Diagnosis _diagnosis;
	private String _name;
	private String _dose;

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	public Diagnosis getDiagnosis() {
		return _diagnosis;
	}
	public void setDiagnosis(Diagnosis diagnosis) {
		_diagnosis = diagnosis;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDose() {
		return _dose;
	}

	public void setDose(String dose) {
		_dose = dose;
	}
}
