/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "orthoptist", schema = "", catalog = "oculus_f")
public class OrthoptistEntity {
    private int _id;
    private Integer _userId;
    private int _calendarId;
    private UserEntity _user;
    private CalendarEntity _calendar;
    private Collection<QueueEntity> _queues;

    @Id
    @Column(name = "orthoptistId", nullable = false, insertable = true, updatable = true)
    public int get_id() {
        return _id;
    }

    public void set_id(int orthoptistId) {
        this._id = orthoptistId;
    }

    @Basic
    @Column(name = "userId", nullable = true, insertable = true, updatable = true)
    public Integer get_userId() {
        return _userId;
    }

    public void set_userId(Integer userId) {
        this._userId = userId;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int get_calendarId() {
        return _calendarId;
    }

    public void set_calendarId(int calendarId) {
        this._calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrthoptistEntity that = (OrthoptistEntity) o;

        if (_id != that._id) return false;
        if (_calendarId != that._calendarId) return false;
        if (_userId != null ? !_userId.equals(that._userId) : that._userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_userId != null ? _userId.hashCode() : 0);
        result = 31 * result + _calendarId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUser() {
        return _user;
    }

    public void setUser(UserEntity userByUserId) {
        this._user = userByUserId;
    }

    @OneToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendar() {
        return _calendar;
    }

    public void setCalendar(CalendarEntity calendar) {
        this._calendar = calendar;
    }

    @OneToMany(mappedBy = "queue")
    public Collection<QueueEntity> getQueues() {
        return _queues;
    }

    public void setQueues(Collection<QueueEntity> queues) {
        this._queues = queues;
    }
}