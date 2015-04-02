/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.databaseconnectiontests;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

/**
 * Created by Norskan on 02.04.2015.
 */
@Entity
@javax.persistence.Table(name = "patient", schema = "", catalog = "oculus")
public class PatientEntity {
	private int _patientId;

	@Id
	@javax.persistence.Column(name = "patientId", nullable = false, insertable = true, updatable = true)
	public int getPatientId() {
		return _patientId;
	}

	public void setPatientId(int patientId) {
		_patientId = patientId;
	}

	private String _socialInsuranceNr;

	@Basic
	@javax.persistence.Column(name = "socialInsuranceNr", nullable = true, insertable = true, updatable = true,
	                          length = 10)
	public String getSocialInsuranceNr() {
		return _socialInsuranceNr;
	}

	public void setSocialInsuranceNr(String socialInsuranceNr) {
		_socialInsuranceNr = socialInsuranceNr;
	}

	private String _firstName;

	@Basic
	@javax.persistence.Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 30)
	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	private String _lastName;

	@Basic
	@javax.persistence.Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 30)
	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	private Date _birthDay;

	@Basic
	@javax.persistence.Column(name = "birthDay", nullable = true, insertable = true, updatable = true)
	public Date getBirthDay() {
		return _birthDay;
	}

	public void setBirthDay(Date birthDay) {
		_birthDay = birthDay;
	}

	private String _gender;

	@Basic
	@javax.persistence.Column(name = "gender", nullable = false, insertable = true, updatable = true, length = 2)
	public String getGender() {
		return _gender;
	}

	public void setGender(String gender) {
		_gender = gender;
	}

	private String _street;

	@Basic
	@javax.persistence.Column(name = "street", nullable = true, insertable = true, updatable = true, length = 255)
	public String getStreet() {
		return _street;
	}

	public void setStreet(String street) {
		_street = street;
	}

	private String _postalCode;

	@Basic
	@javax.persistence.Column(name = "postalCode", nullable = true, insertable = true, updatable = true, length = 20)
	public String getPostalCode() {
		return _postalCode;
	}

	public void setPostalCode(String postalCode) {
		_postalCode = postalCode;
	}

	private String _city;

	@Basic
	@javax.persistence.Column(name = "city", nullable = true, insertable = true, updatable = true, length = 50)
	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		_city = city;
	}

	private String _countryIsoCode;

	@Basic
	@javax.persistence.Column(name = "countryIsoCode", nullable = true, insertable = true, updatable = true, length = 2)
	public String getCountryIsoCode() {
		return _countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		_countryIsoCode = countryIsoCode;
	}

	private String _phone;

	@Basic
	@javax.persistence.Column(name = "phone", nullable = true, insertable = true, updatable = true, length = 50)
	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		_phone = phone;
	}

	private String _email;

	@Basic
	@javax.persistence.Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}

	private String _allergy;

	@Basic
	@javax.persistence.Column(name = "allergy", nullable = true, insertable = true, updatable = true, length = 65535)
	public String getAllergy() {
		return _allergy;
	}

	public void setAllergy(String allergy) {
		_allergy = allergy;
	}

	private String _childhoodAilments;

	@Basic
	@javax.persistence.Column(name = "childhoodAilments", nullable = true, insertable = true, updatable = true,
	                          length = 65535)
	public String getChildhoodAilments() {
		return _childhoodAilments;
	}

	public void setChildhoodAilments(String childhoodAilments) {
		_childhoodAilments = childhoodAilments;
	}

	private String _medicineIntolerance;

	@Basic
	@javax.persistence.Column(name = "medicineIntolerance", nullable = true, insertable = true, updatable = true,
	                          length = 65535)
	public String getMedicineIntolerance() {
		return _medicineIntolerance;
	}

	public void setMedicineIntolerance(String medicineIntolerance) {
		_medicineIntolerance = medicineIntolerance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PatientEntity that = (PatientEntity) o;

		if (_patientId != that._patientId)
			return false;
		if (_socialInsuranceNr != null ? !_socialInsuranceNr.equals(that._socialInsuranceNr) :
		    that._socialInsuranceNr != null)
			return false;
		if (_firstName != null ? !_firstName.equals(that._firstName) : that._firstName != null)
			return false;
		if (_lastName != null ? !_lastName.equals(that._lastName) : that._lastName != null)
			return false;
		if (_birthDay != null ? !_birthDay.equals(that._birthDay) : that._birthDay != null)
			return false;
		if (_gender != null ? !_gender.equals(that._gender) : that._gender != null)
			return false;
		if (_street != null ? !_street.equals(that._street) : that._street != null)
			return false;
		if (_postalCode != null ? !_postalCode.equals(that._postalCode) : that._postalCode != null)
			return false;
		if (_city != null ? !_city.equals(that._city) : that._city != null)
			return false;
		if (_countryIsoCode != null ? !_countryIsoCode.equals(that._countryIsoCode) : that._countryIsoCode != null)
			return false;
		if (_phone != null ? !_phone.equals(that._phone) : that._phone != null)
			return false;
		if (_email != null ? !_email.equals(that._email) : that._email != null)
			return false;
		if (_allergy != null ? !_allergy.equals(that._allergy) : that._allergy != null)
			return false;
		if (_childhoodAilments != null ? !_childhoodAilments.equals(that._childhoodAilments) :
		    that._childhoodAilments != null)
			return false;
		if (_medicineIntolerance != null ? !_medicineIntolerance.equals(that._medicineIntolerance) :
		    that._medicineIntolerance != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = _patientId;
		result = 31 * result + (_socialInsuranceNr != null ? _socialInsuranceNr.hashCode() : 0);
		result = 31 * result + (_firstName != null ? _firstName.hashCode() : 0);
		result = 31 * result + (_lastName != null ? _lastName.hashCode() : 0);
		result = 31 * result + (_birthDay != null ? _birthDay.hashCode() : 0);
		result = 31 * result + (_gender != null ? _gender.hashCode() : 0);
		result = 31 * result + (_street != null ? _street.hashCode() : 0);
		result = 31 * result + (_postalCode != null ? _postalCode.hashCode() : 0);
		result = 31 * result + (_city != null ? _city.hashCode() : 0);
		result = 31 * result + (_countryIsoCode != null ? _countryIsoCode.hashCode() : 0);
		result = 31 * result + (_phone != null ? _phone.hashCode() : 0);
		result = 31 * result + (_email != null ? _email.hashCode() : 0);
		result = 31 * result + (_allergy != null ? _allergy.hashCode() : 0);
		result = 31 * result + (_childhoodAilments != null ? _childhoodAilments.hashCode() : 0);
		result = 31 * result + (_medicineIntolerance != null ? _medicineIntolerance.hashCode() : 0);
		return result;
	}
}
