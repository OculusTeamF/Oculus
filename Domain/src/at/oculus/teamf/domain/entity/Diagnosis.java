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
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
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
	private Collection<VisualAid> _visualAid;

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

	/**
	 * add medicine to diagnosis
	 *
	 * @param medicine
	 * 		medicine to add
	 *
	 * @throws DatabaseOperationException
	 * @throws NoBrokerMappedException
	 * @throws BadConnectionException
	 */
	public void addMedicine(Medicine medicine)
			throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException,
			       CouldNotGetMedicineException {
		if (_medicine == null) {
			getMedicine();
		}
		medicine.setDiagnosis(this);
		_medicine.add(medicine);
		Facade.getInstance().save(medicine);
	}

	public void addVisualAid(VisualAid visualAid)
			throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException,
			       CouldNotGetVisualAidException {
		if (_visualAid == null) {
			getVisualAid();
		}
		visualAid.setDiagnosis(this);
		_visualAid.add(visualAid);
		Facade.getInstance().save(visualAid);
	}

	public Collection<VisualAid> getVisualAid() throws CouldNotGetVisualAidException {
		try {
			Facade.getInstance().reloadCollection(this, VisualAid.class);
		} catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
			log.error(e.getMessage());
			throw new CouldNotGetVisualAidException();
		}
		return _visualAid;
	}

	public void setVisualAid(Collection<VisualAid> visualAid) {
		_visualAid = visualAid;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_title != null ? _title.hashCode() : 0);
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_doctorId != null ? _doctorId.hashCode() : 0);
		result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
		result = 31 * result + (_medicine != null ? _medicine.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Diagnosis))
			return false;

		Diagnosis diagnosis = (Diagnosis) o;

		if (_id != diagnosis._id)
			return false;
		if (_description != null ? !_description.equals(diagnosis._description) : diagnosis._description != null)
			return false;
		if (_doctor != null ? !_doctor.equals(diagnosis._doctor) : diagnosis._doctor != null)
			return false;
		if (_doctorId != null ? !_doctorId.equals(diagnosis._doctorId) : diagnosis._doctorId != null)
			return false;
		if (_medicine != null ? !_medicine.equals(diagnosis._medicine) : diagnosis._medicine != null)
			return false;
		if (_title != null ? !_title.equals(diagnosis._title) : diagnosis._title != null)
			return false;

		return true;
	}

	@Override
    public String toString(){
        return  _title + " " + _description.substring(0,50) + "...";
    }
}
