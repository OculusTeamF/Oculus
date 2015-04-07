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
    private int userId;
    private Integer userGroupId;
    private String userName;
    private String password;
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp createDate;
    private Timestamp idleDate;
    private Collection<DoctorEntity> doctorsByUserId;
    private Collection<OrthoptistEntity> orthoptistsByUserId;
    private Collection<ReceptionistEntity> receptionistsByUserId;
    private UsergroupEntity usergroupByUserGroupId;

    @Id
    @Column(name = "userId", nullable = false, insertable = true, updatable = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userGroupId", nullable = true, insertable = true, updatable = true)
    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Basic
    @Column(name = "userName", nullable = false, insertable = true, updatable = true, length = 30)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 30)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "createDate", nullable = false, insertable = true, updatable = true)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "idleDate", nullable = true, insertable = true, updatable = true)
    public Timestamp getIdleDate() {
        return idleDate;
    }

    public void setIdleDate(Timestamp idleDate) {
        this.idleDate = idleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (userGroupId != null ? !userGroupId.equals(that.userGroupId) : that.userGroupId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (idleDate != null ? !idleDate.equals(that.idleDate) : that.idleDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userGroupId != null ? userGroupId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (idleDate != null ? idleDate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<DoctorEntity> getDoctorsByUserId() {
        return doctorsByUserId;
    }

    public void setDoctorsByUserId(Collection<DoctorEntity> doctorsByUserId) {
        this.doctorsByUserId = doctorsByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<OrthoptistEntity> getOrthoptistsByUserId() {
        return orthoptistsByUserId;
    }

    public void setOrthoptistsByUserId(Collection<OrthoptistEntity> orthoptistsByUserId) {
        this.orthoptistsByUserId = orthoptistsByUserId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<ReceptionistEntity> getReceptionistsByUserId() {
        return receptionistsByUserId;
    }

    public void setReceptionistsByUserId(Collection<ReceptionistEntity> receptionistsByUserId) {
        this.receptionistsByUserId = receptionistsByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "userGroupId", referencedColumnName = "userGroupId")
    public UsergroupEntity getUsergroupByUserGroupId() {
        return usergroupByUserGroupId;
    }

    public void setUsergroupByUserGroupId(UsergroupEntity usergroupByUserGroupId) {
        this.usergroupByUserGroupId = usergroupByUserGroupId;
    }
}
