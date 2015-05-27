/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.controller;

import at.oculus.teamf.application.controller.dependenceResolverTB2.DependenceResolverTB2;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.controller.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetPrescriptionException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.exceptions.NoPrescriptionToPrintException;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <h1>$CreateDiagnosisController.java</h1>
 *
 * @author $jpo2433
 * @since $30.04.2015
 * <p/>
 * <b>Description:</b>
 * This File contains the PrescriptionController class,
 * which is responsible for the creation of a new Prescription object, to create new Prescription Entries,
 * put the entries into the new prescription object and save it into the database.
 * It also provides a class to get all prescribed medicine of the given patient and the possibility
 * to print the prescription.
 */

/**
 * <h2>$CreateDiagnosisController</h2>
 *
 * <b>Description:</b>
 * With this controller a new prescription can be created.
 * In the controller, a new Prescription is created and the specified patient is saved in the prescription
 * and in the controller. It contains te methods createPrescriptionEntry, getAllPrescribedMedicines and printPrescription.
 **/
public class PrescriptionController implements ILogger, IPrinter {

    private IPatient _iPatient;
    private IPrescription iPrescription;

    /**
     *<h3>$PrescriptionController</h3>
     *
     * <b>Description:</b>
     * this is the private constructor of the PrescriptionController. To get an instance of this controller,
     * the static method createController(IPatient iPatient) has to be invoked. This method checks if the given Patient
     * interface is not null, throws an exception if it is null or returns an instance of the PrescriptionController.
     *
     *<b>Parameter</b>
     * @param iPatient this parameter shows the interface of the Patient who wants a prescription
     */
    private PrescriptionController(IPatient iPatient) throws NotInitiatedExceptions {
        _iPatient = iPatient;

        iPrescription = DependenceResolverTB2.getInstance().getFactory().createPrescription();
        iPrescription.setPatient(iPatient);


    }

    /**
     *<h3>$PrescriptionController</h3>
     *
     * <b>Description:</b>
     * This is the static factory to be called when creating a PrescriptionController. It will then call the private
     * constructor.
     *
     *<b>Parameter</b>
     * @param iPatient this parameter shows the interface of the Patient who wants a prescription
     */
    public static PrescriptionController createController(IPatient iPatient) throws NoPatientException, NotInitiatedExceptions {
        if(iPatient == null){
            throw new NoPatientException();
        }
        return new PrescriptionController(iPatient);
    }

    /**
     *<h3>$createPrescriptionEntry</h3>
     *
     * <b>Description:</b>
     * This method created a new prescription entry from the given parameter iMedicine. It will then save the
     * prescription again to update it. Then it will save the entry and return it.
     *
     *<b>Parameter</b>
     * @param iMedicine the medicine to be added into the prescription entry
     */
    public IPrescriptionEntry createPrescriptionEntry(IMedicine iMedicine) throws CouldNotAddPrescriptionEntryException, DatabaseOperationException, NoBrokerMappedException, BadConnectionException, NotInitiatedExceptions {
        IPrescriptionEntry entry = DependenceResolverTB2.getInstance().getFactory().createPrescriptionEntry();

        IFacade facade = DependenceResolverTB2.getInstance().getFacade();
        facade.save(iPrescription);

        entry.setMedicine(iMedicine);
        facade.save(entry);
        try {
            iPrescription.addPrescriptionEntry(entry);
        } catch (CouldNotAddPrescriptionEntryException couldNotAddPrescriptionEntryException) {
            log.error("Could not add Entry to prescription! " + couldNotAddPrescriptionEntryException.getMessage());
            throw couldNotAddPrescriptionEntryException;
        }
        return entry;
    }

