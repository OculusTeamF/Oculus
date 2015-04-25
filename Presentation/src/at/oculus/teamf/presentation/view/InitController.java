/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.technical.loggin.ILogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Fabian on 23.04.2015.
 */
public class InitController implements Initializable, ILogger {


    @FXML ProgressIndicator loader;
    @FXML Label loadLabel;
    @FXML Pane loaderPane;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

      /*  Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                new Thread(new Runnable() {
                    @Override public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }
                }).start();
                return null;
            }
        };

        Thread th = new Thread(task);
        th.start();*/

    }

}
