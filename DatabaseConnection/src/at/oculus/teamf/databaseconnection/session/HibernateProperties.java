/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnection.session;

import at.oculus.teamf.technical.properties.PropertiesHelper;

/**
 * HibernatePropertie helper to access properties from the config.properties file
 *
 * @author Simon Angerer
 * @date 30.03.2015
 * @version 1.0
 */
class HibernateProperties extends PropertiesHelper {

	/**
	 * Creates a new HibernateProperties object.
	 *
	 * @param configFile
	 * 		name of the ConfigFile
	 */
	public HibernateProperties(String configFile) {
		super(configFile);
	}

	/**
	 * Returns the URL property of the configFile
	 *
	 * @return the url string
	 */
	public String getURL() {
		return (String) _prop.get("hibernate.connection.url");
	}

	/**
	 * Returns the driver property of the configFile
	 *
	 * @return the driver string
	 */
	public String getDriver() {
		return (String) _prop.get("hibernate.connection.driver_class");
	}

	/**
	 * Returns the user property of the configFile
	 *
	 * @return the user string
	 */
	public String getUser() {
		return (String) _prop.get("hibernate.connection.username");
	}

	/**
	 * Returns the password property of the configFile
	 *
	 * @return the password string
	 */
	public String getPassword() {
		return (String) _prop.get("hibernate.connection.password");
	}
}
