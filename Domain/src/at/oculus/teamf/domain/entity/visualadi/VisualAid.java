/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.visualadi;

import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;

import java.util.Date;

/**
 * VisualAid.java Created by oculus on 11.05.15.
 */
public class VisualAid implements IVisualAid {
	private int _id;
	private String _description;
	private Date _issueDate;
	private Date _lastPrint;
	private Float _dioptreLeft;
	private Float _dioptreRight;
	private IDiagnosis _diagnosis;

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

	public Float getDioptreLeft() {
		return _dioptreLeft;
	}

	public void setDioptreLeft(Float dioptreLeft) {
		_dioptreLeft = dioptreLeft;
	}

	public Float getDioptreRight() {
		return _dioptreRight;
	}

	public void setDioptreRight(Float dioptreRight) {
		_dioptreRight = dioptreRight;
	}

	public IDiagnosis getDiagnosis() {
		return _diagnosis;
	}

	public void setDiagnosis(IDiagnosis diagnosis) {
		_diagnosis = diagnosis;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof IVisualAid))
			return false;

		IVisualAid visualAid = (VisualAid) o;

		if (_id != visualAid.getId())
			return false;/*
		if (_description != null ? !_description.equals(visualAid._description) : visualAid._description != null)
			return false;
		if (_diagnosis != null ? !_diagnosis.equals(visualAid._diagnosis) : visualAid._diagnosis != null)
			return false;
		if (_dioptreLeft != null ? !_dioptreLeft.equals(visualAid._dioptreLeft) : visualAid._dioptreLeft != null)
			return false;
		if (_dioptreRight != null ? !_dioptreRight.equals(visualAid._dioptreRight) : visualAid._dioptreRight != null)
			return false;
        if (_issueDate != null ? !((_issueDate.getTime() - visualAid._issueDate.getTime()) > -1000 &&
                (_issueDate.getTime() - visualAid._issueDate.getTime()) < 1000) : visualAid._issueDate != null)
            return false;
        if (_lastPrint != null ? !((_lastPrint.getTime() - visualAid._lastPrint.getTime()) > -1000 &&
                (_lastPrint.getTime() - visualAid._lastPrint.getTime()) < 1000) : visualAid._lastPrint != null)
            return false;*/

		return true;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_description != null ? _description.hashCode() : 0);
		result = 31 * result + (_issueDate != null ? _issueDate.hashCode() : 0);
		result = 31 * result + (_lastPrint != null ? _lastPrint.hashCode() : 0);
		result = 31 * result + (_dioptreLeft != null ? _dioptreLeft.hashCode() : 0);
		result = 31 * result + (_dioptreRight != null ? _dioptreRight.hashCode() : 0);
		result = 31 * result + (_diagnosis != null ? _diagnosis.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
        String visualAid = _issueDate.toString();
        if(_lastPrint!=null){
            visualAid += " last printed on " + _lastPrint.toString();
        } else {
            visualAid += " not printed";
        }
		return  visualAid;
	}
}
