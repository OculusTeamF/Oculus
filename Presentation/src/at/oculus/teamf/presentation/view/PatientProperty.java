/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;
/*
import at.oculus.teamf.application.facade;
*/
/**
 * Created by Karo on 09.04.2015.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PropertySheet;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class PatientProperty implements Initializable{

    @FXML Tab patientPropertyTab;

    private static Map<String, Object> customDataMap = new LinkedHashMap<>();
    public enum Gender {Male, Female};

    static {
        customDataMap.put("1. Patient#First Name", "Jonathan");
        customDataMap.put("1. Patient#Last Name", "Giles");
        customDataMap.put("1. Patient#Birthday", LocalDate.of(1985, Month.JANUARY, 12));
        customDataMap.put("1. Patient#Social Insurance #", "2342342345");
        customDataMap.put("2. Address#Street", "");
        customDataMap.put("2. Address#City", "");
        customDataMap.put("2. Address#Country ISO Code", "");
        customDataMap.put("2. Adress#Postal Code", "");
        customDataMap.put("2. Adress#Gender", Gender.Male);
        customDataMap.put("3. Contact#Phone", "123-123-1234");
        customDataMap.put("3. Contact#E-Mail", "jon@jon.com");
        customDataMap.put("4. User#Boolena", true);
        customDataMap.put("4. User#NumberOnly", 10);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<PropertySheet.Item> list = FXCollections.observableArrayList();
        for (String key : customDataMap.keySet()) {
            list.add(new CustomPropertyItem(key));
        }

        PropertySheet propertySheet = new PropertySheet(list);
        VBox vBox = new VBox();
        ToolBar tBar = new ToolBar();
        //patientPropertyTab.setContent(propertySheet);
        patientPropertyTab.setContent(vBox);
        vBox.getChildren().add(tBar);
        vBox.getChildren().add(propertySheet);
        tBar.setMinHeight(60);

        propertySheet.modeSwitcherVisibleProperty().set(false);
        propertySheet.searchBoxVisibleProperty().set(false);
        propertySheet.setStyle("-fx-background-color: white;");
        propertySheet.setMaxWidth(800);
        vBox.setStyle("-fx-background-color: #F4F4F4;");


/*        propertySheet.setPropertyEditorFactory(new Callback<PropertySheet.Item, PropertyEditor<?>>() {
            @Override
            public PropertyEditor<?> call(PropertySheet.Item param) {
                if(param.getValue() instanceof String[]) {
                    return Editors.createChoiceEditor(param, choices);
                } else if (param.getValue() instanceof Boolean) {
                    return Editors.createCheckEditor(param);
                } else if (param.getValue() instanceof Integer) {
                    return Editors.createNumericEditor(param);
                } else {
                    return Editors.createTextEditor(param);
                    Editors.createCustomEditor(param);
                }
            }
        });*/
    }

    class CustomPropertyItem implements PropertySheet.Item {

        private String key;
        private String category, name;

        public CustomPropertyItem(String key) {
            this.key = key;
            String[] skey = key.split("#");
            category = skey[0];
            name = skey[1];
        }

        @Override
        public Class<?> getType() {
            return customDataMap.get(key).getClass();
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public Object getValue() {
            return customDataMap.get(key);
        }

        @Override
        public void setValue(Object value) {
            customDataMap.put(key, value);
        }

    }
}
