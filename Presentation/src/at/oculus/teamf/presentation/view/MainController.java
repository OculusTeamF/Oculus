/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;
/**
 * Created by Karo on 09.04.2015.
 */
import at.oculus.teamf.application.facade.SearchPatientController;
import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import jfxtras.labs.scene.control.window.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final MainController mainController = new MainController();

    private Collection<PatientQueue> _allQueues;
    private Tab _newPatientTab;
    private Tab _calendarTab;
    private Tab _searchPatientTab;
    private User _user;
    private StartupController _startupController = new StartupController();

    @FXML private TabPane displayPane;
    @FXML private ListView wList1, wList2, wList3, wListO;

    private MainController() {}

    public static MainController getInstance()
    {
        return mainController;
    }

    /**
     * Initialize the waiting queue
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> patients = null;
        String lname;
        String fname;

        _allQueues =  _startupController.getAllQueues();

        for(PatientQueue pq : _allQueues)
        {
            for(QueueEntry qe : pq.getEntries()){

                System.out.println(qe.getPatient().getLastName());
                wList1.setItems((ObservableList) pq.getEntries());
            }

            }

    }

       /*
        ObservableList<String> wList = FXCollections.observableArrayList("Donald Duck", "Daisy Duck ", "Dagobert Duck");
        wList1.setItems(wList);*/

    /*Close the application by clicking the Menuitem 'Exit'*/
    @FXML
    public void onClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    /*Opens the calendar view by clicking Menuitem 'Calendar'*/
    @FXML
    public void openCal(ActionEvent event) {
        generateTab("Calendar");
    }
    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        generateTab("New Patient");
    }
    @FXML
    public void openPatient(ActionEvent actionEvent) {
       //TODO:
    }

    /*Opens a patient search tab*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        generateTab("Search Patient");
    }

    /**
     * Opens new Tabs on displayscreen
     * @param tabName
     * @return
     */
     public void generateTab(String tabName) {
        Tab tab = new Tab(tabName);

        Group root = new Group();
        tab.setContent(root);

        Window w = new Window(tabName);
        w.setLayoutX(40);
        w.setLayoutY(40);
       /* w.setPrefSize(900, 700);*/
        /*w.getRightIcons().add(new MinimizeIcon(w));*/
        w.getRightIcons().add(new CloseIconImpl(w, displayPane, tab));

        if (tabName.equals("New Patient")) {
            try {
                w.getContentPane().getChildren().add((Node) FXMLLoader.load(getClass().getResource("patientRecord.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tabName.equals("Calendar")) {
            try {
                w.getContentPane().getChildren().add((Node) FXMLLoader.load(getClass().getResource("agenda.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tabName.equals("Search Patient")) {
            try {
                w.getContentPane().getChildren().add((Node) FXMLLoader.load(getClass().getResource("searchPatient.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tabName.equals("Patient Record")) {
            try {
                w.getContentPane().getChildren().add((Node) FXMLLoader.load(getClass().getResource("patientRecord.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            root.getChildren().add(w);
            displayPane.getTabs().add(tab);
            displayPane.getSelectionModel().selectNext();
    }
}
