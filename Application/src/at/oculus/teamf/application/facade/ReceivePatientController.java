/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

/**<h1>$ReceivePatientController.java</h1>
 * @author $jpo2433
 * @author $sha9939
 * @since $13.04.15
 *
 * Description:
 * This file contains all the methods which are necessary for the usecase ReceivePatientController. It also contains the class
 * ReceivePatientController.
 **/

package at.oculus.teamf.application.facade;

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * <h2>$ReceivePatientController</h2>
 *
 * <b>Description:</b>
 * This class contains all the necessary methods for the usecase ReceivePatient.
 **/
public class ReceivePatientController implements ILogger {

    /**
     *<h3>$createNewExaminationProtocol</h3>
     *
     * <b>Description:</b>
     * This method is being used to create a new examination protocol. The given information via parameters is given to
     * the new object and then added to the patient. The protocol is then given to the presentation layer as an
     * interface to review it.
     * <b>Parameter</b>
     * @param starttime start time of the examination
     * @param description description of the examination
     * @param iPatient the patient who is being examined
     * @param iDoctor the doctor who does the examination
     * @param iOrthoptist the orthoptist who does the examination
     */
    public IExaminationProtocol createNewExaminationProtocol(Date starttime, String description, IPatient iPatient, IDoctor iDoctor, IOrthoptist iOrthoptist) throws NoBrokerMappedException, BadConnectionException {
        Date endtime = new Timestamp(new Date().getTime());

        ExaminationProtocol examinationProtocol = new ExaminationProtocol(
                0, starttime, endtime, description, (Patient) iPatient, (Doctor) iDoctor, (Orthoptist) iOrthoptist, null
        );
        Patient patient = (Patient) iPatient;
        patient.addExaminationProtocol(examinationProtocol);

        log.info("New Examination Protocol has been created!");

        return examinationProtocol;
    }

    public Collection<IExaminationProtocol> getAllExaminationProtocols(IPatient iPatient) throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, NoBrokerMappedException, BadConnectionException {
        Patient patient = (Patient) iPatient;
        Collection<IExaminationProtocol> protocols = null;
        try {
            protocols = patient.getExaminationProtocol();
        } catch (InvalidReloadClassException invalidReloadClassException) {
            log.warn("FacadeException caught! Invalid reload class!");
            throw invalidReloadClassException;
        } catch (ReloadInterfaceNotImplementedException reloadInterfaceNotImplementedException) {
            log.warn("FacadeException caught! reload interface not implemented!");
            throw reloadInterfaceNotImplementedException;
        } catch (BadConnectionException badConnectionException) {
            log.warn("FacadeException caught! Bad Connection!");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("FacadeException caught! No broker mapped!");
            throw noBrokerMappedException;
        }

        Collection<IExaminationProtocol> examinationProtocols = new LinkedList<IExaminationProtocol>();
        if(protocols != null){
            for(IExaminationProtocol examinationProtocol : protocols){
                examinationProtocols.add(examinationProtocol);
            }
        }

        return examinationProtocols;
    }

    /**
     *<h3>$removePatientFromQueue</h3>
     *
     * <b>Description:</b>
     * This method should remove a patient from a given queue. The two parameters are interfaces of a
     * patient and of a queue. These interfaces were parsed into objects and afterwards the patient-object
     * gets removed from the queue-object. The patient is no longer available in the specified queue.
     *
     *<b>Parameter</b>
     * @param ipatient the interface of the patient which should be removed from the specified queue
     * @param iqueue the interface of the queue from which the specified patient should be removed
     */

    public void removePatientFromQueue(IPatient ipatient, IPatientQueue iqueue) throws NoBrokerMappedException, BadConnectionException, InvalidSearchParameterException {
        Patient patient = (Patient) ipatient;
        PatientQueue queue = (PatientQueue) iqueue;

        queue.removePatient(patient);
        log.info("Patient has been removed from queue.");
    }

 }
