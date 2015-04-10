/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.Calendar;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.domain.entity.Receptionist;
import at.oculus.teamf.domain.entity.User;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.FacadeException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by jpo2433 on 08.04.15.
 */
public class StartupController {

    /* This method returns all available queues. We get a list of all queues from the persistence layer -
     and at the moment we only cast this object-list into a patientQueue-list and return it.
     Later on, we are going to choose and return only the queues, which the specified user is allowed to see. */

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

    public Collection<PatientQueue> getAllQueues(User user){
        Facade facade = Facade.getInstance();

        Collection<PatientQueue> queues = null;
        try {
            queues = facade.getAll(PatientQueue.class);
        } catch (FacadeException e) {
            e.printStackTrace();
            //TODO
        }

        return queues;
    }

    public Collection<Calendar> getAllCalenders(User user){
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
