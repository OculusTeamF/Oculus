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
import java.util.Collection;

/**
 * Created by Norskan on 07.04.2015.
 */
@Entity
@Table(name = "doctor", schema = "", catalog = "oculus_f")
public class DoctorEntity {
    private int doctorId;
    private Integer userId;
    private int calendarId;
    private Integer doctorIdSubstitute;
    private UserEntity userByUserId;
    private CalendarEntity calendarByCalendarId;
    private DoctorEntity doctorByDoctorIdSubstitute;
    private Collection<DoctorEntity> doctorsByDoctorId;
    private Collection<PatientEntity> patientsByDoctorId;
    private Collection<QueueEntity> queuesByDoctorId;

    @Id
    @Column(name = "doctorId", nullable = false, insertable = true, updatable = true)
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Basic
    @Column(name = "userId", nullable = true, insertable = true, updatable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Basic
    @Column(name = "doctorIdSubstitute", nullable = true, insertable = true, updatable = true)
    public Integer getDoctorIdSubstitute() {
        return doctorIdSubstitute;
    }

    public void setDoctorIdSubstitute(Integer doctorIdSubstitute) {
        this.doctorIdSubstitute = doctorIdSubstitute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorEntity that = (DoctorEntity) o;

        if (doctorId != that.doctorId) return false;
        if (calendarId != that.calendarId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (doctorIdSubstitute != null ? !doctorIdSubstitute.equals(that.doctorIdSubstitute) : that.doctorIdSubstitute != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = doctorId;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + calendarId;
        result = 31 * result + (doctorIdSubstitute != null ? doctorIdSubstitute.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendarByCalendarId() {
        return calendarByCalendarId;
    }

    public void setCalendarByCalendarId(CalendarEntity calendarByCalendarId) {
        this.calendarByCalendarId = calendarByCalendarId;
    }

    @ManyToOne
    @JoinColumn(name = "doctorIdSubstitute", referencedColumnName = "doctorId")
    public DoctorEntity getDoctorByDoctorIdSubstitute() {
        return doctorByDoctorIdSubstitute;
    }

    public void setDoctorByDoctorIdSubstitute(DoctorEntity doctorByDoctorIdSubstitute) {
        this.doctorByDoctorIdSubstitute = doctorByDoctorIdSubstitute;
    }

    @OneToMany(mappedBy = "doctorByDoctorIdSubstitute")
    public Collection<DoctorEntity> getDoctorsByDoctorId() {
        return doctorsByDoctorId;
    }

    public void setDoctorsByDoctorId(Collection<DoctorEntity> doctorsByDoctorId) {
        this.doctorsByDoctorId = doctorsByDoctorId;
    }

    @OneToMany(mappedBy = "doctorByDoctorId")
    public Collection<PatientEntity> getPatientsByDoctorId() {
        return patientsByDoctorId;
    }

    public void setPatientsByDoctorId(Collection<PatientEntity> patientsByDoctorId) {
        this.patientsByDoctorId = patientsByDoctorId;
    }

    @OneToMany(mappedBy = "doctorByDoctorId")
    public Collection<QueueEntity> getQueuesByDoctorId() {
        return queuesByDoctorId;
    }

    public void setQueuesByDoctorId(Collection<QueueEntity> queuesByDoctorId) {
        this.queuesByDoctorId = queuesByDoctorId;
    }
}
