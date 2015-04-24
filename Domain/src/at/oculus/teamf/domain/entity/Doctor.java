/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;

import java.util.Collection;


// Todo: add docs, implement equals
/**
 * @author Simon Angerer
 */
public class Doctor extends User implements IDoctor {
    //<editor-fold desc="Attributes">
    private int _id;
    private Calendar _calendar;
    private PatientQueue _queue;
    private Collection<Patient> _patients;
    private Doctor _doctorSubstitude;
    //</editor-fold>

    public Doctor() {
    }

    public Doctor(int id, Calendar calendar, PatientQueue queue, Collection<Patient> patients,
                  Doctor doctorSubstitude) {
        _id = id;
        _calendar = calendar;
		_queue = queue;
		_patients = patients;
		_doctorSubstitude = doctorSubstitude;
	}

	//<editor-fold desc="Getter/Setter">
    @Override
    public int getId() {
        return _id;
    }
    @Override
    public void setId(int id) {
        _id = id;
    }

	@Override
    public Calendar getCalendar() {
		return _calendar;
    }
    @Override
    public void setCalendar(Calendar _calendar) {
        this._calendar = _calendar;
    }

    @Override
    public PatientQueue getQueue() throws NoBrokerMappedException, BadConnectionException {
        if(_queue != null) {
            _queue = new PatientQueue(this);
        }
        return _queue;
    }
    @Override
    public void setQueue(PatientQueue _queue) {
        this._queue = _queue;
    }

    @Override
    public Doctor getDoctorSubstitude() {
	    return _doctorSubstitude;
    }
    @Override
    public void setDoctorSubstitude(Doctor doctorSubstitude) {
        _doctorSubstitude = doctorSubstitude;
    }

    @Override
    public void addPatient(Patient patient) {
        if (patient != null) {
            _patients.add(patient);
        }
    }

    @Override
    public Collection<Patient> getPatients() throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException {
        Facade facade = Facade.getInstance();

        facade.reloadCollection(this, Patient.class);

        return _patients;
    }

	@Override
    public void setPatients(Collection<Patient> patients) {
		_patients = patients;
	}
    //</editor-fold>
}