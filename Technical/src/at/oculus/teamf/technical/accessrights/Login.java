/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.technical.accessrights;

import at.oculus.teamf.technical.exceptions.HashGenerationException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PatientLogin.java
 * Created by oculus on 27.05.15.
 */
public class Login implements ILogger {
    public static boolean login(ILogin login, String password) throws HashGenerationException {
        log.info("Login User " + login.getUserName());
        String passwordHash = hashString(password, "sha-512");
        if(passwordHash.equals(login.getPasswordHash())){
            log.info("Login successful");
            return true;
        }
        log.info("Login failed");
        return false;
    }

    private static String hashString(String message, String algorithm)
            throws HashGenerationException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
            return convertByteArrayToHexString(hashedBytes);
        } catch (UnsupportedEncodingException ex) {
            throw new HashGenerationException();
        } catch (NoSuchAlgorithmException ex) {
            throw new HashGenerationException();
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString(
                    (arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
