/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "patient", schema = "", catalog = "oculus_f")
public class PatientEntity {
    private int _id;
    private Integer _doctorId;
    private String _socialInsuranceNr;
    private String _firstName;
    private String _lastName;
    private Date _birthDay;
    private String _gender;
    private String _street;
    private String postalCode;
    private String _city;
    private String _countryIsoCode;
    private String _phone;
    private String _email;
    private String _allergy;
    private String _childhoodAilments;
    private String _medicineIntolerance;
    private Collection<CalendareventEntity> _calendareventsByPatientId;
    private DoctorEntity _doctor;
    private QueueEntity _queue;

    @Id
    @Column(name = "patientId", nullable = false, insertable = true, updatable = true)
    public int get_id() {
        return _id;
    }

    public void set_id(int patientId) {
        this._id = patientId;
    }

    @Basic
    @Column(name = "doctorId", nullable = true, insertable = true, updatable = true)
    public Integer get_doctorId() {
        return _doctorId;
    }

    public void set_doctorId(Integer doctorId) {
        this._doctorId = doctorId;
    }

    @Basic
    @Column(name = "socialInsuranceNr", nullable = true, insertable = true, updatable = true, length = 10)
    public String get_socialInsuranceNr() {
        return _socialInsuranceNr;
    }

    public void set_socialInsuranceNr(String socialInsuranceNr) {
        this._socialInsuranceNr = socialInsuranceNr;
    }

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 30)
    public String get_firstName() {
        return _firstName;
    }

    public void set_firstName(String firstName) {
        this._firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 30)
    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String lastName) {
        this._lastName = lastName;
    }

    @Basic
    @Column(name = "birthDay", nullable = true, insertable = true, updatable = true)
    public Date get_birthDay() {
        return _birthDay;
    }

    public void set_birthDay(Date birthDay) {
        this._birthDay = birthDay;
    }

    @Basic
    @Column(name = "gender", nullable = false, insertable = true, updatable = true, length = 2)
    public String get_gender() {
        return _gender;
    }

    public void set_gender(String gender) {
        this._gender = gender;
    }

    @Basic
    @Column(name = "street", nullable = true, insertable = true, updatable = true, length = 255)
    public String get_street() {
        return _street;
    }

    public void set_street(String street) {
        this._street = street;
    }

    @Basic
    @Column(name = "postalCode", nullable = true, insertable = true, updatable = true, length = 20)
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Basic
    @Column(name = "city", nullable = true, insertable = true, updatable = true, length = 50)
    public String get_city() {
        return _city;
    }

    public void set_city(String city) {
        this._city = city;
    }

    @Basic
    @Column(name = "countryIsoCode", nullable = true, insertable = true, updatable = true, length = 2)
    public String get_countryIsoCode() {
        return _countryIsoCode;
    }

    public void set_countryIsoCode(String countryIsoCode) {
        this._countryIsoCode = countryIsoCode;
    }

    @Basic
    @Column(name = "phone", nullable = true, insertable = true, updatable = true, length = 50)
    public String get_phone() {
        return _phone;
    }

    public void set_phone(String phone) {
        this._phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    public String get_email() {
        return _email;
    }

    public void set_email(String email) {
        this._email = email;
    }

    @Basic
    @Column(name = "allergy", nullable = true, insertable = true, updatable = true, length = 65535)
    public String get_allergy() {
        return _allergy;
    }

    public void set_allergy(String allergy) {
        this._allergy = allergy;
    }

    @Basic
    @Column(name = "childhoodAilments", nullable = true, insertable = true, updatable = true, length = 65535)
    public String get_childhoodAilments() {
        return _childhoodAilments;
    }

    public void set_childhoodAilments(String childhoodAilments) {
        this._childhoodAilments = childhoodAilments;
    }

    @Basic
    @Column(name = "medicineIntolerance", nullable = true, insertable = true, updatable = true, length = 65535)
    public String get_medicineIntolerance() {
        return _medicineIntolerance;
    }

    public void set_medicineIntolerance(String medicineIntolerance) {
        this._medicineIntolerance = medicineIntolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientEntity that = (PatientEntity) o;

        if (_id != that._id) return false;
        if (_doctorId != null ? !_doctorId.equals(that._doctorId) : that._doctorId != null) return false;
        if (_socialInsuranceNr != null ? !_socialInsuranceNr.equals(that._socialInsuranceNr) : that._socialInsuranceNr != null)
            return false;
        if (_firstName != null ? !_firstName.equals(that._firstName) : that._firstName != null) return false;
        if (_lastName != null ? !_lastName.equals(that._lastName) : that._lastName != null) return false;
        if (_birthDay != null ? !_birthDay.equals(that._birthDay) : that._birthDay != null) return false;
        if (_gender != null ? !_gender.equals(that._gender) : that._gender != null) return false;
        if (_street != null ? !_street.equals(that._street) : that._street != null) return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
        if (_city != null ? !_city.equals(that._city) : that._city != null) return false;
        if (_countryIsoCode != null ? !_countryIsoCode.equals(that._countryIsoCode) : that._countryIsoCode != null)
            return false;
        if (_phone != null ? !_phone.equals(that._phone) : that._phone != null) return false;
        if (_email != null ? !_email.equals(that._email) : that._email != null) return false;
        if (_allergy != null ? !_allergy.equals(that._allergy) : that._allergy != null) return false;
        if (_childhoodAilments != null ? !_childhoodAilments.equals(that._childhoodAilments) : that._childhoodAilments != null)
            return false;
        if (_medicineIntolerance != null ? !_medicineIntolerance.equals(that._medicineIntolerance) : that._medicineIntolerance != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_doctorId != null ? _doctorId.hashCode() : 0);
        result = 31 * result + (_socialInsuranceNr != null ? _socialInsuranceNr.hashCode() : 0);
        result = 31 * result + (_firstName != null ? _firstName.hashCode() : 0);
        result = 31 * result + (_lastName != null ? _lastName.hashCode() : 0);
        result = 31 * result + (_birthDay != null ? _birthDay.hashCode() : 0);
        result = 31 * result + (_gender != null ? _gender.hashCode() : 0);
        result = 31 * result + (_street != null ? _street.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (_city != null ? _city.hashCode() : 0);
        result = 31 * result + (_countryIsoCode != null ? _countryIsoCode.hashCode() : 0);
        result = 31 * result + (_phone != null ? _phone.hashCode() : 0);
        result = 31 * result + (_email != null ? _email.hashCode() : 0);
        result = 31 * result + (_allergy != null ? _allergy.hashCode() : 0);
        result = 31 * result + (_childhoodAilments != null ? _childhoodAilments.hashCode() : 0);
        result = 31 * result + (_medicineIntolerance != null ? _medicineIntolerance.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "patient")
    public Collection<CalendareventEntity> get_calendareventsByPatientId() {
        return _calendareventsByPatientId;
    }

    public void set_calendareventsByPatientId(Collection<CalendareventEntity> calendareventsByPatientId) {
        this._calendareventsByPatientId = calendareventsByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    public DoctorEntity getDoctor() {
        return _doctor;
    }

    public void setDoctor(DoctorEntity doctorByDoctorId) {
        this._doctor = doctorByDoctorId;
    }

    @OneToOne(mappedBy = "queue")
    public QueueEntity getQueue() {
        return _queue;
    }

    public void setQueue(QueueEntity queue) {
        _queue = queue;
    }
}
