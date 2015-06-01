/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.controller.exceptions.CheckinControllerExceptions.CheckinControllerException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotAddPatientToQueueException;
import at.oculus.teamf.domain.entity.exception.patientqueue.CouldNotRemovePatientFromQueueException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.domain.entity.interfaces.IPatientQueue;
import at.oculus.teamf.domain.entity.interfaces.IQueueEntry;
import at.oculus.teamf.domain.entity.interfaces.IUser;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.presentation.view.DialogBoxController;
import javafx.application.Platform;

import java.util.Collection;

/**
 * Created by Fabian on 10.05.2015.
 */
public class QueueModel {

    private static QueueModel _queuemodel = new QueueModel();
    private Model _model;

    public static QueueModel getInstance() {
        if(_queuemodel == null) {
            _queuemodel = new QueueModel();
        }

        return _queuemodel;
    }


    /**
     * returns the Queue from the given User
     * @param user
     * @return IPatientQueue
     */
    public IPatientQueue getQueueFromUser(IUser user)
    {
        _model = Model.getInstance();
        IPatientQueue queue = null;

        try {
            queue =  _model.getStartupController().getQueueByUser(user);
        } catch (BadConnectionException | NoBrokerMappedException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("BadConnectionException | NoBrokerMappedException", "Please contact support");
        } catch (final NullPointerException nullPointerException) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    DialogBoxController.getInstance().showErrorDialog("NullPointerException", "Please contact support");
                }
            });
        }
        return queue;
    }

    /**
     * returns a Collection of the QueueEntries of the UserQueue
     * @param user
     * @return Collection<QueueEntry>
     */
    public Collection<IQueueEntry> getEntriesFromQueue(IUser user) {
        _model = Model.getInstance();
        Collection<IQueueEntry> entries = null;

        IPatientQueue queue = getQueueFromUser(user);
        entries = queue.getEntries();
        return entries;
    }

    /**
     * insert the Patient into the right Queue from given User
     * @param user
     */
    public void insertPatientIntoQueue(IUser user){

        _model = Model.getInstance();

        try {
            _model.getCheckinController().insertPatientIntoQueue(_model.getPatient(), user);
        } catch (BadConnectionException badConnectionException) {
            badConnectionException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("BadConnectionException", "Please contact support");
        } catch (CheckinControllerException checkinControllerException) {
            checkinControllerException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CheckinControllerException", "Please contact support");
        } catch (CriticalClassException criticalClassException) {
            criticalClassException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CriticalClassException", "Please contact support");
        } catch (CouldNotAddPatientToQueueException couldNotAddPatientToQueueException) {
            couldNotAddPatientToQueueException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotAddPatientToQueueException", "Make sure that the patient is not in a queue.");
        }
        _model.refreshQueue(user);
    }

    /**
     * removes the patient from the queue and opens a new examinationTab
     * @param patient
     */
    public void removePatientFromQueue(IPatient patient, IUser user)
    {
        _model = Model.getInstance();
        IPatientQueue queue = getQueueFromUser(user);

        try {
            _model.getReceivePatientController().removePatientFromQueue(patient, queue);
        } catch (CouldNotRemovePatientFromQueueException couldNotRemovePatientFromQueueException) {
            couldNotRemovePatientFromQueueException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotRemovePatientFromQueueException", "Please contact support");
        }

        _model.getTabModel().addExaminationTab(patient);
        _model.refreshQueue(user);
    }

    /**
     * delete the patient without starting an examination
     * @param patient
     */
    public void deletePatientFromQueue(IPatient patient, IUser user)
    {
        _model = Model.getInstance();
        IPatientQueue queue = getQueueFromUser(user);

        try {
            _model.getReceivePatientController().removePatientFromQueue(patient, queue);
        } catch (CouldNotRemovePatientFromQueueException couldNotRemovePatientFromQueueException) {
            couldNotRemovePatientFromQueueException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("CouldNotRemovePatientFromQueueException", "Please contact support");
        }

        _model.refreshQueue(user);
    }
}
