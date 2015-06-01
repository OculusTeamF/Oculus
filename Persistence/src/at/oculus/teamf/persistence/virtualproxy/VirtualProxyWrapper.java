/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.interfaces.IDomain;

import java.util.HashMap;

/**
 * Created by Simon Angerer on 28.05.2015.
 */
public abstract class VirtualProxyWrapper {
    private static HashMap<Class, VirtualProxyWrapper> _wrapperMapping;

    protected VirtualProxyWrapper(Class domainClass) {

        _wrapperMapping.put(domainClass, this);
    }

    private static void initWrapperMapping() {
        _wrapperMapping = new HashMap<>();

        //init all wrapper
        new DefaultWrapper();
        new PatientProxyWrapper();
        new DoctorProxyWrapper();
        new DiagnosisProxyWrapper();
    }

    public abstract IDomain wrap(IDomain domain);

    public static VirtualProxyWrapper getWrapper(Class domainClass) {
        if (_wrapperMapping == null) {
            initWrapperMapping();
        }

        VirtualProxyWrapper vpw;
        if (_wrapperMapping.containsKey(domainClass)) {
            vpw = _wrapperMapping.get(domainClass);
        } else {
            vpw = _wrapperMapping.get(null);
        }

        return vpw;
    }
}
