/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.EventCanNotBeDeletedException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.EventCanNotBeNullException;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.technical.loggin.ILogger;
import beans.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Fabian on 13.06.2015.
 */

@WebServlet(name = "DeleteAppointmentController")
public class DeleteAppointmentController extends HttpServlet implements ILogger{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String loggedinkey = session.getAttribute("loggedin").toString();
        UserBean b = UserController.getCurrentUser(loggedinkey);
        log.debug("REDIRECT SERVLET: delete appointment for " + b.getLastName());

        try {
            System.out.println("DELETE APPOINTMENT CALLED");
            EventChooserController eventChooserController = EventChooserController.createEventChooserController(b._patient);
            eventChooserController.deleteExistingEvent(b._calendarEvent);
            b.erase();
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
            //TODO
        } catch (EventCanNotBeDeletedException e) {
            e.printStackTrace();
        } catch (EventCanNotBeNullException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }
}
