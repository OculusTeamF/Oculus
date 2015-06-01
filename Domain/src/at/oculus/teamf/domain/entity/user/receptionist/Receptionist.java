/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.user.receptionist;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamf.domain.entity.user.User;
import at.oculus.teamf.domain.entity.ICalendar;
import at.oculus.teamf.domain.entity.queue.IPatientQueue;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by Norskan on 03.04.2015.
 */
public class Receptionist extends User implements IReceptionist {

    //<editor-fold desc="Attributes">
    private int _id;
    private Collection<ICalendar> _calendars;
    private Collection<IPatientQueue> _queues;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int _id) {
        this._id = _id;
    }

    @Override
    public Collection<ICalendar> getCalendars() {
        return _calendars;
    }

    @Override
    public void setCalendars(Collection<ICalendar> calendars) {
        _calendars = calendars;
    }

    @Override
    public Collection<IPatientQueue> getQueues() {
        return _queues;
    }

    @Override
    public void setQueues(Collection<IPatientQueue> queues) {
        _queues = queues;
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
