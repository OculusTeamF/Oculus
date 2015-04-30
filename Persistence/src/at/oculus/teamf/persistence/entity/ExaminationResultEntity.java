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
import java.util.Date;

/**
 * ExaminationResultEntity.java
 * Created by oculus on 30.04.15.
 */
@Entity
@Table(name = "examinationresult", schema = "", catalog = "oculus_f")
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

	// TODO toString, hashCode, equals
}
