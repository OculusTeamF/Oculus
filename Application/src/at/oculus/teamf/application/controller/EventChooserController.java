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
import at.oculus.teamf.domain.criteria.DatePeriodICriteria;
import at.oculus.teamf.domain.criteria.WeekDayTime;
import at.oculus.teamf.domain.criteria.WeekDayTimeCriteria;
import at.oculus.teamf.domain.criteria.interfaces.ICriteria;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.entity.WeekDayKey;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalTime;
import java.util.*;

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
    private String description;
    private Collection<WeekDayTime> weekDayTimeCollection;

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
        weekDayTimeCollection = new LinkedList<>();
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
     * @param start this is the startdate of the time period in which the patient doesn't have time
     * @param end this is the enddate of the time period in which the patient doesn't have time
     * @param weekDayTimes these are the restrictions, when the patient wants to come
     */
    public Collection<ICalendarEvent> getAvailableEvents(Date start, Date end, Collection<WeekDayTime> weekDayTimes) throws NotAllowedToChooseEventException, NoDoctorException, ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException, DatabaseOperationException {
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

        WeekDayTimeCriteria weekDayTimeCriteria = new WeekDayTimeCriteria(weekDayTimes);
        DatePeriodICriteria datePeriodICriteria = new DatePeriodICriteria(start, end);

        Collection<ICriteria> criterias = new LinkedList<>();
        criterias.add(weekDayTimeCriteria);
        criterias.add(datePeriodICriteria);

        Iterator<ICalendarEvent> iterator = null;
        try {
            iterator = iCalendar.availableEventsIterator(criterias, 30);
        } catch (ReloadInterfaceNotImplementedException | InvalidReloadClassException | BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error("Facade exception caught! Could not get Events - " + e.getMessage());
            throw e;
        }

        Collection<ICalendarEvent> results = new LinkedList<>();

        if(iterator != null){
           while(results.size() < 3){
               results.add(iterator.next());
           }
        }

        return results;
    }

    /**
     *<h3>$getAvailableEvents</h3>
     *
     * <b>Description:</b>
     * this method creates new criterias. It creates a new WeekDayTime when the patient wants to come and adds it to a collection
     * which is returned. 
     *
     *<b>Parameter</b>
     * @param start this is the starttime of the period in which the patient wants to come
     * @param end this is the endtime of the period in which the patient wants to come
     * @param weekDay this is the weekday on which the patient wants to come
     */
    public Collection<WeekDayTime> addCriteria(String weekDay, LocalTime start, LocalTime end){
        WeekDayKey key;
        switch(weekDay){
            case "SUN":
                key = WeekDayKey.SUN;
                break;
            case "MON":
                key = WeekDayKey.MON;
                break;
            case "TUE":
                key = WeekDayKey.TUE;
                break;
            case "WED":
                key = WeekDayKey.WED;
                break;
            case "THU":
                key = WeekDayKey.THU;
                break;
            case "FRI":
                key = WeekDayKey.FRI;
                break;
            case "SAT":
                key = WeekDayKey.SAT;
                break;
            default:
                key = WeekDayKey.NULL;
        }

        WeekDayTime weekDayTime = new WeekDayTime(key, start, end);
        weekDayTimeCollection.add(weekDayTime);
        return weekDayTimeCollection;
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
    public void saveChosenEvent(ICalendarEvent iCalendarEvent, String reason) throws EventChooserControllerException {
        description = reason;
        iCalendarEvent.setPatient(iPatient);
        iCalendarEvent.setDescription(description);
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
