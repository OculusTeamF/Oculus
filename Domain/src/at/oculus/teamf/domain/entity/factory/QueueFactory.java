package at.oculus.teamf.domain.entity.factory;/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;
import at.oculus.teamf.technical.loggin.ILogger;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Simon Angerer on 29.04.2015.
 */
public class QueueFactory implements ILogger{
    private static QueueFactory _selfe = new QueueFactory();

    private HashMap<User, PatientQueue> _userQueues;
    private PatientQueue _generalQueue;

    private HashMap<Class, String> _keyWordMap;

    private boolean _updating;


    public static QueueFactory getInstance() {
        if(_selfe == null) {
            _selfe = new QueueFactory();
        }
        return _selfe;
    }

    private QueueFactory() {
        _userQueues = new HashMap<>();

        _keyWordMap = new HashMap<>();
        _keyWordMap.put(Orthoptist.class, "Orthoptist");
        _keyWordMap.put(Doctor.class, "Doctor");


        /*_updating = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(_updating) {
                    //update orthoptistQueues

                    //update doctoreQueues

                    //update general queue
                }
            }
        }).run();*/
    }

    private Collection<QueueEntry> searchForQueueEntries(User user) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        int id;

        if(user instanceof Doctor) {
            id = ((Doctor) user).getId();
        } else {
            id = ((Orthoptist)user).getId();
        }
        return  Facade.getInstance().search(QueueEntry.class, _keyWordMap.get(user), Integer.toString(id));
    }


    public PatientQueue getUserQueue(User user) {
        if(_userQueues.get(user) == null) {
            try {
                loadUserQueue(user);
            } catch (InvalidSearchParameterException | BadConnectionException | SearchInterfaceNotImplementedException | NoBrokerMappedException e) {
                log.error("Could not load queue from database! Original Message " + e.getMessage());
                return null;
            }
        }
        return _userQueues.get(user);
    }

    private void loadUserQueue(User user) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        PatientQueue queue = createQueue(user, searchForQueueEntries(user));
        _userQueues.put(user, queue);
    }

    public PatientQueue getGeneralQueue() throws SearchInterfaceNotImplementedException, InvalidSearchParameterException, BadConnectionException, NoBrokerMappedException {
        if(_generalQueue == null) {
            loadGeneralQueue();
        }
        return _generalQueue;
    }

    private void loadGeneralQueue() throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException {
        _generalQueue = createQueue(null, (Collection<QueueEntry>)(Collection<?>)Facade.getInstance().search(QueueEntry.class, "General"));
    }

    private PatientQueue createQueue(User user, Collection<QueueEntry> entries) {
        return new PatientQueue(user, entries);
    }


}
