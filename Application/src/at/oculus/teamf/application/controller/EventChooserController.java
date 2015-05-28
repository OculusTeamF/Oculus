/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.technical.loggin.ILogger;

/**
 * <h1>$EventChooserController.java</h1>
 *
 * @author $jpo2433
 * @since $28.05.2015
 * <p/>
 * <b>Description:</b>
 * This File contains the EventChooserController class,
 * which is after the login of a patient on the webapplication, responsible for the management of choosing an event.
 */
public class EventChooserController implements ILogger {

    private IPatient iPatient;

    /**
     *<h3>$EventChooserController</h3>
     *
     * <b>Description:</b>
     *this is the constructor of the EventChooserController. To get an instance of this controller,
     * an interface of a patient has to be set at the beginning (because the patient has to be logged in)
     *
     *<b>Parameter</b>
     * @param patient this parameter shows the interface of the patient, who wants to choose an event
     */
    public EventChooserController(IPatient patient){
        iPatient = patient;
    }

    

}
