/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentationcalendar.view;

/**
 * MiG Calendar Test (embedded swing into javafx)
 *
 * @author Fabian Salzgeber
 * @date 13.4.2015
 */

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;


public class agendaController implements Initializable{
    @FXML private SwingNode swingNode;

    private JPanel test = new calendarFrame();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createSwingContent(swingNode);
    }

    private void createSwingContent(final SwingNode swingNode) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                swingNode.setContent(test);
                //swingNode.setContent(new JButton("Click me!"));

            }
        });
    }
}
