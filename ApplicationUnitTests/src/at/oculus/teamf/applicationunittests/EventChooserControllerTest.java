/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.patient.Patient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import org.junit.Assert;

import java.util.Collection;

/**
 * Created by jpo2433 on 28.05.15.
 */
public class EventChooserControllerTest {

    private EventChooserController eventChooserController;
    private IPatient iPatient;
    @org.junit.Before
    public void setUp() throws Exception {
        //TODO implement SetUp()
        try {
            iPatient = Facade.getInstance().getById(Patient.class, 1);
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }
        System.out.println(iPatient);

        try {
            eventChooserController = EventChooserController.createEventChooserController(iPatient);
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        }
    }

    @org.junit.After
    public void tearDown() throws Exception{
        //TODO implement tearDown()
    }

    @org.junit.Test
    public void deleteExistingEvent(){
        //TODO implement deleteExistingEvent()
    }

    @org.junit.Test
    public void getAvailableEvents(){
        //TODO implement getAvailableEvents()
    }

    @org.junit.Test
    public void saveChosenEvent(){
        //TODO implement saveChosenEvent()
    }

    @org.junit.Test
    public void checkPatientsAppointments(){
        Collection<ICalendarEvent> events = null;
        try {
            events =  eventChooserController.checkPatientsAppointments();
        } catch (CouldNotGetCalendarEventsException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(events.size(), 1);
    }

}
