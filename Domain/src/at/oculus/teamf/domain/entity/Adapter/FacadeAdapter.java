/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.Adapter;

import at.oculus.teamE.persistence.PersistenceExceptionTb2;
import at.oculus.teamE.persistence.api.*;

import java.util.function.Function;

/**
 * Created by jpo2433 on 18.05.15.
 */
public class FacadeAdapter implements IPersistenceFacadeTb2 {

    @Override
    public <T> void loadLazyLoadedProperty(T t, Function<T, ?> function) throws PersistenceExceptionTb2 {
        //not relevant
    }

    @Override
    public IDiagnosisDaoTb2 getNewDiagnosisDao() {
        return new DiagnosisDaoAdapter();
    }

    @Override
    public IExaminationProtocolDaoTb2 getNewExaminationProtocolDao() {
        return new ExaminationProtocolDaoAdapter();
    }

    @Override
    public IMedicineDaoTb2 getNewMedicineDao() {
        return new MedicineDaoAdapter();
    }

    @Override
    public IExaminationDaoTb2 getNewExaminationDao() {
        return new ExaminationDaoAdapter();
    }

}
