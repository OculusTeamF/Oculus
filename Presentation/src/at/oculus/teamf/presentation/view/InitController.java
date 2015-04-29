/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.technical.loggin.ILogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Fabian on 23.04.2015.
 *
 *  Class: InitController
 *  Description: Handles the loading screen. Thread loading not yet implemented.
 *
 */
public class InitController implements Initializable, ILogger {


    @FXML ProgressIndicator loader;
    @FXML Label loadLabel;
    @FXML Label labelQuote;
    @FXML Pane loaderPane;
    @FXML Rectangle initRectangle;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

       // run init as thread (unused currently)
       /*  Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new Thread(new Runnable() {
                    @Override public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }
                }).start();
                return null;
            }
        };
        Thread th = new Thread(task);
        th.start();*/

        // set true when init window is set "undeocarted"
        initRectangle.setVisible(true);

        // comment this out if 'QuoteGenerator' create errors
        labelQuote.setText(getQuote());
        labelQuote.setEffect(new DropShadow());
    }

    /* fetch quote from quote webpgae and print it on loading screen */
    private String getQuote(){
        String html = "http://ivyjoy.com/quote.shtml";
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");

            //Elements tableHeaderEles = tableElements.select("thead tr th");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Element row = tableRowElements.get(0);

            Elements rowItems = row.select("td");
            for (int j = 0; j < rowItems.size(); j++) {
                return (rowItems.get(j).text());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
