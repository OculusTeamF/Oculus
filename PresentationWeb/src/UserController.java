/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.application.controller.LoginController;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.EmailNotFoundException;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.PasswordIncorrectException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;
import beans.UserBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Fabian on 31.05.2015.
 */
public class UserController extends HttpServlet implements ILogger {

    private LoginController _loginapp = new LoginController();
    private IPatient _loginpatient = null;

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
            _loginpatient  = _loginapp.checkLoginData(userEmail,userPassword);

        } catch (EmailNotFoundException e) {
            log.error("No user with this e-mail available. " + e.getMessage());
            request.getRequestDispatcher("errorPages/emailNotFound.jsp").forward(request, response);
        } catch (PasswordIncorrectException e) {
            log.error("Password is incorrect. " + e.getMessage());
            request.getRequestDispatcher("errorPages/passwordIncorrect.jsp").forward(request, response);
        }


        if (_loginpatient != null){
            UserBean user = new UserBean();
            user.loadUserPatient(_loginpatient);


            request.setAttribute("user", user);
            RequestDispatcher view = request.getRequestDispatcher("userprofile.jsp");
            view.forward(request, response);

            response.sendRedirect(response.encodeRedirectURL("userprofile.jsp"));
        } else {
            // user not found
            response.sendRedirect(response.encodeRedirectURL("login.jsp"));
        }
    }

    @Override
    public String getServletInfo() {
        return "login servlet";
    }
}
