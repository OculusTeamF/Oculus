/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.entities.UsergroupEntity;

import java.sql.Timestamp;

/**
 * Created by oculus on 16.04.15.
 */
public interface IUser {
    //<editor-fold desc="Getter/Setter">
    int getUserId();

    void setUserId(int id);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    String getTitle();

    void setTitle(String title);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    Timestamp getCreateDate();

    void setCreateDate(Timestamp createDate);

    Timestamp getIdleDate();

    void setIdleDate(Timestamp idleDate);

    UsergroupEntity getUserGroup();

    void setUserGroup(UsergroupEntity usergroup);
}
