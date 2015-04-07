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
@Table(name = "usergroup", schema = "", catalog = "oculus_f")
public class UsergroupEntity {
    private int userGroupId;
    private String userGroupName;
    private String description;
    private Collection<UserEntity> usersByUserGroupId;

    @Id
    @Column(name = "userGroupId", nullable = false, insertable = true, updatable = true)
    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Basic
    @Column(name = "userGroupName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsergroupEntity that = (UsergroupEntity) o;

        if (userGroupId != that.userGroupId) return false;
        if (userGroupName != null ? !userGroupName.equals(that.userGroupName) : that.userGroupName != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userGroupId;
        result = 31 * result + (userGroupName != null ? userGroupName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "usergroupByUserGroupId")
    public Collection<UserEntity> getUsersByUserGroupId() {
        return usersByUserGroupId;
    }

    public void setUsersByUserGroupId(Collection<UserEntity> usersByUserGroupId) {
        this.usersByUserGroupId = usersByUserGroupId;
    }
}
