/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view;

import at.oculus.teamf.domain.entity.IPatient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * Created by Karo on 20.04.2015.
 */
public class examinationController {

    @FXML
    public Text examinationCurrDate;
    @FXML
    public Text examinationLnameFnameSvn;
    @FXML
    public TextArea examinationAllergies;
    @FXML
    public ListView examinationList;
    @FXML
    public TextArea examinationDocumentation;


}
