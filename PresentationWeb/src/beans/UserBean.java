/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package beans;

import at.oculus.teamf.domain.entity.patient.IPatient;

import javax.annotation.ManagedBean;

/**
 * Created by Fabian on 31.05.2015.
 */

@ManagedBean
public class UserBean {
    private IPatient _patient;
    private String firstName = null;
    private String lastName = null;
    private String svNumber = null;

    public UserBean() {
    }

    public void loadUserPatient(IPatient patient){
        _patient = patient;

        firstName = _patient.getFirstName();
        lastName = _patient.getLastName();
        svNumber = _patient.getSocialInsuranceNr();
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getSvNumber(){
        return svNumber;
    }
}
