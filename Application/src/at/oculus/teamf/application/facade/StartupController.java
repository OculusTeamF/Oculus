/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

/**<h1>$StartupController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $08.04.15
 *
 * Description:
 * This file contains the main class StartupController for the startup of the program. It contains methods to get
 * objects the program needs when started to begin the UseCases.
 * At the moment this file contains a method to get a user, a method to get all Queues and a method to get all
 * Calendars.
 **/

package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$StartupController</h2>
 *
 * <b>Description:</b>
 * This class is being used for loading the data the programs needs when it is started. It contains the method to get
 * the user and methods to get all the necessary things for startup like queues and calendars.
 **/
public class StartupController implements ILogger{

    /**
     *<h3>$StartupController</h3>
     *
     * <b>Description:</b>
     * This is the contructor of the StartupController class. It gets a instance of a facade, so that the facade
     * is loaded at the start up.
     *
     **/
    public StartupController(){
        Facade facade = Facade.getInstance();
    }

    /**
     *<h3>$getUser</h3>
     *
     * <b>Description:</b>
     * This method returns a User-Interface (at the moment a receptionist) to try and test the first UseCase (where only a
     * receptionist is needed)
     *
     **/
    public IUser getUser () throws BadConnectionException, NoBrokerMappedException {
        Facade facade = Facade.getInstance();
        User user = null;

        try {
            user = facade.getById(Receptionist.class, 1);
            log.info("Got receptionist.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }
        return user;
    }

    /**
     *<h3>$getAllQueues</h3>
     *
     * <b>Description:</b>
     * This method returns all available queues. We get a list of all queues from the persistence layer and return  a list of interfaces.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     *
     **/
    public Collection<IPatientQueue> getAllQueues() throws BadConnectionException, NoBrokerMappedException {

        Collection <Doctor> doctors = null;
        Facade facade = Facade.getInstance();

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<IPatientQueue> queues = new LinkedList<IPatientQueue>();

        if (doctors != null){
            for (Doctor doctor : doctors){
                queues.add(doctor.getQueue());
            }
        }
        log.info("All queues have been acquired.");

        return queues;
    }

    /**
     *<h3>$getAllDoctors</h3>
     *
     * <b>Description:</b>
     *
     * This method returns all available doctors. We get a list of all doctors from the persistence layer,
     * convert it into Interfaces and return it.
     *
     **/
    public Collection<IDoctor> getAllDoctors() throws NoBrokerMappedException, BadConnectionException {
        Collection<Doctor> doctors = null;
        Facade facade = Facade.getInstance();

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<IDoctor> iDoctors = new LinkedList<IDoctor>();

        if (doctors != null){
            for(Doctor doc : doctors){
                iDoctors.add(doc);
            }
        }
        log.info("All doctors have been added to the IDoctor collection.");
        return iDoctors;
    }

    /**
     *<h3>$getAllOrthoptists</h3>
     *
     * <b>Description:</b>
     *
     * This method returns all available orthoptists. We get a list of all orthoptists from the persistence layer,
     * convert it into Interfaces and return it.
     **/
    public Collection<IOrthoptist> getAllOrthoptists() throws BadConnectionException, NoBrokerMappedException {
        Collection<Orthoptist> orthoptists = null;
        Facade facade = Facade.getInstance();

        try {
            orthoptists = facade.getAll(Orthoptist.class);
            log.info("All orthoptists have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<IOrthoptist> iOrthoptists = new LinkedList<IOrthoptist>();

        if (orthoptists != null){
            for(Orthoptist o : orthoptists){
                iOrthoptists.add(o);
            }
        }
        log.info("All doctors have been added to the IOrthoptist collection.");
        return iOrthoptists;
    }

    /**
     *<h3>$getAllCalendars</h3>
     *
     * <b>Description:</b>
     * This method returns all available calendars. We get a list of all calendars from the persistence layer
     * and return a list of interfaces.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     *
     **/
    public Collection<ICalendar> getAllCalendars() throws BadConnectionException, NoBrokerMappedException {
        Facade facade = Facade.getInstance();

        Collection<Calendar> calendars = null;
        try {
            calendars = facade.getAll(Calendar.class);
            log.info("All calendars have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }
        Collection<ICalendar> iCalendars = new LinkedList<ICalendar>();

        if (calendars != null){
            for(Calendar c : calendars){
                iCalendars.add(c);
            }
        }
        log.info("All doctors have been added to the ICalendar collection.");
        return iCalendars;
    }

    /**
     *<h3>$getAllEntries</h3>
     *
     * <b>Description:</b>
     * This method returns all available calendar entries. We get a list of all calendar events from the persistence
     * layer and return a list of interfaces.
     *
     **/
    public Collection<ICalendarEvent> getAllEntries(ICalendar iCalendar) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException {
        Calendar calendar = (Calendar) iCalendar;
        Collection <CalendarEvent> calendarEvents = calendar.getEvents();
        Collection <ICalendarEvent> iCalendarEvents = new LinkedList <ICalendarEvent>();

        if (calendarEvents != null){
            for (CalendarEvent event : calendarEvents){
                iCalendarEvents.add(event);
            }
        }
        log.info("All calendar events have been acquired and added to the ICalendarEvent collection.");

        return iCalendarEvents;
    }

    /**
     *<h3>$getAllEntries</h3>
     *
     * <b>Description:</b>
     * This method returns all available calendar entries for a single patient.
     *
     **/
    public Collection<ICalendarEvent> getEventsFromPatient(IPatient iPatient) throws ReloadInterfaceNotImplementedException, InvalidReloadClassException, BadConnectionException, NoBrokerMappedException {
        Patient patient = (Patient) iPatient;
        Collection <CalendarEvent> events = patient.getCalendarEvents();
        Collection <ICalendarEvent> iEvents = new LinkedList<ICalendarEvent>();

        if (events != null){
            for (CalendarEvent event : events){
                iEvents.add(event);
            }
        }
        log.info("All calendar events have been acquired and added to the ICalendarEvent collection.");
        return iEvents;
    }

    /**
    * <h3>$getAllDoctorsAndOrthoptists</h3>
    *
    * <b>Description:</b>
    *
    * This method returns all available orthoptists and doctors in one collection. We get a list of all
    * orthoptists from the persistence layer and one list of all doctors, convert it into Interfaces and return it.
    **/
    public Collection<IUser> getAllDoctorsAndOrthoptists() throws BadConnectionException, NoBrokerMappedException {
        Collection<Orthoptist> orthoptists;
        Facade facade = Facade.getInstance();

        try {
            orthoptists = facade.getAll(Orthoptist.class);
            log.info("All orthoptists have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<Doctor> doctors;

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<IUser> iUsers = new LinkedList<>();

        if (orthoptists != null){
            for(Orthoptist o : orthoptists){
                iUsers.add(o);
            }
        }
        if (doctors != null){
            for (Doctor doc : doctors){
                iUsers.add(doc);
            }
        }

        log.info("All doctors and orthoptists have been added to the IUser collection.");
        return iUsers;
    }


    /**
     * <h3>$getQueueByUserId</h3>
     *
     * <b>Description:</b>
     *
     * This method gets a user-id from the presentation-layer, fetches the correct queue from the given user
     * and returns an interface of the chosen queue.
     *
     * <b>Parameter</b>
     * @param userId this parameter shows the user id of the user, who's queue should be returned
     **/
    public IPatientQueue getQueueByUserId(int userId) throws BadConnectionException, NoBrokerMappedException {
        Facade facade = Facade.getInstance();

        User user;
        Doctor doctor;
        Orthoptist orthoptist;
        PatientQueue queue = null;

        try {
            user = facade.getById(Doctor.class, userId);
            if(user == null){
                user = facade.getById(Orthoptist.class, userId);
            }
            log.info("The correct user have been acquired.");
        } catch (BadConnectionException badConnectionException) {
            log.warn("BadConnectionException caught! Bad connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        if(user != null){
            if(user instanceof Doctor){
                doctor = (Doctor) user;
                queue = doctor.getQueue();
            }else if(user instanceof Orthoptist){
                orthoptist = (Orthoptist) user;
                queue = orthoptist.getQueue();
            }
        }
        return queue;
    }
}
