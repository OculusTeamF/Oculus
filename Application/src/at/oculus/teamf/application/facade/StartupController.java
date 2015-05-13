/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * <h1>$StartupController.java</h1>
 *
 * @author $jpo2433
 * @author $sha9939
 * @since $08.04.15
 * <p/>
 * Description:
 * This file contains the main class StartupController for the startup of the program. It contains methods to get
 * objects the program needs when started to begin the UseCases.
 * At the moment this file contains a method to get a user, a method to get all Queues and a method to get all
 * Calendars.
 */

package at.oculus.teamf.application.facade;

import at.oculus.teamE.domain.interfaces.IDomainFactory;
import at.oculus.teamE.persistence.api.IPersistenceFacadeTb2;
import at.oculus.teamE.support.DependencyResolver;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * <h2>$StartupController</h2>
 * <p/>
 * <b>Description:</b>
 * This class is being used for loading the data the programs needs when it is started. It contains the method to get
 * the user and methods to get all the necessary things for startup like queues and calendars.
 */
public class StartupController implements ILogger {

    /**
     * <h3>$StartupController</h3>
     * <p/>
     * <b>Description:</b>
     * This is the contructor of the StartupController class. It gets a instance of a facade, so that the facade
     * is loaded at the start up.
     */
    public StartupController() {
        Facade facade = Facade.getInstance();
        DependencyResolver teamEDependencies = DependencyResolver.getInstance();
        teamEDependencies.registerPersistenceFacade(new Supplier<IPersistenceFacadeTb2>() {
            @Override
            public IPersistenceFacadeTb2 get() {
                return null;
            }
        });
        teamEDependencies.registerDomainFactory(new Supplier<IDomainFactory>() {
            @Override
            public IDomainFactory get() {
                return null;
            }
        });
    }

    /**
     * <h3>$getUser</h3>
     * <p/>
     * <b>Description:</b>
     * This method returns a User-Interface (at the moment a receptionist) to try and test the first UseCase (where only a
     * receptionist is needed)
     */
    public IUser getUser() throws BadConnectionException, CriticalClassException, CriticalDatabaseException {
        Facade facade = Facade.getInstance();
        User user = null;

        try {
            user = facade.getById(Receptionist.class, 1);
        } catch (NoBrokerMappedException e) {
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            throw new CriticalDatabaseException();
        }

        return user;
    }

    /**
     * <h3>$getAllQueues</h3>
     * <p/>
     * <b>Description:</b>
     * This method returns all available queues. We get a list of all queues from the persistence layer and return  a list of interfaces.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     */
    public Collection<IPatientQueue> getAllQueues() throws BadConnectionException, CriticalClassException, CriticalDatabaseException {

        Collection<Doctor> doctors = null;
        Facade facade = Facade.getInstance();

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (NoBrokerMappedException e) {
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            throw new CriticalDatabaseException();
        }

        Collection<IPatientQueue> queues = new LinkedList<IPatientQueue>();

        if (doctors != null) {
            for (Doctor doctor : doctors) {
                queues.add(doctor.getQueue());
            }
        }
        log.info("All queues have been acquired.");

        return queues;
    }

    /**
     * <h3>$getAllDoctors</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method returns all available doctors. We get a list of all doctors from the persistence layer,
     * convert it into Interfaces and return it.
     */
    public Collection<IDoctor> getAllDoctors() throws NoBrokerMappedException, BadConnectionException, CriticalClassException, CriticalDatabaseException {
        Collection<Doctor> doctors = null;
        Facade facade = Facade.getInstance();

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }

