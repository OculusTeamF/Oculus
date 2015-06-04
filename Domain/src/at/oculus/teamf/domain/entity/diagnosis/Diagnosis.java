/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.diagnosis;

import at.oculus.teamE.domain.interfaces.IDiagnosisTb2;
import at.oculus.teamE.domain.interfaces.IMedicineTb2;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.domain.entity.visualadi.VisualAid;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CouldNotAddMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddVisualAidException;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;

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
    private Collection<IMedicine> _medicine;
    private Collection<IVisualAid> _visualAid;

    public Diagnosis() {
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
        return (List<? extends IMedicineTb2>) (Collection<?>) _medicine;
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
        _doctor = doctor;
        _doctorId = doctor.getId();
    }

    public Collection<IMedicine> getMedicine() {
        return (Collection<IMedicine>) (Collection<?>) _medicine;
    }


    /**
     * add medicine to diagnosis
     *
     * @param medicine medicine to add
     * @throws DatabaseOperationException
     * @throws NoBrokerMappedException
     * @throws BadConnectionException
     */
    public void addMedicine(IMedicine medicine) throws CouldNotAddMedicineException {
        medicine.setDiagnosis(this);
        _medicine.add(medicine);
    }

    public void addVisualAid(IVisualAid visualAid) throws CouldNotAddVisualAidException {
        if (_visualAid == null) {
            getVisualAid();
        }
        visualAid.setDiagnosis(this);
        _visualAid.add((VisualAid) visualAid);
    }

    public Collection<IVisualAid> getVisualAid() {
        return _visualAid;
    }

    public void setVisualAid(Collection<IVisualAid> visualAid) {
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
    public String toString() {
        if (_description.length() > 50) {
            return _title + ": " + _description.substring(0, 30) + "...";
        } else {
            return _title + ": " + _description;
        }


    }

    @Override
    public void addMedicine(IMedicineTb2 iMedicineTb2) {
        try {
            addMedicine((IMedicine) iMedicineTb2);
        } catch (CouldNotAddMedicineException e) {
            log.error("Could not add medicine exception caught! " + e.getMessage());

        }
    }

    public void setMedicine(Collection<Medicine> medicines) {
        _medicine = (Collection<IMedicine>) (Collection<?>) medicines;
    }
}
