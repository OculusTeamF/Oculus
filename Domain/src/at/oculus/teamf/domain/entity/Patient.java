/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.databaseconnection.session.exception.BadSessionException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.reload.InvalidReloadClassException;
import at.oculus.teamf.persistence.exception.reload.ReloadInterfaceNotImplementedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Date;
import java.util.Collection;

/**
 * Todo: add docs, implement equals, logger
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Patient implements IPatient, IDomain, ILogger {

	//<editor-fold desc="Attributes">
	private int _id;
	private String _firstName;
	private String _lastName;
	private Gender _gender;
	private String _socialInsuranceNr;
	private Doctor _doctor;
	private Collection<CalendarEvent> _calendarEvents;
	private Date _birthDay;
	private String _street;
	private String _postalCode;
	private String _city;
	private String _countryIsoCode;
	private String _phone;
	private String _email;
	private String _allergy;
	private String _childhoodAilments;
	private String _medicineIntolerance;
	private Collection<ExaminationProtocol> _examinationProtocol;

	//private EntityBroker eb;

	//</editor-fold>

	public Patient() {
		//IEntity p = eb.getEnity(PatientEntity.class, 0);

	}

	//<editor-fold desc="Getter/Setter">

	public int getId() {

		return _id;
	}

	public void setId(int id) {
		_id = id;
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

	public Patient setLastName(String lastName) {
		_lastName = lastName;
		return this;
	}

	public Gender getGender() {
		return _gender;
	}

	public void setGender(String gender) {
		if (gender.equals("male")) {
			setGender(Gender.Male);
		} else if (gender.equals("female")) {
			setGender(Gender.Female);
		}
	}

	public void setGender(Gender gender) {
		_gender = gender;
	}

	public Doctor getDoctor() {
		return _doctor;
	}

	public void setDoctor(Doctor doctor) {
		_doctor = doctor;
	}

	public Collection<CalendarEvent> getCalendarEvents()
            throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
            NoBrokerMappedException {

		Facade.getInstance().reloadCollection(this, CalendarEvent.class);

		return _calendarEvents;
	}

	public void setCalendarEvents(Collection<CalendarEvent> calendarEvents) {
		_calendarEvents = calendarEvents;
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
		_doctor = (Doctor) idoctor;
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

	public void setCountryIsoCode(String countryIsoCode) {
		_countryIsoCode = countryIsoCode;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		_phone = phone;
	}

	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
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

	public Collection<IExaminationProtocol> getExaminationProtocol()
            throws InvalidReloadClassException, ReloadInterfaceNotImplementedException, BadConnectionException,
            NoBrokerMappedException {

		Facade.getInstance().reloadCollection(this, ExaminationProtocol.class);

		return (Collection<IExaminationProtocol>)(Collection<?>) _examinationProtocol;
	}

    @Override
    public void addExaminationProtocol(IExaminationProtocol examinationProtocol) {}

    public void setExaminationProtocol(Collection<ExaminationProtocol> examinationProtocol) {
		_examinationProtocol = examinationProtocol;
	}

	//</editor-fold>

	public Collection<ExaminationResult> getExaminationResults() {
		Collection<ExaminationResult> examinationResults = null;

		try {
			examinationResults = Facade.getInstance().search(ExaminationResult.class, this.getId()+"");
		} catch (SearchInterfaceNotImplementedException e) {
			e.printStackTrace();
		} catch (BadConnectionException e) {
			e.printStackTrace();
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
		} catch (InvalidSearchParameterException e) {
			e.printStackTrace();
		}

		return examinationResults;
	}

	public Collection<Diagnosis> getDiagnoses() {
		Collection<Diagnosis> diagnoses = null;

		try {
			diagnoses = Facade.getInstance().search(Diagnosis.class, this.getId()+"");
		} catch (SearchInterfaceNotImplementedException e) {
			e.printStackTrace();
		} catch (BadConnectionException e) {
			e.printStackTrace();
		} catch (NoBrokerMappedException e) {
			e.printStackTrace();
		} catch (InvalidSearchParameterException e) {
			e.printStackTrace();
		}

		return diagnoses;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + ", " + getSocialInsuranceNr();
	}

	public void addExaminationProtocol(ExaminationProtocol examinationProtocol)
            throws NoBrokerMappedException, BadConnectionException {
		log.debug("adding examination protocol to patient " + this);
		examinationProtocol.setPatient(this);
		_examinationProtocol.add(examinationProtocol);
		Facade.getInstance().save(examinationProtocol);
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
}
