/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamE.persistence.PersistenceExceptionTb2;
import at.oculus.teamE.persistence.api.IExaminationProtocolDaoTb2;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;

/**
 * Created by oculus on 18.05.15.
 */
public class ExaminationProtocolDaoAdapter implements IExaminationProtocolDaoTb2, ILogger{
    @Override
    public void saveOrUpdate(IExaminationProtocolTb2 iExaminationProtocolTb2) throws PersistenceExceptionTb2 {
        Facade facade = Facade.getInstance();
        try {
            facade.save((IExaminationProtocol)iExaminationProtocolTb2);
        } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
            log.error("Exception caught! " + e.getMessage());
            throw new PersistenceExceptionTb2();
        }
    }
}
