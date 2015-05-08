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
 * PrescriptionEntryEntity.java Created by oculus on 08.05.15.
 */
@Entity
@Table(name = "prescriptionEntry", schema = "", catalog = "oculus_f")
public class PrescriptionEntryEntity {
	private int _id;
	private Integer _prescriptionId;
	private Integer _medicineId;
	private PrescriptionEntity _prescription;
	private MedicineEntity _medicineEntity;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "prescriptionEntryId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	@Basic
	@Column(name = "prescriptionId", nullable = true, insertable = false, updatable = false)
	public Integer getPrescriptionId() {
		return _prescriptionId;
	}
	public void setPrescriptionId(Integer prescriptionId) {
		_prescriptionId = prescriptionId;
	}

	@Basic
	@Column(name = "medicineId", nullable = true, insertable = false, updatable = false)
	public Integer getMedicineId() {
		return _medicineId;
	}
	public void setMedicineId(Integer medicineId) {
		_medicineId = medicineId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescriptionId", referencedColumnName = "prescriptionId")
	public PrescriptionEntity getPrescription() {
		return _prescription;
	}
	public void setPrescription(PrescriptionEntity prescription) {
		_prescription = prescription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medicineId", referencedColumnName = "medicineId")
	public MedicineEntity getMedicineEntity() {
		return _medicineEntity;
	}
	public void setMedicineEntity(MedicineEntity medicineEntity) {
		_medicineEntity = medicineEntity;
	}
}
