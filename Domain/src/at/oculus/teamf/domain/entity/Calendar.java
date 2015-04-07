/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.entity.CalendarEntity;
import at.oculus.teamf.persistence.entity.IEntity;

import java.util.Collection;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
@EntityClass("CalendarEntity.class")
public class Calendar implements DomainEntity {

    //<editor-fold desc="Attributes">
    private int _calendarID;
    private Collection<CalendarEvent> _events;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    public int getCalendarID() {
        return _calendarID;
    }

    public void setCalendarID(int calendarID) {
        _calendarID = calendarID;
    }

    public Collection<CalendarEvent> getEvents() {
        return _events;
    }

    public void setEvents(Collection<CalendarEvent> events) {
        _events =events;
    }

	@Override
	public int getId() {
		return _calendarID;
	}

	@Override
	public void set(IEntity entity) {
		CalendarEntity that = (CalendarEntity) entity;
		this.setCalendarID(that.getCalendarId());
	}
	//</editor-fold>
}
