/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import java.time.LocalTime;

/**
 * WorkingHours.java Created by oculus on 27.05.15.
 */
public class WorkingHours implements IWorkingHours {
	private int _id;
	private LocalTime _morningFrom;
	private LocalTime _morningTo;
	private LocalTime _afternoonFrom;
	private LocalTime _afternoonTo;

	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
	public LocalTime getMorningFrom() {
		return _morningFrom;
	}

	@Override
	public void setMorningFrom(LocalTime morningFrom) {
		_morningFrom = morningFrom;
	}

	@Override
	public LocalTime getMorningTo() {
		return _morningTo;
	}

	@Override
	public void setMorningTo(LocalTime morningTo) {
		_morningTo = morningTo;
	}

	@Override
	public LocalTime getAfternoonFrom() {
		return _afternoonFrom;
	}

	@Override
	public void setAfternoonFrom(LocalTime afternoonFrom) {
		_afternoonFrom = afternoonFrom;
	}

	@Override
	public LocalTime getAfternoonTo() {
		return _afternoonTo;
	}

	@Override
	public void setAfternoonTo(LocalTime afternoonTo) {
		_afternoonTo = afternoonTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorkingHours))
			return false;

		WorkingHours that = (WorkingHours) o;

		if (_id != that._id)
			return false;
		if (_afternoonFrom != null ? !_afternoonFrom.equals(that._afternoonFrom) : that._afternoonFrom != null)
			return false;
		if (_afternoonTo != null ? !_afternoonTo.equals(that._afternoonTo) : that._afternoonTo != null)
			return false;
		if (_morningFrom != null ? !_morningFrom.equals(that._morningFrom) : that._morningFrom != null)
			return false;
		if (_morningTo != null ? !_morningTo.equals(that._morningTo) : that._morningTo != null)
			return false;

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

	@Override
	public String toString(){
		String out = null;
		if(_morningFrom != null){
			out = "Morning from " + _morningFrom + " to " + _morningTo + " - ";
		} else {
			out = "Morning closed - ";
		}
		if(_afternoonFrom != null){
			out += "Afternoon from " + _afternoonFrom + " to " + _afternoonTo;
		} else {
			out += "Afternoon closed";
		}
		return out;
	}
}
