/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;

import java.util.Date;

/**
 * VisualAid.java Created by oculus on 11.05.15.
 */
public class VisualAid implements IVisualAid {
	private int _id;
	private String _description;
	private Date _issueDate;
	private Date _lastPrint;
	private Diagnosis _diagnosis;

	public VisualAid() {
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public Date getIssueDate() {
		return _issueDate;
	}

	public void setIssueDate(Date issueDate) {
		_issueDate = issueDate;
	}

	public Date getLastPrint() {
		return _lastPrint;
	}

	public void setLastPrint(Date lastPrint) {
		_lastPrint = lastPrint;
	}

	public Diagnosis getDiagnosis() {
		return _diagnosis;
	}

	public void setDiagnosis(IDiagnosis diagnosis) {
		_diagnosis = (Diagnosis)diagnosis;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_issueDate != null ? _issueDate.hashCode() : 0);
		result = 31 * result + (_lastPrint != null ? _lastPrint.hashCode() : 0);
		result = 31 * result + (_diagnosis != null ? _diagnosis.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VisualAid))
			return false;

		VisualAid visualAid = (VisualAid) o;

		if (_id != visualAid._id)
			return false;
		if (_description != null ? !_description.equals(visualAid._description) : visualAid._description != null)
			return false;
		if (_diagnosis != null ? !_diagnosis.equals(visualAid._diagnosis) : visualAid._diagnosis != null)
			return false;
		if (_issueDate != null ? !_issueDate.equals(visualAid._issueDate) : visualAid._issueDate != null)
			return false;
		if (_lastPrint != null ? !_lastPrint.equals(visualAid._lastPrint) : visualAid._lastPrint != null)
			return false;

		return true;
	}
}
