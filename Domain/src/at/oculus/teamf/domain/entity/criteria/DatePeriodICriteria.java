/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.criteria;

import at.oculus.teamf.domain.entity.CalendarEvent;

import java.util.Date;

/**
 * Created by Karo on 28.05.2015.
 */
public class DatePeriodICriteria implements ICriteria {
    private Date _from;
    private Date _to;

    public DatePeriodICriteria(Date _from, Date _to) {
        this._from = _from;
        this._to = _to;
    }

    @Override
    public boolean isValidEvent(CalendarEvent event) {
        return false;
    }
}
