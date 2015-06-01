/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

/**<h1>$VisualAidPrescriptionController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $30.04.2015
 *
 * <b>Description:</b>
 * This File contains the VisualAidPrescriptionController class,
 * which is responsible for the creation of a new optical aid prescription and to save it into the database
 **/

import at.oculus.teamf.application.controller.dependenceResolverTB2.DependenceResolverTB2;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.controller.exceptions.PrescriptionControllerExceptions.NoPatientException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetVisualAidException;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IVisualAid;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * <h2>$VisualAidPrescriptionController</h2>
 *
 * <b>Description:</b>
 * With this controller a new optical aid prescription can be created.
 **/
public class VisualAidController implements ILogger, IPrinter{

    private IVisualAid visualAid;

    /**
     *<h3>$VisualAidController</h3>
     *
     * <b>Description:</b>
     * private Constructor
     *
     *<b>Parameter</b>
     * @param iDiagnosis diagnosis to be created
     */
    private VisualAidController(IDiagnosis iDiagnosis) throws NotInitiatedExceptions {
            visualAid = DependenceResolverTB2.getInstance().getFactory().createVisualAid();
            visualAid.setDiagnosis(iDiagnosis);
    }

    /**
     *<h3>$VisualAidController</h3>
     *
     * <b>Description:</b>
     * This is the factory to create a new VisualAidController. It will call the private Constructor if the given
     * parameter is not null.
     *
     *<b>Parameter</b>
     * @param iDiagnosis diagnosis to be created
     */
    public static VisualAidController createController(IDiagnosis iDiagnosis) throws NoPatientException, NotInitiatedExceptions {
        if(iDiagnosis == null){
            throw new NoPatientException();
        }
        return new VisualAidController(iDiagnosis);
    }

    /**
     *<h3>$createVisualAidPrescription</h3>
     *
     * <b>Description:</b>
     * This method will create a new visual aid prescription and then save it.
     *
     *<b>Parameter</b>
     * @param description this is the description of the prescription (why the patient needs one)
     * @param dioptreLeft amount of dioptre that will be needed for the left eye
     * @param dioptreRight amount of dioptre that will be needed for the right eye
     */
    public IVisualAid createVisualAidPrescription(String description, Float dioptreLeft, Float dioptreRight) throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException, NotInitiatedExceptions {
        visualAid.setDescription(description);
        visualAid.setIssueDate(new Timestamp(new Date().getTime()));
        visualAid.setDioptreLeft(dioptreLeft);
        visualAid.setDioptreRight(dioptreRight);

        IFacade facade = DependenceResolverTB2.getInstance().getFacade();
        facade.save(visualAid);

        return visualAid;
    }

    /**
     *<h3>$getNotPrintedPrescriptions</h3>
     *
     * <b>Description:</b>
     * This method will create a Collection with all visual aid prescriptions of the patient that have not yet been
     * printed.
     *
     *<b>Parameter</b>
     * @param iPatient the patient from which the prescriptions will be fetched
     */
    public Collection<IVisualAid> getNotPrintedPrescriptions(IPatient iPatient) throws CouldNotGetVisualAidException {
        Collection<IVisualAid> notPrinted = null;
        try {
            notPrinted = iPatient.getVisualAid();
        } catch (CouldNotGetVisualAidException e) {
            throw e;
        }
        for(IVisualAid iVisualAid: notPrinted){
            if(iVisualAid.getLastPrint() == null){
                notPrinted.remove(iVisualAid);
            }
        }

        return notPrinted;
    }

}