        log.info("All doctors have been added to the IDoctor collection.");
        return (Collection<IDoctor>) (Collection<?>) doctors;
    }

    /**
     * <h3>$getAllOrthoptists</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method returns all available orthoptists. We get a list of all orthoptists from the persistence layer,
     * convert it into Interfaces and return it.
     */
    public Collection<IOrthoptist> getAllOrthoptists() throws BadConnectionException, CriticalClassException, CriticalDatabaseException {
        Collection<Orthoptist> orthoptists = null;
        Facade facade = Facade.getInstance();

        try {
            orthoptists = facade.getAll(Orthoptist.class);
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }


        log.info("All doctors have been added to the IOrthoptist collection.");
        return (Collection<IOrthoptist>) (Collection<?>) orthoptists;
    }

    /**
     * <h3>$getAllCalendars</h3>
     * <p/>
     * <b>Description:</b>
     * This method returns all available calendars. We get a list of all calendars from the persistence layer
     * and return a list of interfaces.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     */
    public Collection<ICalendar> getAllCalendars() throws CriticalClassException, CriticalDatabaseException, BadConnectionException {
        Facade facade = Facade.getInstance();

        Collection<Calendar> calendars = null;
        try {
            calendars = facade.getAll(Calendar.class);
            log.info("All calendars have been acquired.");
        } catch (NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }

        log.info("All doctors have been added to the ICalendar collection.");
        return (Collection<ICalendar>) (Collection<?>) calendars;
    }

    /**
     * <h3>$getAllEntries</h3>
     * <p/>
     * <b>Description:</b>
     * This method returns all available calendar entries. We get a list of all calendar events from the persistence
     * layer and return a list of interfaces.
     */
    public Collection<ICalendarEvent> getAllEntries(ICalendar iCalendar) throws CriticalClassException, BadConnectionException, CriticalDatabaseException {
        Calendar calendar = (Calendar) iCalendar;
        Collection<CalendarEvent> calendarEvents = null;
        try {
            calendarEvents = calendar.getEvents();
        } catch (ReloadInterfaceNotImplementedException | InvalidReloadClassException | NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }
        Collection<ICalendarEvent> iCalendarEvents = new LinkedList<ICalendarEvent>();

        if (calendarEvents != null) {
            for (CalendarEvent event : calendarEvents) {
                iCalendarEvents.add(event);
            }
        }
        log.info("All calendar events have been acquired and added to the ICalendarEvent collection.");

        return iCalendarEvents;
    }

    /**
     * <h3>$getAllEntries</h3>
     * <p/>
     * <b>Description:</b>
     * This method returns all available calendar entries for a single patient.
     */
    public Collection<ICalendarEvent> getEventsFromPatient(IPatient iPatient) throws CouldNotGetCalendarEventsException {
        Patient patient = (Patient) iPatient;
        Collection<CalendarEvent> events = null;

        events = (Collection<CalendarEvent>) (Collection<?>) patient.getCalendarEvents();

        Collection<ICalendarEvent> iEvents = new LinkedList<ICalendarEvent>();

        if (events != null) {
            for (CalendarEvent event : events) {
                iEvents.add(event);
            }
        }
        log.info("All calendar events have been acquired and added to the ICalendarEvent collection.");
        return iEvents;
    }

    /**
     * <h3>$getAllDoctorsAndOrthoptists</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method returns all available orthoptists and doctors in one collection. We get a list of all
     * orthoptists from the persistence layer and one list of all doctors, convert it into Interfaces and return it.
     */
    //Todo: add getAllDoctors, getAllOrthoptists
    public Collection<IUser> getAllDoctorsAndOrthoptists() throws BadConnectionException, CriticalClassException, CriticalDatabaseException {
        Collection<Orthoptist> orthoptists;
        Facade facade = Facade.getInstance();


        try {
            orthoptists = facade.getAll(Orthoptist.class);
        } catch (NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }
        log.info("All orthoptists have been acquired.");

        Collection<Doctor> doctors;

        try {
            doctors = facade.getAll(Doctor.class);
            log.info("All doctors have been acquired.");
        } catch (NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }

        Collection<IUser> iUsers = new LinkedList<>();

        if (orthoptists != null) {
            for (Orthoptist o : orthoptists) {
                iUsers.add(o);
            }
        }
        if (doctors != null) {
            for (Doctor doc : doctors) {
                iUsers.add(doc);
            }
        }

        log.info("All doctors and orthoptists have been added to the IUser collection.");
        return iUsers;
    }

    /**
     * <h3>$getQueueByUser</h3>
     * <p/>
     * <b>Description:</b>
     * <p/>
     * This method gets a user-id from the presentation-layer, fetches the correct queue from the given user
     * and returns an interface of the chosen queue.
     * <p/>
     * <b>Parameter</b>
     *
     * @param iUser this parameter shows the interface of the user, who's queue should be returned
     */
    public IPatientQueue getQueueByUser(IUser iUser) throws BadConnectionException, NoBrokerMappedException {
        User user = (User) iUser;
        Doctor doctor;
        Orthoptist orthoptist;
        PatientQueue queue = null;

        if (user != null) {
            if (user instanceof Doctor) {
                doctor = (Doctor) user;
                queue = (PatientQueue) doctor.getQueue();
            } else if (user instanceof Orthoptist) {
                orthoptist = (Orthoptist) user;
                queue = orthoptist.getQueue();
            }
        }
        return queue;
    }
}
