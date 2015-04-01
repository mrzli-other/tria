package com.symbolplay.tria.persistence.gamepreferences;

import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.screens.general.ScoreLines;

final class GamePreferencesDefault {
    
    public static ObjectMap<String, Object> getDefaultGamePreferencesValues() {
        ObjectMap<String, Object> values = new ObjectMap<String, Object>();
        values.put("version", (long) GamePreferences.VERSION);
        values.put("lastEnteredHighScoreName", "Player");
        values.put("isHowToPlayShown", false);
        values.put("isRatingRequestDisabled", false);
        values.put("lastRatingRequestTime", 0L);
        values.put("gamesPlayedSinceLastRatingRequest", 0L);
        values.put("lastNewsShownIndex", 0L);
        values.put("isSoundOn", true);
        values.put("isVibrationOn", true);
        values.put("isConfirmDialogsEnabled", true);
        values.put("selectedScoreLines", ScoreLines.DEFAULT);
        return values;
    }
}
