/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

/**
 * Created by Norskan on 03.04.2015.
 */
public abstract class EventType {
	//<editor-fold desc="Attributes">
	private int _id;
	private String _eventTypeName;
	private int _estimatedTime;
	private String _description;
	//</editor-fold>

	public EventType() {
	}

	public EventType(int id, String eventTypeName, int estimatedTime, String description) {
		_id = id;
		_eventTypeName = eventTypeName;
		_estimatedTime = estimatedTime;
		_description = description;
	}

	//<editor-fold desc="Getter/Setter">
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public String getEventTypeName() {
		return _eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		_eventTypeName = eventTypeName;
	}

	public int getEstimatedTime() {
		return _estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		_estimatedTime = estimatedTime;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}
	//</editor-fold>
}