/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entity;

import javax.persistence.*;

/**
 * Hibernate annotated user group class
 */
@Entity
@Table(name = "usergroup", schema = "", catalog = "oculus_f")
public class UsergroupEntity implements IEntity {
    private int _id;
    private String _userGroupName;
    private String _description;
    //private Collection<UserEntity> _users;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "userGroupId", nullable = false, insertable = false, updatable = false)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    @Basic
    @Column(name = "userGroupName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getUserGroupName() {
        return _userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this._userGroupName = userGroupName;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsergroupEntity that = (UsergroupEntity) o;

        if (_id != that._id) return false;
        if (_userGroupName != null ? !_userGroupName.equals(that._userGroupName) : that._userGroupName != null)
            return false;
        if (_description != null ? !_description.equals(that._description) : that._description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_userGroupName != null ? _userGroupName.hashCode() : 0);
        result = 31 * result + (_description != null ? _description.hashCode() : 0);
        return result;
    }

    /*
    @OneToMany(mappedBy = "_usergroupByUserGroupId")
    public Collection<UserEntity> get_users() {
        return _users;
    }

    public void set_users(Collection<UserEntity> usersByUserGroupId) {
        _users = usersByUserGroupId;
    }*/
}
