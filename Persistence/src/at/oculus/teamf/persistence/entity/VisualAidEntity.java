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
 * VisualAidEntity.java Created by oculus on 11.05.15.
 */
@Entity
@Table(name = "visualaid", schema = "", catalog = "oculus_f")
@NamedNativeQueries({@NamedNativeQuery(
		name = "getAllVisualAidOfPatient",
		query = "SELECT v.* " +
		        "FROM visualAid v, examinationProtocol e " +
		        "WHERE v.diagnosisId = e.diagnosisId " +
		        "AND e.patientId = ?0",
		resultClass = VisualAidEntity.class)})
public class VisualAidEntity implements IEntity {
	private int _id;
	private Integer _diagnosisId;
	private String _description;
	private Timestamp _issueDate;
	private Timestamp _lastPrint;
	private DiagnosisEntity _diagnosis;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "visualAidId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Basic
	@Column(name = "diagnosisId", nullable = true, insertable = false, updatable = false)
	public Integer getDiagnosisId() {
		return _diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		_diagnosisId = diagnosisId;
	}

	@Basic
	@Column(name = "description", nullable = false, insertable = true, updatable = true)
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	@Basic
	@Column(name = "issueDate", nullable = false, insertable = true, updatable = true)
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
	@JoinColumn(name = "diagnosisId", referencedColumnName = "diagnosisId")
	public DiagnosisEntity getDiagnosis() {
		return _diagnosis;
	}

	public void setDiagnosis(DiagnosisEntity diagnosis) {
		if (diagnosis != null) {
			setDiagnosisId(diagnosis.getId());
		}
		_diagnosis = diagnosis;
	}
}
