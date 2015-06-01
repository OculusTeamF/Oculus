/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.EventChooserController;
import at.oculus.teamf.application.controller.LoginController;
import at.oculus.teamf.application.controller.exceptions.EventChooserControllerExceptions.PatientCanNotBeNullException;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.EmailNotFoundException;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.EmailValidationFailedException;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.PasswordIncorrectException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetCalendarEventsException;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;
import beans.UserBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Fabian on 31.05.2015.
 */
public class UserController extends HttpServlet implements ILogger {

    private LoginController _loginapp = new LoginController();
    private IPatient _loginpatient = null;
    private EventChooserController _eventChooserController;
    private ICalendarEvent _calendarEvent = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //empty
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("loginemail");
        String userPassword = request.getParameter("loginpw");

        // testdaten:
        // email: donald.duck@quack.eh
        // pw: letmein

        log.debug("RECEIVED LOGIN DATA email: [" + userEmail + "] pw: [" + userPassword + "]");

        try {
            _loginpatient = _loginapp.checkLoginData(userEmail, userPassword);
            _eventChooserController = EventChooserController.createEventChooserController(_loginpatient);
            LinkedList <ICalendarEvent> events = (LinkedList<ICalendarEvent>) _eventChooserController.checkPatientsAppointments();
            if (events.getFirst() != null){
                _calendarEvent = events.getFirst();
            }
        } catch (EmailNotFoundException e) {
            log.error("No user with this e-mail available. " + e.getMessage());
            request.getRequestDispatcher("errorPages/emailNotFound.jsp").forward(request, response);
        } catch (PasswordIncorrectException e) {
            log.error("Password is incorrect. " + e.getMessage());
            request.getRequestDispatcher("errorPages/passwordIncorrect.jsp").forward(request, response);
        } catch (EmailValidationFailedException e) {
            log.error("E-Mail validation failed. " + e.getMessage());
            request.getRequestDispatcher("errorPages/noVerifiedEmail.jsp").forward(request, response);
        } catch (InvalidSearchParameterException e) {
            e.printStackTrace();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (SearchInterfaceNotImplementedException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (PatientCanNotBeNullException e) {
            e.printStackTrace();
        } catch (CouldNotGetCalendarEventsException e) {
            //TODO
            e.printStackTrace();
        }

        if (_loginpatient != null){
            UserBean user = new UserBean();
            user.loadUserPatient(_loginpatient);

            if (_calendarEvent != null){
                user.loadCalendarEvent(_calendarEvent);
            }

            request.setAttribute("user", user);
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);

            response.sendRedirect(response.encodeRedirectURL("index.jsp"));
        } else {
            log.error("User not found.");
            request.getRequestDispatcher("errorPages/noVerifiedEmail.jsp").forward(request, response);
            response.sendRedirect(response.encodeRedirectURL("login.jsp"));
        }
    }

    @Override
    public String getServletInfo() {
        return "login servlet";
    }
}
