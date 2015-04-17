/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Karo on 13.04.2015.
 */
public class TestClass {


    public Collection<MyPatient> searchPatients (String svn, String lastName, String firstName)
    {
        Collection<MyPatient> patients = new LinkedList<MyPatient>();
        MyPatient myPat = new MyPatient("Mustermann","Max",12345678);
        patients.add(myPat);

        return patients;
    }

}

class MyPatient {

    String fname;
    String lname;
    Integer svn;

    public MyPatient(String lname, String fname, Integer svn)
    {
        lname = lname;
        fname = fname;
        svn = svn;
    }
}