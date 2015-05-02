/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.ISession;
import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * ExaminationResultBroker.java Created by oculus on 30.04.15.
 */
public class ExaminationResultBroker extends EntityBroker implements ISearch {
	public ExaminationResultBroker() {
		super(ExaminationResult.class, ExaminationResultEntity.class);
	}

	@Override
	protected ExaminationResult persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException {
		ExaminationResultEntity examinationResultEntity = (ExaminationResultEntity) entity;

		ExaminationProtocol examinationProtocol = null;
		if ((examinationResultEntity.getExaminationProtocolEntity() != null)) {
            try {
                examinationProtocol = (ExaminationProtocol) Facade.getInstance().getBroker(ExaminationProtocol.class)
                                                                  .persistentToDomain(examinationResultEntity
                                                                          .getExaminationProtocolEntity());
            } catch (BadSessionException e) {
                e.printStackTrace();
            }
        }

		User user = null;
		Doctor doctor = null;
		Orthoptist orthoptist = null;
		if (examinationResultEntity.getUserId() != null) {
            try {

	                for(Doctor d : ((LinkedList<Doctor>) (LinkedList<?>) Facade.getInstance().search(Doctor.class, examinationResultEntity.getUserId()+""))){
		                doctor = d;
	                }
	                if(doctor!=null){
		                user = doctor;
	                }
	                for(Orthoptist o : ((LinkedList<Orthoptist>) (LinkedList<?>) Facade.getInstance().search(
			                Orthoptist.class, examinationResultEntity.getUserId() + ""))){
		                orthoptist = o;
	                }
	                if(orthoptist!=null){
		                user = orthoptist;
	                }
            } catch (InvalidSearchParameterException e) {
                e.printStackTrace();
            } catch (SearchInterfaceNotImplementedException e) {
                e.printStackTrace();
            }
        }

		return new ExaminationResult(examinationResultEntity.getId(), examinationProtocol, user,
		                             examinationResultEntity.getResult(), examinationResultEntity.getCreateDate(),
		                             examinationResultEntity.getDevice(), examinationResultEntity.getDeviceData());
	}

	@Override
	protected ExaminationResultEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException {
		ExaminationResult examinationResult = (ExaminationResult) obj;

		ExaminationProtocolEntity examinationProtocolEntity = null;
		if (examinationResult.getExaminationProtocol() != null) {
            try {
                examinationProtocolEntity =
                        (ExaminationProtocolEntity) Facade.getInstance().getBroker(ExaminationProtocol.class)
                                                          .domainToPersistent(examinationResult.getExaminationProtocol());
            } catch (BadSessionException e) {
                e.printStackTrace();
            }
        }

		UserEntity userEntity = null;
		if (examinationResult.getUser() != null) {
            try {
                if(examinationResult.getDoctor()!=null){
                    DoctorEntity doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(Doctor.class)
	                                                     .domainToPersistent(examinationResult.getDoctor());
	                if(doctorEntity!=null) {
		                userEntity = doctorEntity.getUser();
	                }
                } else if (examinationResult.getOrthoptist()!=null) {
	                OrthoptistEntity orthoptistEntity = (OrthoptistEntity) Facade.getInstance().getBroker(Orthoptist.class)
	                                                                              .domainToPersistent(examinationResult.getOrthoptist());
	                if(orthoptistEntity!=null) {
		                userEntity = orthoptistEntity.getUser();
	                }
                }

            } catch (BadSessionException e) {
                e.printStackTrace();
            }
        }

		return new ExaminationResultEntity(examinationResult.getId(), examinationProtocolEntity, userEntity,
		                                   examinationResult.getResult(), new Timestamp(examinationResult.getCreateDate().getTime()),
		                                   examinationResult.getDevice(), examinationResult.getDeviceData());
	}

    @Override
    public Collection<ExaminationResult> search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException {
        if (params.length == 1) {
            return (Collection<ExaminationResult>) (Collection<?>) session.search("getExaminationResultsByPatientId", params[0]);
        } else {
            return null;
        }
    }
}
