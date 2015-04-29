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

/**
 * Hibernate annotated event type class
 */
@Entity
@Table(name = "eventtype", schema = "", catalog = "oculus_f")
public class EventtypeEntity implements IEntity {
	private int _id;
	private String _eventTypeName;
	private Integer _estimatedTime;
	private String _description;

	public EventtypeEntity() {
	}

	public EventtypeEntity(int id, String eventTypeName, Integer estimatedTime, String description) {
		_id = id;
		_eventTypeName = eventTypeName;
		_estimatedTime = estimatedTime;
		_description = description;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "eventTypeId", nullable = false, insertable = false, updatable = false)
	public int getId() {
		return _id;
	}

	public void setId(int eventTypeId) { _id = eventTypeId; }

	@Basic
	@Column(name = "eventTypeName", nullable = false, insertable = true, updatable = true, length = 50)
	public String getEventTypeName() {
		return _eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		_eventTypeName = eventTypeName;
	}

	@Basic
	@Column(name = "estimatedTime", nullable = true, insertable = true, updatable = true)
	public Integer getEstimatedTime() {
		return _estimatedTime;
	}

	public void setEstimatedTime(Integer estimatedTime) {
		_estimatedTime = estimatedTime;
	}

	@Basic
	@Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_eventTypeName != null ? _eventTypeName.hashCode() : 0);
		result = 31 * result + (_estimatedTime != null ? _estimatedTime.hashCode() : 0);
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EventtypeEntity that = (EventtypeEntity) o;

		if (_id != that._id)
			return false;
		if (_eventTypeName != null ? !_eventTypeName.equals(that._eventTypeName) : that._eventTypeName != null)
			return false;
		if (_estimatedTime != null ? !_estimatedTime.equals(that._estimatedTime) : that._estimatedTime != null)
			return false;
		if (_description != null ? !_description.equals(that._description) : that._description != null)
			return false;

		return true;
	}
}
