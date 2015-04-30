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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

/**
 * ExaminationResultEntity.java
 * Created by oculus on 30.04.15.
 */
@Entity
@Table(name = "examinationresult", schema = "", catalog = "oculus_f")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getExaminationResultByPatient",
                query = "select er.* " +
                        "from examinationResult er, examinationProtocol ep " +
                        "where er.examinationProtocolId = ep.examinationProtocolId " +
                        "and ep.patientId = 1",
                resultClass = PatientEntity.class)})
public class ExaminationResultEntity implements IEntity {
    private int _id;
    private ExaminationProtocolEntity _examinationProtocolEntity;
    private Integer _examinationProtocolId;
    private UserEntity _userEntity;
    private Integer _userId;
    private String _result;
    private Timestamp _createDate;
    private String _device;
    private byte[] _deviceData;

	public ExaminationResultEntity() {
	}
	public ExaminationResultEntity(int id, ExaminationProtocolEntity examinationProtocolEntity,
	                               UserEntity userEntity, String result,
	                               Timestamp createDate, String device, byte[] deviceData) {
		_id = id;
		if(examinationProtocolEntity!=null) {
			_examinationProtocolEntity = examinationProtocolEntity;
			_examinationProtocolId = examinationProtocolEntity.getId();
		}
		if(userEntity!=null) {
			_userEntity = userEntity;
			_userId = userEntity.getId();
		}
		_result = result;
		_createDate = createDate;
		_device = device;
		_deviceData = deviceData;
	}

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "examinationResultId", nullable = false, insertable = false, updatable = false)
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }

	@Basic
	@Column(name = "examinationProtocolId", nullable = true, insertable = false, updatable = false)
	public Integer getExaminationProtocolId() {
		return _examinationProtocolId;
	}
	public void setExaminationProtocolId(Integer examinationProtocolId) {
		_examinationProtocolId = examinationProtocolId;
	}

	@Basic
	@Column(name = "userId", nullable = true, insertable = false, updatable = false)
	public Integer getUserId() {
		return _userId;
	}
	public void setUserId(Integer userId) {
		_userId = userId;
	}

	@Basic
	@Column(name = "result", nullable = true, insertable = true, updatable = true)
	public String getResult() {
		return _result;
	}
	public void setResult(String result) {
		_result = result;
	}

	@Basic
	@Column(name = "createDate", nullable = true, insertable = true, updatable = true)
	public Timestamp getCreateDate() {
		return _createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		_createDate = createDate;
	}

	@Basic
	@Column(name = "device", nullable = true, insertable = true, updatable = true)
	public String getDevice() {
		return _device;
	}
	public void setDevice(String device) {
		_device = device;
	}

	@Basic
	@Column(name = "deviceData", nullable = true, insertable = true, updatable = true)
	public byte[] getDeviceData() {
		return _deviceData;
	}
	public void setDeviceData(byte[] deviceData) {
		_deviceData = deviceData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examinationProtocolId", referencedColumnName = "examinationProtocolId")
	public ExaminationProtocolEntity getExaminationProtocolEntity() {
		return _examinationProtocolEntity;
	}
	public void setExaminationProtocolEntity(ExaminationProtocolEntity examinationProtocolEntity) {
		_examinationProtocolEntity = examinationProtocolEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	public UserEntity getUserEntity() {
		return _userEntity;
	}
	public void setUserEntity(UserEntity userEntity) {
		_userEntity = userEntity;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationResultEntity)) return false;

        ExaminationResultEntity that = (ExaminationResultEntity) o;

        if (_id != that._id) return false;
        if (_createDate != null ? !_createDate.equals(that._createDate) : that._createDate != null) return false;
        if (_device != null ? !_device.equals(that._device) : that._device != null) return false;
        if (!Arrays.equals(_deviceData, that._deviceData)) return false;
        if (_examinationProtocolEntity != null ? !_examinationProtocolEntity.equals(that._examinationProtocolEntity) : that._examinationProtocolEntity != null)
            return false;
        if (_examinationProtocolId != null ? !_examinationProtocolId.equals(that._examinationProtocolId) : that._examinationProtocolId != null)
            return false;
        if (_result != null ? !_result.equals(that._result) : that._result != null) return false;
        if (_userEntity != null ? !_userEntity.equals(that._userEntity) : that._userEntity != null) return false;
        if (_userId != null ? !_userId.equals(that._userId) : that._userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_examinationProtocolEntity != null ? _examinationProtocolEntity.hashCode() : 0);
        result = 31 * result + (_examinationProtocolId != null ? _examinationProtocolId.hashCode() : 0);
        result = 31 * result + (_userEntity != null ? _userEntity.hashCode() : 0);
        result = 31 * result + (_userId != null ? _userId.hashCode() : 0);
        result = 31 * result + (_result != null ? _result.hashCode() : 0);
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_device != null ? _device.hashCode() : 0);
        result = 31 * result + (_deviceData != null ? Arrays.hashCode(_deviceData) : 0);
        return result;
    }
}
