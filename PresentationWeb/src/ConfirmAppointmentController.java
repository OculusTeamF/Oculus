/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.EventChooserControllerException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;
import beans.DataBean;
import beans.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Created by Fabian on 09.06.2015.
 */

@WebServlet(name = "ConfirmAppointmentController")
public class ConfirmAppointmentController extends HttpServlet implements ILogger {
    private LinkedList<ICalendarEvent> _events;
    private IPatient _currentp;
    private EventChooserController _eventchooserController;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eve = request.getParameter("selevent");
        String reason = request.getParameter("reason");

        HttpSession session = request.getSession(false);
        String loggedinkey = session.getAttribute("loggedin").toString();
        UserBean b = UserController.getCurrentUser(loggedinkey);

        _currentp = b._patient;


        // get loaded eventslist
        _events = DataBean.loadedEvents;
        int id = Integer.parseInt(eve);
        log.debug("SELECTED EVENT TO CONFIRM: " + _events.get(id - 1).getEventStart() + " for " + b.getLastName());

        // save selected event
        try {
            _eventchooserController = EventChooserController.createEventChooserController(_currentp);
            _eventchooserController.saveChosenEvent(_events.get(id - 1),reason);
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        } catch (EventChooserControllerException e) {
            e.printStackTrace();
        }

        // send event results back to jsp (as xml)
        try (PrintWriter out = response.getWriter()) {
            String resl = "done";
            out.println(resl);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}
