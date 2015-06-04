/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.criteria;

import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTime;
import at.oculus.teamf.domain.criteria.interfaces.IWeekDayTimeCriteria;
import at.oculus.teamf.domain.entity.calendar.calendarevent.ICalendarEvent;
import java.util.Collection;

/**
 * Created by Karo on 28.05.2015.
 */
public class WeekDayTimeCriteria implements IWeekDayTimeCriteria {
    private Collection<IWeekDayTime> _weekDayTimes;

    public WeekDayTimeCriteria(Collection<IWeekDayTime> _weekDayTimes) {
        this._weekDayTimes = _weekDayTimes;
    }

    @Override
    public boolean isValidEvent(ICalendarEvent event) {
        for(IWeekDayTime w : _weekDayTimes){
            if(w.isInTime(event)){
                return true;
            }
        }
        return false;
    }
}