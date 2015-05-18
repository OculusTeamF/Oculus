/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamE.domain.interfaces.IUserTb2;
import at.oculus.teamf.domain.entity.exception.CantLoadPatientsException;
import at.oculus.teamf.domain.entity.factory.QueueFactory;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;


// Todo: add docs, implement equals
/**
 * @author Simon Angerer
 */
public class Doctor extends User implements IDoctor, ILogger{
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
    public ICalendar getCalendar() {
		return _calendar;
    }
    @Override
    public void setCalendar(ICalendar _calendar) {
        this._calendar = (Calendar) _calendar;
    }

    @Override
    public IPatientQueue getQueue() {
        if(_queue == null) {
            _queue = QueueFactory.getInstance().getUserQueue(this);
        }
        return _queue;
    }
    @Override
    public void setQueue(IPatientQueue _queue) {
        this._queue = (PatientQueue) _queue;
    }

    @Override
    public IDoctor getDoctorSubstitude() {
	    return _doctorSubstitude;
    }
    @Override
    public void setDoctorSubstitude(IDoctor doctorSubstitude) {
        _doctorSubstitude = (Doctor) doctorSubstitude;
    }

    @Override
    public void addPatient(IPatient patient) {
        if (patient != null) {
            _patients.add((Patient)patient);
        }
    }

    @Override
    public Collection<IPatient> getPatients() throws CantLoadPatientsException {
        Facade facade = Facade.getInstance();

        try {
            facade.reloadCollection(this, Patient.class);
        } catch (BadConnectionException | NoBrokerMappedException | ReloadInterfaceNotImplementedException | DatabaseOperationException | InvalidReloadClassException e) {
            log.error(e.getMessage());
            throw new CantLoadPatientsException();
        }

        return (Collection<IPatient>)(Collection<?>)_patients;
    }

	@Override
    public void setPatients(Collection<IPatient> patients) {
		_patients = (Collection<Patient>)(Collection<?>)patients;
	}

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(obj instanceof Doctor) {
            if(((Doctor)obj).getId() == getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
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
        //TODO implement getExaminationProtocols()
        return null;
    }

    //</editor-fold>
}