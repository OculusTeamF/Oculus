/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.application.facade.dependenceResolverTB2;

import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitatedExceptions;
import at.oculus.teamf.domain.entity.factory.IFactoryTB2;
import at.oculus.teamf.persistence.IFacade;

/**
 * Created by Simon Angerer on 13.05.2015.
 */
public class DependenceResolverTB2 {
    private static IFacade _facade;
    private static IFactoryTB2 _factory;

    private static DependenceResolverTB2 _selfe;

    public static DependenceResolverTB2 getInstance() {
        if(_selfe == null) {
            _selfe = new DependenceResolverTB2();
        }
        return _selfe;
    }

    public static void init(IFacade facade, IFactoryTB2 factory) {
        _facade = facade;
        _factory = factory;
    }

    public IFactoryTB2 getFactory() throws NotInitatedExceptions {
        if(_factory == null) {
            throw new NotInitatedExceptions();
        }

        return _factory;
    }

    public IFacade getFacade() throws NotInitatedExceptions {
        if(_facade == null) {
            throw new NotInitatedExceptions();
        }

        return _facade;
    }
}
