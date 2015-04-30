/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDomain;

import java.util.Date;

/**
 * ExaminationResult.java Created by oculus on 30.04.15.
 */
public class ExaminationResult implements IDomain {
	private int _id;
	private ExaminationProtocol _examinationProtocol;
	private Integer _examinationProtocolId;
	private User _user;
	private Integer _userId;
    private Doctor _doctor;
    private Orthoptist _orthoptist;
	private String _result;
	private Date _createDate;
	private String _device;
	private byte[] _deviceData;

	public ExaminationResult() {
	}

	public ExaminationResult(int id, ExaminationProtocol examinationProtocol,
	                         User user, String result, Date createDate, String device,
	                         byte[] deviceData) {
		_id = id;
		if(examinationProtocol!=null){
			_examinationProtocol = examinationProtocol;
			_examinationProtocolId = examinationProtocol.getId();
		}
		if(user!=null){
			_user = user;
			_userId = user.getUserId();
            if(user instanceof Doctor){
                _doctor = (Doctor) user;
            } else if (user instanceof Orthoptist) {
                _orthoptist = (Orthoptist) user;
            }
		}

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

	public ExaminationProtocol getExaminationProtocol() {
		return _examinationProtocol;
	}
	public void setExaminationProtocol(ExaminationProtocol examinationProtocolEntity) {
		_examinationProtocol = examinationProtocolEntity;
	}

	public Integer getExaminationProtocolId() {
		return _examinationProtocolId;
	}
	public void setExaminationProtocolId(Integer examinationProtocolId) {
		_examinationProtocolId = examinationProtocolId;
	}

	public User getUser() {
		return _user;
	}
	public void setUser(User userEntity) {
		_user = userEntity;
	}

	public Integer getUserId() {
		return _userId;
	}
	public void setUserId(Integer userId) {
		_userId = userId;
	}

	public String getResult() {
		return _result;
	}
	public void setResult(String result) {
		_result = result;
	}

	public Date getCreateDate() {
		return _createDate;
	}
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public String getDevice() {
		return _device;
	}
	public void setDevice(String device) {
		_device = device;
	}

	public byte[] getDeviceData() {
		return _deviceData;
	}
	public void setDeviceData(byte[] deviceData) {
		_deviceData = deviceData;
	}

	public Doctor getDoctor() {
		return _doctor;
	}
	public void setDoctor(Doctor doctor) {
		_doctor = doctor;
	}

	public Orthoptist getOrthoptist() {
		return _orthoptist;
	}
	public void setOrthoptist(Orthoptist orthoptist) {
		_orthoptist = orthoptist;
	}
}