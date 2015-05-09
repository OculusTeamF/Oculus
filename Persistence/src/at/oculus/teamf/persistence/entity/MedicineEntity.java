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

/**
 * MedicineEntity.java Created by oculus on 06.05.15.
 */

@Entity
@Table(name = "medicine", schema = "", catalog = "oculus_f")
@NamedNativeQueries({@NamedNativeQuery(
		name = "getMedicineByPatientId",
		query = "SELECT m.* " +
		        "FROM medicine m, examinationprotocol e " +
		        "WHERE m.diagnosisId = e.diagnosisId " +
		        "AND e.patientId = ?0",
		resultClass = MedicineEntity.class)})
public class MedicineEntity implements IEntity {
	private int _id;
	private Integer _diagnosisId;
	private DiagnosisEntity _diagnosis;
	private String _name;
	private String _dose;

	public MedicineEntity() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "medicineId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	@Basic
	@Column(name = "diagnosisId", nullable = true, insertable = false, updatable = false)
	public Integer getDiagnosisId() {
		return _diagnosisId;
	}

	public void setDiagnosisId(Integer _diagnosisId) {
		this._diagnosisId = _diagnosisId;
	}

	@Basic
	@Column(name = "name", nullable = false, insertable = true, updatable = true)
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	@Basic
	@Column(name = "dose", nullable = true, insertable = true, updatable = true)
	public String getDose() {
		return _dose;
	}

	public void setDose(String dose) {
		_dose = dose;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosisId", referencedColumnName = "diagnosisId")
	public DiagnosisEntity getDiagnosis() {
		return _diagnosis;
	}

	public void setDiagnosis(DiagnosisEntity diagnosis) {
		_diagnosis = diagnosis;
	}
}