/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Karo on 09.04.2015.
 */
public class Main extends Application {

    public  FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
    public MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception  {

        Parent root = (Parent) loader.load();
        primaryStage.setTitle("OCULUS - Gemeinschaftspraxis Dr. Tavolato");
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
        controller = loader.getController();

        // setup components in main.fxml
        controller.getSplitter().setDividerPosition(0, 0.75);

        // update splitter position
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                controller.getSplitter().setDividerPosition(0, 0.75);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                controller.getSplitter().setDividerPosition(0, 0.75);
            }
        });

        // add app icon
        //primaryStage.getIcons().add(new Image("file:icon.png"));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
