/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.technical.loggin.ILogger;

/**
 * <h1>$LoginController.java</h1>
 *
 * @author $jpo2433
 * @since $27.05.2015
 * <p/>
 * <b>Description:</b>
 * This File contains the LoginController class,
 * which is responsible for the login of a patient on the webapplication of timebox 3.
 */
public class LoginController implements ILogger{

    /**
     * <h3>$checkLoginData</h3>
     * <p/>
     * <b>Description:</b>
     * This method validates the login data of the patient. First, it fetches the correct patient with the email address,
     * afterwards the password is checked. If something fails, the correct exception is thrown. 
     * <p/>
     * <b>Parameter</b>
     *
     * @param email this is the email address/the username of the patient who wants to log in
     * @param password this is the matching password of the patient who wants to log in
     */
    public boolean checkLoginData(String email, String password){

        return true;
    }
}
