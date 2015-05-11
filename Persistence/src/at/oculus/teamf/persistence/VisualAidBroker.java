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
import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.VisualAid;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;
import at.oculus.teamf.persistence.entity.DiagnosisEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.VisualAidEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

/**
 * VisualAidBroker.java Created by oculus on 11.05.15.
 */
public class VisualAidBroker extends EntityBroker implements ISearch {
    public VisualAidBroker() {
		super(VisualAid.class, VisualAidEntity.class);
		addDomainClass(IVisualAid.class);
	}

	@Override
	protected IDomain persistentToDomain(IEntity entity)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException,
			       SearchInterfaceNotImplementedException, InvalidSearchParameterException {
		VisualAidEntity visualAidEntity = (VisualAidEntity) entity;
		VisualAid visualAid = new VisualAid();

		visualAid.setId(visualAidEntity.getId());
		visualAid.setDiagnosis((Diagnosis) Facade.getInstance().getBroker(Diagnosis.class)
		                                         .persistentToDomain(visualAidEntity.getDiagnosis()));
		visualAid.setDescription(visualAidEntity.getDescription());
		visualAid.setIssueDate(visualAidEntity.getIssueDate());
		visualAid.setLastPrint(visualAidEntity.getLastPrint());

		return visualAid;
	}

	@Override
	protected IEntity domainToPersistent(IDomain obj)
			throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException,
			       ClassNotMappedException {
		VisualAid visualAid = (VisualAid) obj;
		VisualAidEntity visualAidEntity = new VisualAidEntity();

		visualAidEntity.setId(visualAid.getId());
		visualAidEntity.setDiagnosis((DiagnosisEntity) Facade.getInstance().getBroker(Diagnosis.class)
		                                                     .domainToPersistent(visualAid.getDiagnosis()));
		visualAidEntity.setDescription(visualAid.getDescription());
		visualAidEntity.setIssueDate(new Timestamp(visualAid.getIssueDate().getTime()));
		visualAidEntity.setLastPrint(new Timestamp(visualAid.getLastPrint().getTime()));

		return visualAidEntity;
	}

    @Override
    public Collection search(ISession session, String... params) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, BadSessionException, ClassNotMappedException, DatabaseOperationException, SearchInterfaceNotImplementedException {
        Collection<Object> searchResult = null;

        searchResult = session.search("getAllVisualAidOfPatient", params);

        Collection<VisualAid> result = new LinkedList<>();
        for (Object o : searchResult) {
            result.add((VisualAid) persistentToDomain((VisualAidEntity) o));
        }

        return result;
    }
}
