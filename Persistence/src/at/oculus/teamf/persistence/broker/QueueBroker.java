/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.broker;

import at.oculus.teamf.databaseconnection.session.*;
import at.oculus.teamf.domain.entity.Doctor;
import at.oculus.teamf.domain.entity.Orthoptist;
import at.oculus.teamf.domain.entity.Patient;
import at.oculus.teamf.domain.entity.PatientQueue;
import at.oculus.teamf.persistence.entities.DoctorEntity;
import at.oculus.teamf.persistence.entities.OrthoptistEntity;
import at.oculus.teamf.persistence.entities.QueueEntity;
import at.oculus.teamf.persistence.entities.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * QueueBroker.java Created by dgr on 08.04.15.
 */
public class QueueBroker extends EntityBroker<PatientQueue,QueueEntity> {

	public QueueBroker() {

		super(PatientQueue.class, QueueEntity.class);
	}

	public PatientQueue getEntity(ISession session, int id) {
		UserEntity user = null;
		PatientQueue pq = new PatientQueue();
		Collection<Object> entities = null;
		DoctorEntity doc = null;
		OrthoptistEntity ort = null;

		try {
			// load user
			user = (UserEntity) session.getByID(UserEntity.class, id);
			// user is doctor
			for(Object obj : session.getAll(UserEntity.class)){
				DoctorEntity d = (DoctorEntity) obj;
				if(d.getUserId().equals(id)){
					doc = d;
				}
			}
			// user is orthoptist
			if(doc==null) {
				for (Object obj : session.getAll(OrthoptistEntity.class)) {
					OrthoptistEntity o = (OrthoptistEntity) obj;
					if (o.getUserId().equals(id)) {
						ort = o;
					}
				}
			}

			entities = session.getAll(_entityClass);
			for(Object obj : entities){
				QueueEntity qe = (QueueEntity) obj;
				if(qe)
			}

		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		}

		PatientQueue result = persitentToDomain(entity);

		return result;
	}

	public Collection<PatientQueue> getAll(ISession session) {
		QueueEntity qe = (QueueEntity) obj;
		HashMap<Integer, Orthoptist> orthoptists = new HashMap<Integer, Orthoptist>();
		HashMap<Integer, Doctor> doctors = new HashMap<Integer, Doctor>();
		Collection<PatientQueue> patientQueues = new LinkedList<PatientQueue>();

		// create queues for all users
		DoctorBroker<Doctor, DoctorEntity> docBroker = new DoctorBroker<Doctor, DoctorEntity>();
		for(Object obj : docBroker.getAll(session)) {
			Doctor d = (Doctor) obj;
			doctors.put(d.getUserID(), d);
			PatientQueue pq = new PatientQueue();
			pq.setUserID(d.getUserID());
			patientQueues.add(pq);
		}

		Collection<Object> entities = null;
		try {
			entities = (Collection<Object>) session.getAll(_entityClass);
		} catch (BadSessionException e) {
			e.printStackTrace();
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
		}
		for(Object obj : entities){
			// get userId
			int userId = 0;
			if(qe.getDoctor()!=null){
				userId = qe.getDoctor().getUser().getId();
			}
			if(qe.getOrthoptist()!=null){
				userId = qe.getOrthoptist().getUser().getId();
			}

			// set to correct queue

				// search existing queues by user
				PatientQueue patientQueue = null;
				for(PatientQueue pq : patientQueues){
					// insert if match
					if(pq.getUserID()==userId){
						patientQueue == pq;
						Patient patient = (Patient) PatientBroker.getEntity(session, qe.getPatientId());
						pq.insert(, qe.getQueueParent().getPatient());
					} else if (userId = 0 && pq.getUserID()) {

					}
				}
			// create new if no match

		}

		Collection<PatientQueue> domainObjects = new ArrayList<PatientQueue>();
		for(QueueEntity entity : entities) {
			domainObjects.add(persitentToDomain(entity));
		}

		return domainObjects;
	}

	public boolean saveEntity(ISession session, PatientQueue domainObj) {
		QueueEntity entity = domainToPersitent(domainObj);

		try {
			session.beginTransaction();

			session.saveOrUpdate(entity);

			session.commit();

		} catch (BadSessionException e) {
			e.printStackTrace();
			return false;
		} catch (AlreadyInTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (NoTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean saveAll(ISession session, Collection<D> domainObjs) {
		Collection<QueueEntity> entities = new ArrayList<P>();
		for(PatientQueue obj : domainObjs) {
			entities.add(domainToPersitent(obj));
		}

		try {
			session.beginTransaction();
			for(QueueEntity entity : entities){
				session.save(entity);
			}

		} catch (BadSessionException e) {
			e.printStackTrace();
			return false;
		} catch (AlreadyInTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (NoTransactionException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotMappedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	protected PatientQueue persitentToDomain(QueueEntity entity) {
		PatientQueue
		return null;
	}

	@Override
	protected QueueEntity domainToPersitent(PatientQueue entity) {
		return null;
	}
}