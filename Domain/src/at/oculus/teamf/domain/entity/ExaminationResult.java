/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IExaminationResult;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Date;

/**
 * ExaminationResult.java Created by oculus on 30.04.15.
 */
public class ExaminationResult implements IExaminationResult, ILogger {
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
    public ExaminationProtocol getExaminationProtocol() {
		return _examinationProtocol;
	}
	@Override
    public void setExaminationProtocol(ExaminationProtocol examinationProtocolEntity) {
		_examinationProtocol = examinationProtocolEntity;
	}

	@Override
    public Integer getExaminationProtocolId() {
		return _examinationProtocolId;
	}
	@Override
    public void setExaminationProtocolId(Integer examinationProtocolId) {
		_examinationProtocolId = examinationProtocolId;
	}

	@Override
    public User getUser() {
		return _user;
	}
	@Override
    public void setUser(User user) {
        if(user!=null){
            _user = user;
            _userId = user.getUserId();
            if(user instanceof Doctor){
                _doctor = (Doctor) user;
            } else if (user instanceof Orthoptist) {
                _orthoptist = (Orthoptist) user;
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
    public String getResult() {
		return _result;
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
    public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
    public String getDevice() {
		return _device;
	}
	@Override
    public void setDevice(String device) {
		_device = device;
	}

	@Override
    public byte[] getDeviceData() {
		return _deviceData;
	}
	@Override
    public void setDeviceData(byte[] deviceData) {
		_deviceData = deviceData;
	}

	@Override
    public Doctor getDoctor() {
		return _doctor;
	}

	@Override
    public Orthoptist getOrthoptist() {
		return _orthoptist;
	}

    @Override
    public String toString(){
        return _result;
    }
}