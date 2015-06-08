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
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.domain.entity.queue.IQueueEntry;
import at.oculus.teamf.domain.entity.queue.PatientQueue;
import at.oculus.teamf.domain.entity.queue.QueueEntry;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.domain.entity.user.doctor.Doctor;
import at.oculus.teamf.domain.entity.user.orthoptist.Orthoptist;
import at.oculus.teamf.persistence.entity.QueueEntity;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.persistence.factory.DomainWrapperFactory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Simon Angerer on 08.06.2015.
 */
public class PatientQueueBroker extends EntityBroker<IPatientQueue, QueueEntity> {
    public PatientQueueBroker() {
        super(IPatientQueue.class, QueueEntity.class);
        addDomainClassMapping(PatientQueue.class);
    }

    @Override
    public IPatientQueue getEntity(ISession session, int id) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException, InvalidSearchParameterException, SearchInterfaceNotImplementedException {
        IPatientQueue queue = (IPatientQueue) DomainWrapperFactory.getInstance().create(IPatientQueue.class);

        IUser user = null;
        LinkedList<IUser> searchResult = new LinkedList<>(Facade.getInstance().search(Doctor.class, Integer.toString(id)));
        if((searchResult.size() == 1) &&(searchResult.getFirst().getUserGroupId() == 2) ) { // one doctor found
            user = searchResult.getFirst();
            queue.setEntries(Facade.getInstance().search(QueueEntry.class, "Doctor", Integer.toString(id)));
            queue.setUser(user);
        }

        if(user == null) {
            searchResult = new LinkedList<>(Facade.getInstance().search(Orthoptist.class, Integer.toString(id)));
            if((searchResult.size() == 1) &&(searchResult.getFirst().getUserGroupId() == 3) ) { // one Orthoptist found
                user = searchResult.getFirst();
                queue.setEntries(Facade.getInstance().search(QueueEntry.class, "Orthopist", Integer.toString(id)));
                queue.setUser(user);
            }
        }

        return queue;
    }

    @Override
    public void saveEntity(ISession session, IPatientQueue domainObj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        for(IQueueEntry qe : domainObj.getEntries()) {
            Facade.getInstance().save(qe);
        }
    }

    @Override
    protected IPatientQueue persistentToDomain(QueueEntity entity) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException, SearchInterfaceNotImplementedException, InvalidSearchParameterException {
        //not needed
        return null;
    }

    @Override
    protected QueueEntity domainToPersistent(IPatientQueue obj) throws NoBrokerMappedException, BadConnectionException, DatabaseOperationException, ClassNotMappedException {

        //not needed
        return null;
    }
}
