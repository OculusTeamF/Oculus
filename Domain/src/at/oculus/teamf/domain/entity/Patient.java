/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.FacadeException;

import java.util.Date;
import java.util.Collection;

/**
 * Todo: add docs, implement equals, getter into interface wrappen
 *
 * @author Simon Angerer
 * @date 03.4.2015
 */
public class Patient implements IPatient, IDomain {

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

    //private EntityBroker eb;

    //</editor-fold>

    public Patient(){
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

    public void setGender(String gender){
        if(gender.equals("male")){
            setGender(Gender.Male);
        }else if(gender.equals("female")){
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

    public Collection<CalendarEvent> getCalendarEvents() {
        try {
            Facade.getInstance().reloadCollection(this, CalendarEvent.class);
        } catch (FacadeException e) {
            //Todo: add loging
            e.printStackTrace();
        }
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

	//</editor-fold>

	@Override
	public String toString(){
		return getFirstName() + " " + getLastName();
	}
}
