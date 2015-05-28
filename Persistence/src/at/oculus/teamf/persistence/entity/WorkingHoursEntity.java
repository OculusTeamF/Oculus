/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entity;

import javax.persistence.*;
import java.sql.Time;

/**
 * Hibernate annotated workinghours class
 */
@Entity
@Table(name = "workinghours", schema = "", catalog = "oculus_f")
public class WorkingHoursEntity implements IEntity {
    private int _id;
    private Time _morningFrom;
    private Time _morningTo;
    private Time _afternoonFrom;
    private Time _afternoonTo;

	public WorkingHoursEntity(){}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "workingHoursId", nullable = false, insertable = false, updatable = false)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    @Basic
    @Column(name = "morningFrom", nullable = true, insertable = true, updatable = true)
    public Time getMorningFrom() {
        return _morningFrom;
    }

    public void setMorningFrom(Time morningFrom) {
        _morningFrom = morningFrom;
    }

    @Basic
    @Column(name = "morningTo", nullable = true, insertable = true, updatable = true)
    public Time getMorningTo() {
        return _morningTo;
    }

    public void setMorningTo(Time morningTo) {
        _morningTo = morningTo;
    }

    @Basic
    @Column(name = "afternoonFrom", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonFrom() {
        return _afternoonFrom;
    }

    public void setAfternoonFrom(Time afternoonFrom) {
        _afternoonFrom = afternoonFrom;
    }

    @Basic
    @Column(name = "afternoonTo", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonTo() {
        return _afternoonTo;
    }

    public void setAfternoonTo(Time afternoonTo) {
        _afternoonTo = afternoonTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingHoursEntity that = (WorkingHoursEntity) o;

        if (_id != that._id) return false;
        if (_morningFrom != null ? !_morningFrom.equals(that._morningFrom) : that._morningFrom != null) return false;
        if (_morningTo != null ? !_morningTo.equals(that._morningTo) : that._morningTo != null) return false;
        if (_afternoonFrom != null ? !_afternoonFrom.equals(that._afternoonFrom) : that._afternoonFrom != null)
            return false;
        if (_afternoonTo != null ? !_afternoonTo.equals(that._afternoonTo) : that._afternoonTo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_morningFrom != null ? _morningFrom.hashCode() : 0);
        result = 31 * result + (_morningTo != null ? _morningTo.hashCode() : 0);
        result = 31 * result + (_afternoonFrom != null ? _afternoonFrom.hashCode() : 0);
        result = 31 * result + (_afternoonTo != null ? _afternoonTo.hashCode() : 0);
        return result;
    }
}
