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
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.examination.ExaminationResult;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.examination.IExaminationResult;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.persistence.entity.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * ExaminationResultBroker.java Created by oculus on 30.04.15.
 */
class ExaminationResultBroker extends EntityBroker implements ISearch {
	public ExaminationResultBroker() {
		super(ExaminationResult.class, ExaminationResultEntity.class);
		addDomainClassMapping(IExaminationResult.class);
	}

	@Override
	protected ExaminationResult persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, ClassNotMappedException, DatabaseOperationException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		ExaminationResultEntity examinationResultEntity = (ExaminationResultEntity) entity;

		ExaminationProtocol examinationProtocol = null;
		if ((examinationResultEntity.getExaminationProtocolEntity() != null)) {
			examinationProtocol = (ExaminationProtocol) Facade.getInstance().getBroker(ExaminationProtocol.class)
                                                                  .persistentToDomain(examinationResultEntity
                                                                          .getExaminationProtocolEntity());

        }

		IUser user = null;
		IDoctor doctor = null;
		IOrthoptist orthoptist = null;
		if (examinationResultEntity.getUserId() != null) {
	                for(IDoctor d : ((LinkedList<IDoctor>) (LinkedList<?>) Facade.getInstance().search(IDoctor.class, examinationResultEntity.getUserId()+""))){
		                doctor = d;
	                }
	                if(doctor!=null){
		                user = doctor;
	                }
	                for(IOrthoptist o : ((LinkedList<IOrthoptist>) (LinkedList<?>) Facade.getInstance().search(
							IOrthoptist.class, examinationResultEntity.getUserId() + ""))){
		                orthoptist = o;
	                }
	                if(orthoptist!=null){
		                user = orthoptist;
	                }
        }

		return new ExaminationResult(examinationResultEntity.getId(), examinationProtocol, user,
		                             examinationResultEntity.getResult(), examinationResultEntity.getCreateDate(),
		                             examinationResultEntity.getDevice(), examinationResultEntity.getDeviceData());
	}

	@Override
	protected ExaminationResultEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, ClassNotMappedException, DatabaseOperationException {
		ExaminationResult examinationResult = (ExaminationResult) obj;

		ExaminationProtocolEntity examinationProtocolEntity = null;
		if (examinationResult.getExaminationProtocol() != null) {
			examinationProtocolEntity =
                        (ExaminationProtocolEntity) Facade.getInstance().getBroker(ExaminationProtocol.class)
                                                          .domainToPersistent(examinationResult.getExaminationProtocol());
        }

		UserEntity userEntity = null;
		if (examinationResult.getUser() != null) {
                if(examinationResult.getDoctor()!=null){
                    DoctorEntity doctorEntity = (DoctorEntity) Facade.getInstance().getBroker(IDoctor.class)
	                                                     .domainToPersistent(examinationResult.getDoctor());
	                if(doctorEntity!=null) {
		                userEntity = doctorEntity.getUser();
	                }
                } else if (examinationResult.getOrthoptist()!=null) {
	                OrthoptistEntity orthoptistEntity = (OrthoptistEntity) Facade.getInstance().getBroker(IOrthoptist.class)
	                                                                              .domainToPersistent((IDomain) examinationResult.getOrthoptist());
	                if(orthoptistEntity!=null) {
		                userEntity = orthoptistEntity.getUser();
	                }
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
