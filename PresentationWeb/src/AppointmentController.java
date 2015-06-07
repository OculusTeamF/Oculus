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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    private static final int CRITERIA_AMOUNT = 7;
    private static final int DEFAULT_ENDTIME = 30;
    LocalDateTime[] dates = new LocalDateTime[CRITERIA_AMOUNT];

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        _currentp = UserBean._patient;  // get current patient (= current logged in user) from user bean

        // format received dates
        log.debug("CHECK received appointments for " + _currentp.getLastName());
        String param = "";
        String recdate = "";
        for (int i=0; i < CRITERIA_AMOUNT; i++) {
            param = "date" + i;
            recdate = request.getParameter(param);

            if (!recdate.equals("null")) {
                // convert string to LocalDateTime object
                // format for: Wed Jun 17 2015 00:00:00
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d yyyy HH:mm:ss", Locale.ENGLISH);
                dates[i] = LocalDateTime.parse(recdate.substring(0, 24), formatter);
                log.debug("RECEIVED criteria date:" + dates[i].toString());
            }
        }

        // add criteras
        try {
            _eventchooserController = EventChooserController.createEventChooserController(_currentp);
            log.debug("EventController started");
            for (int i = 0; i < CRITERIA_AMOUNT; i++) {
                if (dates[i] != null) {
                    DayOfWeek day = dates[i].getDayOfWeek();
                    String dayFormatted = day.toString().substring(0,3);    // cut dayname to 3 letter dayname (TUESDAY -> TUE)
                    log.debug("ADD EVENT CRITERIA #" + i + ": " + dayFormatted + " / " + dates[i].toLocalTime() + " - " + dates[i].toLocalTime().plusMinutes(DEFAULT_ENDTIME));

                    // add 30 minutes as default endtime
                    _eventchooserController.addWeekDayTimeCriteria(dayFormatted, dates[i].toLocalTime(), dates[i].toLocalTime().plusMinutes(DEFAULT_ENDTIME));
                }
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

        // send event results back to jsp
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        request.setAttribute("eventResults", _checkedevents);
        view.forward(request, response);

       /* try {
            _patient = (IPatient) request.getAttribute("user");
            _eventchooserController = EventChooserController.createEventChooserController(_patient);
            _events = new LinkedList<>();

            LinkedList <Date> appointmentList = new LinkedList<>();

            for (int i = 0; i < 10; ++i){
                if (request.getParameter("date" + String.valueOf(i)) != null){
                    appointmentList.add(Date.valueOf(request.getParameter("date" + String.valueOf(i))));
                }
            }

            LinkedList <IWeekDayTime> weekDayTimes = new LinkedList<>();

            //TODO while new statements add to collection
            //TODO implement correctly!
            LocalTime time = null;
            IWeekDayTime weekDayTime = (IWeekDayTime) _eventchooserController.addCriteria(new String(), time, time);
            weekDayTimes.add(weekDayTime);

            _eventchooserController.getAvailableEvents(Date.valueOf(request.getParameter("choose_date")), Date.valueOf(request.getParameter("choose_time")), weekDayTimes);
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
            //TODO exception handling
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

        if (_events.size() > 0){
            DataBean dataBean = new DataBean();
            dataBean.loadAvailableAppointments(_events);

            request.setAttribute("data", dataBean);
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);

            response.sendRedirect("index.jsp");
        }*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}
