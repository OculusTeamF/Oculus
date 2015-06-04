/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.exception.CantGetPresciptionEntriesException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.prescription.prescriptionentry.IPrescriptionEntry;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Simon Angerer on 04.06.2015.
 */
class PrescriptionProxy extends VirtualProxy<IPrescription> implements IPrescription{
    protected PrescriptionProxy(IPrescription real) {
        super(real);
    }

    @Override
    public IPatient getPatient() {
        return _real.getPatient();
    }

    @Override
    public void setPatient(IPatient iPatient) {
        _real.setPatient(iPatient);
    }

    @Override
    public void setLastPrint(Date lastPrint) {
        _real.setLastPrint(lastPrint);
    }

    @Override
    public Date getLastPrint() {
        return _real.getLastPrint();
    }

    @Override
    public Collection<IPrescriptionEntry> getPrescriptionEntries() throws CantGetPresciptionEntriesException {
        return _real.getPrescriptionEntries();
    }

    @Override
    public void addPrescriptionEntry(IPrescriptionEntry prescriptionEntry) throws CouldNotAddPrescriptionEntryException {
        _real.addPrescriptionEntry(prescriptionEntry);
    }

    @Override
    public void setIssueDate(Date issueDate) {
        _real.setIssueDate(issueDate);
    }

    @Override
    public Date getIssueDate() {
        return _real.getIssueDate();
    }

    @Override
    public void setPrescriptionEntries(Collection<IPrescriptionEntry> iPrescriptionEntries) {
        _real.setPrescriptionEntries(iPrescriptionEntries);
    }

    @Override
    public int getId() {
        return _real.getId();
    }

    @Override
    public void setId(int id) {
        _real.setId(id);
    }
}
