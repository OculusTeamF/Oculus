/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.examination;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamE.domain.interfaces.IExaminationTb2;
import at.oculus.teamE.domain.interfaces.IUserTb2;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.domain.entity.user.User;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Blob;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * ExaminationResult.java Created by oculus on 30.04.15.
 */
public class ExaminationResult implements IExaminationResult, ILogger, IExaminationTb2 {
	private int _id;
	private IExaminationProtocol _examinationProtocol;
	private Integer _examinationProtocolId;
	private IUser _user;
	private Integer _userId;
    private IDoctor _doctor;
    private IOrthoptist _orthoptist;
	private String _result;
	private Date _createDate;
	private String _device;
	private byte[] _deviceData;

    public ExaminationResult(IUserTb2 iUserTb2, IExaminationProtocolTb2 iExaminationProtocolTb2){
        _user = (User) iUserTb2;
        _examinationProtocol = (ExaminationProtocol) iExaminationProtocolTb2;
        _createDate = new Timestamp(new Date().getTime());
    }

	public ExaminationResult(int id, IExaminationProtocol examinationProtocol,
	                         IUser user, String result, Date createDate, String device,
	                         byte[] deviceData) {
		_id = id;
		if(examinationProtocol!=null){
			_examinationProtocol = examinationProtocol;
			_examinationProtocolId = examinationProtocol.getId();
		}
		setUser(user);

		_result = result;
		_createDate = createDate;
		_device = device;
		_deviceData = deviceData;
	}

	@Override
	public int getId() {
		return _id;
	}
	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
    public IExaminationProtocol getExaminationProtocol() {
		return _examinationProtocol;
	}
	@Override
    public void setExaminationProtocol(IExaminationProtocol examinationProtocolEntity) {
		_examinationProtocol = examinationProtocolEntity;
	}

	@Override
    public IUser getUser() {
		return _user;
	}
	@Override
    public void setUser(IUser user) {
        if(user!=null){
            _user = user;
            _userId = user.getTeamFUserId();
            if(user instanceof IDoctor){
                _doctor = (IDoctor) user;
            } else if (user instanceof IOrthoptist) {
                _orthoptist = (IOrthoptist) user;
            }
        }
	}

	@Override
    public Integer getUserId() {
		return _userId;
	}
	@Override
    public void setUserId(Integer userId) {
		_userId = userId;
	}

    @Override
    public Integer getExaminationId() {
        return getId();
    }

    @Override
    public String getResult() {
		return _result;
	}

    @Override
    public void setCreationDate(LocalDateTime localDateTime) {

    }

    @Override
    public LocalDateTime getCreationDate() {
        Date ts = _createDate;
        Instant instant = Instant.ofEpochMilli(ts.getTime());
        LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return res;
    }

    @Override
    public void setMeasuringDevice(String s) {
        _device = s;
    }

    @Override
    public String getMeasuringDevice() {
        return _device;
    }

    @Override
    public IExaminationProtocolTb2 getProtocol() {
        return (IExaminationProtocolTb2) _examinationProtocol;
    }

    //not used
    @Override
    public Blob getMeasuringDeviceData() {
        return null;
    }

    @Override
    public void setCreator(IUserTb2 iUserTb2) {
        _doctor = (IDoctor) iUserTb2;
    }

    @Override
    public IUserTb2 getCreator() {
        if(_doctor != null){
            return (IUserTb2) _doctor;
        }else if(_orthoptist != null){
            return (IUserTb2) _orthoptist;
        }
        return null;
    }

    @Override
    public void setResult(String result) {
		_result = result;
	}

	@Override
    public Date getCreateDate() {
		return _createDate;
	}

	@Override
    public String getDevice() {
		return _device;
	}

	@Override
    public byte[] getDeviceData() {
		return _deviceData;
	}

	@Override
    public IDoctor getDoctor() {
		return _doctor;
	}

	@Override
    public IOrthoptist getOrthoptist() {
		return _orthoptist;
	}

    @Override
    public String toString(){
        return _result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationResult)) return false;

        ExaminationResult that = (ExaminationResult) o;

        if (_id != that._id) return false;
        if (_createDate != null ? !_createDate.equals(that._createDate) : that._createDate != null) return false;
        if (_device != null ? !_device.equals(that._device) : that._device != null) return false;
        if (!Arrays.equals(_deviceData, that._deviceData)) return false;
        if (_doctor != null ? !_doctor.equals(that._doctor) : that._doctor != null) return false;
        if (_examinationProtocol != null ? !_examinationProtocol.equals(that._examinationProtocol) : that._examinationProtocol != null)
            return false;
        if (_examinationProtocolId != null ? !_examinationProtocolId.equals(that._examinationProtocolId) : that._examinationProtocolId != null)
            return false;
        if (_orthoptist != null ? !_orthoptist.equals(that._orthoptist) : that._orthoptist != null) return false;
        if (_result != null ? !_result.equals(that._result) : that._result != null) return false;
        if (_user != null ? !_user.equals(that._user) : that._user != null) return false;
        if (_userId != null ? !_userId.equals(that._userId) : that._userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_examinationProtocol != null ? _examinationProtocol.hashCode() : 0);
        result = 31 * result + (_examinationProtocolId != null ? _examinationProtocolId.hashCode() : 0);
        result = 31 * result + (_user != null ? _user.hashCode() : 0);
        result = 31 * result + (_userId != null ? _userId.hashCode() : 0);
        result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
        result = 31 * result + (_orthoptist != null ? _orthoptist.hashCode() : 0);
        result = 31 * result + (_result != null ? _result.hashCode() : 0);
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_device != null ? _device.hashCode() : 0);
        result = 31 * result + (_deviceData != null ? Arrays.hashCode(_deviceData) : 0);
        return result;
    }
}