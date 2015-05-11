/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence;

import at.oculus.teamf.databaseconnection.session.exception.ClassNotMappedException;
import at.oculus.teamf.domain.entity.interfaces.IDomain;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.DatabaseOperationException;
import at.oculus.teamf.persistence.exception.FacadeException;
import at.oculus.teamf.persistence.exception.NoBrokerMappedException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.persistence.exception.search.SearchInterfaceNotImplementedException;

import java.util.Collection;

/**
 * IFacade.java
 * Created by oculus on 08.05.15.
 */
public interface IFacade {
	//public static IFacade getInstance();
	public <T> T getById(Class clazz, int id) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException;
    public <T> Collection<T> getAll(Class clazz) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException;
    //public void reloadCollection(IDomain obj, Class clazz) throws BadConnectionException, NoBrokerMappedException, ReloadInterfaceNotImplementedException, InvalidReloadClassException, DatabaseOperationException;
    public boolean save(IDomain obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException;
    public boolean saveAll(Collection<IDomain> obj) throws BadConnectionException, NoBrokerMappedException, DatabaseOperationException;
    public boolean delete(IDomain obj) throws BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, DatabaseOperationException;
    public boolean deleteAll(Collection<IDomain> obj) throws FacadeException, ClassNotMappedException;
    public <T> Collection<T> search(Class clazz, String... search) throws SearchInterfaceNotImplementedException, BadConnectionException, NoBrokerMappedException, InvalidSearchParameterException, DatabaseOperationException;
}
