/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.interfaces;

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.QueueEntry;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * interface patient queue for presentation layer
 */
public interface IPatientQueue {

    Collection<QueueEntry> getEntries() throws NoBrokerMappedException, BadConnectionException, BadSessionException;

    void addPatient(Patient patient, Timestamp arrivaltime) throws NoBrokerMappedException, BadConnectionException, BadSessionException;

    void removePatient(Patient patient) throws NoBrokerMappedException, BadConnectionException, InvalidSearchParameterException, BadSessionException;
}
