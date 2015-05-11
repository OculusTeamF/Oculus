/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.domain.entity.exception.CouldNotAddPrescriptionEntryException;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPrescription;
import at.oculus.teamf.domain.entity.interfaces.IPrescriptionEntry;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Simon Angerer on 08.05.2015.
 */
public class Prescription implements IPrescription, ILogger {
	private int _id;
	private Date _issueDate;
	private Date _lastPrint;
	private Patient _patient;
	private Collection<PrescriptionEntry> _prescriptionEntries;

    public Prescription(IPatient iPatient){
        _patient = (Patient) iPatient;
        _issueDate = new Timestamp(new Date().getTime());
        _prescriptionEntries = new LinkedList<PrescriptionEntry>();
    }

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public Date getIssueDate() {
		return _issueDate;
	}

	public void setIssueDate(Date issueDate) {
		_issueDate = issueDate;
	}

	public Date getLastPrint() {
		return _lastPrint;
	}

	public void setLastPrint(Date lastPrint) {
		_lastPrint = lastPrint;
	}

	public IPatient getPatient() {
		return _patient;
	}

	public void setPatient(Patient patient) {
		_patient = patient;
	}

	public Collection<IPrescriptionEntry> getPrescriptionEntries() {
		return (Collection<IPrescriptionEntry>) (Collection<?>) _prescriptionEntries;
	}

	public void setPrescriptionEntries(Collection<PrescriptionEntry> prescriptionEntries) {
		_prescriptionEntries = prescriptionEntries;
	}

	/**
	 * add a entry to the prescription
	 *
	 * @param prescriptionEntry
	 * 		entry to add
	 *
	 * @throws CouldNotAddPrescriptionEntryException
	 */
	public void addPrescriptionEntry(IPrescriptionEntry prescriptionEntry)
			throws CouldNotAddPrescriptionEntryException {
		PrescriptionEntry entry = (PrescriptionEntry) prescriptionEntry;
		entry.setPrescription(this);
		_prescriptionEntries.add(entry);
		try {
			Facade.getInstance().save(prescriptionEntry);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			log.error(e.getMessage());
			throw new CouldNotAddPrescriptionEntryException();
		}
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_issueDate != null ? _issueDate.hashCode() : 0);
		result = 31 * result + (_lastPrint != null ? _lastPrint.hashCode() : 0);
		result = 31 * result + (_patient != null ? _patient.hashCode() : 0);
		result = 31 * result + (_prescriptionEntries != null ? _prescriptionEntries.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Prescription))
			return false;

		Prescription that = (Prescription) o;

		System.out.println(_issueDate.getTime() - that._issueDate.getTime());
		System.out.println(_lastPrint.getTime() - that._lastPrint.getTime());

		if (_id != that._id)
			return false;
		if (_issueDate != null ? !((_issueDate.getTime() - that._issueDate.getTime()) > -1000 &&
		                           (_issueDate.getTime() - that._issueDate.getTime()) < 1000) : that._issueDate != null)
			return false;
		if (_lastPrint != null ? !((_lastPrint.getTime() - that._lastPrint.getTime()) > -1000 &&
		                           (_lastPrint.getTime() - that._lastPrint.getTime()) < 1000) : that._lastPrint != null)
			return false;
		if (_patient != null ? !_patient.equals(that._patient) : that._patient != null)
			return false;
		if (_prescriptionEntries != null ? !_prescriptionEntries.equals(that._prescriptionEntries) :
		    that._prescriptionEntries != null)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return _issueDate.toString();
	}
}
