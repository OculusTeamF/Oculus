/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.*;

import java.util.Collection;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Doctor extends User implements IDomain {
    //<editor-fold desc="Attributes">
    private int _id;
    private Calendar _calendar;
    private PatientQueue _queue;
    private Collection<Patient> _patients;
    private Doctor _doctorSubstitude;
    //</editor-fold>

	public Doctor(){};

	public Doctor(int id, Calendar calendar, PatientQueue queue, Collection<Patient> patients,
	              Doctor doctorSubstitude) {
		_id = id;
		_calendar = calendar;
		_queue = queue;
		_patients = patients;
		_doctorSubstitude = doctorSubstitude;
	}

	//<editor-fold desc="Getter/Setter">
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }

	public Calendar getCalendar() {
		return _calendar;
    }
    public void setCalendar(Calendar _calendar) {
        this._calendar = _calendar;
    }

    public PatientQueue getQueue() {
	    _queue = new PatientQueue(this);
	    return _queue;
    }
    public void setQueue(PatientQueue _queue) {
        this._queue = _queue;
    }

    public Doctor getDoctorSubstitude() {
	    return _doctorSubstitude;
    }
    public void setDoctorSubstitude(Doctor doctorSubstitude) {
        _doctorSubstitude = doctorSubstitude;
    }

    public void addPatient(Patient patient) {
        if (patient != null) {
            _patients.add(patient);
        }
    }

	public void setPatients(Collection<Patient> patients) {
		_patients = patients;
	}
    public Collection<Patient> getPatients() {
	    Facade facade = Facade.getInstance();

	    try {
		    facade.reloadCollection(this, Patient.class);
	    } catch (ReloadInterfaceNotImplementedException e) {
		    e.printStackTrace();
		    //Todo: Add Logging
	    } catch (InvalidReloadParameterException invalidReloadParameterException) {
		    invalidReloadParameterException.printStackTrace();
	    } catch (NotAbleToLoadClassException e) {
		    e.printStackTrace();
	    } catch (NoBrokerMappedException e) {
		    e.printStackTrace();
	    } catch (FacadeException e) {
		    e.printStackTrace();
	    }

	    return _patients;
    }
    //</editor-fold>

	@Override
	public String toString(){
		return getTitle() + " " + getFirstName() + " " + getLastName();
	}
}