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
@Table(name = "calendar", schema = "", catalog = "oculus_f")
public class CalendarEntity {
    private int calendarId;
    private String title;
    private Collection<CalendareventEntity> calendareventsByCalendarId;
    private Collection<CalendarworkinghoursEntity> calendarworkinghoursesByCalendarId;
    private Collection<DoctorEntity> doctorsByCalendarId;
    private Collection<OrthoptistEntity> orthoptistsByCalendarId;

    @Id
    @Column(name = "calendarId", nullable = false, insertable = true, updatable = true)
    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEntity that = (CalendarEntity) o;

        if (calendarId != that.calendarId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = calendarId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "calendarByCalendarId")
    public Collection<CalendareventEntity> getCalendareventsByCalendarId() {
        return calendareventsByCalendarId;
    }

    public void setCalendareventsByCalendarId(Collection<CalendareventEntity> calendareventsByCalendarId) {
        this.calendareventsByCalendarId = calendareventsByCalendarId;
    }

    @OneToMany(mappedBy = "calendarByCalendarId")
    public Collection<CalendarworkinghoursEntity> getCalendarworkinghoursesByCalendarId() {
        return calendarworkinghoursesByCalendarId;
    }

    public void setCalendarworkinghoursesByCalendarId(Collection<CalendarworkinghoursEntity> calendarworkinghoursesByCalendarId) {
        this.calendarworkinghoursesByCalendarId = calendarworkinghoursesByCalendarId;
    }

    @OneToMany(mappedBy = "calendarByCalendarId")
    public Collection<DoctorEntity> getDoctorsByCalendarId() {
        return doctorsByCalendarId;
    }

    public void setDoctorsByCalendarId(Collection<DoctorEntity> doctorsByCalendarId) {
        this.doctorsByCalendarId = doctorsByCalendarId;
    }

    @OneToMany(mappedBy = "calendarByCalendarId")
    public Collection<OrthoptistEntity> getOrthoptistsByCalendarId() {
        return orthoptistsByCalendarId;
    }

    public void setOrthoptistsByCalendarId(Collection<OrthoptistEntity> orthoptistsByCalendarId) {
        this.orthoptistsByCalendarId = orthoptistsByCalendarId;
    }
}
