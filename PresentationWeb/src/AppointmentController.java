/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.NoDoctorException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.NotAllowedToChooseEventException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;
import beans.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by KYUSS on 02.06.2015.
 */
@WebServlet(name = "AppointmentController")
public class AppointmentController extends HttpServlet implements ILogger{
    EventChooserController _eventchooserController;
    IPatient _currentp;
    LinkedList <ICalendarEvent> _checkedevents;
    private static final int DEFAULT_ENDTIME = 30;
    LocalTime[] times = new LocalTime[6];

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        _currentp = UserBean._patient;  // get current patient (= current logged in user) from user bean

        // format received dates
        log.debug("CHECK received appointments for " + _currentp.getLastName());

        String recdate = request.getParameter("checkdays");
        String rectimes = request.getParameter("checktimes");
        log.debug("CHECK received parameters: " + recdate + " / " + rectimes);

        String[] weekdayparts = recdate.split(",");
        String[] timeparts = rectimes.split(",");


        // fconvert string to LocalTime object
        for (int i = 0; i < weekdayparts.length; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            times[i] = LocalTime.parse(timeparts[i], formatter);
            log.debug("RECEIVED criteria time:" + times[i].toString());
        }

        // add criteras
        try {
            _eventchooserController = EventChooserController.createEventChooserController(_currentp);
            log.debug("EventController started");
            for (int i = 0; i < weekdayparts.length; i++) {
                log.debug("ADD EVENT CRITERIA #" + i + ": " + weekdayparts[i] + " / " + times[i] + " - " + times[i].plusMinutes(DEFAULT_ENDTIME));

                // add 30 minutes as default endtime
                _eventchooserController.addWeekDayTimeCriteria(weekdayparts[i], times[i], times[i].plusMinutes(DEFAULT_ENDTIME));

                // add not available daterange
                //_eventchooserController.addDatePeriodCriteria(lol,lol);
            }
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        }

        // now process criterias and dates and receive results
        try {
            log.debug("CHECK passed criterias");
            _checkedevents = (LinkedList<ICalendarEvent>) _eventchooserController.getAvailableEvents();
        } catch (NotAllowedToChooseEventException e) {
            e.printStackTrace();
        } catch (InvalidReloadClassException e) {
            e.printStackTrace();
        } catch (NoDoctorException e) {
            e.printStackTrace();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        }

        // send event results back to jsp (as xml)
        try (PrintWriter out = response.getWriter()) {
            String resl = "";
            log.debug("RESULTS SIZE: " + _checkedevents.size());
            // TODO change to XML
            //out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>hi</root>");
            for (ICalendarEvent e : _checkedevents) {
                log.debug("ACCEPTED EVENTDATE #:"  + e.toString());
                resl = resl + e.toString() + ",";

            }
            out.println(resl);

            log.debug("SEND response");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}
