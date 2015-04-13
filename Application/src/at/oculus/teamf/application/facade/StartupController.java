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
 * objects the program needs when started to begin the Usecases.
 * At the moment this file contains a method to get a user, a method to get all Queues and a method to get all
 * Calendars.
 **/
package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$StartupController</h2>
 *
 * <b>Description:</b>
 * This class
 **/
public class StartupController {

    /**
     *<h3>$getUser</h3>
     *
     * <b>Description:</b>
     * This method returns a user (at the moment a receptionist) to try and test the first Usecase (where only a
     * receptionist ist needed.
     *
     **/

    public User getUser (){
        Facade facade = Facade.getInstance();

        User user = null;
        try {
            user = facade.getById(Receptionist.class, 1);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

        return user;
    }

    /**
     *<h3>$getAllQueues</h3>
     *
     * <b>Description:</b>
     * This method returns all available queues. We get a list of all queues from the persistence layer and return it.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     *
     **/

    public Collection<PatientQueue> getAllQueues() {

        Collection <Doctor> doctors = null;
        Facade facade = Facade.getInstance();

        try {
            doctors = facade.getAll(Doctor.class);
        } catch (FacadeException e) {
            //TODO
        }
        Collection<PatientQueue> queues = new LinkedList<PatientQueue>();

        for (Doctor doctor : doctors){
            queues.add(doctor.getQueue());
        }

        return queues;
    }

    /**
     *<h3>$getAllCalendars</h3>
     *
     * <b>Description:</b>
     * This method returns all available calendars. We get a list of all calendars from the persistence layer
     * and return it.
     * Later on, we are going to choose and return only the queues which the specified user is allowed to see.
     *
     *<b>Parameter</b>
     * @param user description
     **/

    public Collection<Calendar> getAllCalendars(User user){
        Facade facade = Facade.getInstance();

        Collection<Calendar> calendars = null;
        try {
            calendars = facade.getAll(Calendar.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

        return calendars;
    }


}
