/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entity;

import java.util.Calendar;
import java.util.Date;

/**
 * WeekDayKey enum
 */
public enum WeekDayKey {
	// null because weekday starts at 1 = SUN
	NULL, SUN, MON, TUE, WED, THU, FRI, SAT;

	public static WeekDayKey getWeekDayKey(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		for(WeekDayKey w : WeekDayKey.values()){
			if(w.ordinal()==dayOfWeek){
				return w;
			}
		}

		return null;
	}
}