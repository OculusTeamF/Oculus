/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.application.controller.exceptions.EmailNotFoundException;
import at.oculus.teamf.application.controller.exceptions.PasswordIncorrectException;
import at.oculus.teamf.domain.entity.interfaces.ILogin;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.accessrights.Login;
import at.oculus.teamf.technical.exceptions.HashGenerationException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private IPatient patient;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /**
     * <h3>$checkLoginData</h3>
     * <p/>
     * <b>Description:</b>
     * This method validates the login data of the patient. First, it fetches the correct patient with the email address,
     * afterwards the password is checked. If something fails, an exception is thrown.
     * <p/>
     * <b>Parameter</b>
     *
     * @param email this is the email address/the username of the patient who wants to log in
     * @param password this is the matching password of the patient who wants to log in
     */
    public IPatient checkLoginData(String email, String password) throws EmailNotFoundException, PasswordIncorrectException {

        if(email == null || email.equals("")){
            throw new EmailNotFoundException();
        }else{
            if(!validateEmail(email)){
                log.error("Email validation failed");
                throw new EmailNotFoundException();
            }

            IFacade facade = Facade.getInstance();
            try {
                for (Object p : facade.search(IPatient.class, email)){
                    patient = (IPatient) p;
                }
            } catch (SearchInterfaceNotImplementedException | BadConnectionException | NoBrokerMappedException | InvalidSearchParameterException | DatabaseOperationException e) {
                log.error("Facade Exception caught while searching patient - " + e.getMessage());
                throw new EmailNotFoundException();
            }
        }

        if(patient != null){
            log.info("Patient found! --> Going to check password.");
            if(password == null || password.equals("")){
                throw new PasswordIncorrectException();
            }else{
                try {
                    if(!Login.login((ILogin)patient, password)){
                        throw new PasswordIncorrectException();
                    }
                } catch (HashGenerationException e) {
                    log.error("HashGenerationException caught! Password could not be confirmed" + e.getMessage());
                    throw new PasswordIncorrectException();
                }
            }
        }else{
            return null;
        }

        log.info("Email address and password correct! Patient is allowed to log in the system.");
        return patient;
    }

    /**
     * <h3>$validateEmail</h3>
     * <p/>
     * <b>Description:</b>
     * This method validates the email address with regular expressions. If the email address matches the specified format,
     * the method returns the value true, else it returns false.
     * <p/>
     * <b>Parameter</b>
     *
     * @param email this is the email address which should be validated
     */
    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
