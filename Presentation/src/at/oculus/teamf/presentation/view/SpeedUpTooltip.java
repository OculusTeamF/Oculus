/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Field;

/**
 * Created by Karo on 19.05.2015.
 */
public class SpeedUpTooltip extends Tooltip
{
    private Tooltip _tooltip;

    public SpeedUpTooltip(){

        _tooltip = new Tooltip();
        _tooltip = speedUpTooltip(_tooltip);
    }

    //speeds up the Tooltip
    public Tooltip speedUpTooltip(Tooltip tooltip){

        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));

        } catch (Exception e) {
            e.printStackTrace();

        }
        return tooltip;
    }

}
