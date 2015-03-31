package at.oculus.teamf.application.facade;

/**
 * UsecaseFacede though which UsecaseController can be requested.
 * @author Simon Angerer
 * @date 30.3.2015
 */
public class UsecaseFacade {

    //<editor-fold desc="Singelton">
    private static UsecaseFacade ourInstance = new UsecaseFacade();

    public static UsecaseFacade getInstance() {
        return ourInstance;
    }
    //</editor-fold>

    private UsecaseFacade() {

    }

    public IUsecaseController getUsecaseController(UsecaseType type) {
        return null;
    }
}
