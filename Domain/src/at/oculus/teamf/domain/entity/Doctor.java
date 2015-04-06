/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Doctor extends User{

    //<editor-fold desc="Attributes">
    private Calendar _calendar;
    private PatientQueue _queue;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    public Calendar getCalendar() {
        return _calendar;
    }

    public void setCalendar(Calendar _calendar) {
        this._calendar = _calendar;
    }

    public PatientQueue getQueue() {
        return _queue;
    }

    public void setQueue(PatientQueue _queue) {
        this._queue = _queue;
    }
    //</editor-fold>
}
