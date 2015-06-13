/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package beans;

import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import at.oculus.teamf.domain.entity.patient.IPatient;

import javax.annotation.ManagedBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fabian on 31.05.2015.
 */

@ManagedBean
public class UserBean {
    public IPatient _patient;
    private String firstName = null;
    private String lastName = null;
    private String svNumber = null;
    private String doctor = null;

    public ICalendarEvent _calendarEvent;
    private String dateStart = null;
    private String dateEnd = null;
    private String description = null;
    private boolean appointAvailable = false;

    public UserBean() {
    }

    public void loadUserPatient(IPatient patient){
        _patient = patient;

        appointAvailable = false;
        firstName = _patient.getFirstName();
        lastName = _patient.getLastName();
        svNumber = _patient.getSocialInsuranceNr();
        doctor = _patient.getDoctor().toString();
    }

    public void loadCalendarEvent (ICalendarEvent calendarEvent){
        _calendarEvent = calendarEvent;
        dateStart = _calendarEvent.getEventStart().toString();
        dateEnd = _calendarEvent.getEventEnd().toString();
        description = _calendarEvent.getDescription();
        appointAvailable = true;
    }

    public void erase (){
        _calendarEvent = null;
        description = null;
        dateStart = null;
        dateEnd = null;
        appointAvailable = false;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getSvNumber(){
        return svNumber;
    }

    public boolean getAppointAvailable(){
        return appointAvailable;
    }

    public void setAppointAvailable(boolean available){
        appointAvailable = available;
    }

    public String getDescription() {
        return description;
    }

    public String getDateStart() {
        String finaldate = null;

        if (dateStart != null) {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("dd. MMMM yyyy");
            toFormat.setLenient(false);
            Date date = null;
            try {
                date = fromFormat.parse(dateStart.substring(0, 10));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finaldate = toFormat.format(date);
        }


        return finaldate;
    }

    public String getDateEnd() {
        String dateconv = null;
        if (dateStart != null) {
            dateconv = dateStart.substring(10, 16) + " - " + dateEnd.substring(10, 16);
        }
        return dateconv;
    }

    public String getDoctor() {
        return doctor;
    }
}
