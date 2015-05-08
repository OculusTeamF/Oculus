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

/**
 * PrescriptionEntity.java
 * Created by oculus on 08.05.15.
 */
@Entity
@Table(name = "prescription", schema = "", catalog = "oculus_f")
public class PrescriptionEntity {
    private int _id;
    private Integer _patientId;
    private Timestamp _issueDate;
    private Timestamp _lastPrint;
    private PatientEntity _patient;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "prescriptionId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
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
	@Column(name = "issueDate", nullable = true, insertable = true, updatable = true)
	public Timestamp getIssueDate() {
		return _issueDate;
	}
	public void setIssueDate(Timestamp issueDate) {
		_issueDate = issueDate;
	}

	@Basic
	@Column(name = "lastPrint", nullable = true, insertable = true, updatable = true)
	public Timestamp getLastPrint() {
		return _lastPrint;
	}
	public void setLastPrint(Timestamp lastPrint) {
		_lastPrint = lastPrint;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patientId", referencedColumnName = "patientId")
	public PatientEntity getPatient() {
		return _patient;
	}
	public void setPatient(PatientEntity patient) {
		_patient = patient;
	}
}
