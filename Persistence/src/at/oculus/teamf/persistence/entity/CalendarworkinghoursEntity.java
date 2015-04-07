package at.oculus.teamf.persistence.entity;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "calendarworkinghours", schema = "", catalog = "oculus")
public class CalendarworkinghoursEntity {
    private int calendarWorkingHoursId;
    private Time morningFrom;
    private Time morningTo;
    private Time afternoonFrom;
    private Time afternoonTo;

    @Id
    @Column(name = "calendarWorkingHoursId", nullable = false, insertable = true, updatable = true)
    public int getCalendarWorkingHoursId() {
        return calendarWorkingHoursId;
    }

    public void setCalendarWorkingHoursId(int calendarWorkingHoursId) {
        this.calendarWorkingHoursId = calendarWorkingHoursId;
    }

    @Basic
    @Column(name = "morningFrom", nullable = true, insertable = true, updatable = true)
    public Time getMorningFrom() {
        return morningFrom;
    }

    public void setMorningFrom(Time morningFrom) {
        this.morningFrom = morningFrom;
    }

    @Basic
    @Column(name = "morningTo", nullable = true, insertable = true, updatable = true)
    public Time getMorningTo() {
        return morningTo;
    }

    public void setMorningTo(Time morningTo) {
        this.morningTo = morningTo;
    }

    @Basic
    @Column(name = "afternoonFrom", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonFrom() {
        return afternoonFrom;
    }

    public void setAfternoonFrom(Time afternoonFrom) {
        this.afternoonFrom = afternoonFrom;
    }

    @Basic
    @Column(name = "afternoonTo", nullable = true, insertable = true, updatable = true)
    public Time getAfternoonTo() {
        return afternoonTo;
    }

    public void setAfternoonTo(Time afternoonTo) {
        this.afternoonTo = afternoonTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarworkinghoursEntity that = (CalendarworkinghoursEntity) o;

        if (calendarWorkingHoursId != that.calendarWorkingHoursId) return false;
        if (morningFrom != null ? !morningFrom.equals(that.morningFrom) : that.morningFrom != null) return false;
        if (morningTo != null ? !morningTo.equals(that.morningTo) : that.morningTo != null) return false;
        if (afternoonFrom != null ? !afternoonFrom.equals(that.afternoonFrom) : that.afternoonFrom != null)
            return false;
        if (afternoonTo != null ? !afternoonTo.equals(that.afternoonTo) : that.afternoonTo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = calendarWorkingHoursId;
        result = 31 * result + (morningFrom != null ? morningFrom.hashCode() : 0);
        result = 31 * result + (morningTo != null ? morningTo.hashCode() : 0);
        result = 31 * result + (afternoonFrom != null ? afternoonFrom.hashCode() : 0);
        result = 31 * result + (afternoonTo != null ? afternoonTo.hashCode() : 0);
        return result;
    }
}
