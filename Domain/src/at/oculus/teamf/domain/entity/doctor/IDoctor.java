/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.doctor;

import at.oculus.teamf.domain.entity.exception.CantLoadPatientsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.domain.entity.patient.IPatient;

import java.util.Collection;

public interface IDoctor extends IUser, IDomain {
    //<editor-fold desc="Getter/Setter">
    int getId();

    void setId(int id);

    ICalendar getCalendar();

    void setCalendar(ICalendar _calendar);

    IPatientQueue getQueue();

    void setQueue(IPatientQueue _queue);

    IDoctor getDoctorSubstitude();

    void setDoctorSubstitude(IDoctor doctorSubstitude);

    void addPatient(IPatient patient);

    Collection<IPatient> getPatients() throws CantLoadPatientsException;

    void setPatients(Collection<IPatient> patients);

    @Override
    String toString();
}
