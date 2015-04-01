package com.symbolplay.tria.persistence.gamepreferences;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.persistence.UserDataBase;

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
public final class GamePreferences extends UserDataBase {
    
    public static final int VERSION = 1;
    
    public GamePreferences(FileHandle fileHandle) {
        super(fileHandle, VERSION, null);
    }
    
    @Override
    protected ObjectMap<String, Object> updateBySingleVersion(ObjectMap<String, Object> values, int currentVersion) {
        return GamePreferencesUpdater.updateBySingleVersion(values, currentVersion);
    }
    
    @Override
    protected ObjectMap<String, Object> getDefault() {
        return GamePreferencesDefault.getDefaultGamePreferencesValues();
    }
}
