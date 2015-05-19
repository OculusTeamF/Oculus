/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.adapter;

import at.oculus.teamE.domain.interfaces.*;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.ExaminationResult;
import at.oculus.teamf.domain.entity.Medicine;

import java.time.LocalDate;

/**
 * Created by jpo2433 on 18.05.15.
 */
public class FactoryAdapter implements IDomainFactory {

    @Override
    public IMedicineTb2 newMedicine(IDiagnosisTb2 iDiagnosisTb2, String s, String s1, LocalDate localDate, LocalDate localDate1) {
        return new Medicine(iDiagnosisTb2, s, s1, localDate, localDate1);
    }

    @Override
    public IExaminationProtocolTb2 newExaminationProtocol(IPatientTb2 iPatientTb2, IUserTb2 iUserTb2) {
        return new ExaminationProtocol(iPatientTb2, iUserTb2);
    }

    @Override
    public IExaminationTb2 newExamination(IUserTb2 iUserTb2, IExaminationProtocolTb2 iExaminationProtocolTb2) {
        return new ExaminationResult(iUserTb2, iExaminationProtocolTb2);
    }
}
