/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.factory;

import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.*;
import at.oculus.teamf.persistence.Facade;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
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
        _keyWordMap.put(Orthoptist.class, "Orthopist");
        _keyWordMap.put(Doctor.class, "Doctor");


        //currently no automatic updating needed!
        _updating = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("Start updater thread!");
                while(_updating) {
                    //update user queue
                    Collection<User> users = _userQueues.keySet();
                    for(User user : users) {
                        PatientQueue queue = _userQueues.get(user);
                        try {
                            queue.getEntries().removeAll(queue.getEntries());
                            queue.getEntries().addAll(searchForQueueEntries(user));
                        } catch (DatabaseOperationException | ClassNotMappedException | NoBrokerMappedException | BadConnectionException | SearchInterfaceNotImplementedException | InvalidSearchParameterException e) {
                            log.error("Error when trying to automaticaly update queue! Orignial message" + e.getMessage());
                            //eat up
                        }
                    }
                    log.info("Updated all user queues");

                    //update general queue
                }
            }
        }).run();
    }

    /**
     * Uses the Facade to search for all {@QueueEntry} that belong to one user
     * @param user
     * @return list of {@QueueEntry}
     * @throws InvalidSearchParameterException
     * @throws BadConnectionException
     * @throws SearchInterfaceNotImplementedException
     * @throws NoBrokerMappedException
     */
    private Collection<QueueEntry> searchForQueueEntries(User user) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        int id;

        if(user instanceof Doctor) {
            id = ((Doctor) user).getId();
        } else {
            id = ((Orthoptist)user).getId();
        }
        return  Facade.getInstance().search(QueueEntry.class, _keyWordMap.get(user.getClass()), Integer.toString(id));
    }

    /**
     * Creates a PatientQueue for a given User or extracts it from cache
     * @param user that needs a queue
     * @return PatientQueue
     */
    public PatientQueue getUserQueue(User user) {
        if(_userQueues.get(user) == null) {
            try {
                loadUserQueue(user);
            } catch (DatabaseOperationException | ClassNotMappedException | InvalidSearchParameterException | BadConnectionException | SearchInterfaceNotImplementedException | NoBrokerMappedException e) {
                log.error("Could not load queue from database! Original Message " + e.getMessage());
                return null;
            }
        }
        return _userQueues.get(user);
    }

    /**
     * Loads a queue form the database
     * @param user which queue needs to be loaded
     * @throws InvalidSearchParameterException
     * @throws BadConnectionException
     * @throws SearchInterfaceNotImplementedException
     * @throws NoBrokerMappedException
     */
    private void loadUserQueue(User user) throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        PatientQueue queue = createQueue(user, searchForQueueEntries(user));
        _userQueues.put(user, queue);
    }

    /**
     * Creates a general patient queue or loads it from cache
     * @return
     */
    public PatientQueue getGeneralQueue(){
        if(_generalQueue == null) {
            try {
                loadGeneralQueue();
            } catch (InvalidSearchParameterException | BadConnectionException | NoBrokerMappedException | SearchInterfaceNotImplementedException | DatabaseOperationException | ClassNotMappedException e) {
                log.error("Could not load queue from database! Original Message " + e.getMessage());
                return null;
            }
        }
        return _generalQueue;
    }

    /**
     * Loads  the general queue from the facade
     * @throws InvalidSearchParameterException
     * @throws BadConnectionException
     * @throws SearchInterfaceNotImplementedException
     * @throws NoBrokerMappedException
     */
    private void loadGeneralQueue() throws InvalidSearchParameterException, BadConnectionException, SearchInterfaceNotImplementedException, NoBrokerMappedException, DatabaseOperationException, ClassNotMappedException {
        _generalQueue = createQueue(null, (Collection<QueueEntry>)(Collection<?>)Facade.getInstance().search(QueueEntry.class, "General"));
    }

    /**
     * Creates new PatientQueue from a User and entries
     * @param user
     * @param entries
     * @return
     */
    private PatientQueue createQueue(User user, Collection<QueueEntry> entries) {
        return new PatientQueue(user, entries);
    }


}
