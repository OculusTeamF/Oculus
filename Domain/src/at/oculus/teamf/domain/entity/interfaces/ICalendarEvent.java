/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.interfaces;

import java.util.Date;

/**
 * Created by oculus on 20.04.15.
 */
public interface ICalendarEvent extends IDomain {
    //<editor-fold desc="Getter/Setter">
    int getId();

    void setId(int id);

    String getDescription();

    void setDescription(String description);

    Date getEventStart();

    void setEventStart(Date eventStart);

    Date getEventEnd();

    void setEventEnd(Date eventEnd);

    //Patient getPatient();

    //void setPatient(Patient patient);
}
