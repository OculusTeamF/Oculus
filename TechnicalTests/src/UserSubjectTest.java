/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.technical.accessrights.UserSubject;
import at.oculus.teamf.technical.loggin.ILogger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 *
 * @author Fabian Salzgeber
 * @date 6.4.2015
 * @version 1.0
 *
 */

// SHIRO API TEST
public class UserSubjectTest implements ILogger {

    @Test
    public void UserSessionsRunTest(){
        //UserSessionFactory test = new UserSessionFactory();
        //UserSubject user = test.createUserSession("user1");

        //user.loginUser("simonangerer", "default");
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);


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
    }
}