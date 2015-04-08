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

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "receptionist", schema = "", catalog = "oculus_f")
public class ReceptionistEntity {
    private int _receptionistId;
    private Integer _userId;
    private UserEntity _user;

    @Id
    @Column(name = "receptionistId", nullable = false, insertable = true, updatable = true)
    public int getReceptionistId() {
        return _receptionistId;
    }

    public void setReceptionistId(int receptionistId) {
        receptionistId = receptionistId;
    }

    @Basic
    @Column(name = "userId", nullable = true, insertable = true, updatable = true)
    public Integer getUserId() {
        return _userId;
    }

    public void setUserId(Integer userId) {
        userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceptionistEntity that = (ReceptionistEntity) o;

        if (_receptionistId != that._receptionistId) return false;
        if (_userId != null ? !_userId.equals(that._userId) : that._userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _receptionistId;
        result = 31 * result + (_userId != null ? _userId.hashCode() : 0);
        return result;
    }

    /*@ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUser() {
        return _user;
    }

    public void setUser(UserEntity userByUserId) {
        userByUserId = userByUserId;
    }*/
}
