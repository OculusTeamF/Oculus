/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.domain.entity.user.orthoptist;

import at.oculus.teamf.domain.entity.IDomain;
import at.oculus.teamf.domain.entity.factory.DomainFactory;

/**
 * Created by Simon Angerer on 04.06.2015.
 */
public class OrthoptistFactory extends DomainFactory<IOrthoptist> {
    public OrthoptistFactory() {
        super(IOrthoptist.class);
    }

    @Override
    public IOrthoptist create() {
        return new Orthoptist();
    }
}
