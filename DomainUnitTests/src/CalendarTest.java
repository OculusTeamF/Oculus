/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.criteria.DatePeriodCriteria;
import at.oculus.teamf.domain.criteria.WeekDayTime;
import at.oculus.teamf.domain.criteria.WeekDayTimeCriteria;
import at.oculus.teamf.domain.criteria.interfaces.ICriteria;
import at.oculus.teamf.domain.criteria.interfaces.IDatePeriodCriteria;
import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTime;
import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTimeCriteria;
import at.oculus.teamf.domain.entity.interfaces.ICalendar;
import at.oculus.teamf.domain.entity.interfaces.ICalendarEvent;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.entity.WeekDayKey;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * CalendarTest.java Created by oculus on 01.06.15.
 */
public class CalendarTest {
    private ICalendar _calendar;
    private Collection<ICriteria> _criterias;
    private Date _from;
    private Date _to;
    private Collection<ICalendarEvent> _calendarEvents;

    @Before
    public void setUp() {
        try {
            _calendar = Facade.getInstance().getById(ICalendar.class, 1);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            e.printStackTrace();
            assertTrue(false);
        }

	    try {
		    if(_calendar.getEvents().size()<=0){
		        assertTrue(false);
		    }
	    } catch (InvalidReloadClassException | ReloadInterfaceNotImplementedException | NoBrokerMappedException | DatabaseOperationException | BadConnectionException e) {
		    e.printStackTrace();
		    assertTrue(false);
	    }

	    // date period
        _from = new Date();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(_from);
        calendar.add(Calendar.DAY_OF_MONTH,14);
        _to = calendar.getTime();
        IDatePeriodCriteria datePeriodCriteria = new DatePeriodCriteria(_from, _to);

	    _criterias = new LinkedList<>();
        _criterias.add(datePeriodCriteria);

        // work day time
        Collection<IWeekDayTime> weekDayTimes = new LinkedList<IWeekDayTime>();
        IWeekDayTime weekDayTimeMon = new WeekDayTime(WeekDayKey.MON, LocalTime.of(8,0), LocalTime.of(15,0));
        weekDayTimes.add(weekDayTimeMon);
        IWeekDayTime weekDayTimeTue = new WeekDayTime(WeekDayKey.TUE, LocalTime.of(16,0), LocalTime.of(19,0));
        weekDayTimes.add(weekDayTimeTue);
        IWeekDayTime weekDayTimeWed = new WeekDayTime(WeekDayKey.WED, LocalTime.of(6,0), LocalTime.of(8,0));
        weekDayTimes.add(weekDayTimeWed);
        IWeekDayTime weekDayTimeThu = new WeekDayTime(WeekDayKey.THU, LocalTime.of(11,0), LocalTime.of(13,0));
        weekDayTimes.add(weekDayTimeThu);
        IWeekDayTime weekDayTimeFri = new WeekDayTime(WeekDayKey.FRI, LocalTime.of(15,0), LocalTime.of(19,0));
        weekDayTimes.add(weekDayTimeFri);
        IWeekDayTime weekDayTimeSat = new WeekDayTime(WeekDayKey.SAT, LocalTime.of(5,0), LocalTime.of(12,0));
        weekDayTimes.add(weekDayTimeSat);
        IWeekDayTime weekDayTimeSun = new WeekDayTime(WeekDayKey.SUN, LocalTime.of(4,0), LocalTime.of(20,0));
        weekDayTimes.add(weekDayTimeSun);

        IWeekDayTimeCriteria weekDayTimeCriteria = new WeekDayTimeCriteria(weekDayTimes);

        _criterias.add(weekDayTimeCriteria);
    }

    @Test
    public void getCalendarEvents() {
	    Iterator<ICalendarEvent> eventIterator = null;

        try {
	        eventIterator = _calendar.availableEventsIterator(_criterias, 30);
        } catch (ReloadInterfaceNotImplementedException | InvalidReloadClassException | NoBrokerMappedException | BadConnectionException | DatabaseOperationException e) {
            e.printStackTrace();
	        assertTrue(false);
        }

	    if(eventIterator == null){
		    assertTrue(false);
	    }

	    _calendarEvents = new LinkedList<ICalendarEvent>();

	    for(int i = 0; i<100; i++){
		    ICalendarEvent c = eventIterator.next();
		    _calendarEvents.add(c);
	    }

	    for(ICalendarEvent c : _calendarEvents){
		    System.out.println(c);
	    }
    }
}
