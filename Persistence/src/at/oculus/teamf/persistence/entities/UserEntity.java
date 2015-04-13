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
import java.sql.Timestamp;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "user", schema = "", catalog = "oculus_f")
public class UserEntity implements IEntity {
    private int _id;
    private Integer _userGroupId;
    private String _userName;
    private String _password;
    private String _title;
    private String _firstName;
    private String _lastName;
    private String _email;
    private Timestamp _createDate;
    private Timestamp _idleDate;
    private UsergroupEntity _usergroup;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "userId", nullable = false, insertable = false, updatable = false)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    @Basic
    @Column(name = "userGroupId", nullable = true, insertable = true, updatable = true)
    public Integer getUserGroupId() {
        return _userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        _userGroupId = userGroupId;
    }

    @Basic
    @Column(name = "userName", nullable = false, insertable = true, updatable = true, length = 30)
    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 255)
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 30)
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    @Basic
    @Column(name = "createDate", nullable = false, insertable = true, updatable = true)
    public Timestamp getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        _createDate = createDate;
    }

    @Basic
    @Column(name = "idleDate", nullable = true, insertable = true, updatable = true)
    public Timestamp getIdleDate() {
        return _idleDate;
    }

    public void setIdleDate(Timestamp idleDate) {
        _idleDate = idleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (_id != that._id) return false;
        if (_userGroupId != null ? !_userGroupId.equals(that._userGroupId) : that._userGroupId != null) return false;
        if (_userName != null ? !_userName.equals(that._userName) : that._userName != null) return false;
        if (_password != null ? !_password.equals(that._password) : that._password != null) return false;
        if (_title != null ? !_title.equals(that._title) : that._title != null) return false;
        if (_firstName != null ? !_firstName.equals(that._firstName) : that._firstName != null) return false;
        if (_lastName != null ? !_lastName.equals(that._lastName) : that._lastName != null) return false;
        if (_email != null ? !_email.equals(that._email) : that._email != null) return false;
        if (_createDate != null ? !_createDate.equals(that._createDate) : that._createDate != null) return false;
        if (_idleDate != null ? !_idleDate.equals(that._idleDate) : that._idleDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_userGroupId != null ? _userGroupId.hashCode() : 0);
        result = 31 * result + (_userName != null ? _userName.hashCode() : 0);
        result = 31 * result + (_password != null ? _password.hashCode() : 0);
        result = 31 * result + (_title != null ? _title.hashCode() : 0);
        result = 31 * result + (_firstName != null ? _firstName.hashCode() : 0);
        result = 31 * result + (_lastName != null ? _lastName.hashCode() : 0);
        result = 31 * result + (_email != null ? _email.hashCode() : 0);
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_idleDate != null ? _idleDate.hashCode() : 0);
        return result;
    }


    /*@ManyToOne
    @JoinColumn(name = "userGroupId", referencedColumnName = "userGroupId")
    public UsergroupEntity getUsergroup() {
        return _usergroup;
    }

    public void setUsergroup(UsergroupEntity usergroup) {
       _usergroup = usergroup;
    }*/
}
