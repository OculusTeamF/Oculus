/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamE.domain.interfaces.IDiagnosisTb2;
import at.oculus.teamE.domain.interfaces.IMedicineTb2;
import at.oculus.teamf.domain.entity.exception.CouldNotAddMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddVisualAidException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Diagnosis.java
 * Created by oculus on 16.04.15.
 */
public class Diagnosis implements IDiagnosis, IDomain, ILogger, IDiagnosisTb2 {
    private int _id;
    private String _title;
    private String _description;
	private Integer _doctorId;
	private IDoctor _doctor;
	private Collection<Medicine> _medicine;
	private Collection<VisualAid> _visualAid;

	public Diagnosis() {}

	public Diagnosis(String title, String description, IDoctor doctor) {
		_id = 0;
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

    @Override
    public Integer getDiagnosisId() {
        return getId();
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

    @Override
    public List<? extends IMedicineTb2> getMedicines() {
        return (List<? extends IMedicineTb2>) _medicine;
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

    public IDoctor getDoctor() {
		return _doctor;
	}
    public void setDoctor(IDoctor doctor) {
		_doctor = (Doctor) doctor;
		if(doctor!=null) {
			_doctorId = doctor.getId();
		}
	}

    public Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException {
        try {
			Facade.getInstance().reloadCollection(this, Medicine.class);
		} catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
			log.error(e.getMessage());
			throw new CouldNotGetMedicineException();
		}
        return (Collection<IMedicine>) (Collection<?>) _medicine;
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
    public void addMedicine(IMedicine medicine) throws CouldNotAddMedicineException {
        try {
            if (_medicine == null) {
                getMedicine();
            }
            medicine.setDiagnosis(this);
            _medicine.add((Medicine) medicine);
            Facade.getInstance().save(medicine);
        } catch (DatabaseOperationException | CouldNotGetMedicineException | NoBrokerMappedException | BadConnectionException e) {
            log.error(e.getMessage());
			throw new CouldNotAddMedicineException();
		}
	}

    public void addVisualAid(IVisualAid visualAid)
            throws CouldNotAddVisualAidException {
        try {
            if (_visualAid == null) {
                getVisualAid();
            }
            visualAid.setDiagnosis(this);
            _visualAid.add((VisualAid) visualAid);
            Facade.getInstance().save(visualAid);
        } catch (NoBrokerMappedException | CouldNotGetVisualAidException | BadConnectionException | DatabaseOperationException e) {
            log.error(e.getMessage());
            throw new CouldNotAddVisualAidException();
        }
    }

    public Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException {
        try {
			Facade.getInstance().reloadCollection(this, VisualAid.class);
		} catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
			log.error(e.getMessage());
			throw new CouldNotGetVisualAidException();
		}
        return (Collection<IVisualAid>) (Collection<?>) _visualAid;
    }

    public void setVisualAid(Collection<IVisualAid> visualAid) {
        _visualAid = (Collection<VisualAid>) (Collection<?>) visualAid;
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
		if(_description.length() > 50) {
            return _title + ": " + _description.substring(0,30) + "...";
		} else {
            return _title + ": " + _description;
		}


    }

    @Override
    public void addMedicine(IMedicineTb2 iMedicineTb2) {
        try {
            addMedicine((IMedicine)iMedicineTb2);
        } catch (CouldNotAddMedicineException e) {
            log.error("Could not add medicine exception caught! " + e.getMessage());

        }
    }
}
