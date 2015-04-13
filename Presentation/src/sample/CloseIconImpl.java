/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package sample;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.Window;

/**
 * Created by Karo on 11.04.2015.
 */
public class CloseIconImpl extends CloseIcon
{
    public CloseIconImpl(Window w, TabPane pane, Tab tab) {
        super(w);

        this.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                w.close();
                pane.getTabs().remove(tab);
            }

        });
    }
}
