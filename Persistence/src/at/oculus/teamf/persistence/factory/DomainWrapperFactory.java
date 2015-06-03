/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.factory;

import at.oculus.teamf.domain.entity.factory.DomainFactory;
import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.persistence.virtualproxy.VirtualProxyWrapper;

/**
 * Created by Simon Angerer on 01.06.2015.
 */
public class DomainWrapperFactory {
    private static DomainWrapperFactory _selfe;

    private DomainWrapperFactory() {
    //Singelton
    }

    public static DomainWrapperFactory getInstance() {
        if(_selfe == null) {
            _selfe = new DomainWrapperFactory();
        }

        return _selfe;
    }

    public IDomain create(Class<? extends IDomain> clazz) {
        return (IDomain) VirtualProxyWrapper.getWrapper(clazz).wrap(DomainFactory.getFactory(clazz).create());
    }
}
