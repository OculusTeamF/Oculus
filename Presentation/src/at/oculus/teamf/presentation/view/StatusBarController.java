/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.controlsfx.control.StatusBar;

/**
 * Created by Fabian on 25.04.2015.
 *
 *  Class: StatusBarController
 *  Description: Returns instance for statebar for presenation module
 *               Add here custom statebar functions.
 *
 */
public class StatusBarController {

    private static StatusBar _stateBar = new StatusBar();

    private StatusBarController() {
        // singleton
    };

    public static StatusBar getInstance() {
        if(_stateBar == null) {
            _stateBar = new StatusBar();
        }

        return _stateBar;
    }

    // *******************************************************************
    // Some additional functions (progressbar)
    // *******************************************************************

    public static void showProgressBarIdle(String loadingtext) {
        ProgressBar pb = new ProgressBar();
        Label lb = new Label(loadingtext + "  ");
        _stateBar.getRightItems().add(lb);
        _stateBar.getRightItems().add(pb);
    }

    public static void hideStatusBarProgressBarIdle(){
        _stateBar.getRightItems().remove(0, StatusBarController.getInstance().getRightItems().size());
    }

}
