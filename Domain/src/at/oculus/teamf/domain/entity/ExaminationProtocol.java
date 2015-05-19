/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamE.domain.interfaces.*;
import at.oculus.teamE.domain.readonly.IRDiagnosisTb2;
import at.oculus.teamE.domain.readonly.IRUserTb2;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddExaminationProtocol;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationResultException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * ExaminationProtocol.java
 * Created by oculus on 16.04.15.
 */
//Todo: Proxy
public class ExaminationProtocol implements IExaminationProtocol, ILogger, IExaminationProtocolTb2 {
    private int _id;
	private Date _startTime;
	private Date _endTime;
	private String _description;
	private Doctor _doctor;
	private Orthoptist _orthoptist;
	private Diagnosis _diagnosis;
	private Patient _patient;
	private Collection<ExaminationResult> _results;

	public ExaminationProtocol() {}

    public ExaminationProtocol(IPatientTb2 iPatientTb2, IUserTb2 iUserTb2){
        _patient = (Patient) iPatientTb2;
        _doctor = (Doctor) iUserTb2;
    }

	public ExaminationProtocol(int id, Date startTime, Date endTime, String description, Patient patient, Doctor doctor,
	                           Orthoptist orthoptist, Diagnosis diagnosis) {
		_id = id;
		_startTime = startTime;
		_endTime = endTime;
		_description = description;
		_doctor = doctor;
		_orthoptist = orthoptist;
		_diagnosis = diagnosis;
		_patient = patient;
	}

	@Override
	public int getId() {
		return _id;
	}
	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
    public Date getStartTime() {
		return _startTime;
	}
	@Override
    public void setStartTime(Date startTime) {
		_startTime = startTime;
	}

	@Override
    public Date getEndTime() {
		return _endTime;
	}
	@Override
    public void setEndTime(Date endTime) {
		_endTime = endTime;
	}

	@Override
    public String getDescription() {
		return _description;
	}

    @Override
    public void setEndTimeProtocol(LocalDateTime localDateTime) {
        _endTime = Date.from(localDateTime.toInstant(ZoneOffset.ofHours(0)));
    }

    @Override
    public void setDescription(String description) {
		_description = description;
	}

    @Override
    public void addExamination(IExaminationTb2 iExaminationTb2) {
        //TODO addExamination
        log.debug("adding examination result to examination protocol " + this);
        IExaminationResult result = (IExaminationResult) iExaminationTb2;
        result.setExaminationProtocol(this);

        if (_results == null) {
            _results = new LinkedList<ExaminationResult>();
        }
        _results.add((ExaminationResult)result);

    }

    @Override
    public List<? extends IExaminationTb2> getExaminations() {
        return (List<? extends IExaminationTb2>) _results;
    }

    @Override
    public void removeExamination(IExaminationTb2 iExaminationTb2) {
        //TODO removeExamination
    }

    @Override
    public Doctor getDoctor() {
		return _doctor;
	}

	@Override
	public void setDoctor(IDoctor doctor) {
		_doctor = (Doctor) doctor;
	}

	@Override
    public Orthoptist getOrthoptist() {
		return _orthoptist;
	}

	@Override
	public void setOrthoptist(IOrthoptist orthoptist) {
		_orthoptist = (Orthoptist) orthoptist;
	}

    @Override
    public Integer getExaminationProtocolId() {
        return getId();
    }

    @Override
    public IRUserTb2 getCreator() {
        if(_orthoptist != null){
            return _orthoptist;
        }else if(_doctor != null){
            return _doctor;
        }
       return null;
    }

    @Override
    public IRDiagnosisTb2 getDiagnosis() {
        return _diagnosis;
    }

    @Override
    public IDiagnosis getTeamFDiagnosis() {
		return _diagnosis;
	}


    @Override
    public LocalDateTime getStartTimeProtocol() {
        Date ts = _startTime;
        Instant instant = Instant.ofEpochMilli(ts.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    @Override
    public LocalDateTime getEndTimeProtocol() {
        Date ts = _endTime;
        Instant instant = Instant.ofEpochMilli(ts.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    @Override
    public void setDiagnosis(IDiagnosis diagnosis) {
		_diagnosis = (Diagnosis) diagnosis;
	}

	@Override
    public Patient getPatient() {
		return _patient;
	}

	@Override
	public void setPatient(IPatient patient) {
		_patient = (Patient) patient;
	}

	public Collection<IExaminationResult> getExaminationResults() throws CouldNotGetExaminationResultException {

		if(_results == null) {
			try {
				Facade.getInstance().reloadCollection(this, ExaminationResult.class);
			} catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
				log.error(e.getMessage());
				throw new CouldNotGetExaminationResultException();

			}
		}

		return (Collection<IExaminationResult>)(Collection<?>) _results;
	}

	public void setResults(Collection<ExaminationResult> results) {
		_results = results;
	}

	@Override
    public String toString(){
        return (new SimpleDateFormat("dd.MM.yyyy").format(_startTime));
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExaminationProtocol))
			return false;

		ExaminationProtocol that = (ExaminationProtocol) o;

		if (_id != that._id)
			return false;
		if (_description != null ? !_description.equals(that._description) : that._description != null)
			return false;
		if (_diagnosis != null ? !_diagnosis.equals(that._diagnosis) : that._diagnosis != null)
			return false;
		if (_doctor != null ? !_doctor.equals(that._doctor) : that._doctor != null)
			return false;
		if (_endTime != null ? !_endTime.equals(that._endTime) : that._endTime != null)
			return false;
		if (_orthoptist != null ? !_orthoptist.equals(that._orthoptist) : that._orthoptist != null)
			return false;
		if (_patient != null ? !_patient.equals(that._patient) : that._patient != null)
			return false;
		if (_startTime != null ? !_startTime.equals(that._startTime) : that._startTime != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_startTime != null ? _startTime.hashCode() : 0);
		result = 31 * result + (_endTime != null ? _endTime.hashCode() : 0);
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
		result = 31 * result + (_orthoptist != null ? _orthoptist.hashCode() : 0);
		result = 31 * result + (_diagnosis != null ? _diagnosis.hashCode() : 0);
		result = 31 * result + (_patient != null ? _patient.hashCode() : 0);
		return result;
	}
}