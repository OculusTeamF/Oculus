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

import at.oculus.teamf.application.facade.StartupController;
import at.oculus.teamf.domain.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.window.Window;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML public MenuItem openPatientsearch;
    @FXML private VBox vboxQueues;
    @FXML private TabPane displayPane;
    //@FXML private ListView wList1, wList2, wList3, wListO;
    @FXML private SplitPane splitter;

    private Collection<PatientQueue> _allQueues;
    private Tab _newPatientTab;
    private Tab _calendarTab;
    private Tab _searchPatientTab;
    private User _user;
    private StartupController _startupController = new StartupController();

    private TitledPane[] tps;
    private ListView<String> lists[];

    /**
     * Initialize the waiting queue
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<IDoctor> doctors = FXCollections.observableArrayList(_startupController.getAllDoctors());
        ObservableList<IOrthoptist> orthoptists = FXCollections.observableArrayList(_startupController.getAllOrthoptists());

        int userAmount =  doctors.size() + orthoptists.size(); // only user with permission 2 & 3 (usergroup)
        tps = new TitledPane[userAmount];
        lists = new ListView[userAmount];


        // setup listviews
        for (int i = 0; i < userAmount; i++) {
            lists[i] = new ListView<>();
            lists[i].setPrefSize(200, 250);
            lists[i].minWidth(Region.USE_COMPUTED_SIZE);
            lists[i].minHeight(Region.USE_COMPUTED_SIZE);
            lists[i].maxWidth(Region.USE_COMPUTED_SIZE);
            lists[i].maxHeight(Region.USE_COMPUTED_SIZE);
            lists[i].setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        System.out.println("clicked on " + lists[0].getSelectionModel().getSelectedItem());
                        addPatientTab();
                    }
                }
            });
        }


        // setup titlepane (doctors & orthoptists)
        // todo: add unassigned patients to list
        String userQueueName;
        int orth = 0;
        for (int i = 0; i < userAmount; i++) {
            if (i < doctors.size()) {
                userQueueName = doctors.get(i).getTitle() + " " + doctors.get(i).getFirstName() + " " + doctors.get(i).getLastName();
            } else {
                orth = i - doctors.size();
                if (orthoptists.get(orth).getTitle() != null) {
                    userQueueName = orthoptists.get(orth).getTitle() + " " + orthoptists.get(orth).getFirstName() + " " + orthoptists.get(orth).getLastName();
                } else {
                    userQueueName = orthoptists.get(orth).getFirstName() + " " + orthoptists.get(orth).getLastName();
                }
            }

            tps[i] = new TitledPane(userQueueName, lists[i]);
            tps[i].setExpanded(false); tps[i].setAnimated(true);
        }

        // add to stage
        tps[0].setExpanded(true);
        vboxQueues.getChildren().addAll(tps);

        // fill queuelists with available patients
        LinkedList<ObservableList<String>> userQueueLists = new LinkedList();

        List<String> list = new ArrayList<String>();
        ObservableList<String> userQList = FXCollections.observableList(list);

        for (QueueEntry qe : doctors.get(0).getQueue().getEntries()) {
            userQList.add(qe.getPatient().getFirstName() + " " + qe.getPatient().getLastName() + " / " + qe.getPatient().getSocialInsuranceNr());
        }
        userQueueLists.add(userQList);
        lists[0].setItems(userQueueLists.get(0));

        //userQList.clear();

  /*      for (QueueEntry qe : doctors.get(1).getQueue().getEntries()) {
            userQList.add(qe.getPatient().getFirstName());
        }
        userQueueLists.add(userQList);
        lists[1].setItems(userQueueLists.get(1));*/

    }

    /*Close the application by clicking the Menuitem 'Exit'*/
    @FXML
    public void onClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    /*Opens the calendar view by clicking Menuitem 'Calendar'*/
    @FXML
    public void openCal(ActionEvent event) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("agenda2.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Opens a new Patient record to add a patient*/
    @FXML
    public void newPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("newPatientTab.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPatientTab(){
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("patientRecordTab.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SplitPane getSplitter(){
        return this.splitter;
    }

    @FXML
    public void openPatient(ActionEvent actionEvent) {
       //TODO:
    }

    /*Opens a patient search tab*/
    @FXML
    public void searchPatient(ActionEvent actionEvent) {
        try {
            displayPane.getTabs().addAll((Tab) FXMLLoader.load(this.getClass().getResource("searchPatientTab.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens new Tabs on displayscreen
     * @param tabName
     * @return
     */
    public Tab generateTab(String tabName) {
        Tab tab = new Tab(tabName);

        Group root = new Group();
        tab.setContent(root);

        Window w = new Window(tabName);
        w.setLayoutX(40);
        w.setLayoutY(40);
        w.setPrefSize(1400, 900);
        w.getRightIcons().add(new CloseIconImpl(w, displayPane, tab));

        if (tabName.equals("New Patient")) {
            try {
                w.getContentPane().getChildren().add((Node) FXMLLoader.load(getClass().getResource("newPatient.fxml")));
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
            return tab;
    }

    public TabPane getDisplayPane(){
        return displayPane;
    }

}
