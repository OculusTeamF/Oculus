/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.factory.IFactoryTB2;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.domain.entity.patient.IPatient;

import java.util.Date;

/**
 * Created by Simon Angerer on 13.05.2015.
 */
public class FactoryTB2 implements IFactoryTB2 {

    @Override
    public IVisualAid createVisualAid() {
        return new VisualAid();
    }

    @Override
    public IDiagnosis createDiagnos(String title, String description, IDoctor doctor) {
        return new Diagnosis(title, description, doctor);
    }

    @Override
    public IPrescription createPrescription() {
        return new Prescription();
    }

    @Override
    public IPrescriptionEntry createPrescriptionEntry() {
        return new PrescriptionEntry();
    }

}
