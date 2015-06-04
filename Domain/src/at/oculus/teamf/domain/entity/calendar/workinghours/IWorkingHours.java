/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.calendar.workinghours;


import at.oculus.teamf.domain.entity.IDomain;

import java.time.LocalTime;

/**
 * Created by Simon Angerer on 28.05.2015.
 */
public interface IWorkingHours extends IDomain {
    @Override
    int getId();

    @Override
    void setId(int id);

    LocalTime getMorningFrom();

    void setMorningFrom(LocalTime morningFrom);

    LocalTime getMorningTo();

    void setMorningTo(LocalTime morningTo);

    LocalTime getAfternoonFrom();

    void setAfternoonFrom(LocalTime afternoonFrom);

    LocalTime getAfternoonTo();

    void setAfternoonTo(LocalTime afternoonTo);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
