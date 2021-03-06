/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistencetests.brokertests;

import at.oculus.teamf.domain.entity.examination.ExaminationProtocol;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.user.doctor.IDoctor;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationResultException;
import at.oculus.teamf.domain.entity.examination.IExaminationResult;
import at.oculus.teamf.domain.entity.user.orthoptist.IOrthoptist;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class ExaminationProtocolBrokerTest extends BrokerTest {
	private IPatient _patient;
	private IDoctor _doctor;
	private IOrthoptist _orthoptist;
	private ExaminationProtocol _examinationProtocolDoctor;
	private ExaminationProtocol _examinationProtocolOrthoptist;

	@Override
	public void setUp() {
		try {
			_patient = Facade.getInstance().getById(IPatient.class, 1);
			_doctor = Facade.getInstance().getById(IDoctor.class, 1);
			_orthoptist = Facade.getInstance().getById(IOrthoptist.class, 1);
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}

		_examinationProtocolDoctor = new ExaminationProtocol();
		_examinationProtocolDoctor.setStartTime(new Date());
		_examinationProtocolDoctor.setEndTime(new Date());
		_examinationProtocolDoctor.setDescription("Untersuchungsprotokoll Unit Test");
		_examinationProtocolDoctor.setPatient(_patient);
		_examinationProtocolDoctor.setDoctor(_doctor);

		_examinationProtocolOrthoptist = new ExaminationProtocol();
		_examinationProtocolOrthoptist.setStartTime(new Date());
		_examinationProtocolOrthoptist.setEndTime(new Date());
		_examinationProtocolOrthoptist.setDescription("Untersuchungsprotokoll Unit Test");
		_examinationProtocolOrthoptist.setPatient(_patient);
		_examinationProtocolOrthoptist.setOrthoptist(_orthoptist);

		try {
			assertTrue(Facade.getInstance().save(_examinationProtocolDoctor));
			assertTrue(Facade.getInstance().save(_examinationProtocolOrthoptist));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void tearDown() {
		try {
			assertTrue(Facade.getInstance().delete(_examinationProtocolDoctor));
			assertTrue(Facade.getInstance().delete(_examinationProtocolOrthoptist));
		} catch (FacadeException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Override
	public void testGetById() {
		ExaminationProtocol examinationProtocolDoctor = null;
		ExaminationProtocol examinationProtocolOrthoptist = null;
		try {
			examinationProtocolDoctor =
					Facade.getInstance().getById(ExaminationProtocol.class, _examinationProtocolDoctor.getId());
			examinationProtocolOrthoptist =
					Facade.getInstance().getById(ExaminationProtocol.class, _examinationProtocolOrthoptist.getId());
		} catch (FacadeException e) {
			assertTrue(false);
			e.printStackTrace();
		}
		assertTrue(examinationProtocolDoctor != null);
		//		assertTrue(examinationProtocolDoctor.getDoctor().getId()==1);
		assertTrue(examinationProtocolDoctor.getPatient().getId() == 1);
		assertTrue(examinationProtocolDoctor.getDescription().equals("Untersuchungsprotokoll Unit Test"));
		assertTrue(examinationProtocolDoctor.getStartTime() != null);
		assertTrue(examinationProtocolDoctor.getEndTime() != null);
		assertTrue(examinationProtocolOrthoptist != null);
		//		assertTrue(examinationProtocolOrthoptist.getOrthoptist().getId()==1);
		assertTrue(examinationProtocolOrthoptist.getPatient().getId() == 1);
		assertTrue(examinationProtocolOrthoptist.getDescription().equals("Untersuchungsprotokoll Unit Test"));
		assertTrue(examinationProtocolOrthoptist.getStartTime() != null);
		assertTrue(examinationProtocolOrthoptist.getEndTime() != null);
	}


	@Override
	public void testGetAll() {
		// too much records in database
		/*Collection<ExaminationProtocol> examinationProtocolCollection = null;
		try {
			examinationProtocolCollection = Facade.getInstance().getAll(ExaminationProtocol.class);
		} catch (FacadeException e) {
			e.printStackTrace();
		}
		assertTrue(examinationProtocolCollection!=null);
		assertTrue(examinationProtocolCollection.size()>0);*/
	}

	@Test
	public void reloadExaminationResults() {
		ExaminationProtocol examinationProtocol = null;

		try {
			examinationProtocol = Facade.getInstance().getById(ExaminationProtocol.class, 1);
		} catch (BadConnectionException | NoBrokerMappedException | DatabaseOperationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Collection<IExaminationResult> examinationResults = null;

		try {
			examinationResults = examinationProtocol.getExaminationResults();
		} catch (CouldNotGetExaminationResultException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		assertTrue(examinationResults.size() == 1);
	}
}