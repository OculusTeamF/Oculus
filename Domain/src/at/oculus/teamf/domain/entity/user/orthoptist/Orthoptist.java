/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.user.orthoptist;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamf.domain.entity.calendar.ICalendar;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;
import at.oculus.teamf.domain.entity.queue.PatientQueue;
import at.oculus.teamf.domain.entity.queue.QueueEntry;
import at.oculus.teamf.domain.entity.user.User;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * domain orthoptist class
 */
class Orthoptist extends User implements IOrthoptist, ILogger {
	//<editor-fold desc="Attributes">
	private int _id;
    private ICalendar _calendar;
    private IPatientQueue _queue;

    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    @Override
    public int getId() {
	    return _id;
    }

	@Override
    public void setId(int id) {
		_id = id;
	}

    @Override
    public ICalendar getCalendar() {
	    return _calendar;
    }

    @Override
    public void setCalendar(ICalendar calendar) {
        _calendar = calendar;
    }

    @Override
    public IPatientQueue getQueue() throws NoBrokerMappedException, BadConnectionException {
        if(_queue == null) {
            try {
                _queue = new  PatientQueue(this, Facade.getInstance().search(QueueEntry.class, "Orthopist", Integer.toString(_id)));
            } catch (SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | DatabaseOperationException | NoBrokerMappedException e) {
                log.error(e.getMessage());
            }
        }
        return _queue;
    }

    @Override
    public void setQueue(IPatientQueue queue) {
        _queue = queue;
    }

    @Override
    public List<? extends IExaminationProtocolTb2> getExaminationProtocols() {
        //not used
        return null;
    }

    @Override
    public Integer getUserId() {
        return _id;
    }

    //not used
    @Override
    public LocalDateTime getCreationDate() {
        return null;
    }

    //not used
    @Override
    public LocalDateTime getIdleDate() {
        return null;
    }
    //</editor-fold>
}
