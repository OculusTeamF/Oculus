/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entities;

import javax.persistence.*;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "weekday", schema = "", catalog = "oculus_f")
public class WeekdayEntity {
    private String weekDayKey;
    private String name;

    @Id
    @Column(name = "weekDayKey", nullable = false, insertable = true, updatable = true, length = 3)
    public String getWeekDayKey() {
        return weekDayKey;
    }

    public void setWeekDayKey(String weekDayKey) {
        this.weekDayKey = weekDayKey;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeekdayEntity that = (WeekdayEntity) o;

        if (weekDayKey != null ? !weekDayKey.equals(that.weekDayKey) : that.weekDayKey != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = weekDayKey != null ? weekDayKey.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
