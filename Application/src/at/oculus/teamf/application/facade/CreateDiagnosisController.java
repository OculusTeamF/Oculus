/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

/**
 * <h1>$CreateDiagnosisController.java</h1>
 *
 * @author $jpo2433
 * @author $sha9939
 * @since $30.04.2015
 * <p/>
 * <b>Description:</b>
 * This File contains the CreateDiagnosisController class,
 * which is responsible for the creation of a new diagnosis object, to save it into an examination protocol
 * and to save it into the database.
 */

import at.oculus.teamf.application.facade.exceptions.RequirementsUnfulfilledException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.Diagnosis;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.ExaminationResult;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationProtolException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationResultException;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h2>$CreateDiagnosisController</h2>
 *
 * <b>Description:</b>
 * With this controller a new diagnosis can be created.
 * All the information given in the method createDiagnosis is used to set up a new diagnosis object,
 * afterwards this object is saved to the specified examination protocol and into the database
 **/
public class CreateDiagnosisController implements ILogger {

    private ExaminationProtocol examinationProtocol;

    /**
     *<h3>$CreateDiagnosisController</h3>
     *
     * <b>Description:</b>
     *this is the constructor of the CreateDiagnosisController. To get an instance of this controller,
     * an examination protocol has to be set at the beginning (because a diagnosis depends on an examination
     * protocol)
     *
     *<b>Parameter</b>
     * @param iExaminationProtocol this parameter shows the interface of the examination protocol,
     *                             into which the new diagnosis should be saved
     */
    public CreateDiagnosisController(IExaminationProtocol iExaminationProtocol) {
        examinationProtocol = (ExaminationProtocol) iExaminationProtocol;
    }

    /**
     *<h3>$createDiagnosis</h3>
     *
     * <b>Description:</b>
     * this method first checks the parameters (because they are all required and can not be null or empty). If something is missing ,
     * a new RequirementsUnfulfilledException is thrown.
     * Afterwards a new diagnosis object with the incoming parameters is created, and saved into the given examination
     * protocol. Then the new created diagnosis and the examination protocol are saved into the database.
     * If no exception is thrown, the examination protocol and the diagnosis have been saved correct and
     * an interface of the created diagnosis is returned.
     *
     *<b>Parameter</b>
     * @param title this is the title of the diagnosis
     * @param description this is the description of the diagnosis
     * @param iDoctor this is the interface of the doctor, which created the diagnosis
     */
    public IDiagnosis createDiagnosis(String title, String description, IDoctor iDoctor) throws RequirementsUnfulfilledException, BadConnectionException, CriticalClassException, CriticalDatabaseException {

        //check parameters
        if (!checkRequirements(title, description, iDoctor)) {
            log.info("Requirememts unfulfilled");
            throw new RequirementsUnfulfilledException();
        }

        //create new diagnosis
        Diagnosis diagnosis = new Diagnosis(0, title, description, (Doctor) iDoctor);
        log.info("New diagnosis created.");
        examinationProtocol.setDiagnosis(diagnosis);
        log.info("Diagnosis added to examination protocol");

        //save diagnosis and examinationprotocol
        Facade facade = Facade.getInstance();
        try {
            facade.save(diagnosis); //braucht es das?
            facade.save(examinationProtocol);
            log.info("Diagnosis and examination protocol saved");
        } catch (NoBrokerMappedException e) {
            log.error("Major implementation error was found! " + e.getMessage());
            throw new CriticalClassException();
        } catch (DatabaseOperationException e) {
            log.error("Major database error was found! " + e.getMessage());
            throw new CriticalDatabaseException();
        }
        //return an interface of the new diagnosis
        return diagnosis;
    }

    /**
     *<h3>$checkRequirements</h3>
     *
     * <b>Description:</b>
     * this method checks the parameters, if they are null or not. All parameters are required, so if only one string
     * is empty or if the doctor is null, the method return false. If everything is okay it return true.
     *
     *<b>Parameter</b>
     * @param title this is the title of the diagnosis - it is not allowed to be empty
     * @param description this is the description of the diagnosis - it is not allowed to be empty
     * @param iDoctor this is the doctor, which created the diagnosis - it is not allowed to be null
     */
    private boolean checkRequirements(String title, String description, IDoctor iDoctor) {
        if (title.equals("")) {
            log.info("Title is empty");
            return false;
        } else if (description.equals("")) {
            log.info("Description is empty");
            return false;
        } else if (iDoctor == null) {
            log.info("Doctor is not set");
            return false;
        }
        return true;
    }

    /**
     *<h3>$getAllDiagnoses</h3>
     *
     * <b>Description:</b>
     * this method returns all diagnoses from the specified patient. with the interface of the patient, we get all examinationprotocols
     * which exist from this patient and afterwards we fetch all diagnoses out of the examinationprotocols and return them as a collection
     * of diagnoses interfaces
     *
     *<b>Parameter</b>
     * @param iPatient this is the patient, whos diagnoses should be returned
     */
    public Collection<IDiagnosis> getAllDiagnoses(IPatient iPatient) throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, CouldNotGetExaminationProtolException {

        Collection<IExaminationProtocol> protocols;
        try {
            protocols = iPatient.getExaminationProtocol();
        } catch (InvalidReloadClassException invalidReloadClassException) {
           log.warn("Invalid Reload Class Exception caught! Can not load Examinationprotocols");
            throw invalidReloadClassException;
        } catch (ReloadInterfaceNotImplementedException reloadInterfaceNotImplementedException) {
            log.warn("Reload Interface Not Implemented Exception caught! Can not load Examinationprotocols");
            throw reloadInterfaceNotImplementedException;
        } catch (BadConnectionException badConnectionException) {
            log.warn("Bad Connection Exception caught! Can not load Examinationprotocols");
            throw badConnectionException;
        } catch (NoBrokerMappedException noBrokerMappedException) {
            log.warn("No Broker Mapped Exception caught! Can not load Examinationprotocols");
            throw noBrokerMappedException;
        } catch (CouldNotGetExaminationProtolException couldNotGetExaminationProtocolException) {
            log.warn("Could Not Get Examinationprotocol Exception caught! Can not load Examinationprotocols");
            throw couldNotGetExaminationProtocolException;
        }

        Collection<IDiagnosis> diagnoses = new LinkedList<IDiagnosis>();
        for(IExaminationProtocol e : protocols){
            diagnoses.add(e.getDiagnosis());
        }
        return diagnoses;
    }

    /**
     *<h3>$getAllDiagnoses</h3>
     *
     * <b>Description:</b>
     * This method returns all examination results from the examination protocol attribute.
     */
    public Collection<IExaminationResult> getAllExaminationResults () throws CouldNotGetExaminationResultException {
        Collection <ExaminationResult> examinationResults = new LinkedList<>();
        try {
             examinationResults = examinationProtocol.getExaminationResults();
        } catch (CouldNotGetExaminationResultException couldNotGetExaminationResultException) {
            log.warn("Could Not Get Examination Results Exception caught! Can not load examination results");
            throw couldNotGetExaminationResultException;
        }
        Collection <IExaminationResult> iExaminationResults = new LinkedList<>();

        iExaminationResults.addAll((Collection<? extends IExaminationResult>) examinationResults);
        return iExaminationResults;
    }
}
