/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.interfaces;

import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.User;

import java.util.Date;

/**
 * Created by oculus on 04.05.15.
 */
public interface IExaminationResult extends IDomain {
    @Override
    int getId();

    @Override
    void setId(int id);

    ExaminationProtocol getExaminationProtocol();

    void setExaminationProtocol(ExaminationProtocol examinationProtocolEntity);

    Integer getExaminationProtocolId();

    void setExaminationProtocolId(Integer examinationProtocolId);

    User getUser();

    void setUser(User user);

    Integer getUserId();

    void setUserId(Integer userId);

    String getResult();

    void setResult(String result);

    Date getCreateDate();

    void setCreateDate(Date createDate);

    String getDevice();

    void setDevice(String device);

    byte[] getDeviceData();

    void setDeviceData(byte[] deviceData);

    Doctor getDoctor();

    Orthoptist getOrthoptist();
}