    /**
     *<h3>$createPrescriptionEntry</h3>
     *
     * <b>Description:</b>
     * This method creates a prescription entry by a given collection of medicines. It will create a prescription
     * entry for each medicine from the collection and then return the whole prescription. For each iteration
     * it will call the createPrescriptionEntry-method with the single medicine parameter.
     *
     *<b>Parameter</b>
     * @param medicines a list of medicines to be added to the prescription
     */
    public IPrescription createPrescriptionEntry(Collection<IMedicine> medicines) throws CouldNotAddPrescriptionEntryException, DatabaseOperationException, BadConnectionException, NoBrokerMappedException, NotInitiatedExceptions {
        for(IMedicine medicine : medicines){
            try {
                createPrescriptionEntry(medicine);
            } catch (CouldNotAddPrescriptionEntryException couldNotAddPrescriptionEntryException) {
                log.error("Could not add Entry to prescription! " + couldNotAddPrescriptionEntryException.getMessage());
                throw couldNotAddPrescriptionEntryException;
            }
        }

        return iPrescription;
    }

    /**
     *<h3>$getAllPrescribedMedicines</h3>
     *
     * <b>Description:</b>
     * This method will return all prescribed medicines in a collection.
     */
    public Collection<IMedicine> getAllPrescribedMedicines() throws CouldNotGetMedicineException {
        Collection<IMedicine> medicines = new LinkedList<IMedicine>();
        try {
            medicines = _iPatient.getMedicine();
        } catch (CouldNotGetMedicineException e) {
            log.error("Could not get Medicine! " + e.getMessage());
            throw e;
        }
        return medicines;
    }

    /**
     *<h3>$printPrescription</h3>
     *
     * <b>Description:</b>
     * This method will print the prescription via the IPrinter interface. It will also set the attribute last printed.
     */
    public IPrescription printPrescription()
		    throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException, COSVisitorException,
		           IOException, CantGetPresciptionEntriesException, NotInitiatedExceptions,
		           NoPrescriptionToPrintException {
        //IPrinter only has to be implemented in class head and then can be used with printer.METHOD
        try {
	        if(iPrescription.getId()==0){
                DependenceResolverTB2.getInstance().getFacade().save(iPrescription);
	        }
            printer.printPrescription(iPrescription, _iPatient.getIDoctor());
        } catch (COSVisitorException | IOException | CantGetPresciptionEntriesException | NoPrescriptionToPrintException e) {
            throw e;
        }

        iPrescription.setLastPrint(new Timestamp(new Date().getTime()));

        IFacade facade = DependenceResolverTB2.getInstance().getFacade();
        facade.save(iPrescription);

        return iPrescription;
    }

    /**
     *<h3>$PrescriptionController</h3>
     *
     * <b>Description:</b>
     * This method will fetch all prescriptions of a patient which have not yet been printed.
     *
     *<b>Parameter</b>
     * @param patient this parameter shows the interface of the Patient of which the collection will be created
     */
    public Collection<IPrescription> getNotPrintedPrescriptions(IPatient patient) throws CouldNotGetPrescriptionException {
        Collection<IPrescription> notPrinted = null;
        try {
            notPrinted = patient.getPrescriptions();
        } catch (CouldNotGetPrescriptionException e) {
            throw e;
        }

        Iterator<IPrescription> iter = notPrinted.iterator();

        while (iter.hasNext()) {
            IPrescription ipres = iter.next();

            if (ipres.getLastPrint() != null) {
                iter.remove();
            }
        }
        return notPrinted;
    }

    /**
     *<h3>$printPrescription</h3>
     *
     * <b>Description:</b>
     * This method will print the prescription via the IPrinter interface. It will also set the attribute last printed.
     */
    public void printPrescription (IPrescription iPrescription) throws COSVisitorException, CantGetPresciptionEntriesException, NoPrescriptionToPrintException, IOException {
        try {
            printer.printPrescription(iPrescription, iPrescription.getPatient().getIDoctor());
            iPrescription.setLastPrint(new Timestamp(new Date().getTime()));
        } catch (COSVisitorException | NoPrescriptionToPrintException | CantGetPresciptionEntriesException | IOException e) {
            log.error("Could not print Prescription! " + e.getMessage());
            throw  e;
        }
    }

}
