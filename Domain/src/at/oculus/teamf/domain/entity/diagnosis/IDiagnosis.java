/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.diagnosis;

import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CouldNotAddMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddVisualAidException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;

import java.util.Collection;

/**
 * Created by oculus on 20.04.15.
 */
public interface IDiagnosis extends IDomain {
    @Override
    int getId();

    @Override
    void setId(int id);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Integer getDoctorId();

    void setDoctorId(Integer doctorId);

    IDoctor getDoctor();

    void setDoctor(IDoctor doctor);

    Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException;

    void addMedicine(IMedicine medicine)
            throws CouldNotAddMedicineException, CouldNotGetMedicineException;

    void addVisualAid(IVisualAid visualAid) throws CouldNotGetVisualAidException, CouldNotAddVisualAidException;

    Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException;

    void setMedicine(Collection<Medicine> medicines);

    void setVisualAid(Collection<IVisualAid> iVisualAids);
}
