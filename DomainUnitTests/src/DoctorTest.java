/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Simon Angerer on 07.06.2015.
 */
public class DoctorTest {

    IDoctor _doctor1;
    IDoctor _doctor2;

    @Before
    public void setUp() {
        try {
            _doctor1 = Facade.getInstance().getById(IDoctor.class, 1);
            _doctor2 = Facade.getInstance().getById(IDoctor.class, 1);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Before
    public void teardown() {

    }

    //checks if both doctores are the same without changing any of there properties
    @Test
    public void simpleEquals() {
        assertTrue(_doctor1.equals(_doctor2));
    }


}
