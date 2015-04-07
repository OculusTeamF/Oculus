/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.databaseconnection.session.BadSessionException;
import at.oculus.teamf.databaseconnection.session.ClassNotMappedException;
import at.oculus.teamf.persistence.entity.CalendareventEntity;
import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;
import at.oculus.teamf.persistence.facade.Facade;

import java.util.Date;
import java.util.LinkedList;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
@EntityClass("CalendareventEntity.class")
public class CalendarEvent implements DomainEntity {

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

    public void setPatient(Patient patient) {
        _patient = patient;
    }

	@Override
	public int getId() {
		return _eventID;
	}

	@Override
	public void set(IEntity entity) {
		CalendareventEntity that = (CalendareventEntity) entity;
		this.setEventID(that.getCalendarEventId());
		this.setDescription(that.getDescription());
		this.setEventStart(that.getEventStart());
		this.setEventEnd(that.getEventEnd());
		// load patient
		LinkedList<Class> clazzes = new LinkedList<Class>();
		clazzes.add(PatientEntity.class);
		Facade facade = Facade.getInstance(clazzes);
		try {
			this.setPatient((Patient) facade.getObjectById(Patient.class,that.getPatientId().getPatientId()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	//</editor-fold>
}
