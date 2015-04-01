package com.symbolplay.tria.persistence.gamepreferences;

import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.util.ExceptionThrower;

final class GamePreferencesUpdater {
    
    public static ObjectMap<String, Object> updateBySingleVersion(ObjectMap<String, Object> values, int currentVersion) {
        switch (currentVersion) {
            case 1:
                // add update code here
                // return updateVersion001(userDataValues);
                return values;
            
            default:
                ExceptionThrower.throwException("Invalid version of user data: %d", currentVersion);
                return null;
        }
    }
    
    // v001
    // - gamePreferences : ObjectMap
    //   - version : long
    //   - lastEnteredHighScoreName: String
    //   - isHowToPlayShown : boolean
    //   - isRatingRequestDisabled : boolean
    //   - lastRatingRequestTime : long
    //   - gamesPlayedSinceLastRatingRequest : long
    //   - lastNewsShownIndex : long
    //   - isSoundOn : boolean
    //   - isVibrationOn : boolean
    //   - isConfirmDialogsEnabled : boolean
    //   - selectedScoreLines : String
}
