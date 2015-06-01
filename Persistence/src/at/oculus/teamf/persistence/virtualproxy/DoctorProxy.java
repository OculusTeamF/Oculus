/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamE.domain.interfaces.IUserTb2;
import at.oculus.teamf.domain.entity.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CantLoadPatientsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Simon Angerer on 01.06.2015.
 */
public class DoctorProxy extends VirtualProxy<IDoctor> implements IDoctor, ILogger, IUser, IUserTb2 {

    protected DoctorProxy(IDoctor real) {
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
    public ICalendar getCalendar() {
        return _real.getCalendar();
    }

    @Override
    public void setCalendar(ICalendar _calendar) {
        _real.setCalendar(_calendar);
    }

    @Override
    public IPatientQueue getQueue() {
        return _real.getQueue();
    }

    @Override
    public void setQueue(IPatientQueue _queue) {
        _real.setQueue(_queue);
    }

    @Override
    public IDoctor getDoctorSubstitude() {
        return _real.getDoctorSubstitude();
    }

    @Override
    public void setDoctorSubstitude(IDoctor doctorSubstitude) {
        _real.setDoctorSubstitude(doctorSubstitude);
    }

    @Override
    public void addPatient(IPatient patient) {
        _real.addPatient(patient);
        try {
            Facade.getInstance().save(_real);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Collection<IPatient> getPatients() throws CantLoadPatientsException {
        if(_real.getPatients() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, IPatient.class);
            } catch (BadConnectionException | NoBrokerMappedException | ReloadInterfaceNotImplementedException | DatabaseOperationException | InvalidReloadClassException e) {
                log.error(e.getMessage());
                throw new CantLoadPatientsException();
            }
        }
        return _real.getPatients();
    }

    @Override
    public void setPatients(Collection<IPatient> patients) {
        _real.setPatients(patients);
    }

    @Override
    public int getTeamFUserId() {
        return _real.getTeamFUserId();
    }

    @Override
    public void setUserId(int id) {
        _real.setUserId(id);
    }

    @Override
    public Integer getUserGroupId() {
        return _real.getUserGroupId();
    }

    @Override
    public void setUserGroupId(Integer userGroupId) {
        _real.setUserGroupId(userGroupId);
    }

    @Override
    public Integer getUserId() {
        return _real.getId();
    }

    @Override
    public String getUserName() {
        return _real.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        _real.setUserName(userName);
    }

    @Override
    public String getPassword() {
        return _real.getPassword();
    }

    @Override
    public LocalDateTime getCreationDate() {
        return ((IUserTb2)_real).getCreationDate();
    }

    @Override
    public LocalDateTime getIdleDate() {
        return ((IUserTb2)_real).getIdleDate();
    }

    @Override
    public void setPassword(String password) {
        _real.setPassword(password);
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
    public String getFirstName() {
        return _real.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        _real.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return _real.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        _real.setLastName(lastName);
    }

    @Override
    public String getEmail() {
        return _real.getEmail();
    }

    @Override
    public void setEmail(String email) {
        _real.setEmail(email);
    }

    @Override
    public Date getCreateDate() {
        return _real.getCreateDate();
    }

    @Override
    public void setCreateDate(Date createDate) {
        _real.setCreateDate(createDate);
    }

    @Override
    public Date getTeamFIdleDate() {
        return _real.getTeamFIdleDate();
    }

    @Override
    public void setIdleDate(Date idleDate) {
        _real.setIdleDate(idleDate);
    }

    @Override
    public String toString() {
        return _real.toString();
    }

    @Override
    public List<? extends IExaminationProtocolTb2> getExaminationProtocols() {
        return ((IUserTb2)_real).getExaminationProtocols();
    }
}
