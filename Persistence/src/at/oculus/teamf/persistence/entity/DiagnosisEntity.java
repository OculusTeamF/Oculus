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
import java.util.Collection;

/**
 * Hibernate annotated diagnosis class
 */
@Entity
@Table(name = "diagnosis", schema = "", catalog = "oculus_f")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getAllDiagnosisOfPatient",
                query = "SELECT d.* " +
                        "FROM diagnosis d, examinationProtocol e " +
                        "WHERE d.diagnosisId = e.diagnosisId " +
                        "AND e.patientId = ?0",
                resultClass = DiagnosisEntity.class)
})
public class DiagnosisEntity implements IEntity {
	private int _id;
	private String _title;
	private String _description;
	private Integer _doctorId;
	private DoctorEntity _doctor;
	private Collection<MedicineEntity> _medicine;

	public DiagnosisEntity() {	}

	public DiagnosisEntity(int id, String title, String description, DoctorEntity doctor) {
		_id = id;
		_title = title;
		_description = description;
		_doctor = doctor;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "diagnosisId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	@Basic
	@Column(name = "title", nullable = false, insertable = true, updatable = true, length = 255)
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		_title = title;
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
	@Column(name = "doctorId", nullable = true, insertable = false, updatable = false)
	public Integer getDoctorId() {
		return _doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		_doctorId = doctorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
	public DoctorEntity getDoctor() {
		return _doctor;
	}
	public void setDoctor(DoctorEntity doctor) {
		_doctor = doctor;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "diagnosis")
	public Collection<MedicineEntity> getMedicine() {
		return _medicine;
	}

	public void setMedicine(Collection<MedicineEntity> medicine) {
		_medicine = medicine;
	}


	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_title != null ? _title.hashCode() : 0);
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_doctorId != null ? _doctorId.hashCode() : 0);
		result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DiagnosisEntity that = (DiagnosisEntity) o;

		if (_id != that._id)
			return false;
		if (_title != null ? !_title.equals(that._title) : that._title != null)
			return false;
		if (_description != null ? !_description.equals(that._description) : that._description != null)
			return false;
		if (_doctorId != null ? !_doctorId.equals(that._doctorId) : that._doctorId != null)
			return false;
		if (_doctor != null ? !_doctor.equals(that._doctor) : that._doctor != null)
			return false;

		return true;
	}
}
