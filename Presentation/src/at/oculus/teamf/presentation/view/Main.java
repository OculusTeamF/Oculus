/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.presentation.view.exceptions.InitLoadingException;
import at.oculus.teamf.technical.localization.ILocal;
import at.oculus.teamf.technical.loggin.ILogger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/**
 * Created by Karo on 09.04.2015.
 *
 * Description: Initializes GUI and does loading tasks
 *
 */
public class Main extends Application implements ILocal, ILogger {
    public FXMLLoader mainloader = new FXMLLoader(getClass().getResource("fxml/MainWindow.fxml"));
    public FXMLLoader initloader = new FXMLLoader(getClass().getResource("fxml/Init.fxml"));

    // TODO dispatcher
    public static MainController controller;
    public static QueueController controlQueue;

    public static Service<Void> service;

    public static Scene scene;
    final Stage initStage = new Stage(StageStyle.UNDECORATED);;
    final Stage primaryStage = new Stage(StageStyle.DECORATED);

    /*start (init and build) application*/
    @Override
    public void start(Stage mStage) throws Exception {

        //TODO create thread for loading
        //Logger4J.setLevel(log, Level.ERROR);
        initLoadingScreen();        // create splashcreen
        initStage.show();           // show splashcreen

        // cheap thread workaround (can be replaced later)
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        initMainWindow();  // build main window & queuelist)
                        return null;
                    }
                };
            }
            @Override
            protected void succeeded() {
                // Called when finished without exception
                primaryStage.setScene(scene);
                primaryStage.setMaximized(true);
                primaryStage.show();
                initStage.close();
            }
        };

        service.start(); // starts Thread

        //initLoadingScreen();        // create splashcreen
        //initStage.show();           // show splashcreen
        //initMainWindow();           // build main window (heavy queue load process)
        //primaryStage.show();        // show main window
        //initStage.close();          // close splashscreen

    }

    /*create main screen*/
    private void initMainWindow() throws IOException {
        Parent root = (Parent) mainloader.load();
        primaryStage.setTitle(locstring.getString("MainWindowTitle"));      //localization example
        scene = new Scene(root, 900, 600);
        scene.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_default.css").toExternalForm());
        controller = mainloader.getController();

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
        //primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        //primaryStage.show();
    }

    /*create loading screen (splashcreen)*/
    private void initLoadingScreen() throws InitLoadingException {

        // uncomment 'undecorated' code line when splashcreen is running as a thread
        //init = new Stage(StageStyle.UNDECORATED);

        Parent initroot = null;
        try {
            initroot = (Parent) initloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene sceneInit = new Scene(initroot, 553, 362);
        sceneInit.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_default.css").toExternalForm());
        initStage.setScene(sceneInit);
        initStage.setTitle("Oculus is loading...");
        initStage.setResizable(false);
        initStage.centerOnScreen();
        initStage.getIcons().add(new Image("/res/32x32.png"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
