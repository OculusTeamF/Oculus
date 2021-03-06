/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.persistence.virtualproxy;

import at.oculus.teamf.domain.entity.queue.IPatientQueue;

/**
 * Created by Simon Angerer on 08.06.2015.
 */
public class PatientQueueProxyWrapper extends VirtualProxyWrapper<IPatientQueue> {
    protected PatientQueueProxyWrapper() {
        super(IPatientQueue.class);
    }

    @Override
    public IPatientQueue wrap(IPatientQueue domain) {
        return new PatientQueueProxy(domain);
    }
}
