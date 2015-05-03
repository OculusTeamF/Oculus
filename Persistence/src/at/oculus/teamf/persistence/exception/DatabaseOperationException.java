/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.exception;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Simon Angerer on 02.05.2015.
 */
public class DatabaseOperationException extends FacadeException {

    public DatabaseOperationException(Exception e) {

        LinkedList<StackTraceElement> newStacktrace = new LinkedList<>();

        //add of capselt exception
        for(StackTraceElement ste : e.getStackTrace()) {
            newStacktrace.addFirst(ste);
        }

    }

}