/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.facade.CreateDiagnosisController;
import at.oculus.teamf.application.facade.dependenceResolverTB2.exceptions.NotInitatedExceptions;
import at.oculus.teamf.application.facade.exceptions.NoExaminationProtocolException;
import at.oculus.teamf.application.facade.exceptions.RequirementsUnfulfilledException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.facade.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.exception.CouldNotAddExaminationProtocol;
import at.oculus.teamf.domain.entity.exception.CouldNotGetExaminationProtolException;
import at.oculus.teamf.domain.entity.interfaces.IDoctor;
import at.oculus.teamf.domain.entity.interfaces.IExaminationProtocol;
import at.oculus.teamf.domain.entity.interfaces.IOrthoptist;
import at.oculus.teamf.domain.entity.interfaces.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.presentation.view.DialogBoxController;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fabian on 10.05.2015.
 */
public class ExaminationModel {
    private static ExaminationModel _examinationmodel = new ExaminationModel();
    private Model _model;

    private IExaminationProtocol _eximationprotocol;
    private CreateDiagnosisController _createDiagnosisController;

    private HashMap<IPatient, IExaminationProtocol> _examinationmap;

    public static ExaminationModel getInstance() {
        if(_examinationmodel == null) {
            _examinationmodel = new ExaminationModel();
        }

        return _examinationmodel;
    }


    /**
     * returns a List with all Examinationprotcols from the given patient
     * @param patient
     * @return Collection<IExaminationProtocol>
     */
    public Collection<IExaminationProtocol> getAllExaminationProtcols(IPatient patient){
        _model = Model.getInstance();
        Collection<IExaminationProtocol> protocols = null;

        try {
            protocols = _model.getReceivePatientController().getAllExaminationProtocols(patient);
        } catch (CouldNotGetExaminationProtolException e) {
            e.printStackTrace();
        }

        return protocols;
    }

    /**
     * creates a new examinationprotocol
     */
    public IExaminationProtocol newExaminationProtocol(Date startdate, Date enddate, String examinationDocumentation,IPatient patient, IDoctor doctor, IOrthoptist orthoptist) {
        _model = Model.getInstance();
        try {
            return _model.getReceivePatientController().createNewExaminationProtocol(startdate, enddate, examinationDocumentation, patient, doctor, orthoptist);
        } catch (CouldNotAddExaminationProtocol couldNotAddExaminationProtocol) {
            couldNotAddExaminationProtocol.printStackTrace();
        }

        return null;
    }


    public void addNewPatientDiagnosis(String title, String description, IDoctor doc, IExaminationProtocol exp) throws NotInitatedExceptions {
        try {
            _createDiagnosisController = CreateDiagnosisController.CreateController(exp);
        } catch (NoExaminationProtocolException e) {
            e.printStackTrace();
        }

        try {
            _createDiagnosisController.createDiagnosis(title,description, doc);
        } catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (RequirementsUnfulfilledException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "RequirementsUnfulfilledException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }
    }

    public IExaminationProtocol getCurrentExaminationProtocol(){
        return _eximationprotocol;
    }
    public void setCurrentExaminationProtocol(IExaminationProtocol ep){
        _eximationprotocol = ep;
    }

    public void setExaminationMapEntry(IExaminationProtocol exp, IPatient pat){  _examinationmap.put(pat, exp); }

    public void removeExaminationMapEntry(IExaminationProtocol exp){  _examinationmap.remove(exp); }

    public IExaminationProtocol getExaminationFromSelectedPatient(IPatient patient){
        IExaminationProtocol exp =  _examinationmap.get(patient);
        return exp;
    }

}
