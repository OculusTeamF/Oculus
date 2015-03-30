package at.oculus.teamf.technical.accessrights;

import at.oculus.teamf.domain.entity.UserEntity;

/**
 * Created by Norskan on 30.03.2015.
 */
public class AccessRights {

    //<editor-fold desc="Description">
    private static AccessRights ourInstance = new AccessRights();

    public static AccessRights getInstance() {
        return ourInstance;
    }

    private AccessRights() {
    }
    //</editor-fold>

    boolean hasRights(UserEntity user) {
        return true;
    }
}
