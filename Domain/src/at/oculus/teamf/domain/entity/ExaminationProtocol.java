/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExaminationProtocol.java
 * Created by oculus on 16.04.15.
 */
public class ExaminationProtocol implements IExaminationProtocol {
    private int _id;
	private Date _startTime;
	private Date _endTime;
	private String _description;
	private Doctor _doctor;
	private Orthoptist _orthoptist;
	private Diagnosis _diagnosis;
	private Patient _patient;

	public ExaminationProtocol() {}

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
    public void setDescription(String description) {
		_description = description;
	}

	@Override
    public Doctor getDoctor() {
		return _doctor;
	}
	@Override
    public void setDoctor(Doctor doctor) {
		_doctor = doctor;
	}

	@Override
    public Orthoptist getOrthoptist() {
		return _orthoptist;
	}
	@Override
    public void setOrthoptist(Orthoptist orthoptist) {
		_orthoptist = orthoptist;
	}

	@Override
    public Diagnosis getDiagnosis() {
		return _diagnosis;
	}
	@Override
    public void setDiagnosis(Diagnosis diagnosis) {
		_diagnosis = diagnosis;
	}

	@Override
    public Patient getPatient() {
		return _patient;
	}
	@Override
    public void setPatient(Patient patient) {
		_patient = patient;
	}

    @Override
    public String toString(){
        return (new SimpleDateFormat("dd.MM.yyyy").format(_startTime));
    }
}
