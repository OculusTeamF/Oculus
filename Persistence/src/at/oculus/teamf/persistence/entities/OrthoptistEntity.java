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
    private int orthoptistId;
    private Integer userId;
    private int calendarId;
    private UserEntity userByUserId;
    private CalendarEntity calendarByCalendarId;
    private Collection<QueueEntity> queuesByOrthoptistId;

    @Id
    @Column(name = "orthoptistId", nullable = false, insertable = true, updatable = true)
    public int getOrthoptistId() {
        return orthoptistId;
    }

    public void setOrthoptistId(int orthoptistId) {
        this.orthoptistId = orthoptistId;
    }

    @Basic
    @Column(name = "userId", nullable = true, insertable = true, updatable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrthoptistEntity that = (OrthoptistEntity) o;

        if (orthoptistId != that.orthoptistId) return false;
        if (calendarId != that.calendarId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orthoptistId;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + calendarId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendarByCalendarId() {
        return calendarByCalendarId;
    }

    public void setCalendarByCalendarId(CalendarEntity calendarByCalendarId) {
        this.calendarByCalendarId = calendarByCalendarId;
    }

    @OneToMany(mappedBy = "orthoptistByOrthoptistId")
    public Collection<QueueEntity> getQueuesByOrthoptistId() {
        return queuesByOrthoptistId;
    }

    public void setQueuesByOrthoptistId(Collection<QueueEntity> queuesByOrthoptistId) {
        this.queuesByOrthoptistId = queuesByOrthoptistId;
    }
}
