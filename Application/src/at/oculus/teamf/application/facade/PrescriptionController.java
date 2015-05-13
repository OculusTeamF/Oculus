/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade;

import at.oculus.teamf.application.facade.exceptions.NoPatientException;
import at.oculus.teamf.domain.entity.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.Prescription;
import at.oculus.teamf.domain.entity.PrescriptionEntry;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.IFacade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;
import at.oculus.teamf.technical.printing.IPrinter;
import org.apache.pdfbox.exceptions.COSVisitorException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by jpo2433 on 08.05.15.
 */
public class PrescriptionController implements ILogger, IPrinter {

    private IPatient _iPatient;
    private IPrescription iPrescription;


    private PrescriptionController(IPatient iPatient){
        _iPatient = iPatient;
        iPrescription = new Prescription();
        iPrescription.setPatient(iPatient);

    }

    public static PrescriptionController createController(IPatient iPatient) throws NoPatientException {
        if(iPatient == null){
            throw new NoPatientException();
        }
        return new PrescriptionController(iPatient);
    }

    public IPrescriptionEntry createPrescriptionEntry(IMedicine iMedicine) throws CouldNotAddPrescriptionEntryException, DatabaseOperationException, NoBrokerMappedException, BadConnectionException {
        IPrescriptionEntry entry = new PrescriptionEntry();

        IFacade facade = Facade.getInstance();
        facade.save(entry);

        entry.setMedicine(iMedicine);
        try {
            iPrescription.addPrescriptionEntry(entry);
        } catch (CouldNotAddPrescriptionEntryException couldNotAddPrescriptionEntryException) {
            log.error("Could not add Entry to prescription! " + couldNotAddPrescriptionEntryException.getMessage());
            throw couldNotAddPrescriptionEntryException;
        }
        return entry;
    }

    public IPrescription createPrescriptionEntry(Collection<IMedicine> medicines) throws CouldNotAddPrescriptionEntryException, DatabaseOperationException, BadConnectionException, NoBrokerMappedException {
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

    public Collection<IMedicine> getAllPrescribedMedicines(){
        Collection<IMedicine> medicines = new LinkedList<IMedicine>();
        try {
            medicines = _iPatient.getMedicine();
        } catch (CouldNotGetMedicineException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public IPrescription printPrescription() throws DatabaseOperationException, NoBrokerMappedException, BadConnectionException, COSVisitorException, IOException, CantGetPresciptionEntriesException {
        //IPrinter only has to be implemented in class head and then can be used with printer.METHOD
        try {
            printer.printPrescription(iPrescription, _iPatient.getIDoctor());
        } catch (COSVisitorException | IOException | CantGetPresciptionEntriesException e) {
            throw e;
        }

        iPrescription.setLastPrint(new Timestamp(new Date().getTime()));

        IFacade facade = Facade.getInstance();
        facade.save(iPrescription);

        return iPrescription;
    }

}
