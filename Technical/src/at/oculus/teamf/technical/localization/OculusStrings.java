package at.oculus.teamf.technical.localization;

/* Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Fabian Salzgeber
 * @date 17.4.2015
 * @version 1.0
 *
 */

public class OculusStrings {

    public static ResourceBundle getInstance() {
        ResourceBundle bundle = ResourceBundle.getBundle("OculusStrings", Locale.US);

        return bundle;
    }

}