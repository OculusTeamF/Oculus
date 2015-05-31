/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package beans;

import at.oculus.teamf.domain.entity.interfaces.IPatient;

import javax.annotation.ManagedBean;

/**
 * Created by Fabian on 31.05.2015.
 */

@ManagedBean
public class UserBean implements java.io.Serializable {
    private IPatient patient;
    private String firstName = null;

    public UserBean() {
    }

    public void loadUserPatient(IPatient patient){
        this.patient = patient;
        firstName = patient.getFirstName();
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
}
