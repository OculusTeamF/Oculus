/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.resourcebundel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Simon Angerer on 26.04.2015.
 */
public class HashResourceBundle extends ResourceBundle {

    private HashMap<String, Object> _map;

    public HashResourceBundle(HashMap<String, Object> map) {
        _map = map;
    }

    @Override
    protected Object handleGetObject(String key) {
        return _map.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }
}
