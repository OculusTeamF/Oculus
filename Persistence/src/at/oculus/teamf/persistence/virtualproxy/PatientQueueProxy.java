/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotRemovePatientFromQueueException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.domain.entity.queue.IQueueEntry;
import at.oculus.teamf.domain.entity.user.IUser;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Simon Angerer on 08.06.2015.
 */
public class PatientQueueProxy extends VirtualProxy<IPatientQueue> implements IPatientQueue, ILogger {
    protected PatientQueueProxy(IPatientQueue real) {
        super(real);
    }

    @Override
    public Collection<IQueueEntry> getEntries() {
        return _real.getEntries();
    }

    @Override
    public void setEntries(Collection<IQueueEntry> entries) {
        _real.setEntries(entries);
    }

    @Override
    public void setUser(IUser user) {
        _real.setUser(user);
    }

    @Override
    public void addPatient(IPatient patient, Timestamp arrivaltime) throws CouldNotAddPatientToQueueException {
        _real.addPatient(patient, arrivaltime);
        try {
            Facade.getInstance().save(_real);
        } catch (DatabaseOperationException | NoBrokerMappedException | BadConnectionException e) {
            log.error(e.getMessage());
            throw new CouldNotAddPatientToQueueException();
        }
    }

    @Override
    public IQueueEntry removePatient(IPatient patient) throws CouldNotRemovePatientFromQueueException {
        IQueueEntry toDelete = _real.removePatient(patient);
        try {
            Facade.getInstance().save(_real);
            Facade.getInstance().delete(toDelete);
        } catch (BadConnectionException | NoBrokerMappedException | InvalidSearchParameterException | DatabaseOperationException e) {
            log.error(e.getMessage());
            throw new CouldNotRemovePatientFromQueueException();
        }
        return null;
    }

    @Override
    public int getId() {
        return _real.getId();
    }

    @Override
    public void setId(int id) {
        _real.setId(id);
    }
}
