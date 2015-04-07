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
    private int patientId;
    private Integer doctorId;
    private String socialInsuranceNr;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String gender;
    private String street;
    private String postalCode;
    private String city;
    private String countryIsoCode;
    private String phone;
    private String email;
    private String allergy;
    private String childhoodAilments;
    private String medicineIntolerance;
    private Collection<CalendareventEntity> calendareventsByPatientId;
    private DoctorEntity doctorByDoctorId;
    private Collection<QueueEntity> queuesByPatientId;

    @Id
    @Column(name = "patientId", nullable = false, insertable = true, updatable = true)
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "doctorId", nullable = true, insertable = true, updatable = true)
    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "socialInsuranceNr", nullable = true, insertable = true, updatable = true, length = 10)
    public String getSocialInsuranceNr() {
        return socialInsuranceNr;
    }

    public void setSocialInsuranceNr(String socialInsuranceNr) {
        this.socialInsuranceNr = socialInsuranceNr;
    }

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 30)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 30)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "birthDay", nullable = true, insertable = true, updatable = true)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Basic
    @Column(name = "gender", nullable = false, insertable = true, updatable = true, length = 2)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "street", nullable = true, insertable = true, updatable = true, length = 255)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "countryIsoCode", nullable = true, insertable = true, updatable = true, length = 2)
    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    @Basic
    @Column(name = "phone", nullable = true, insertable = true, updatable = true, length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "allergy", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    @Basic
    @Column(name = "childhoodAilments", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getChildhoodAilments() {
        return childhoodAilments;
    }

    public void setChildhoodAilments(String childhoodAilments) {
        this.childhoodAilments = childhoodAilments;
    }

    @Basic
    @Column(name = "medicineIntolerance", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getMedicineIntolerance() {
        return medicineIntolerance;
    }

    public void setMedicineIntolerance(String medicineIntolerance) {
        this.medicineIntolerance = medicineIntolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientEntity that = (PatientEntity) o;

        if (patientId != that.patientId) return false;
        if (doctorId != null ? !doctorId.equals(that.doctorId) : that.doctorId != null) return false;
        if (socialInsuranceNr != null ? !socialInsuranceNr.equals(that.socialInsuranceNr) : that.socialInsuranceNr != null)
            return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (birthDay != null ? !birthDay.equals(that.birthDay) : that.birthDay != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (countryIsoCode != null ? !countryIsoCode.equals(that.countryIsoCode) : that.countryIsoCode != null)
            return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (allergy != null ? !allergy.equals(that.allergy) : that.allergy != null) return false;
        if (childhoodAilments != null ? !childhoodAilments.equals(that.childhoodAilments) : that.childhoodAilments != null)
            return false;
        if (medicineIntolerance != null ? !medicineIntolerance.equals(that.medicineIntolerance) : that.medicineIntolerance != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = patientId;
        result = 31 * result + (doctorId != null ? doctorId.hashCode() : 0);
        result = 31 * result + (socialInsuranceNr != null ? socialInsuranceNr.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (countryIsoCode != null ? countryIsoCode.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (allergy != null ? allergy.hashCode() : 0);
        result = 31 * result + (childhoodAilments != null ? childhoodAilments.hashCode() : 0);
        result = 31 * result + (medicineIntolerance != null ? medicineIntolerance.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "_patient")
    public Collection<CalendareventEntity> getCalendareventsByPatientId() {
        return calendareventsByPatientId;
    }

    public void setCalendareventsByPatientId(Collection<CalendareventEntity> calendareventsByPatientId) {
        this.calendareventsByPatientId = calendareventsByPatientId;
    }

    @ManyToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    public DoctorEntity getDoctorByDoctorId() {
        return doctorByDoctorId;
    }

    public void setDoctorByDoctorId(DoctorEntity doctorByDoctorId) {
        this.doctorByDoctorId = doctorByDoctorId;
    }

    @OneToMany(mappedBy = "_patient")
    public Collection<QueueEntity> getQueuesByPatientId() {
        return queuesByPatientId;
    }

    public void setQueuesByPatientId(Collection<QueueEntity> queuesByPatientId) {
        this.queuesByPatientId = queuesByPatientId;
    }
}
