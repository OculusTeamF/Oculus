/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamE.domain.interfaces.IDiagnosisTb2;
import at.oculus.teamE.domain.interfaces.IMedicineTb2;
import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.visualadi.VisualAid;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CouldNotAddMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddVisualAidException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Simon Angerer on 01.06.2015.
 */
class DiagnosisProxy extends VirtualProxy<IDiagnosis> implements IDiagnosis, IDomain, ILogger, IDiagnosisTb2 {
    protected DiagnosisProxy(IDiagnosis real) {
        super(real);
    }

    @Override
    public int getId() {
        return _real.getId();
    }

    @Override
    public void setId(int id) {
        _real.setId(id);
    }

    @Override
    public Integer getDiagnosisId() {
        return _real.getId();
    }

    @Override
    public String getTitle() {
        return _real.getTitle();
    }

    @Override
    public void setTitle(String title) {
        _real.setTitle(title);
    }

    @Override
    public String getDescription() {
        return _real.getDescription();
    }

    @Override
    public List<? extends IMedicineTb2> getMedicines() {
        try {
            if (_real.getMedicine() == null) {
                try {
                    Facade.getInstance().reloadCollection(_real, Medicine.class);
                } catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
                    log.error(e.getMessage());
                }
            }
        } catch (CouldNotGetMedicineException e) {
            log.error(e.getMessage());
        }
        try {
            return new LinkedList<>((Collection) _real.getMedicine());
        } catch (CouldNotGetMedicineException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void setDescription(String description) {
        _real.setDescription(description);
    }

    @Override
    public Integer getDoctorId() {
        return _real.getDoctorId();
    }

    @Override
    public void setDoctorId(Integer doctorId) {
        _real.setDoctorId(doctorId);
    }

    @Override
    public IDoctor getDoctor() {
        return _real.getDoctor();
    }

    @Override
    public void setDoctor(IDoctor doctor) {
        _real.setDoctor(doctor);
    }

    @Override
    public Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException {
        if (_real.getMedicine() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, Medicine.class);
            } catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
                log.error(e.getMessage());
                throw new CouldNotGetMedicineException();
            }
        }
        return _real.getMedicine();
    }

    @Override
    public void addMedicine(IMedicine medicine) throws CouldNotAddMedicineException, CouldNotGetMedicineException {
        if (_real.getMedicine() == null) {
            getMedicine();
        }
        _real.addMedicine(medicine);
        try {
            Facade.getInstance().save(medicine);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error(e.getMessage());
            throw new CouldNotAddMedicineException();
        }
    }

    @Override
    public void addVisualAid(IVisualAid visualAid) throws CouldNotGetVisualAidException, CouldNotAddVisualAidException {
        if (_real.getVisualAid() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, VisualAid.class);
            } catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
                log.error(e.getMessage());
                throw new CouldNotAddVisualAidException();
            }
        }
        _real.addVisualAid(visualAid);
        try {
            Facade.getInstance().save(visualAid);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error(e.getMessage());
            throw new CouldNotAddVisualAidException();
        }
    }

    @Override
    public Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException {
        if (_real.getVisualAid() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, VisualAid.class);
            } catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | DatabaseOperationException | ReloadInterfaceNotImplementedException e) {
                log.error(e.getMessage());
                throw new CouldNotGetVisualAidException();
            }
        }

        return _real.getVisualAid();
    }

    @Override
    public void setMedicine(Collection<Medicine> medicines) {
        _real.setMedicine(medicines);
    }

    @Override
    public void setVisualAid(Collection<IVisualAid> iVisualAids) {
        _real.setVisualAid(iVisualAids);
    }

    @Override
    public void addMedicine(IMedicineTb2 iMedicineTb2) {
        try {
            _real.addMedicine((IMedicine) iMedicineTb2);
        } catch (CouldNotAddMedicineException | CouldNotGetMedicineException e) {
            log.error(e.getMessage());
        }
    }
}
