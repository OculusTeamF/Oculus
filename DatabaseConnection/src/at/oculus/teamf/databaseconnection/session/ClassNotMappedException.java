/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

/**
 * Is thrown when a class was not mapped.
 * <p/>
 *
 * @author Simon Angerer
 * @date 30.03.2015
 * @version 1.0
 */
public class ClassNotMappedException extends SessionException {

    private String _clazz;

    public ClassNotMappedException(String clazz) {
        _clazz = clazz;
    }

    @Override
    public String getMessage() {
        return "Class was not mapped: " + _clazz;
    }
}
