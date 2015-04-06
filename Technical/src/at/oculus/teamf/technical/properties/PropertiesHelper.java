/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.technical.properties;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Norskan on 02.04.2015.
 * //TODO: add docs
 */
public class PropertiesHelper {

    protected Properties _prop;

    public PropertiesHelper(String fileName) {
        _prop = new Properties();
        String propFileName = fileName;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        try {
            if (inputStream != null) {
                _prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            //TODO: add log
            e.printStackTrace();
        }
    }

    public String getProperty(String propertie) {
        return _prop.getProperty(propertie);
    }
}
