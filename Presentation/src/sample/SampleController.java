/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable{

    @FXML private ListView wList1, wList2, wList3, wListO;
    @FXML Stage _agendaStage;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //TODO: implement the Object waitingQueue
        ObservableList<String> wList = FXCollections.observableArrayList("Donald Duck", "Daisy Duck ", "Dagobert Duck");
        wList1.setItems(wList);
    }

    /*
        Close Main Window with MenuItem in Menubar
     */
    @FXML
    public void onClose(ActionEvent actionEvent)
    {
        System.exit(0);
    }


    public void openCal() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("agenda.fxml"));
        Stage _agendaStage = new Stage();
        Scene agendaScene = new Scene(root);
        _agendaStage.setScene(agendaScene);
        _agendaStage.show();
    }
}
