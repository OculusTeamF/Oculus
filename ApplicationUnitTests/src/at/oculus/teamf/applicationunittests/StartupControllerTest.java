/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.controller.SearchPatientController;
import at.oculus.teamf.application.controller.StartupController;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.doctor.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.domain.entity.patient.IPatient;

import java.util.Collection;
import java.util.LinkedList;

public class StartupControllerTest {

    private StartupController startupController = new StartupController();

    @org.junit.Test
    public void testGetUser() throws Exception {
        IUser user = startupController.getUser();
        assert(user != null);
        assert(user instanceof Receptionist);
    }

    @org.junit.Test
    public void testGetAllQueues() throws Exception {
        Collection<IPatientQueue> queues = startupController.getAllQueues();
        assert(queues != null);
    }

    @org.junit.Test
    public void testGetAllDoctors() throws Exception {
        Collection <IDoctor> doctors = startupController.getAllDoctors();
        assert (doctors != null);
        assert (doctors.size() == 4);
    }

    @org.junit.Test
    public void testGetAllOrthoptists() throws Exception {
        Collection <IOrthoptist> orthoptists = startupController.getAllOrthoptists();
        assert (orthoptists != null);
        assert (orthoptists.size() == 2);
    }

    @org.junit.Test
    public void testGetAllCalendars() throws Exception {
        Collection <ICalendar> calendars = startupController.getAllCalendars();
        assert (calendars != null);
        assert (calendars.size() == 6);
    }

    @org.junit.Test
    public void testGetAllEntries() throws Exception {
        LinkedList<ICalendar> calendars = new LinkedList<>();
        calendars.addAll(startupController.getAllCalendars());
        Collection <ICalendarEvent> events= startupController.getAllEntries(calendars.getFirst());
        assert (events != null);
        System.out.println(events.size());
    }

    @org.junit.Test
    public void testGetEventsFromPatient() throws Exception {
        SearchPatientController searchPatientController = new SearchPatientController();
        LinkedList <IPatient> patients = (LinkedList<IPatient>) searchPatientController.searchPatients("Hanson");
        Collection <ICalendarEvent> events = startupController.getEventsFromPatient(patients.getFirst());
        assert (events != null);
    }

    @org.junit.Test
    public void testGetQueueByUser() throws Exception {
        LinkedList<IDoctor> docs = new LinkedList<>();
        docs.addAll(startupController.getAllDoctors());

        IUser user = docs.getFirst();

        IPatientQueue queue = startupController.getQueueByUser(user);

        System.out.println(user.getFirstName());
        assert (queue.getEntries().size() > 0);
    }

}