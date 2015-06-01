/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.*;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

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
    private Collection<ICalendarEvent> futureEvents;

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
    private EventChooserController(IPatient patient){
        iPatient = patient;
    }

    public static EventChooserController createEventChooserController(IPatient iPatient) throws PatientCanNotBeNullException {
        if(iPatient == null){
            throw new PatientCanNotBeNullException();
        }
        return new EventChooserController(iPatient);
    }

    /**
     * <h3>$deleteExistingEvent</h3>
     * <p/>
     * <b>Description:</b>
     * This method deletes the existing event from a patient. If the event can not be deleted (for whatever reason),
     * an exception is thrown or the method returns the value false. If the Event is deleted successfully, the method
     * returns the value true.
     * <p/>
     * *<b>Parameter</b>
     * @param event this is the calendar-event, which should be deleted
     */
    public Collection<ICalendarEvent> deleteExistingEvent(ICalendarEvent event) throws EventCanNotBeNullException, EventCanNotBeDeletedException {
        if(event == null){
            throw new EventCanNotBeNullException();
        }

        if(!checkDate(event.getEventStart())){
            throw new EventCanNotBeDeletedException();
        }else{
            if(futureEvents.contains(event)){
                futureEvents.remove(event);
            }

            IFacade facade = Facade.getInstance();
            try {
                facade.delete(event);
                log.info("Event deleted successfully");
            } catch (BadConnectionException | NoBrokerMappedException | InvalidSearchParameterException | DatabaseOperationException e) {
                log.error("Facade exception caught! Event can not be deleted" + e.getMessage());
                throw new EventCanNotBeDeletedException();
            }

            return futureEvents;
        }
    }

    /**
     * <h3>$checkDate</h3>
     * <p/>
     * <b>Description:</b>
     * This method checks if the given date is at least 2 days in the future. If not, the method returns the value false
     * <p/>
     * *<b>Parameter</b>
     * @param date this is the date, which should be checked
     */
    private boolean checkDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 2);

        Date checkDate = cal.getTime();

        if(!date.after(checkDate)){
            return  false;
        }

        return true;
    }

    /**
     *<h3>$getAvailableEvents</h3>
     *
     * <b>Description:</b>
     * this method gets all available information in the parameters, with which some suitable calendar-events are searched.
     * First it checks if the patient is allowed to choose a new appointment, by checking his or her future appointments again.
     * It returns a collection of calendar-events, or an empty collection, if there are no fitting events available.
     *
     *<b>Parameter</b>
     * @param
     */
    public Collection<ICalendarEvent> getAvailableEvents() throws NotAllowedToChooseEventException, NoDoctorException {
        //TODO add parameters

        if(futureEvents.size() > 0){
            for(ICalendarEvent event : futureEvents){
                if(!checkDate(event.getEventStart())){
                    log.error("Patient has a future appointment. - not allowed to choose another one!");
                    throw new NotAllowedToChooseEventException();
                }
            }
        }

        IDoctor iDoctor = iPatient.getIDoctor();
        if(iDoctor == null){
            log.error("Patient has no Doctor!");
            throw new NoDoctorException();
        }
        ICalendar iCalendar = iDoctor.getCalendar();


        //TODO check input --> create criterias

        //TODO getAvailableEvents from ICalendar

        //TODO return 3 ICalendarEvents as Collection

        return null;
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
    public void saveChosenEvent(ICalendarEvent iCalendarEvent) throws EventChooserControllerException {
        //TODO further checkup of the given calendarevent before saving?

        IFacade facade = Facade.getInstance();
        try {
            facade.save(iCalendarEvent);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error("Facade exception caught! Calendarevent could not be saved - " + e.getMessage());
            throw new EventChooserControllerException();
        }

    }

    /**
     *<h3>$checkPatientAppointments</h3>
     *
     * <b>Description:</b>
     *this method checks if the specified patient already has future appointments.
     * The method returns a collection of ICalendarEvents. The collectioin is empty, if there is no
     * future appointment saved for the patient. If the size of the collection is bigger than 0, the patient already
     * has future appointments, and is not allowed to choose another one.
     */
    public Collection<ICalendarEvent> checkPatientsAppointments() throws CouldNotGetCalendarEventsException {
        Collection<ICalendarEvent> events =  iPatient.getCalendarEvents();
        futureEvents = new LinkedList<>();

        for(ICalendarEvent event : events){
            Date date = event.getEventStart();
            if(date.after(new Date())){
                futureEvents.add(event);
            }
        }
        return futureEvents;
    }
}
