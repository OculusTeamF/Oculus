package at.oculus.teamf.domain.entity;/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.util.HashMap;

/**
 * Created by Simon Angerer on 29.04.2015.
 */
class QueueFactory {
    private static QueueFactory _selfe = new QueueFactory();

    private HashMap<Orthoptist, PatientQueue> _orthoptistPatientQueues;
    private HashMap<Doctor, PatientQueue> _doctorPatientQueues;
    private PatientQueue _generalQueue;

    private boolean _updating;


    public static QueueFactory getInstance() {
        if(_selfe == null) {
            _selfe = new QueueFactory();
        }
        return _selfe;
    }

    private QueueFactory() {
        _orthoptistPatientQueues = new HashMap<>();
        _doctorPatientQueues = new HashMap<>();

        _updating = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(_updating) {
                    //update orthoptistQueues

                    //update doctoreQueues

                    //update general queue
                }
            }
        }).run();
    }

    private PatientQueue searchForQueue(String key, String id) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
       return (PatientQueue) Facade.getInstance().search(PatientQueue.class, key, id);
    }

    public PatientQueue getOrtoptistQueue(Orthoptist orthoptist) throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException {
        if(_doctorPatientQueues.get(orthoptist) == null) {
            loadOrtoptistQueue(orthoptist);
        }
        return _orthoptistPatientQueues.get(orthoptist);
    }

    private void loadOrtoptistQueue(Orthoptist orthoptist) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        PatientQueue queue = searchForQueue("Orthoptist", Integer.toString(orthoptist.getId()));
        _orthoptistPatientQueues.put(orthoptist, queue);
    }

    public PatientQueue getDoctorQueue(Doctor doctor) throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException {
        if(_doctorPatientQueues.get(doctor) == null) {
            loadDoctorQueue(doctor);
        }
        return _orthoptistPatientQueues.get(doctor);
    }

    private void loadDoctorQueue(Doctor doctor) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        PatientQueue queue = searchForQueue("Doctor", Integer.toString(doctor.getId()));
        _doctorPatientQueues.put(doctor, queue);
    }

    public PatientQueue getGeneralQueue() throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException {
        if(_generalQueue == null) {
            loadGeneralQueue();
        }
        return _generalQueue;
    }

    private void loadGeneralQueue() throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        _generalQueue = (PatientQueue) Facade.getInstance().search(PatientQueue.class, "General");
    }

    private void createQueue(QueueEntry entry) {

    }


}
