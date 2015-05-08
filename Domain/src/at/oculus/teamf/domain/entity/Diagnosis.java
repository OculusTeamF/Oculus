/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;

/**
 * Diagnosis.java
 * Created by oculus on 16.04.15.
 */
public class Diagnosis implements IDiagnosis, IDomain, ILogger {
    private int _id;
    private String _title;
    private String _description;
	private Integer _doctorId;
	private Doctor _doctor;
	private Collection<Medicine> _medicine;

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

	public Collection<Medicine> getMedicine() throws CouldNotGetMedicineException {
		try {
			Facade.getInstance().reloadCollection(this, Medicine.class);
		} catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
			log.error(e.getMessage());
			throw new CouldNotGetMedicineException();
		}
		return _medicine;
	}

	public void setMedicine(Collection<Medicine> medicine) {
		_medicine = medicine;
	}

	public void addMedicine(Medicine medicine)
			throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException {
		medicine.setDiagnosis(this);
		_medicine.add(medicine);
		Facade.getInstance().save(medicine);
		//Todo: implement
	}

	@Override
    public String toString(){
        return _title + " " + _description.substring(0,50) + "...";
    }
}
