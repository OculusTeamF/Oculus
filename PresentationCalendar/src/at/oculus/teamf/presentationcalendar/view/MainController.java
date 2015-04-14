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
 * Created by Karo on 09.04.2015.
 */
/*
import at.oculus.teamf.application.facade;
*/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Queue;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML private VBox vboxQueues;
    @FXML private TabPane displayPane;
    @FXML private SwingNode swingNode;

    private Queue _patientQueue;
    private Collection<Queue> _allQueues;
    private Tab _newPatientTab;
    private Tab _searchPatientTab;

    private int userAmount = 5;  // only user with permission 2 & 3 (usergroup)
    final TitledPane[] tps = new TitledPane[userAmount];
    final ListView<String> lists[] = new ListView[userAmount];


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // setup css
        displayPane.setId("pane");

        // add panes (TODO get users from UserBroker)
        for (int i = 0; i < userAmount; i++) {
            //create listViews
            lists[i] = new ListView<>();
            lists[i].setPrefSize(200, 250);
            lists[i].minWidth(Region.USE_COMPUTED_SIZE);
            lists[i].minHeight(Region.USE_COMPUTED_SIZE);
            lists[i].maxWidth(Region.USE_COMPUTED_SIZE);
            lists[i].maxHeight(Region.USE_COMPUTED_SIZE);

            //create titlePanes
            tps[i] = new TitledPane("User#" + i, lists[i]);
            tps[i].setExpanded(false);
            tps[i].setAnimated(true);
        }
        tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);

        // fill queuelists
        ObservableList<String> wList = FXCollections.observableArrayList("Donald Duck", "Daisy Duck ", "Dagobert Duck");
        lists[0].setItems(wList);
    }

    /*Close the application by clicking the Menuitem 'Exit'*/
    @FXML
    public void onClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void openCalFX(ActionEvent event) {
        /*try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("agenda2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    @FXML
    public void openCalXtras(ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("agenda2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openCalMig(ActionEvent event) {
       /* try {
            displayPane.getTabs().addAll((Tab)FXMLLoader.load(this.getClass().getResource("agenda.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
 /*       try {
            displayPane.getTabs().addAll((Tab)FXMLLoader.load(this.getClass().getResource("newPatient.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void openPatient(ActionEvent actionEvent) {
       //TODO:
    }

    /*Opens a patient record after patient search*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        //_searchPatientTab = generateTab("Search Patient");
        //displayPane.getTabs().add(_searchPatientTab);
    }
}
