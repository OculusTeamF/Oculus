/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

/**<h1>$VisualAidPrescriptionController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $30.04.2015
 *
 * <b>Description:</b>
 * This File contains the VisualAidPrescriptionController class,
 * which is responsible for the creation of a new optical aid prescription and to save it into the database
 **/

import at.oculus.teamf.application.facade.dependenceResolverTB2.DependenceResolverTB2;
import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitatedExceptions;
import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.VisualAid;
import at.oculus.teamf.domain.entity.interfaces.IDiagnosis;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <h2>$VisualAidPrescriptionController</h2>
 *
 * <b>Description:</b>
 * With this controller a new optical aid prescription can be created.
 **/
public class VisualAidPrescriptionController implements ILogger, IPrinter{

    private IVisualAid visualAid;

    private VisualAidPrescriptionController(IDiagnosis iDiagnosis) throws NotInitatedExceptions {
            visualAid = DependenceResolverTB2.getInstance().getFactory().createVisualAid();
            visualAid.setDiagnosis(iDiagnosis);
    }

    public static VisualAidPrescriptionController createController(IDiagnosis iDiagnosis) throws NoPatientException, NotInitatedExceptions {
        if(iDiagnosis == null){
            throw new NoPatientException();
        }
        return new VisualAidPrescriptionController(iDiagnosis);
    }

    public IVisualAid createVisualAidPrescription(String description) throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException {
        visualAid.setDescription(description);
        visualAid.setIssueDate(new Timestamp(new Date().getTime()));

        IFacade facade = Facade.getInstance();
        facade.save(visualAid);

        return visualAid;
    }

}
