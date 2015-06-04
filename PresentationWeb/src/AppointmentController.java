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
import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTime;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.calendar.ICalendarEvent;
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
    IPatient _patient;
    LinkedList <ICalendarEvent> _events;
    LocalDateTime[] dates = new LocalDateTime[9];

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IPatient currentp = UserBean._patient;

        log.debug("CHECK received appointments for " + currentp.getLastName());
        String param = "";
        String recdate = "";
        for (int i=0; i<7; i++) {
            param = "date" + i;
            recdate = request.getParameter(param);

            if (!recdate.equals("null")) {
                // convert string to date
                // format f�r: Wed Jun 17 2015 00:00:00
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d yyyy HH:mm:ss", Locale.ENGLISH);
                dates[i] = LocalDateTime.parse(recdate.substring(0, 24), formatter);
                log.debug("received date:" + dates[i].toString());
            }
        }

        // TODO: send back available dates
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
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
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}