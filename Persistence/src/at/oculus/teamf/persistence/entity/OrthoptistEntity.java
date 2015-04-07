package at.oculus.teamf.persistence.entity;

import javax.persistence.*;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "orthoptist", schema = "", catalog = "oculus")
public class OrthoptistEntity {
    private int orthoptistId;
    private CalendarEntity calendarId;
    private UserEntity userId;

    @Id
    @Column(name = "orthoptistId", nullable = false, insertable = true, updatable = true)
    public int getOrthoptistId() {
        return orthoptistId;
    }

    public void setOrthoptistId(int orthoptistId) {
        this.orthoptistId = orthoptistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrthoptistEntity that = (OrthoptistEntity) o;

        if (orthoptistId != that.orthoptistId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return orthoptistId;
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
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }
}
