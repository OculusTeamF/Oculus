/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

import java.util.Date;

/**
 * Created by Simon Angerer on 04.06.2015.
 */
class OrthoptistProxy extends VirtualProxy<IOrthoptist> implements IOrthoptist, IUser {
    protected OrthoptistProxy(IOrthoptist real) {
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
    public void setCalendar(ICalendar calendar) {
        _real.setCalendar(calendar);
    }

    @Override
    public IPatientQueue getQueue() throws NoBrokerMappedException, BadConnectionException {
        return _real.getQueue();
    }

    @Override
    public void setQueue(IPatientQueue queue) {
        _real.setQueue(queue);
    }

    @Override
    public int getTeamFUserId() {
        return _real.getId();
    }

    @Override
    public void setUserId(int id) {
        _real.setId(id);
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
}
