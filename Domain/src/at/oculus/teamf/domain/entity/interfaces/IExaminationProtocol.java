/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.interfaces;

import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.Patient;

import java.util.Date;

/**
 * Created by oculus on 20.04.15.
 */
public interface IExaminationProtocol extends IDomain {
    @Override
    int getId();

    @Override
    void setId(int id);

    Date getStartTime();

    void setStartTime(Date startTime);

    Date getEndTime();

    void setEndTime(Date endTime);

    String getDescription();

    void setDescription(String description);

    Doctor getDoctor();

    void setDoctor(Doctor doctor);

    Orthoptist getOrthoptist();

    void setOrthoptist(Orthoptist orthoptist);

    Diagnosis getDiagnosis();

    void setDiagnosis(Diagnosis diagnosis);

    Patient getPatient();

    void setPatient(Patient patient);
}
