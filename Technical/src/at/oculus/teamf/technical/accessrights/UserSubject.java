/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.technical.accessrights;

import at.oculus.teamf.technical.loggin.ILogger;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Fabian Salzgeber
 * @date 6.4.2015
 * @version 1.0
 *
 */

public class UserSubject implements ILogger {
    private Subject currentUser;     // user security-object

    public UserSubject(String sessionKey, String Password){
        currentUser = new Subject.Builder().buildSubject();

        // session handling for user
        Session session = currentUser.getSession();
        session.setAttribute(sessionKey, Password);

        log.info("New user session management intialized");
    }

    public void loginUser(String username, String password){
        // let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);

            log.info("Trying to login user [" + username + "]");

            // check if username and password are correct and login
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                //logger.info("There is no user with username of " + token.getPrincipal());
                log.warn("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.warn("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.warn("The account for username " + token.getPrincipal() + " is locked. Please contact your administrator to unlock it.");
            }
            // ... catch more exception here
            catch (AuthenticationException ae) {
                //unexpected conditions ?
            }
        }

        // check if login was succesfull
        if (currentUser.isAuthenticated()) {
            log.info("Login [" + currentUser.getPrincipal() + "] successfull");
        } else {
            log.warn("Login failed");
        }
    }

    public void logoutUser(){
        log.info("Logout ["  + currentUser.getPrincipal() + "] successfull");
        currentUser.logout();
    }

    public boolean checkPermission(String permission){
        if (currentUser.isPermitted(permission)) {
            log.info("User [" + currentUser.getPrincipal() + "] is permitted for " + permission);
        } else {
            log.info("User [" + currentUser.getPrincipal() + "] is not permitted for " + permission);
        }

        return currentUser.isPermitted(permission);
    }

    public boolean checkRole(String role){
        if (currentUser.hasRole(role)) {
            log.info("User [" + currentUser.getPrincipal() + "] has role for " + role);
        } else {
            log.info("User [" + currentUser.getPrincipal() + "] is no role for " + role);
        }

        return currentUser.hasRole(role);
    }

    public boolean isLoggedIn(){
        if (currentUser.isAuthenticated()) {
            log.info("User ["  + currentUser.getPrincipal() + "] is logged in");
        } else {
            log.info("No user in this session logged in");
        }
        return currentUser.isAuthenticated();
    }
}