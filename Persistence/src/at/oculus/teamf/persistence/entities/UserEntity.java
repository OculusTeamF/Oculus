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
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "user", schema = "", catalog = "oculus_f")
public class UserEntity {
    private int _userId;
    private Integer _userGroupId;
    private String _userName;
    private String _password;
    private String _title;
    private String _firstName;
    private String _lastName;
    private String _email;
    private Timestamp _createDate;
    private Timestamp _idleDate;
    private Collection<DoctorEntity> _doctorsByUserId;
    private Collection<OrthoptistEntity> _orthoptistsByUserId;
    private Collection<ReceptionistEntity> _receptionistsByUserId;
    private UsergroupEntity _usergroupByUserGroupId;

    @Id
    @Column(name = "userId", nullable = false, insertable = true, updatable = true)
    public int get_userId() {
        return _userId;
    }

    public void set_userId(int userId) {
        _userId = userId;
    }

    @Basic
    @Column(name = "userGroupId", nullable = true, insertable = true, updatable = true)
    public Integer get_userGroupId() {
        return _userGroupId;
    }

    public void set_userGroupId(Integer userGroupId) {
        _userGroupId = userGroupId;
    }

    @Basic
    @Column(name = "userName", nullable = false, insertable = true, updatable = true, length = 30)
    public String get_userName() {
        return _userName;
    }

    public void set_userName(String userName) {
        _userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 255)
    public String get_password() {
        return _password;
    }

    public void set_password(String password) {
        _password = password;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 30)
    public String get_title() {
        return _title;
    }

    public void set_title(String title) {
        _title = title;
    }

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 50)
    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String firstName) {
        _firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 50)
    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String lastName) {
        _lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    public String get_email() {
        return _email;
    }

    public void set_email(String email) {
        _email = email;
    }

    @Basic
    @Column(name = "createDate", nullable = false, insertable = true, updatable = true)
    public Timestamp get_createDate() {
        return _createDate;
    }

    public void set_createDate(Timestamp createDate) {
        _createDate = createDate;
    }

    @Basic
    @Column(name = "idleDate", nullable = true, insertable = true, updatable = true)
    public Timestamp get_idleDate() {
        return _idleDate;
    }

    public void set_idleDate(Timestamp idleDate) {
        _idleDate = idleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (_userId != that._userId) return false;
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
        int result = _userId;
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

    @OneToMany(mappedBy = "userByUserId")
    public Collection<DoctorEntity> get_doctorsByUserId() {
        return _doctorsByUserId;
    }

    public void set_doctorsByUserId(Collection<DoctorEntity> doctorsByUserId) {
        _doctorsByUserId = doctorsByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<OrthoptistEntity> get_orthoptistsByUserId() {
        return _orthoptistsByUserId;
    }

    public void set_orthoptistsByUserId(Collection<OrthoptistEntity> orthoptistsByUserId) {
        _orthoptistsByUserId = orthoptistsByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<ReceptionistEntity> get_receptionistsByUserId() {
        return _receptionistsByUserId;
    }

    public void set_receptionistsByUserId(Collection<ReceptionistEntity> receptionistsByUserId) {
        _receptionistsByUserId = receptionistsByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "userGroupId", referencedColumnName = "userGroupId")
    public UsergroupEntity get_usergroupByUserGroupId() {
        return _usergroupByUserGroupId;
    }

    public void set_usergroupByUserGroupId(UsergroupEntity usergroupByUserGroupId) {
        this._usergroupByUserGroupId = usergroupByUserGroupId;
    }
}
