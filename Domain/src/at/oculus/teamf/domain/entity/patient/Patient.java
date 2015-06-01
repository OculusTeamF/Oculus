/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.patient;

import at.oculus.teamE.domain.interfaces.IExaminationProtocolTb2;
import at.oculus.teamE.domain.interfaces.IPatientTb2;
import at.oculus.teamf.domain.CalendarEvent;
import at.oculus.teamf.domain.ICalendarEvent;
import at.oculus.teamf.domain.entity.diagnosis.IDiagnosis;
import at.oculus.teamf.domain.entity.examination.IExaminationProtocol;
import at.oculus.teamf.domain.entity.examination.IExaminationResult;
import at.oculus.teamf.domain.entity.medicine.IMedicine;
import at.oculus.teamf.domain.entity.prescription.IPrescription;
import at.oculus.teamf.domain.entity.user.Gender;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.*;
import at.oculus.teamf.domain.entity.visualadi.IVisualAid;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.technical.accessrights.ILogin;
import at.oculus.teamf.technical.loggin.ILogger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Patient implements IPatient, ILogger, IPatientTb2, ILogin {
    //<editor-fold desc="Attributes">
    private int _id;
    private String _firstName;
    private String _lastName;
    private Gender _gender;
    private String _socialInsuranceNr;
    private IDoctor _doctor;
    private Collection<CalendarEvent> _calendarEvents;
    private Date _birthDay;
    private String _street;
    private String _postalCode;
    private String _city;
    private String _countryIsoCode;
    private String _phone;
    private String _email;
	private String _password;
    private String _allergy;
    private String _childhoodAilments;
    private String _medicineIntolerance;
    private Collection<IExaminationProtocol> _examinationProtocol;
    private Collection<IExaminationResult> _examinationResults;
	private Collection<IPrescription> _prescriptions;
    private Collection<IVisualAid> _visualAids;
    private Collection<IDiagnosis> _diagnosis;
    private Collection<IMedicine> _medicine;

    //<editor-fold desc="Getter/Setter">

	public IDoctor getDoctor() {
		return _doctor;
	}

	public void setDoctor(IDoctor doctor) {
		_doctor = doctor;
	}

	@Override
	public int hashCode() {
		int result = _id;
		result = 31 * result + (_firstName != null ? _firstName.hashCode() : 0);
		result = 31 * result + (_lastName != null ? _lastName.hashCode() : 0);
		result = 31 * result + (_gender != null ? _gender.hashCode() : 0);
		result = 31 * result + (_socialInsuranceNr != null ? _socialInsuranceNr.hashCode() : 0);
		result = 31 * result + (_doctor != null ? _doctor.hashCode() : 0);
		result = 31 * result + (_calendarEvents != null ? _calendarEvents.hashCode() : 0);
		result = 31 * result + (_birthDay != null ? _birthDay.hashCode() : 0);
		result = 31 * result + (_street != null ? _street.hashCode() : 0);
		result = 31 * result + (_postalCode != null ? _postalCode.hashCode() : 0);
		result = 31 * result + (_city != null ? _city.hashCode() : 0);
		result = 31 * result + (_countryIsoCode != null ? _countryIsoCode.hashCode() : 0);
		result = 31 * result + (_phone != null ? _phone.hashCode() : 0);
		result = 31 * result + (_email != null ? _email.hashCode() : 0);
		result = 31 * result + (_allergy != null ? _allergy.hashCode() : 0);
		result = 31 * result + (_childhoodAilments != null ? _childhoodAilments.hashCode() : 0);
		result = 31 * result + (_medicineIntolerance != null ? _medicineIntolerance.hashCode() : 0);
		result = 31 * result + (_examinationProtocol != null ? _examinationProtocol.hashCode() : 0);
		return result;
	}

	public void addExaminationProtocol(IExaminationProtocol examinationProtocol) throws CouldNotAddExaminationProtocol {
		log.debug("adding examination protocol to patient " + this);
		examinationProtocol.setPatient(this);
		if (_examinationProtocol == null) {
			_examinationProtocol = new LinkedList<>();
		}
		_examinationProtocol.add( examinationProtocol);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Patient))
			return false;

		Patient patient = (Patient) o;

		if (_id != patient._id)
			return false;
		if (_allergy != null ? !_allergy.equals(patient._allergy) : patient._allergy != null)
			return false;
		if (_birthDay != null ? !_birthDay.equals(patient._birthDay) : patient._birthDay != null)
			return false;
		if (_calendarEvents != null ? !_calendarEvents.equals(patient._calendarEvents) :
		    patient._calendarEvents != null)
			return false;
		if (_childhoodAilments != null ? !_childhoodAilments.equals(patient._childhoodAilments) :
		    patient._childhoodAilments != null)
			return false;
		if (_city != null ? !_city.equals(patient._city) : patient._city != null)
			return false;
		if (_countryIsoCode != null ? !_countryIsoCode.equals(patient._countryIsoCode) :
		    patient._countryIsoCode != null)
			return false;
		if (_doctor != null ? !_doctor.equals(patient._doctor) : patient._doctor != null)
			return false;
		if (_email != null ? !_email.equals(patient._email) : patient._email != null)
			return false;
		if (_examinationProtocol != null ? !_examinationProtocol.equals(patient._examinationProtocol) :
		    patient._examinationProtocol != null)
			return false;
		if (_firstName != null ? !_firstName.equals(patient._firstName) : patient._firstName != null)
			return false;
		if (_gender != patient._gender)
			return false;
		if (_lastName != null ? !_lastName.equals(patient._lastName) : patient._lastName != null)
			return false;
		if (_medicineIntolerance != null ? !_medicineIntolerance.equals(patient._medicineIntolerance) :
		    patient._medicineIntolerance != null)
			return false;
		if (_phone != null ? !_phone.equals(patient._phone) : patient._phone != null)
			return false;
		if (_postalCode != null ? !_postalCode.equals(patient._postalCode) : patient._postalCode != null)
			return false;
		if (_socialInsuranceNr != null ? !_socialInsuranceNr.equals(patient._socialInsuranceNr) :
		    patient._socialInsuranceNr != null)
			return false;
		if (_street != null ? !_street.equals(patient._street) : patient._street != null)
			return false;

		return true;
	}

	public int getId() {
		return _id;
	}

    public void setId(int id) {
        _id = id;
    }

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + ", " + getSocialInsuranceNr();
	}

	public String getFirstName() {
		return _firstName;
    }

	public void setFirstName(String firstName) {
		_firstName = firstName;
    }

	public String getLastName() {
		return _lastName;
    }

    @Override
    public LocalDate getBirthDate() {
        return null;
    }

    public void setLastName(String lastName) {
		_lastName = lastName;
    }

    public Gender getGender() {
        return _gender;
    }

    public void setGender(Gender gender) {
        _gender = gender;
    }

    public void setGender(String gender) {
        if (gender.equals("male")) {
            setGender(Gender.Male);
        } else if (gender.equals("female")) {
            setGender(Gender.Female);
        }
    }

	public Collection<ICalendarEvent> getCalendarEvents() throws CouldNotGetCalendarEventsException {
        return (Collection<ICalendarEvent>) (Collection<?>) _calendarEvents;
    }

    public void setCalendarEvents(Collection<ICalendarEvent> calendarEvents) {
        _calendarEvents = (Collection<CalendarEvent>) (Collection<?>) calendarEvents;
    }

    @Override
    public Integer getPatientId() {
        return null;
    }

    public String getSocialInsuranceNr() {
        return _socialInsuranceNr;
    }

    public void setSocialInsuranceNr(String socialInsuranceNr) {
        _socialInsuranceNr = socialInsuranceNr;
    }

    @Override
    public IDoctor getIDoctor() {
        return _doctor;
    }

    @Override
    public void setIDoctor(IDoctor idoctor) {
        _doctor = idoctor;
    }

    public Date getBirthDay() {
        return _birthDay;
    }

    public void setBirthDay(Date birthDay) {
        _birthDay = birthDay;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCountryIsoCode() {
        return _countryIsoCode;
    }

    @Override
    public String getPhoneNumber() {
        return getPhoneNumber();
    }

    @Override
    public String getEmailAddress() {
        return getEmail();
    }

    public void setCountryIsoCode(String countryIsoCode) {
        _countryIsoCode = countryIsoCode;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

	public String getUserName() { return getEmail(); }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

	public String getPasswordHash() {
		return _password;
	}

	public void setPasswordHash(String password) {
		_password = password;
	}

	public String getAllergy() {
        return _allergy;
    }

    public void setAllergy(String allergy) {
        _allergy = allergy;
    }

    public String getChildhoodAilments() {
        return _childhoodAilments;
    }

    public void setChildhoodAilments(String childhoodAilments) {
        _childhoodAilments = childhoodAilments;
    }

    public String getMedicineIntolerance() {
        return _medicineIntolerance;
    }

    public void setMedicineIntolerance(String medicineIntolerance) {
        _medicineIntolerance = medicineIntolerance;
    }

	public Collection<IExaminationProtocol> getExaminationProtocol() throws CouldNotGetExaminationProtolException {
        return (Collection<IExaminationProtocol>) (Collection<?>) _examinationProtocol;
    }

    public void setExaminationProtocol(Collection<IExaminationProtocol> examinationProtocol) {
        _examinationProtocol = examinationProtocol;
    }

    /**
     * get all examination results
     *
     * @return ExaminationResult Collection
     * @throws NoBrokerMappedException
     * @throws CouldNotGetExaminationResultException
     */
    public Collection<IExaminationResult> getExaminationResults() throws CouldNotGetExaminationResultException {
        return _examinationResults;
    }

    @Override
    public void setExaminationResults(Collection<IExaminationResult> results) {
        _examinationResults = results;
    }

    //</editor-fold>

	/**
	 * get all prescriptions
	 * @return Prescription Collection
	 * @throws CouldNotGetPrescriptionException
	 */
	public Collection<IPrescription> getPrescriptions() throws CouldNotGetPrescriptionException {
		return (Collection<IPrescription>) (Collection<?>) _prescriptions;
    }

    public void setPrescriptions(Collection<IPrescription> prescriptions) {
        _prescriptions = prescriptions;
    }

	/**
	 * get all diagnoses
	 * @return Diagnose Collection
	 * @throws CouldNotGetDiagnoseException
	 */
	public Collection<IDiagnosis> getDiagnoses() throws CouldNotGetDiagnoseException {
	    _diagnosis = null;
        return _diagnosis;
    }

	/**
	 * get all medicine
	 * @return Medicine Collection
	 * @throws CouldNotGetMedicineException
	 */
	public Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException {
		_medicine = null;
        return _medicine;
    }

    public Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException {
        _visualAids = null;
        return _visualAids;
    }

    @Override
    public void setVisualAid(Collection<IVisualAid> visualAids) {
        _visualAids = visualAids;
    }

    @Override
    public List<? extends IExaminationProtocolTb2> getExaminationProtocols() {
        try {
            return (List<? extends IExaminationProtocolTb2>) (Collection<?>)getExaminationProtocol();
        } catch (CouldNotGetExaminationProtolException e) {
             //eat up
        }
        return null;
    }

    @Override
    public void addProtocol(IExaminationProtocolTb2 iExaminationProtocolTb2) {
        try {
            addExaminationProtocol((IExaminationProtocol) iExaminationProtocolTb2);
        } catch (CouldNotAddExaminationProtocol e) {
            //eat up
        }
    }
}
