/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
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

    public void setDiagnosis(IDiagnosis diagnosis) {
        _diagnosis = (Diagnosis) diagnosis;
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

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_diagnosis != null ? _diagnosis.hashCode() : 0);
		result = 31 * result + (_name != null ? _name.hashCode() : 0);
		result = 31 * result + (_dose != null ? _dose.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Medicine))
			return false;

		Medicine medicine = (Medicine) o;

		if (_id != medicine._id)
			return false;
		if (_diagnosis != null ? !_diagnosis.equals(medicine._diagnosis) : medicine._diagnosis != null)
			return false;
		if (_dose != null ? !_dose.equals(medicine._dose) : medicine._dose != null)
			return false;
		if (_name != null ? !_name.equals(medicine._name) : medicine._name != null)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return _name + " " + _dose;
	}
}
