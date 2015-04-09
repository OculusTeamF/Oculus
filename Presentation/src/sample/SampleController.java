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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class SampleController implements Initializable{

    public GridPane displayPane;
    @FXML private ListView wList1, wList2, wList3, wListO;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //TODO: implement the Object waitingQueue
        ObservableList<String> wList = FXCollections.observableArrayList("Donald Duck", "Daisy Duck ", "Dagobert Duck");
        wList1.setItems(wList);
    }

    public void onClose(ActionEvent actionEvent)
    {
        System.exit(0);
    }

    public void openCal(ActionEvent actionEvent)
    {
        /*try {
            displayPane.getChildren().set(0, FXMLLoader.load(getClass().getResource("agenda.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            Stage agenda = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("agenda.fxml"));
            Scene scene = new Scene(root);
            agenda.setScene(scene);
            agenda.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
