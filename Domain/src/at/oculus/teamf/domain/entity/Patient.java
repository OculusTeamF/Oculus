/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;

/**
 * Todo: add docs, implement equals, getter into interface wrappen
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Patient implements IPatient, IDomain {

    //<editor-fold desc="Attributes">
    private int _id;
    private String _firstName;
    private String _lastName;
    private Gender _gender;
    private String _svn;
    private Doctor _doctor;
    private Collection<CalendarEvent> _calendarEvents;

    //private EntityBroker eb;

    //</editor-fold>

    public Patient(){
        //IEntity p = eb.getEnity(PatientEntity.class, 0);

    }

    //<editor-fold desc="Getter/Setter">

    public int getId() {

        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public Patient setLastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public Gender getGender() {
        return _gender;
    }

    public void setGender(Gender gender) {
        _gender = _gender;
    }

    public String getSvn() {
        return _svn;
    }

    public void setSvn(String svn) {
        _svn = svn;
    }

    public Doctor getDoctor() {
        return _doctor;
    }

    public void setDoctor(Doctor doctor) {
        _doctor = doctor;
    }

    public Collection<CalendarEvent> getCalendarEvents() {
        try {
            Facade.getInstance().reloadCollection(this, CalendarEvent.class);
        } catch (FacadeException e) {
            //Todo: add loging
            e.printStackTrace();
        }
        return _calendarEvents;
    }

    public void setCalendarEvents(Collection<CalendarEvent> calendarEvents) {
        _calendarEvents = calendarEvents;
    }
    //</editor-fold>

	@Override
	public String toString(){
		return getFirstName() + " " + getLastName();
	}
}
