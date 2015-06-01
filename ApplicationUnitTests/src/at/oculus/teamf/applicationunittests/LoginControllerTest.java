/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.applicationunittests;

import at.oculus.teamf.application.controller.LoginController;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.EmailNotFoundException;
import at.oculus.teamf.application.controller.exceptions.LoginControllerExceptions.PasswordIncorrectException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import org.junit.Assert;

/**
 * Created by jpo2433 on 27.05.15.
 */
public class LoginControllerTest {

    private LoginController loginController;

    @org.junit.Before
    public void setUp() throws Exception {
        loginController = new LoginController();
    }

    /*
    @org.junit.Test
    public void validateEmail(){
        Assert.assertEquals(loginController.validateEmail("spitze.biene@hotmail.com"), true);
        Assert.assertEquals(loginController.validateEmail("blablabla"), false);
        Assert.assertEquals(loginController.validateEmail("test123@.com"), false);
        Assert.assertEquals(loginController.validateEmail("patient111@gmail.com"), true);
    }
    */

    @org.junit.Test
    public void checkLoginData(){

        IPatient result = null;
        IPatient patient = null;
        
        try {
            patient = Facade.getInstance().getById(IPatient.class, 1);
        } catch (BadConnectionException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (DatabaseOperationException e) {
            e.printStackTrace();
        }
        try {
            result = loginController.checkLoginData("donald.duck@quack.eh", "letmein");
        } catch (EmailNotFoundException e) {
            e.printStackTrace();
        } catch (PasswordIncorrectException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(patient, result);
        result = null;

        try {
            result = loginController.checkLoginData("donald.duck@quack.eh", "lemein");
        } catch (EmailNotFoundException e) {
            e.printStackTrace();
        } catch (PasswordIncorrectException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(null, result);

        result = null;

        try {
            result = loginController.checkLoginData("donald.duckquack.eh", "letmein");
        } catch (EmailNotFoundException e) {
            e.printStackTrace();
        } catch (PasswordIncorrectException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(null, result);
    }
}
