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
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;

/**
 * Created by Simon Angerer on 08.05.2015.
 */
public class PrescriptionEntry implements IPrescriptionEntry {
	private int _id;
	private Prescription _prescription;
	private Medicine _medicine;

	public PrescriptionEntry() {
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public Prescription getPrescription() {
		return _prescription;
	}

	public void setPrescription(Prescription prescription) {
		_prescription = prescription;
	}

    public IMedicine getMedicine() {
	    return _medicine;
    }

	public void setMedicine(IMedicine medicine) {
		_medicine = (Medicine) medicine;
	}
}
