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
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public abstract class User implements IUser {

    //<editor-fold desc="Attributes">
    protected int _id;
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
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    @Override
    public int getUserId() {
        return _id;
    }
    @Override
    public void setUserId(int id) {
        _id = id;
    }

	@Override
    public Integer getUserGroupId() {
		return _userGroupId;
	}
	@Override
    public void setUserGroupId(Integer userGroupId) {
		_userGroupId = userGroupId;
	}

	@Override
    public String getUserName() {
		return _userName;
	}
	@Override
    public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
    public String getPassword() {
		return _password;
	}
	@Override
    public void setPassword(String password) {
		_password = password;
	}

	@Override
    public String getTitle() {
		return _title;
	}
	@Override
    public void setTitle(String title) {
		_title = title;
	}

	@Override
    public String getFirstName() {
		return _firstName;
	}
	@Override
    public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	@Override
    public String getLastName() {
		return _lastName;
	}
	@Override
    public void setLastName(String lastName) {
		_lastName = lastName;
	}

	@Override
    public String getEmail() {
		return _email;
	}
	@Override
    public void setEmail(String email) {
		_email = email;
	}

	@Override
    public Timestamp getCreateDate() {
		return _createDate;
	}
	@Override
    public void setCreateDate(Timestamp createDate) {
		_createDate = createDate;
	}

	@Override
    public Timestamp getIdleDate() {
		return _idleDate;
	}
	@Override
    public void setIdleDate(Timestamp idleDate) {
		_idleDate = idleDate;
	}

	@Override
    public UsergroupEntity getUserGroup() {
		return _usergroup;
	}
	@Override
    public void setUserGroup(UsergroupEntity usergroup) {
		_usergroup = usergroup;
	}
	//</editor-fold>

}
