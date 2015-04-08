/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class PatientQueue {

    //<editor-fold desc="Attributes">
    private int _userID;
	private LinkedList<Patient> _patients;
	private LinkedList<Integer> _queueIds;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    public int getUserID() {
        return _userID;
    }

    public void setUserID(int userID) {
	    _userID = userID;
    }

    public Collection<Patient> getPatients() {
        return _patients;
    }

    public void setPatients(Collection<Patient> _patients) {
        _patients = _patients;
    }
    //</editor-fold>

    public void insert(Patient patient, Patient parent) { }

    public Patient getNext() { return null; }
}
