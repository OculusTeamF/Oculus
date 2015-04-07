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
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "eventtype", schema = "", catalog = "oculus_f")
public class EventtypeEntity {
    private int eventTypeId;
    private String eventTypeName;
    private Integer estimatedTime;
    private String description;
    private Collection<CalendareventEntity> calendareventsByEventTypeId;

    @Id
    @Column(name = "eventTypeId", nullable = false, insertable = true, updatable = true)
    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    @Basic
    @Column(name = "eventTypeName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    @Basic
    @Column(name = "estimatedTime", nullable = true, insertable = true, updatable = true)
    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventtypeEntity that = (EventtypeEntity) o;

        if (eventTypeId != that.eventTypeId) return false;
        if (eventTypeName != null ? !eventTypeName.equals(that.eventTypeName) : that.eventTypeName != null)
            return false;
        if (estimatedTime != null ? !estimatedTime.equals(that.estimatedTime) : that.estimatedTime != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = eventTypeId;
        result = 31 * result + (eventTypeName != null ? eventTypeName.hashCode() : 0);
        result = 31 * result + (estimatedTime != null ? estimatedTime.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "eventtypeByEventTypeId")
    public Collection<CalendareventEntity> getCalendareventsByEventTypeId() {
        return calendareventsByEventTypeId;
    }

    public void setCalendareventsByEventTypeId(Collection<CalendareventEntity> calendareventsByEventTypeId) {
        this.calendareventsByEventTypeId = calendareventsByEventTypeId;
    }
}
