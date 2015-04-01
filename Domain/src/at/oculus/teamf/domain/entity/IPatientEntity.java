/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Norskan on 31.03.2015.
 */
public interface IPatientEntity {
    // <editor-fold desc="Getter/Setter">
    @Id
    @Column(name = "patientId", nullable = false, insertable = true, updatable = true)
    int getPatientId();

    @Basic
    @Column(name = "socialInsuranceNr", nullable = true, insertable = true, updatable = true, length = 10)
    String getSocialInsuranceNr();

    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true, length = 30)
    String getFirstName();

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 30)
    String getLastName();

    @Basic
    @Column(name = "birthDay", nullable = true, insertable = true, updatable = true)
    Date getBirthDay();

    @Basic
    @Column(name = "gender", nullable = false, insertable = true, updatable = true, length = 2)
    String getGender();

    @Basic
    @Column(name = "street", nullable = true, insertable = true, updatable = true, length = 255)
    String getStreet();

    @Basic
    @Column(name = "postalCode", nullable = true, insertable = true, updatable = true, length = 20)
    String getPostalCode();

    @Basic
    @Column(name = "city", nullable = true, insertable = true, updatable = true, length = 50)
    String getCity();

    @Basic
    @Column(name = "countryIsoCode", nullable = true, insertable = true, updatable = true, length = 2)
    String getCountryIsoCode();

    @Basic
    @Column(name = "phone", nullable = true, insertable = true, updatable = true, length = 50)
    String getPhone();

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 255)
    String getEmail();

    @Basic
    @Column(name = "allergy", nullable = true, insertable = true, updatable = true, length = 65535)
    String getAllergy();

    @Basic
    @Column(name = "childhoodAilments", nullable = true, insertable = true, updatable = true, length = 65535)
    String getChildhoodAilments();

    @Basic
    @Column(name = "medicineIntolerance", nullable = true, insertable = true, updatable = true, length = 65535)
    String getMedicineIntolerance();

    @OneToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "doctorId")
    DoctorEntity getDoctor();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
