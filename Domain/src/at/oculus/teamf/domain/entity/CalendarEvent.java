/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class CalendarEvent implements ICalendarEvent {

    //<editor-fold desc="Attributes">
    private int _id;
    private String _description;
    private Date _eventStart;
    private Date _eventEnd;
    private Patient _patient;
    private EventType _type;
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
    public String getDescription() {
        return _description;
    }

    @Override
    public void setDescription(String description) {
        _description = description;
    }

    @Override
    public Date getEventStart() {
        return _eventStart;
    }

    @Override
    public void setEventStart(Date eventStart) {
        _eventStart = eventStart;
    }

    @Override
    public Date getEventEnd() {
        return _eventEnd;
    }

    @Override
    public void setEventEnd(Date eventEnd) {
        _eventEnd = eventEnd;
    }

    public Patient getPatient(){return _patient; }

    public void setPatient(Patient patient) {
        _patient = patient;
    }

	//</editor-fold>

    @Override
    public String toString(){
        String eventDate = (new SimpleDateFormat("dd.MM.yyyy").format(_eventStart)) + " from " + (new SimpleDateFormat("HH:mm").format(_eventStart) + " to " + (new SimpleDateFormat("HH:mm").format(_eventEnd)));
        if(_description!=null && !_description.isEmpty()){
            eventDate = eventDate + ", " + _description;
        }
        return eventDate;
    }
}
