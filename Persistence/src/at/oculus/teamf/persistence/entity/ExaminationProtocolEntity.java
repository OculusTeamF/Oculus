/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Hibernate annotated examination protocol class
 */
@Entity
@Table(name = "examinationprotocol", schema = "", catalog = "oculus_f")
public class ExaminationProtocolEntity implements IEntity {
	private int _id;
    private Timestamp _startTime;
    private Timestamp _endTime;
    private String _description;
	private Integer _patientId;
	private Integer _userId;
	private Integer _diagnosisId;
	private PatientEntity _patient;
	private UserEntity _user;
	private DiagnosisEntity _diagnosis;
    private Collection<ExaminationResultEntity> _results;

	public ExaminationProtocolEntity() {}

    public ExaminationProtocolEntity(int id, Timestamp startTime, Timestamp endTime, String description,
                                     PatientEntity patient, UserEntity user, DiagnosisEntity diagnosis) {
		_id = id;
		_startTime = startTime;
		_endTime = endTime;
		_description = description;
		_patient = patient;
		_user = user;
		_diagnosis = diagnosis;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "examinationProtocolId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	@Basic
	@Column(name = "startProtocol", nullable = false, insertable = true, updatable = true)
    public Timestamp getStartTime() {
        return _startTime;
	}

    public void setStartTime(Timestamp startTime) {
        _startTime = startTime;
	}

	@Basic
	@Column(name = "endProtocol", nullable = false, insertable = true, updatable = true)
    public Timestamp getEndTime() {
        return _endTime;
	}

    public void setEndTime(Timestamp endTime) {
        _endTime = endTime;
	}

	@Basic
	@Column(name = "description", nullable = false, insertable = true, updatable = true, length = 65535)
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		_description = description;
	}

	@Basic
	@Column(name = "patientId", nullable = true, insertable = false, updatable = false)
	public Integer getPatientId() {
		return _patientId;
	}
	public void setPatientId(Integer patientId) {
		_patientId = patientId;
	}

	@Basic
	@Column(name = "userId", nullable = true, insertable = false, updatable = false)
	public Integer getUserId() {
		return _userId;
	}
	public void setUserId(Integer userId) {
		_userId = userId;
	}

	@Basic
	@Column(name = "diagnosisId", nullable = true, insertable = false, updatable = false)
	public Integer getDiagnosisId() {
		return _diagnosisId;
	}
	public void setDiagnosisId(Integer diagnosisId) {
		_diagnosisId = diagnosisId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patientId", referencedColumnName = "patientId")
	public PatientEntity getPatient() {
		return _patient;
	}
	public void setPatient(PatientEntity patient) {
		_patient = patient;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	public UserEntity getUser() {
		return _user;
	}
	public void setUser(UserEntity user) {
		_user = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosisId", referencedColumnName = "diagnosisId")
	public DiagnosisEntity getDiagnosis() {
		return _diagnosis;
	}
	public void setDiagnosis(DiagnosisEntity diagnosis) {
		_diagnosis = diagnosis;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "examinationProtocolId", referencedColumnName = "examinationProtocolId")
	public Collection<ExaminationResultEntity> getResults() {
		return _results;
	}
	public void setResults(Collection<ExaminationResultEntity> results) {
		_results = results;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationProtocolEntity)) return false;

        ExaminationProtocolEntity that = (ExaminationProtocolEntity) o;

        if (_id != that._id) return false;
        if (_description != null ? !_description.equals(that._description) : that._description != null) return false;
        if (_diagnosis != null ? !_diagnosis.equals(that._diagnosis) : that._diagnosis != null) return false;
        if (_diagnosisId != null ? !_diagnosisId.equals(that._diagnosisId) : that._diagnosisId != null) return false;
        if (_endTime != null ? !_endTime.equals(that._endTime) : that._endTime != null) return false;
        if (_patient != null ? !_patient.equals(that._patient) : that._patient != null) return false;
        if (_patientId != null ? !_patientId.equals(that._patientId) : that._patientId != null) return false;
        if (_results != null ? !_results.equals(that._results) : that._results != null) return false;
        if (_startTime != null ? !_startTime.equals(that._startTime) : that._startTime != null) return false;
        if (_user != null ? !_user.equals(that._user) : that._user != null) return false;
        if (_userId != null ? !_userId.equals(that._userId) : that._userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_startTime != null ? _startTime.hashCode() : 0);
        result = 31 * result + (_endTime != null ? _endTime.hashCode() : 0);
        result = 31 * result + (_description != null ? _description.hashCode() : 0);
        result = 31 * result + (_patientId != null ? _patientId.hashCode() : 0);
        result = 31 * result + (_userId != null ? _userId.hashCode() : 0);
        result = 31 * result + (_diagnosisId != null ? _diagnosisId.hashCode() : 0);
        result = 31 * result + (_patient != null ? _patient.hashCode() : 0);
        result = 31 * result + (_user != null ? _user.hashCode() : 0);
        result = 31 * result + (_diagnosis != null ? _diagnosis.hashCode() : 0);
        result = 31 * result + (_results != null ? _results.hashCode() : 0);
        return result;
    }
}