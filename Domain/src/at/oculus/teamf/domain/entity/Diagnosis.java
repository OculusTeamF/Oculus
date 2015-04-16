/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

/**
 * Diagnosis.java
 * Created by oculus on 16.04.15.
 */
public class Diagnosis implements IDomain {
    private int _id;
    private String _title;
    private String _description;
	private Integer _doctorId;
	private Doctor _doctor;

	public Diagnosis() {}

	public Diagnosis(int id, String title, String description, Doctor doctor) {
		_id = id;
		_title = title;
		_description = description;
		_doctor = doctor;
	}

	@Override
	public int getId() {
		return _id;
	}
	@Override
	public void setId(int id) {
		_id = id;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		_title = title;
	}

	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		_description = description;
	}

	public Integer getDoctorId() {
		return _doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		_doctorId = doctorId;
	}

	public Doctor getDoctor() {
		return _doctor;
	}
	public void setDoctor(Doctor doctor) {
		_doctor = doctor;
		if(doctor!=null) {
			_doctorId = doctor.getId();
		}
	}
}
