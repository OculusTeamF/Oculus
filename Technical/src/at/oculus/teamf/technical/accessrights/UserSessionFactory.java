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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import java.util.LinkedList;

/**
 *
 * @author Fabian Salzgeber
 * @date 6.4.2015
 * @version 1.0
 *
 */

public class UserSessionFactory implements ILogger {
    public static LinkedList<UserSubject> sessionList = new LinkedList<UserSubject>();

    private static void UserSessionFactory(){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        log.info("Security management intialized");
    }

    public UserSubject createUserSession(String userSessionName){
        UserSubject newUser = new UserSubject("sessionKey" + sessionList.size(), "sessionValue" + sessionList.size());
        return newUser;
    }


}
