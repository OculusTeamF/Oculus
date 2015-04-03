package at.oculus.teamf.persistence.entity;

import javax.persistence.*;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "doctor", schema = "", catalog = "oculus")
public class DoctorEntity {
    private int doctorId;
    private CalendarEntity calendarId;
    private DoctorEntity doctorIdSubstitute;
    private UserEntity userId;

    @Id
    @Column(name = "doctorId", nullable = false, insertable = true, updatable = true)
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorEntity that = (DoctorEntity) o;

        if (doctorId != that.doctorId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return doctorId;
    }

    @OneToOne
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId", nullable = false)
    public CalendarEntity getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(CalendarEntity calendarId) {
        this.calendarId = calendarId;
    }

    @OneToOne
    @JoinColumn(name = "doctorIdSubstitute", referencedColumnName = "doctorId")
    public DoctorEntity getDoctorIdSubstitute() {
        return doctorIdSubstitute;
    }

    public void setDoctorIdSubstitute(DoctorEntity doctorIdSubstitute) {
        this.doctorIdSubstitute = doctorIdSubstitute;
    }

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }
}
