package at.oculus.teamf.presentation.view;/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * JFXtras Agenda
 *
 * @author Fabian Salzgeber
 * @date 14.4.2015
 *
 * Description: calendar control. Currently unused.
 *
 */


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class AgendaController implements Initializable{
    final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();

    @FXML private Agenda agendaID;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        // create and add calendar groups to agenda
        lAppointmentGroupMap.put("group00", new Agenda.AppointmentGroupImpl().withStyleClass("group0"));
        lAppointmentGroupMap.put("group01", new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
        lAppointmentGroupMap.put("group02", new Agenda.AppointmentGroupImpl().withStyleClass("group2"));
        lAppointmentGroupMap.put("group03", new Agenda.AppointmentGroupImpl().withStyleClass("group3"));
        lAppointmentGroupMap.put("group04", new Agenda.AppointmentGroupImpl().withStyleClass("group4"));

        for (String lId : lAppointmentGroupMap.keySet()) {
            Agenda.AppointmentGroup lAppointmentGroup = lAppointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            agendaID.appointmentGroups().add(lAppointmentGroup);
        }

        // accept new appointments
        agendaID.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>() {
            @Override
            public Agenda.Appointment call(Agenda.CalendarRange calendarRange) {
                return new Agenda.AppointmentImpl()
                        .withStartTime(calendarRange.getStartCalendar())
                        .withEndTime(calendarRange.getEndCalendar())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(lAppointmentGroupMap.get("group01"));
            }
        });

        // initial set date for calendar (current week)
        Calendar lFirstDayOfWeekCalendar = getFirstDayOfWeekCalendar(agendaID.getLocale(), agendaID.getDisplayedCalendar());
        int lFirstDayOfWeekYear = lFirstDayOfWeekCalendar.get(Calendar.YEAR);
        int lFirstDayOfWeekMonth = lFirstDayOfWeekCalendar.get(Calendar.MONTH);
        int FirstDayOfWeek = lFirstDayOfWeekCalendar.get(Calendar.DATE);

        // get current date
        LocalDateTime lToday2 = agendaID.getDisplayedLocalDateTime();
        Calendar lToday = agendaID.getDisplayedCalendar();
        int lTodayYear = lToday.get(Calendar.YEAR);
        int lTodayMonth = lToday.get(Calendar.MONTH);
        int lTodayDay = lToday.get(Calendar.DATE);

        // add test appointments to current dates
        agendaID.appointments().addAll(
                new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 00))
                        .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 00))
                        .withSummary("Doktor Tumor")
                        .withDescription("Max Mustermann")
                        .withAppointmentGroup(lAppointmentGroupMap.get("group01"))
                ,   new Agenda.AppointmentImpl()
                        .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay+1, 8, 30))
                        .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay+1, 10, 00))
                        .withSummary("Doktor Eiter")
                        .withDescription("Rainer Zufall")
                        .withAppointmentGroup(lAppointmentGroupMap.get("group01"))
        );

        // TODO setters: agenda behaviour setting
        agendaID.setAllowResize(true);      // resize appointments
        agendaID.setAllowDragging(true);    // drag appointments



        // TODO getters: getSelectedAppointment, getListofAppointments

    }

    static private Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c) {
        // result
        int lFirstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
        int lCurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int lDelta = 0;
        if (lFirstDayOfWeek <= lCurrentDayOfWeek) {
            lDelta = -lCurrentDayOfWeek + lFirstDayOfWeek;
        } else {
            lDelta = -lCurrentDayOfWeek - (7-lFirstDayOfWeek);
        }
        c = ((Calendar)c.clone());
        c.add(Calendar.DATE, lDelta);
        return c;
    }
}
