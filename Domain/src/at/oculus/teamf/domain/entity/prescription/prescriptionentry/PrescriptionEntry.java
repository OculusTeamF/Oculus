/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.prescription.prescriptionentry;

import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.prescription.IPrescription;

/**
 * Created by Simon Angerer on 08.05.2015.
 */
public class PrescriptionEntry implements IPrescriptionEntry {
	private int _id;
	private IPrescription _prescription;
	private IMedicine _medicine;

	public PrescriptionEntry() {
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public IPrescription getPrescription() {
		return _prescription;
	}

	public void setPrescription(IPrescription prescription) {
		_prescription = prescription;
	}

    public IMedicine getMedicine() {
	    return _medicine;
    }

	public void setMedicine(IMedicine medicine) {
		_medicine = medicine;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_prescription != null ? _prescription.hashCode() : 0);
		result = 31 * result + (_medicine != null ? _medicine.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PrescriptionEntry))
			return false;

		PrescriptionEntry that = (PrescriptionEntry) o;

		if (_id != that._id)
			return false;
		/*if (_medicine != null ? !_medicine.equals(that._medicine) : that._medicine != null)
			return false;
		if (_prescription != null ? !_prescription.equals(that._prescription) : that._prescription != null)
			return false;*/

		return true;
	}

	@Override
	public String toString() {
		return _prescription + " " + _medicine;
	}
}
