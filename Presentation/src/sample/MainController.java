package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML private ListView wList1, wList2, wList3, wListO;

    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        //TODO: implement the Object waitingQueue
        ObservableList<String> wList = FXCollections.observableArrayList("Donald Duck", "Daisy Duck ", "Dagobert Duck");
        wList1.setItems(wList);
    }

    @FXML
    public void onClose(ActionEvent actionEvent) {

        System.exit(0);
    }

    @FXML
    public void openCal(ActionEvent actionEvent) {

    }
}
