/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.ExaminationProtocol;
import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.exception.*;
import at.oculus.teamf.domain.entity.interfaces.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Simon Angerer on 28.05.2015.
 */
public class PatientProxy extends VirtualProxy<Patient> implements IPatient, ILogger {
    protected PatientProxy(Patient real) {
        super(real);
    }

    @Override
    public int getId() {
        return _real.getId();
    }

    @Override
    public void setId(int patientID) {
        _real.setId(patientID);
    }

    @Override
    public String getFirstName() {
        return _real.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        _real.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return _real.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
         _real.setFirstName(lastName);
    }

    @Override
    public Gender getGender() {
        return _real.getGender();
    }

    @Override
    public void setGender(Gender gender) {
        _real.setGender(gender);
    }

    @Override
    public String getSocialInsuranceNr() {
        return _real.getSocialInsuranceNr();
    }

    @Override
    public void setSocialInsuranceNr(String svn) {
        _real.setSocialInsuranceNr(svn);
    }

    @Override
    public IDoctor getIDoctor() {
        return _real.getIDoctor();
    }

    @Override
    public void setIDoctor(IDoctor idoctor) {
        _real.setDoctor(idoctor);
    }

    @Override
    public Collection<ICalendarEvent> getCalendarEvents() throws CouldNotGetCalendarEventsException {
        return _real.getCalendarEvents();
    }

    @Override
    public void setCalendarEvents(Collection<ICalendarEvent> calendarEvents) {
        _real.setCalendarEvents(calendarEvents);
    }

    @Override
    public Date getBirthDay() {
        return _real.getBirthDay();
    }

    @Override
    public void setBirthDay(Date birthDay) {
        _real.setBirthDay(birthDay);
    }

    @Override
    public String getStreet() {
        return _real.getStreet();
    }

    @Override
    public void setStreet(String street) {
        _real.setStreet(street);
    }

    @Override
    public String getPostalCode() {
        return _real.getPostalCode();
    }

    @Override
    public void setPostalCode(String postalCode) {
        _real.setPostalCode(postalCode);
    }

    @Override
    public String getCity() {
        return _real.getCity();
    }

    @Override
    public void setCity(String city) {
        _real.setCity(city);
    }

    @Override
    public String getCountryIsoCode() {
        return _real.getCountryIsoCode();
    }

    @Override
    public void setCountryIsoCode(String countryIsoCode) {
        _real.setCountryIsoCode(countryIsoCode);
    }

    @Override
    public String getPhone() {
        return _real.getPhone();
    }

    @Override
    public void setPhone(String phone) {
        _real.setPhone(phone);
    }

    @Override
    public String getEmail() {
        return _real.getEmail();
    }

    @Override
    public void setEmail(String email) {
        _real.setEmail(email);
    }

    @Override
    public String getAllergy() {
        return _real.getAllergy();
    }

    @Override
    public void setAllergy(String allergy) {
        _real.setAllergy(allergy);
    }

    @Override
    public String getChildhoodAilments() {
        return _real.getChildhoodAilments();
    }

    @Override
    public void setChildhoodAilments(String childhoodAilments) {
        _real.setChildhoodAilments(childhoodAilments);
    }

    @Override
    public String getMedicineIntolerance() {
        return _real.getMedicineIntolerance();
    }

    @Override
    public void setMedicineIntolerance(String medicineIntolerance) {
        _real.setMedicineIntolerance(medicineIntolerance);
    }

    @Override
    public Collection<IExaminationProtocol> getExaminationProtocol() throws CouldNotGetExaminationProtolException {
        if(_real.getExaminationProtocol() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, ExaminationProtocol.class);
            } catch (InvalidReloadClassException | ReloadInterfaceNotImplementedException | NoBrokerMappedException | BadConnectionException | DatabaseOperationException e) {
                log.error(e.getMessage());
                throw new CouldNotGetExaminationProtolException();
            }
        }

        return _real.getExaminationProtocol();
    }

    @Override
    public void setExaminationProtocol(Collection<IExaminationProtocol> protocols) {
        _real.setExaminationProtocol(protocols);
    }

    @Override
    public void addExaminationProtocol(IExaminationProtocol examinationProtocol) throws CouldNotAddExaminationProtocol {
        _real.addExaminationProtocol(examinationProtocol);
    }

    @Override
    public Collection<IDiagnosis> getDiagnoses() throws CouldNotGetDiagnoseException {
        return _real.getDiagnoses();
    }

    @Override
    public Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException {
        return _real.getMedicine();
    }

    @Override
    public Collection<IExaminationResult> getExaminationResults() throws CouldNotGetExaminationResultException {
        return _real.getExaminationResults();
    }

    @Override
    public Collection<IPrescription> getPrescriptions() throws CouldNotGetPrescriptionException {
        return _real.getPrescriptions();
    }

    @Override
    public Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException {
        return _real.getVisualAid();
    }
}
