package at.oculus.teamf.technical.accessrights;

import at.oculus.teamf.technical.loggin.ILogger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

/**
 * Created by Fabian on 06.04.2015.
 */

// SHIRO API TEST
public class shiro_test implements ILogger {

    public static void main(String[] args) {
        // should be somewhere else later (init only)
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        logger.info("Security management intialized");


        //********************************************************************************************
        // TESTS
        //********************************************************************************************

        // session handling
        UserSubject user1 = new UserSubject("someSessionKey1", "someSessionValue1");
        UserSubject user2 = new UserSubject("someSessionKey2", "someSessionValue2");

        // login tests
        user1.loginUser("simonangerer", "default");
        user2.loginUser("fabiansalzgeber", "123");

        //get roles, permissions
        user1.checkPermission("adminstuff");
        user1.checkRole("doctor");
        user1.isLoggedIn();

        //all done - log out!
        user1.logoutUser();
        user2.logoutUser();

        //empty session
        user1.isLoggedIn();
        System.exit(0);
    }
}