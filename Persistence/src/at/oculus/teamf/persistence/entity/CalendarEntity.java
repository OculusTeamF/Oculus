package at.oculus.teamf.persistence.entity;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "calendar", schema = "", catalog = "oculus")
public class CalendarEntity implements IEntityCollection {
	private int calendarId;
	private CalendarworkinghoursEntity calendarWorkingHoursIdFriday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdMonday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdSaturday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdSunday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdThursday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdTuesday;
	private CalendarworkinghoursEntity calendarWorkingHoursIdWednesday;
	private Collection<CalendareventEntity> events;

	@Id
	@Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
	public int getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(int calendarId) {
		this.calendarId = calendarId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CalendarEntity that = (CalendarEntity) o;

		if (calendarId != that.calendarId)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return calendarId;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdFriday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdFriday() {
		return calendarWorkingHoursIdFriday;
	}

	public void setCalendarWorkingHoursIdFriday(CalendarworkinghoursEntity calendarWorkingHoursIdFriday) {
		this.calendarWorkingHoursIdFriday = calendarWorkingHoursIdFriday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdMonday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdMonday() {
		return calendarWorkingHoursIdMonday;
	}

	public void setCalendarWorkingHoursIdMonday(CalendarworkinghoursEntity calendarWorkingHoursIdMonday) {
		this.calendarWorkingHoursIdMonday = calendarWorkingHoursIdMonday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdSaturday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdSaturday() {
		return calendarWorkingHoursIdSaturday;
	}

	public void setCalendarWorkingHoursIdSaturday(CalendarworkinghoursEntity calendarWorkingHoursIdSaturday) {
		this.calendarWorkingHoursIdSaturday = calendarWorkingHoursIdSaturday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdSunday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdSunday() {
		return calendarWorkingHoursIdSunday;
	}

	public void setCalendarWorkingHoursIdSunday(CalendarworkinghoursEntity calendarWorkingHoursIdSunday) {
		this.calendarWorkingHoursIdSunday = calendarWorkingHoursIdSunday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdThursday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdThursday() {
		return calendarWorkingHoursIdThursday;
	}

	public void setCalendarWorkingHoursIdThursday(CalendarworkinghoursEntity calendarWorkingHoursIdThursday) {
		this.calendarWorkingHoursIdThursday = calendarWorkingHoursIdThursday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdTuesday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdTuesday() {
		return calendarWorkingHoursIdTuesday;
	}

	public void setCalendarWorkingHoursIdTuesday(CalendarworkinghoursEntity calendarWorkingHoursIdTuesday) {
		this.calendarWorkingHoursIdTuesday = calendarWorkingHoursIdTuesday;
	}

	@OneToOne
	@JoinColumn(name = "calendarWorkingHoursIdWednesday", referencedColumnName = "calendarWorkingHoursId")
	public CalendarworkinghoursEntity getCalendarWorkingHoursIdWednesday() {
		return calendarWorkingHoursIdWednesday;
	}

	public void setCalendarWorkingHoursIdWednesday(CalendarworkinghoursEntity calendarWorkingHoursIdWednesday) {
		this.calendarWorkingHoursIdWednesday = calendarWorkingHoursIdWednesday;
	}

	@OneToMany
	public Collection<CalendareventEntity> getEvents() {
		return events;
	}

	public void setEvents(Collection<CalendareventEntity> events) {
		this.events = events;
	}

	@Override
	public Collection getCollection(Class clazz) {
		Collection collection = null;

		if (clazz.isInstance(new CalendareventEntity())) {
			Hibernate.initialize(events);
			collection = events;
		}

		return collection;
	}
}
