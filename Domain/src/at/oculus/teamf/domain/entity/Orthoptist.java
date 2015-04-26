/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IOrthoptist;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;

/**
 * domain orthoptist class
 */
public class Orthoptist extends User implements IDomain, IOrthoptist {
	//<editor-fold desc="Attributes">
	private int _id;
    private Calendar _calendar;
    private PatientQueue _queue;

    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    @Override
    public int getId() {
	    return _id;
    }

	@Override
    public void setId(int id) {
		_id = id;
	}

    @Override
    public Calendar getCalendar() {
	    return _calendar;
    }

    @Override
    public void setCalendar(Calendar calendar) {
        _calendar = calendar;
    }

    @Override
    public PatientQueue getQueue() throws NoBrokerMappedException, BadConnectionException {
        if(_queue == null) {
            _queue = new PatientQueue(this);
        }
        return _queue;
    }

    @Override
    public void setQueue(PatientQueue queue) {
        _queue = queue;
    }
    //</editor-fold>
}
