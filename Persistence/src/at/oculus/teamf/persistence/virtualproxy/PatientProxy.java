/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamE.domain.readonly.IRPatientTb2;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.calendar.CalendarEvent;
import at.oculus.teamf.domain.entity.calendar.ICalendarEvent;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.examination.ExaminationResult;
import at.oculus.teamf.domain.entity.examination.IExaminationProtocol;
import at.oculus.teamf.domain.entity.examination.IExaminationResult;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.medicine.Medicine;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.prescription.Prescription;
import at.oculus.teamf.domain.entity.user.Gender;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.*;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.domain.entity.visualadi.VisualAid;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.accessrights.ILogin;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Simon Angerer on 28.05.2015.
 */
public class PatientProxy extends VirtualProxy<IPatient> implements IPatient, ILogger, IRPatientTb2, ILogin {
    protected PatientProxy(IPatient real) {
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
    public LocalDate getBirthDate() {
        return null;
    }

    @Override
    public void setLastName(String lastName) {
         _real.setLastName(lastName);
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
    public Integer getPatientId() {
        return _real.getId();
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
        if(_real.getCalendarEvents() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, CalendarEvent.class);
            } catch (BadConnectionException | NoBrokerMappedException | InvalidReloadClassException | ReloadInterfaceNotImplementedException  | DatabaseOperationException e) {
                log.error(e.getMessage());
                throw new CouldNotGetCalendarEventsException();
            }
        }

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
    public String getPhoneNumber() {
        return _real.getPhone();
    }

    @Override
    public String getEmailAddress() {
        return _real.getEmail();
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
        System.out.println(_real.getExaminationProtocol());
        return _real.getExaminationProtocol();
    }

    @Override
    public void setExaminationProtocol(Collection<IExaminationProtocol> protocols) {
        _real.setExaminationProtocol(protocols);
    }

    @Override
    public void addExaminationProtocol(IExaminationProtocol examinationProtocol) throws CouldNotAddExaminationProtocol {
        _real.addExaminationProtocol(examinationProtocol);

        try {
            Facade.getInstance().save(examinationProtocol);
        } catch (DatabaseOperationException | BadConnectionException | NoBrokerMappedException e) {
            log.error(e.getMessage());
            throw new CouldNotAddExaminationProtocol();
        }
    }

    @Override
    public Collection<IDiagnosis> getDiagnoses() throws CouldNotGetDiagnoseException {
        Collection<IDiagnosis> diagnoses = null;
        if(_real.getDiagnoses() == null) {
            try {
                diagnoses = Facade.getInstance().search(IDiagnosis.class, this.getId() + "");
            } catch (DatabaseOperationException | SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | NoBrokerMappedException e) {
                log.error(e.getMessage());
                throw new CouldNotGetDiagnoseException();
            }
        }

        return diagnoses;
    }

    @Override
    public Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException {
        Collection<IMedicine> medicine;

        try {
            medicine = Facade.getInstance().search(Medicine.class, this.getId() + "");
        } catch (SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | DatabaseOperationException | NoBrokerMappedException e) {
            log.error(e.getMessage());
            throw new CouldNotGetMedicineException();
        }

        return medicine;
    }

    @Override
    public Collection<IExaminationResult> getExaminationResults() throws CouldNotGetExaminationResultException {
        if(_real.getExaminationResults() == null) {
            try {
                _real.setExaminationResults(Facade.getInstance().search(ExaminationResult.class, this.getId() + ""));
            } catch (SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | DatabaseOperationException | NoBrokerMappedException e) {
                log.error(e.getMessage());
                throw new CouldNotGetExaminationResultException();
            }
        }

        return _real.getExaminationResults();
    }

    @Override
    public void setExaminationResults(Collection<IExaminationResult> results) {
        _real.setExaminationResults(results);
    }

    @Override
    public Collection<IPrescription> getPrescriptions() throws CouldNotGetPrescriptionException {
        if(_real.getPrescriptions() == null) {
            try {
                Facade.getInstance().reloadCollection(_real, Prescription.class);
            } catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException | InvalidReloadClassException | ReloadInterfaceNotImplementedException e) {
                log.error(e.getMessage());
                throw new CouldNotGetPrescriptionException();
            }
        }

        return _real.getPrescriptions();
    }

    @Override
    public Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException {
        if(_real.getVisualAid() == null) {
            try {
                 _real.setVisualAid(Facade.getInstance().search(VisualAid.class, "1"));
            } catch (NoBrokerMappedException | SearchInterfaceNotImplementedException | BadConnectionException | InvalidSearchParameterException | DatabaseOperationException e) {
                log.error(e.getMessage());
                throw new CouldNotGetVisualAidException();
            }
        }

        return _real.getVisualAid();
    }

    @Override
    public void setVisualAid(Collection<IVisualAid> visualAids) {
        _real.setVisualAid(visualAids);
    }

    @Override
    public void setDoctor(IDoctor idoctor) {
        _real.setDoctor(idoctor);
    }

    @Override
    public void setPrescriptions(Collection<IPrescription> iPrescriptions) {
        _real.setPrescriptions(iPrescriptions);
    }

    @Override
    public void setPasswordHash(String password) {
        _real.setPasswordHash(password);
    }

    @Override
    public IDomain getDoctor() {
        return _real.getDoctor();
    }

    @Override
    public String getPasswordHash() {
        return _real.getPasswordHash();
    }

    @Override
    public String getUserName() {
        return ((ILogin)_real).getUserName();
    }

    @Override
    public String toString() {
        return _real.toString();
    }
}
