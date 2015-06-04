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
import at.oculus.teamf.presentation.view.models.Model;
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

    public static MainController _controller;
    private Model _model;

    public static Service<Void> _service;

    public static Scene _scene;
    final Stage _initStage = new Stage(StageStyle.UNDECORATED);;
    final Stage _primaryStage = new Stage(StageStyle.DECORATED);

    /*start (init and build) application*/
    @Override
    public void start(Stage mStage) throws Exception {

        //Logger4J.setLevel(log, Level.ERROR);
        initLoadingScreen();        // create splashcreen
        _initStage.show();           // show splashcreen

        // cheap thread workaround (can be replaced later)
        _service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        _model = Model.getInstance();
                        try {
                            initMainWindow();  // build main window & queuelist)
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        _model.setPrimaryStage(_primaryStage);
                        return null;
                    }
                };
            }
            @Override
            protected void succeeded() {
                // Called when finished without exception
                _primaryStage.setScene(_scene);
                _primaryStage.setMaximized(true);
                _primaryStage.show();
                _initStage.close();
            }
        };

        _service.start(); // starts Thread
    }

    /*create main screen*/
    private void initMainWindow() throws IOException {
        Parent root = (Parent) mainloader.load();
        _primaryStage.setTitle(locstring.getString("MainWindowTitle"));   //localization example
        _scene = new Scene(root, 900, 600);
        _scene.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_default.css").toExternalForm());
        _controller = mainloader.getController();

        // setup components in MainWindow.fxml
        _controller.getSplitter().setDividerPosition(0, 0.80);


        // update splitter position
        _scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                _controller.getSplitter().setDividerPosition(0, 0.25);
            }
        });
        _scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                _controller.getSplitter().setDividerPosition(0, 0.25);
            }
        });

        // add app icon
        _primaryStage.getIcons().add(new Image("/res/32x32.png"));
    }

    /*create loading screen (splashcreen)*/
    private void initLoadingScreen() throws InitLoadingException {
        Parent initroot = null;
        try {
            initroot = initloader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            DialogBoxController.getInstance().showErrorDialog("IOException", "Please contact support");
        }

        Scene sceneInit = new Scene(initroot, 553, 362);
        sceneInit.getStylesheets().addAll(this.getClass().getResource("/styles/stylesheet_default.css").toExternalForm());
        _initStage.setScene(sceneInit);
        _initStage.setTitle("Oculus is loading...");
        _initStage.setResizable(false);
        _initStage.centerOnScreen();
        _initStage.getIcons().add(new Image("/res/32x32.png"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
