/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.entity.IEntity;
import at.oculus.teamf.persistence.entity.PatientEntity;

import java.util.Collection;

/**
 * Todo: add docs, implement equals, getter into interface wrappen
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Patient {

    //<editor-fold desc="Attributes">
    private int _patientID;
    private String _firstName;
    private String _lastName;
    private Gender _gender;
    private int _svn;
    private Doctor _doctor;
    private Collection<CalendarEvent> _calendarEvents;

    //private IEntityBroker eb;

    //</editor-fold>

    public Patient(){
        IEntity p = eb.getEnity(PatientEntity.class, 0);

    }

    //<editor-fold desc="Getter/Setter">
    public int getPatientID() {

        return _patientID;
    }

    public void setPatientID(int patientID) {
        _patientID = patientID;
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

    public int getSvn() {
        return _svn;
    }

    public void setSvn(int svn) {
        _svn = svn;
    }

    public Doctor getDoctor() {
        return _doctor;
    }

    public void setDoctor(Doctor doctor) {
        _doctor = doctor;
    }

    public Collection<CalendarEvent> getCalendarEvents() {
        return _calendarEvents;
    }

    public void setCalendarEvents(Collection<CalendarEvent> calendarEvents) {
        _calendarEvents = calendarEvents;
    }
    //</editor-fold>
}
