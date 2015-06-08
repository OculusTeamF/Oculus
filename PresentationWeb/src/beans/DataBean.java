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

import javax.annotation.ManagedBean;
import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by KYUSS on 02.06.2015.
 */
@ManagedBean
public class DataBean {
    private LinkedList <ICalendarEvent> loadedEvents;
    private LinkedList <ICalendarEvent> eventsToLoad;
    private static boolean [] buttons = new boolean [6];
    private static LocalTime [] localTimes = new LocalTime [6];

    public void loadAvailableAppointments (Collection <ICalendarEvent> events){
        loadedEvents = new LinkedList<>();
        loadedEvents.add((ICalendarEvent) events);
    }

    public LinkedList<ICalendarEvent> getLoadedEvents() {
        return loadedEvents;
    }

    public void addEvent (ICalendarEvent event){
        if (eventsToLoad == null){
            eventsToLoad = new LinkedList<>();
        }
        eventsToLoad.add(event);
    }

    public static void fillArrays (boolean [] filledButtons, LocalTime [] filledLocalTimes){
        buttons = filledButtons;
        localTimes = filledLocalTimes;
    }

    public LinkedList<ICalendarEvent> getEventsToLoad() {
        return eventsToLoad;
    }
}
