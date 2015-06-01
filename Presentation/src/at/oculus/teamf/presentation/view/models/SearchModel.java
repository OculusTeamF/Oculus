/*
 * Copyright (c) 2015 Team F
 *
 * This file is part of Oculus.
 * Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.oculus.teamf.presentation.view.models;

import at.oculus.teamf.application.controller.exceptions.critical.CriticalClassException;
import at.oculus.teamf.application.controller.exceptions.critical.CriticalDatabaseException;
import at.oculus.teamf.domain.entity.patient.IPatient;
import at.oculus.teamf.persistence.exception.BadConnectionException;
import at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException;
import at.oculus.teamf.presentation.view.DialogBoxController;

import java.util.Collection;

/**
 * Created by Fabian on 10.05.2015.
 */
public class SearchModel {

    private static SearchModel _searchmodel = new SearchModel();
    private Model _model;

    public static SearchModel getInstance() {
        if(_searchmodel == null) {
            _searchmodel = new SearchModel();
        }

        return _searchmodel;
    }

    /**
     * search Patients by given Text
     */
    public Collection<IPatient> searchPatients(String text) throws  BadConnectionException, CriticalClassException, CriticalDatabaseException, InvalidSearchParameterException
    {
        _model = Model.getInstance();

        return  _model.getSearchPatientController().searchPatients(text);
    }

    /**
     * search Patients by lastname, firstname or svn, detailed search
     */
    public Collection<IPatient> searchPatients(String svn, String fname, String lname){
        _model = Model.getInstance();
        Collection<IPatient> searchresults = null;

        try {
            searchresults = _model.getSearchPatientController().searchPatients(svn, fname, lname);
        }  catch (BadConnectionException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "BadConnectionException - Please contact support");
        } catch (at.oculus.teamf.persistence.exception.search.InvalidSearchParameterException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "FacadeException - Please contact support");
        } catch (CriticalDatabaseException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalDatabaseException - Please contact support");
        } catch (CriticalClassException e) {
            e.printStackTrace();
            DialogBoxController.getInstance().showExceptionDialog(e, "CriticalClassException - Please contact support");
        }

        return searchresults;
    }
}
