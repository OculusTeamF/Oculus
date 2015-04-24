/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.LoginDialog;
import org.controlsfx.dialog.ProgressDialog;

/**
 * Created by Fabian on 23.04.2015.
 */
public class DialogBoxController {


    private static DialogBoxController _selfe;

    private DialogBoxController() { /*singelton*/ };

    public static DialogBoxController getInstance() {
        if(_selfe == null) {
            _selfe = new DialogBoxController();
        }

        return _selfe;
    }

    // *******************************************************************
    // Information Dialog
    // *******************************************************************
    public void showInformationDialog(String title, String information){
        Alert dlg = createAlert(Alert.AlertType.INFORMATION);
        dlg.setTitle(title);
        dlg.setContentText(information);
        //dlg.getDialogPane().setGraphic(new ImageView(new Image(getClass().getResource("../controlsfx-logo.png").toExternalForm())));
        dlg.show();
    }

    // *******************************************************************
    // Error Dialog
    // *******************************************************************
    public void showErrorDialog(String title, String information){
        Alert dlg = createAlert(Alert.AlertType.ERROR);
        dlg.setTitle(title);
        dlg.setContentText(information);
        dlg.show();
    }

    // *******************************************************************
    // Choice (ComboBox) Dialog
    // *******************************************************************
    public void showChoiceComboDialog(String title, String information){
        ChoiceDialog<String> dlg = new ChoiceDialog<>("Donald Duck", "Doktor 3", "Doktor 4", "Usw", "Usw2");
        dlg.setTitle(title);
        dlg.setContentText(information);
        dlg.show();
    }

    // *******************************************************************
    // Progressbox
    // *******************************************************************
    public void showLoadOculusDialog(){
        Task<Object> worker = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 99);
                    updateMessage("Loading queues: " + i + "%");
                    Thread.sleep(100);
                }
                return null;
            }
        };



        ProgressDialog dlg = new ProgressDialog(worker);
        dlg.initModality(Modality.NONE);
        dlg.setHeaderText("Hallo");
        dlg.setContentText("Loading Oculus...");
        dlg.setTitle("Oculus");
        Thread th = new Thread(worker);
        th.setDaemon(true);
        th.start();
    }

    // *******************************************************************
    // Loginbox
    // *******************************************************************
    public void showLoginDialog(String title, String information){
        LoginDialog dlg = new LoginDialog(null, null);
        dlg.show();
    }

    // *******************************************************************
    // Exception Box
    // *******************************************************************
    public void showExceptionDialog(Exception e, String information){
        ExceptionDialog dlg = new ExceptionDialog(e);
        dlg.setContentText(information);
        dlg.setHeaderText("Exception occured !");
        dlg.setTitle("Oculus Error");
        dlg.show();
    }

    // *******************************************************************
    // YesNoBox
    // *******************************************************************
    public void showYesNoialog(String title, String information){
        Alert dlg = createAlert(Alert.AlertType.CONFIRMATION);
        dlg.setTitle(title);
        dlg.getDialogPane().setContentText(information);
        //dlg.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dlg.show();
    }


    private Alert createAlert(Alert.AlertType type) {
        //Window owner = cbSetOwner.isSelected() ? stage : null;
        Alert dlg = new Alert(type, "");
        dlg.initModality(Modality.APPLICATION_MODAL);
        //dlg.initOwner(owner);
        return dlg;
    }
}
