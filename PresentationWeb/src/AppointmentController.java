/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.patient.IPatient;
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
import java.util.LinkedList;

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
        log.debug("CHECK received parameters: " + recdate);

        String[] weekdayparts = recdate.split(",");

/*        int datecount = 0;
        for (String datepart: parts) {
            // convert string to LocalDateTime object
            // format for: Wed Jun 17 2015 00:00:00
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d yyyy HH:mm:ss", Locale.ENGLISH);
            dates[datecount] = LocalDateTime.parse(datepart.substring(0, 24), formatter);
            log.debug("RECEIVED criteria date:" + dates[datecount].toString());
            datecount++;
        }*/

        System.out.println(weekdayparts[0]);
        System.out.println(weekdayparts.length);

        // for debug
        for (int i = 0; i < weekdayparts.length; i++) {
            times[i] = LocalTime.now();
        }

        // add criteras
        try {
            _eventchooserController = EventChooserController.createEventChooserController(_currentp);
            log.debug("EventController started");
            for (int i = 0; i < weekdayparts.length; i++) {
                /*if (dates[i] != null) {
                    DayOfWeek day = dates[i].getDayOfWeek();
                    String dayFormatted = day.toString().substring(0,3);    // cut dayname to 3 letter dayname (TUESDAY -> TUE)
                    log.debug("ADD EVENT CRITERIA #" + i + ": " + dayFormatted + " / " + dates[i].toLocalTime() + " - " + dates[i].toLocalTime().plusMinutes(DEFAULT_ENDTIME));

                    // add 30 minutes as default endtime
                    _eventchooserController.addWeekDayTimeCriteria(dayFormatted, dates[i].toLocalTime(), dates[i].toLocalTime().plusMinutes(DEFAULT_ENDTIME));
                }*/
                log.debug("ADD EVENT CRITERIA #" + i + ": " + weekdayparts[i] + " / " + times[i] + " - " + times[i].plusMinutes(DEFAULT_ENDTIME));

                // add 30 minutes as default endtime
                _eventchooserController.addWeekDayTimeCriteria(weekdayparts[i], times[i], times[i].plusMinutes(DEFAULT_ENDTIME));
            }
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        }

        // now process criterias and dates and receive results
        try {
            log.debug("CHECK passed criterias");
            _checkedevents = (LinkedList<ICalendarEvent>) _eventchooserController.checkPatientsAppointments();
        } catch (CouldNotGetCalendarEventsException e) {
            e.printStackTrace();
        }

        // debug results
        log.debug("RESULTS SIZE: " + _checkedevents.size());
        for (int i = 0; i < _checkedevents.size(); i++){
            log.debug("ACCEPTED EVENTDATE #" + i + ": " + _checkedevents.get(i).getEventStart().toString());
        }

        // send event results back to jsp (as xml)
        try (PrintWriter out = response.getWriter()) {
            String resl = "";

            // TODO change to XML
            //out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>hi</root>");
           /* for (LocalDateTime d : dates) {
                if (d!= null) {
                    resl = resl + d.toString() + ",";
                }
            }*/
            out.println(resl);
            log.debug("SEND response");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}
