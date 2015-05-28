/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;

/**
 * <h1>$EventChooserController.java</h1>
 *
 * @author $jpo2433
 * @since $28.05.2015
 * <p/>
 * <b>Description:</b>
 * This File contains the EventChooserController class,
 * which is after the login of a patient on the webapplication, responsible for the management of choosing an event.
 */
public class EventChooserController implements ILogger {

    private IPatient iPatient;

    /**
     *<h3>$EventChooserController</h3>
     *
     * <b>Description:</b>
     *this is the constructor of the EventChooserController. To get an instance of this controller,
     * an interface of a patient has to be set at the beginning (because the patient has to be logged in)
     *
     *<b>Parameter</b>
     * @param patient this parameter shows the interface of the patient, who wants to choose an event
     */
    public EventChooserController(IPatient patient){
        iPatient = patient;
    }

    /**
     * <h3>$deleteExistingEvent</h3>
     * <p/>
     * <b>Description:</b>
     * This method deletes the existing event from a patient. If the event can not be deleted (for whatever reason),
     * an exception is thrown or the method returns the value false. If the Event is deleted successfully, the method
     * returns the value true.
     * <p/>
     */
    public boolean deleteExistingEvent(){
        //TODO implement deleteExistingEvent()

        //check if the event can be deleted
        //return false if not
        //else

        //delete the event

        //save the updated patient object

        return true;
    }

    /**
     *<h3>$getAvailableEvents</h3>
     *
     * <b>Description:</b>
     * this method gets all available information in the parameters, with which some suitable calendar-events are searched.
     * It returns a collection of calendar-events, or an empty collection, if there are no fitting events available.
     *
     *<b>Parameter</b>
     * @param
     */
    public void getAvailableEvents(){
        //TODO implement getAvailableEvents()
        //parameters!

        //Criteria erstellen!

    }

    /**
     *<h3>$saveChosenEvent</h3>
     *
     * <b>Description:</b>
     *this method saves the given Calendar-event to the specified patient into the database and returns the value true,
     * if everything works.
     *
     *<b>Parameter</b>
     * @param iCalendarEvent this is the chosen calendar-event, which should be fixed
     */
    public void saveChosenEvent(ICalendarEvent iCalendarEvent){
        //TODO implement saveChosenEvent()

    }

    /**
     *<h3>$checkPatientAppointments</h3>
     *
     * <b>Description:</b>
     *this method checks if the specified patient already has an appointment.
     *
     *<b>Parameter</b>
     * @param
     */
    public void checkPatientsAppointments() throws CouldNotGetCalendarEventsException {
        //TODO implement checkPatientsAppointments()
    }
}
