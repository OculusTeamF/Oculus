/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.medicine;

import at.oculus.teamE.domain.interfaces.IDiagnosisTb2;
import at.oculus.teamE.domain.interfaces.IMedicineTb2;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;

import java.time.LocalDate;

/**
 * Created by Simon Angerer on 08.05.2015.
 */
public class Medicine implements IMedicine, IMedicineTb2 {
	private int _id;
	private IDiagnosis _diagnosis;
	private String _name;
	private String _dose;
    private LocalDate _startDate;
    private LocalDate _endDate;

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

    public Medicine(IDiagnosisTb2 diagnosis, String name, String dose, LocalDate start, LocalDate end){
        _name = name;
        _dose = dose;
        _startDate = start;
        _endDate = end;
        _diagnosis = (IDiagnosis) diagnosis;
    }
    public Medicine(){
    }

	public IDiagnosis getTeamFDiagnosis() {
		return _diagnosis;
	}

    @Override
    public IDiagnosisTb2 getDiagnosis() {
        return (IDiagnosisTb2) _diagnosis;
    }

    @Override
    public void setDiagnosis(IDiagnosisTb2 iDiagnosisTb2) {
        _diagnosis = (IDiagnosis) iDiagnosisTb2;
    }

    public void setDiagnosis(IDiagnosis diagnosis) {
        _diagnosis = diagnosis;
    }

    @Override
    public Integer getMedicineId() {
        return getId();
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

    @Override
    public LocalDate getStartDate() {
        return _startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return _endDate;
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
