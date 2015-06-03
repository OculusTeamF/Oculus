/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package beans;

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.EventCanNotBeDeletedException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.EventCanNotBeNullException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.domain.entity.calendar.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;

import javax.annotation.ManagedBean;

/**
 * Created by Fabian on 31.05.2015.
 */

@ManagedBean
public class UserBean {
    private IPatient _patient;
    private String firstName = null;
    private String lastName = null;
    private String svNumber = null;
    private String doctor = null;

    private ICalendarEvent _calendarEvent;
    private String dateStart = null;
    private String dateEnd = null;
    private String description = null;
    private boolean appointAvailable = false;

    public UserBean() {
    }

    public void loadUserPatient(IPatient patient){
        _patient = patient;

        firstName = _patient.getFirstName();
        lastName = _patient.getLastName();
        svNumber = _patient.getSocialInsuranceNr();
        doctor = _patient.getDoctor().toString();
    }

    public void loadCalendarEvent (ICalendarEvent calendarEvent){
        _calendarEvent = calendarEvent;
        dateStart = _calendarEvent.getEventStart().toString();
        dateEnd = _calendarEvent.getEventEnd().toString();
        description = _calendarEvent.getDescription();
        appointAvailable = true;
    }

    public void deleteAppointment (){
        try {
            EventChooserController eventChooserController = EventChooserController.createEventChooserController(_patient);
            eventChooserController.deleteExistingEvent(_calendarEvent);
            erase();
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
            //TODO
        } catch (EventCanNotBeDeletedException e) {
            e.printStackTrace();
        } catch (EventCanNotBeNullException e) {
            e.printStackTrace();
        }
    }

    private void erase (){
        _calendarEvent = null;
        description = null;
        dateStart = null;
        dateEnd = null;
        appointAvailable = false;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getSvNumber(){
        return svNumber;
    }

    public boolean getAppointAvailable(){
        return appointAvailable;
    }

    public void setAppointAvailable(boolean available){
        appointAvailable = available;
    }

    public String getDescription() {
        return description;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getDoctor() {
        return doctor;
    }
}
