/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.facade.Facade;

import java.util.Date;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class CalendarEvent {

    //<editor-fold desc="Attributes">
    private int _eventID;
    private String _description;
    private Date _eventStart;
    private Date _eventEnd;
    private Patient _patient;
    private EventType _type;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    public int getEventID() {
        return _eventID;
    }

    public void setEventID(int eventID) {
        _eventID = eventID;
    }

    public String getDescription() {


        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public Date getEventStart() {
        return _eventStart;
    }

    public void setEventStart(Date eventStart) {
        _eventStart = eventStart;
    }

    public Date getEventEnd() {
        return _eventEnd;
    }

    public void setEventEnd(Date eventEnd) {
        _eventEnd = eventEnd;
    }

    public Patient getPatient(){return _patient; }

    public void addPatient(Patient patient) {
        _patient = patient;
    }
	//</editor-fold>
}
