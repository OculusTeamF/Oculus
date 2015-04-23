/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.technical.localization.ILocal;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Created by Karo on 09.04.2015.
 */
public class Main extends Application implements ILocal {

    public FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainWindow.fxml"));
    public FXMLLoader initloader = new FXMLLoader(getClass().getResource("fxml/Init.fxml"));
    public static MainController controller;
    public static Stage stage;
    public static Stage istage;

    @Override
    public void start(Stage primaryStage) throws Exception  {
        // splashscreen test
        Parent initroot = (Parent) initloader.load();
        Stage init = new Stage();
        Scene sceneInit = new Scene(initroot, 472, 314);
        sceneInit.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
        init.setScene(sceneInit);
        init.setTitle("Oculus is loading...");
        init.setResizable(false);
        init.centerOnScreen();
        init.getIcons().add(new Image("/res/32x32.png"));
        init.show();


        // main window load
        Parent root = (Parent) loader.load();

        primaryStage.setTitle(locstring.getString("MainWindowTitle"));      //localization example
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
        controller = loader.getController();

        // setup components in MainWindow.fxml
        controller.getSplitter().setDividerPosition(0, 0.80);

        // update splitter position
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                controller.getSplitter().setDividerPosition(0, 0.25);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                controller.getSplitter().setDividerPosition(0, 0.25);
            }
        });

        // add app icon
        primaryStage.getIcons().add(new Image("/res/32x32.png"));

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        stage = primaryStage;
        primaryStage.show();
        init.close();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
