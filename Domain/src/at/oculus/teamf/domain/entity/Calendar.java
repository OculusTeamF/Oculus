/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exceptions.*;

import java.util.Collection;

/**
 * Todo: add docs, implement equals
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Calendar {

    //<editor-fold desc="Attributes">
    private int _id;
    private Collection<CalendarEvent> _events;
    private User _user;
    //</editor-fold>

    //<editor-fold desc="Getter/Setter">
    public int getCalendarID() {
        return _id;
    }

    public void setCalendarID(int calendarID) {
        _id = calendarID;
    }

    public Collection<CalendarEvent> getEvents() {
        Facade facade = Facade.getInstance();

        try {
            facade.reloadCollection(this, CalendarEvent.class);
        } catch (ReloadInterfaceNotImplementedException e) {
            e.printStackTrace();
            //Todo: Add Loging
        } catch (InvalideReloadParameterExeption invalideReloadParameterExeption) {
            invalideReloadParameterExeption.printStackTrace();
        } catch (NotAbleToLoadClassException e) {
            e.printStackTrace();
        } catch (NoBrokerMappedException e) {
            e.printStackTrace();
        } catch (FacadeException e) {
            e.printStackTrace();
        }

        return _events;
    }

    public void setEvents(Collection<CalendarEvent> events) {
        _events =events;
    }

    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }


    //</editor-fold>
}