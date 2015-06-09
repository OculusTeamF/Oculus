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
import beans.DataBean;
import beans.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by KYUSS on 02.06.2015.
 */
@WebServlet(name = "AppointmentController")
public class AppointmentController extends HttpServlet implements ILogger{
    EventChooserController _eventchooserController;
    IPatient _currentp;
    private LinkedList <ICalendarEvent> _checkedevents;
    private static final int DEFAULT_ENDTIME = 30;
    LocalTime[] times = new LocalTime[6];
    LocalTime[] timesend = new LocalTime[6];

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        _currentp = UserBean._patient;  // get current patient (= current logged in user) from user bean

        log.debug("CHECK received appointments for " + _currentp.getLastName());

        // get parameters
        String recdate = request.getParameter("checkdays");
        String rectimes = request.getParameter("checktimes");
        String drstart = request.getParameter("daterangestart");
        String drend = request.getParameter("daterangeend");

        log.debug("CHECK received parameters [dates]: " + recdate + " / " + rectimes);
        log.debug("CHECK received parameters [range]: " + drstart + " / " + drend);

        String[] weekdayparts = recdate.split(",");
        String[] timeparts = rectimes.split(",");


        try {
            _eventchooserController = EventChooserController.createEventChooserController(_currentp);
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        }

        // add criteras if available
        if (recdate.toString().length() > 0) {
            // convert string to LocalTime object
            int j = 0;
            for (int i = 0; i < (weekdayparts.length * 2); i++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                times[j] = LocalTime.parse(timeparts[i], formatter);
                timesend[j] = LocalTime.parse(timeparts[i + 1], formatter);
                log.debug("RECEIVED criteria time: start: " + times[j].toString() + " / end: " + timesend[j].toString());
                i++; // iterate two steps
                j++;

            }

            log.debug("EventController started");
            for (int i = 0; i < weekdayparts.length; i++) {
                log.debug("ADD EVENT CRITERIA #" + i + ": " + weekdayparts[i] + " / " + times[i] + " - " + timesend[i]);
                _eventchooserController.addWeekDayTimeCriteria(weekdayparts[i], times[i], timesend[i]);
            }
        }

        // add 'not available' dateranges
        if (drstart.equals("null") == false) {
            // convert string to localdate ...then convert localdate to date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d yyyy HH:mm:ss", Locale.ENGLISH);
            LocalDate localdstart = LocalDate.parse(drstart.substring(0, 24), formatter);
            LocalDate localdend = LocalDate.parse(drend.substring(0, 24), formatter);
            Date dateAStart = Date.from(localdstart.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateAEnd = Date.from(localdend.atStartOfDay(ZoneId.systemDefault()).toInstant());
            _eventchooserController.addDatePeriodCriteria(dateAStart, dateAEnd);
            log.debug("ADD not available daterange: " + dateAStart.toString() + " - " + dateAEnd.toString());
        }

        // now process criterias and dates and receive results
        try {
            log.debug("CHECK passed criterias");
            _checkedevents = (LinkedList<ICalendarEvent>) _eventchooserController.getAvailableEvents();
            DataBean.loadedEvents = _checkedevents;
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
            log.debug("RECEIVED EVENT RESULTS SIZE: " + _checkedevents.size());

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
