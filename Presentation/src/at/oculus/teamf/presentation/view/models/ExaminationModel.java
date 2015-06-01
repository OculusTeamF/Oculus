/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.controller.CreateDiagnosisController;
import at.oculus.teamf.application.controller.dependenceResolverTB2.exceptions.NotInitiatedExceptions;
import at.oculus.teamf.application.controller.exceptions.CreateDiagnosisControllerExceptions.NoExaminationProtocolException;
import at.oculus.teamf.application.controller.exceptions.CreateDiagnosisControllerExceptions.RequirementsUnfulfilledException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddExaminationProtocol;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationProtolException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IOrthoptist;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;

import java.util.Collection;
import java.util.Date;


/**
 * Created by Fabian on 10.05.2015.
 */
public class ExaminationModel {

    private static ExaminationModel _examinationmodel = new ExaminationModel();
    private Model _model;

    private IExaminationProtocol _eximationprotocol;

    public static ExaminationModel getInstance() {

        if(_examinationmodel == null)
        {
            _examinationmodel = new ExaminationModel();
        }

        return _examinationmodel;
    }


    /**
     * returns a List with all Examinationprotcols from the given patient
     * @param patient
     * @return Collection<IExaminationProtocol>
     */
    public Collection<IExaminationProtocol> getAllExaminationProtcols(IPatient patient) throws CouldNotGetExaminationProtolException
    {
        _model = Model.getInstance();

        return _model.getReceivePatientController().getAllExaminationProtocols(patient);
    }

    /**
     * creates a new Exanmination Protocol
     * @param startdate
     * @param enddate
     * @param examinationDocumentation
     * @param patient
     * @param doctor
     * @param orthoptist
     * @return IExaminationProtocol
     */
    public IExaminationProtocol newExaminationProtocol(Date startdate, Date enddate, String examinationDocumentation,IPatient patient, IDoctor doctor, IOrthoptist orthoptist) throws CouldNotAddExaminationProtocol
    {
        _model = Model.getInstance();

        return _model.getReceivePatientController().createNewExaminationProtocol(startdate, enddate, examinationDocumentation, patient, doctor, orthoptist);
    }


    public void addNewPatientDiagnosis(String title, String description, IDoctor doc, IExaminationProtocol exp) throws NoExaminationProtocolException, NotInitiatedExceptions, BadConnectionException, RequirementsUnfulfilledException, CriticalDatabaseException, CriticalClassException{

        CreateDiagnosisController _createDiagnosisController = CreateDiagnosisController.CreateController(exp);
        _createDiagnosisController.createDiagnosis(title,description, doc);

    }

    public IExaminationProtocol getCurrentExaminationProtocol(){
        return _eximationprotocol;
    }
    public void setCurrentExaminationProtocol(IExaminationProtocol ep){
        _eximationprotocol = ep;
    }

}
