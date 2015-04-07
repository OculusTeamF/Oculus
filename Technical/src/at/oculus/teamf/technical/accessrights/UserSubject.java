package at.oculus.teamf.technical.accessrights;

import at.oculus.teamf.technical.loggin.ILogger;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;


public class UserSubject implements ILogger {
    private Subject currentUser;     // user security-object

    public UserSubject(String sessionKey, String Password){
        currentUser = new Subject.Builder().buildSubject();

        // session handling for user
        Session session = currentUser.getSession();
        session.setAttribute(sessionKey, Password);

        logger.info("User session management intialized");
    }

    public void loginUser(String username, String password){
        // let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);

            logger.info("Trying to login user [" + username + "]");

            // check if username and password are correct and login
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                //logger.info("There is no user with username of " + token.getPrincipal());
                logger.warn("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                logger.warn("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                logger.warn("The account for username " + token.getPrincipal() + " is locked. Please contact your administrator to unlock it.");
            }
            // ... catch more exceptions here
            catch (AuthenticationException ae) {
                //unexpected conditions ?
            }
        }

        // check if login was succesfull
        if (currentUser.isAuthenticated()) {
            logger.info("Login ["  + currentUser.getPrincipal() + "] successfull");
        } else {
            logger.info("Login failed");
        }
    }

    public void logoutUser(){
        logger.info("Logout ["  + currentUser.getPrincipal() + "] successfull");
        currentUser.logout();
    }

    public boolean checkPermission(String permission){
        if (currentUser.isPermitted(permission)) {
            logger.info("User [" + currentUser.getPrincipal() + "] is permitted for " + permission);
        } else {
            logger.info("User [" + currentUser.getPrincipal() + "] is not permitted for " + permission);
        }

        return currentUser.isPermitted(permission);
    }

    public boolean checkRole(String role){
        if (currentUser.hasRole(role)) {
            logger.info("User [" + currentUser.getPrincipal() + "] has role for " + role);
        } else {
            logger.info("User [" + currentUser.getPrincipal() + "] is no role for " + role);
        }

        return currentUser.hasRole(role);
    }

    public boolean isLoggedIn(){
        if (currentUser.isAuthenticated()) {
            logger.info("User ["  + currentUser.getPrincipal() + "] is logged in");
        } else {
            logger.info("No user in this session logged in");
        }
        return currentUser.isAuthenticated();
    }
}