/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.user;

import at.oculus.teamE.domain.interfaces.IUserTb2;
import at.oculus.teamf.domain.entity.IDomain;

import java.util.Date;

/**
 *
 * @author Simon Angerer
 */
public abstract class User implements IUser, IDomain, IUserTb2 {

    //<editor-fold desc="Attributes">
    protected int _id;
	private Integer _userGroupId;
	private String _userName;
	private String _password;
	private String _title;
	private String _firstName;
	private String _lastName;
	private String _email;
	private Date _createDate;
	private Date _idleDate;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    @Override
    public int getTeamFUserId() {
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
    public Date getCreateDate() {
		return _createDate;
	}
	@Override
    public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
    public Date getTeamFIdleDate() {
		return _idleDate;
	}
	@Override
    public void setIdleDate(Date idleDate) {
		_idleDate = idleDate;
	}

	//</editor-fold>

    @Override
    public String toString() {
        String title = getTitle();
        if (title == null) {
            title = "";
        } else {
            title = title + " ";
        }
        return title + getFirstName() + " " + getLastName();
    }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;

		User user = (User) o;

		if (_id != user._id)
			return false;
		if (_createDate != null ? !_createDate.equals(user._createDate) : user._createDate != null)
			return false;
		if (_email != null ? !_email.equals(user._email) : user._email != null)
			return false;
		if (_firstName != null ? !_firstName.equals(user._firstName) : user._firstName != null)
			return false;
		if (_idleDate != null ? !_idleDate.equals(user._idleDate) : user._idleDate != null)
			return false;
		if (_lastName != null ? !_lastName.equals(user._lastName) : user._lastName != null)
			return false;
		if (_password != null ? !_password.equals(user._password) : user._password != null)
			return false;
		if (_title != null ? !_title.equals(user._title) : user._title != null)
			return false;
		if (_userGroupId != null ? !_userGroupId.equals(user._userGroupId) : user._userGroupId != null)
			return false;
		if (_userName != null ? !_userName.equals(user._userName) : user._userName != null)
			return false;
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
}
