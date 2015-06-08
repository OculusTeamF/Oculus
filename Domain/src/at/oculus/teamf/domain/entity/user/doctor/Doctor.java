/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.user.doctor;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamf.domain.entity.user.User;
import at.oculus.teamf.domain.entity.exception.CantLoadPatientsException;
import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author Simon Angerer
 */
public class Doctor extends User implements IDoctor, ILogger{
    //<editor-fold desc="Attributes">
    private int _id;
    private ICalendar _calendar;
    private IPatientQueue _queue;
    private Collection<IPatient> _patients;
    private IDoctor _doctorSubstitude;
    //</editor-fold>

    public Doctor() {
    }

    public Doctor(int id, ICalendar calendar, IPatientQueue queue, Collection<IPatient> patients,
                  IDoctor doctorSubstitude) {
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
    public ICalendar getCalendar() {
		return _calendar;
    }
    @Override
    public void setCalendar(ICalendar _calendar) {
        this._calendar = _calendar;
    }

    @Override
    public IPatientQueue getQueue() {
        return _queue;
    }
    @Override
    public void setQueue(IPatientQueue _queue) {
        this._queue = _queue;
    }

    @Override
    public IDoctor getDoctorSubstitude() {
	    return _doctorSubstitude;
    }
    @Override
    public void setDoctorSubstitude(IDoctor doctorSubstitude) {
        _doctorSubstitude = doctorSubstitude;
    }

    @Override
    public void addPatient(IPatient patient) {
        if (patient != null) {
            _patients.add(patient);
        }
    }

    @Override
    public Collection<IPatient> getPatients() throws CantLoadPatientsException {
        return (Collection<IPatient>)(Collection<?>)_patients;
    }

	@Override
    public void setPatients(Collection<IPatient> patients) {
		_patients = (Collection<IPatient>)(Collection<?>)patients;
	}

    @Override
    public Integer getUserId() {
        return _id;
    }

    //not used
    @Override
    public LocalDateTime getCreationDate() {
        return null;
    }

    //not used
    @Override
    public LocalDateTime getIdleDate() {
        return null;
    }

    @Override
    public List<? extends IExaminationProtocolTb2> getExaminationProtocols() {
        //not used
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IDoctor)) return false;

        IDoctor doctor = (IDoctor) o;

        if (_id != doctor.getId()) return false;

        return true;

        //removed until caching is implemented
        /*if (!_calendar.equals(doctor.getCalendar())) return false;
        //if (!_queue.equals(doctor.getQueue())) return false;
        try {
            if (!_patients.equals(doctor.getPatients())) return false;
        } catch (CantLoadPatientsException e) {
            log.error(e.getMessage());
            return false;
        }
        return _doctorSubstitude.equals(doctor.getDoctorSubstitude());
        */
    }

    //</editor-fold>
}