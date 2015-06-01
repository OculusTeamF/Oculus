/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.patient;

import at.oculus.teamf.domain.entity.Gender;
import at.oculus.teamf.domain.entity.exception.*;
import at.oculus.teamf.domain.entity.interfaces.*;

import java.util.Collection;
import java.util.Date;

/**
 * Created by FabianLaptop on 07.04.2015.
 */
public interface IPatient extends IDomain {
    //<editor-fold desc="Getter/Setter">
    int getId();
    void setId(int patientID);

    String getFirstName();
    void setFirstName(String firstName);

    String getLastName();
    void setLastName(String lastName);

    Gender getGender();
    void setGender(Gender gender);

    String getSocialInsuranceNr();
    void setSocialInsuranceNr(String svn);

    IDoctor getIDoctor();
    void setIDoctor(IDoctor idoctor);

    Collection<ICalendarEvent> getCalendarEvents() throws CouldNotGetCalendarEventsException;
    void setCalendarEvents(Collection<ICalendarEvent> calendarEvents);

    Date getBirthDay();
    void setBirthDay(Date birthDay);

    String getStreet();
    void setStreet(String street);

    String getPostalCode();
    void setPostalCode(String postalCode);

    String getCity();
    void setCity(String city);

    String getCountryIsoCode();
    void setCountryIsoCode(String countryIsoCode);

    String getPhone();
    void setPhone(String phone);

    String getEmail();
    void setEmail(String email);

    String getAllergy();
    void setAllergy(String allergy);

    String getChildhoodAilments();
    void setChildhoodAilments(String childhoodAilments);

    String getMedicineIntolerance();
    void setMedicineIntolerance(String medicineIntolerance);

    Collection<IExaminationProtocol> getExaminationProtocol() throws CouldNotGetExaminationProtolException;

    void setExaminationProtocol(Collection<IExaminationProtocol> protocols);

	void addExaminationProtocol(IExaminationProtocol examinationProtocol) throws CouldNotAddExaminationProtocol;

    Collection<IDiagnosis> getDiagnoses() throws CouldNotGetDiagnoseException;

	Collection<IMedicine> getMedicine() throws CouldNotGetMedicineException;

    Collection<IExaminationResult> getExaminationResults() throws CouldNotGetExaminationResultException;

    void setExaminationResults(Collection<IExaminationResult> results);

	Collection<IPrescription> getPrescriptions() throws CouldNotGetPrescriptionException;

    Collection<IVisualAid> getVisualAid() throws CouldNotGetVisualAidException;

    void setVisualAid(Collection<IVisualAid> visualAids);

    void setDoctor(IDoctor idoctor);

    void setPrescriptions(Collection<IPrescription> iPrescriptions);

    void setPasswordHash(String password);

    IDomain getDoctor();

    String getPasswordHash();
}
