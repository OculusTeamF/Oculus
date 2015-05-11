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
import at.oculus.teamf.domain.entity.Prescription;
import at.oculus.teamf.domain.entity.PrescriptionEntry;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.exception.CouldNotGetMedicineException;
import at.oculus.teamf.domain.entity.interfaces.IMedicine;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by jpo2433 on 08.05.15.
 */
public class PrescriptionController implements ILogger {

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

    public IPrescriptionEntry createPrescriptionEntry(IMedicine iMedicine) throws CouldNotAddPrescriptionEntryException {
        IPrescriptionEntry entry = new PrescriptionEntry();
        entry.setMedicine(iMedicine);
        try {
            iPrescription.addPrescriptionEntry(entry);
        } catch (CouldNotAddPrescriptionEntryException couldNotAddPrescriptionEntryException) {
            log.error("Could nor add Entry to prescription! " + couldNotAddPrescriptionEntryException.getMessage());
            throw couldNotAddPrescriptionEntryException;
        }
        return entry;
    }

    public IPrescription createPrescriptionEntry(Collection<IMedicine> medicines) throws CouldNotAddPrescriptionEntryException {
        for(IMedicine medicine : medicines){
            try {
                createPrescriptionEntry(medicine);
            } catch (CouldNotAddPrescriptionEntryException couldNotAddPrescriptionEntryException) {
                log.error("Could nor add Entry to prescription! " + couldNotAddPrescriptionEntryException.getMessage());
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

    public IPrescription printPrescription(){
        //TODO
        return iPrescription;
    }

}
