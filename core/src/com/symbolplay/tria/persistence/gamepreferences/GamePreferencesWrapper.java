package com.symbolplay.tria.persistence.gamepreferences;

import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.screens.general.ScoreLines;

public final class GamePreferencesWrapper {
    
    private final GamePreferences gamePreferences;
    
    private String lastEnteredHighScoreName;
    private boolean isHowToPlayShown;
    private boolean isRatingRequestDisabled;
    private long lastRatingRequestTime;
    private int gamesPlayedSinceLastRatingRequest;
    private int lastNewsShownIndex;
    private boolean isSoundOn;
    private boolean isVibrationOn;
    private boolean isConfirmDialogsEnabled;
    private String selectedScoreLines;
    
    public GamePreferencesWrapper(GamePreferences gamePreferences) {
        this.gamePreferences = gamePreferences;
        
        lastEnteredHighScoreName = (String) getGamePreferencesValues().get("lastEnteredHighScoreName", "Player");
        isHowToPlayShown = (Boolean) getGamePreferencesValues().get("isHowToPlayShown", false);
        isRatingRequestDisabled = (Boolean) getGamePreferencesValues().get("isRatingRequestDisabled", false);
        lastRatingRequestTime = (Long) getGamePreferencesValues().get("lastRatingRequestTime", 0L);
        gamesPlayedSinceLastRatingRequest = ((Long) getGamePreferencesValues().get("gamesPlayedSinceLastRatingRequest", 0L)).intValue();
        lastNewsShownIndex = ((Long) getGamePreferencesValues().get("lastNewsShownIndex", 0L)).intValue();
        isSoundOn = (Boolean) getGamePreferencesValues().get("isSoundOn", true);
        isVibrationOn = (Boolean) getGamePreferencesValues().get("isVibrationOn", true);
        isConfirmDialogsEnabled = (Boolean) getGamePreferencesValues().get("isConfirmDialogsEnabled", true);
        selectedScoreLines = (String) getGamePreferencesValues().get("selectedScoreLines", ScoreLines.DEFAULT);
    }
    
    public String getLastEnteredHighScoreName() {
        return lastEnteredHighScoreName;
    }
    
    public void setLastEnteredHighScoreName(String lastEnteredHighScoreName) {
        this.lastEnteredHighScoreName = lastEnteredHighScoreName;
        getGamePreferencesValues().put("lastEnteredHighScoreName", getLastEnteredHighScoreName());
        gamePreferences.write();
    }
    
    public boolean isHowToPlayShown() {
        return isHowToPlayShown;
    }
    
    public void setHowToPlayShown(boolean isHowToPlayShown) {
        this.isHowToPlayShown = isHowToPlayShown;
        getGamePreferencesValues().put("isHowToPlayShown", isHowToPlayShown());
        gamePreferences.write();
    }
    
    public boolean isRatingRequestDisabled() {
        return isRatingRequestDisabled;
    }
    
    public void setRatingRequestDisabled(boolean isRatingRequestDisabled) {
        this.isRatingRequestDisabled = isRatingRequestDisabled;
        getGamePreferencesValues().put("isRatingRequestDisabled", isRatingRequestDisabled());
        gamePreferences.write();
    }
    
    public long getLastRatingRequestTime() {
        return lastRatingRequestTime;
    }
    
    public void setLastRatingRequestTime(long lastRatingRequestTime) {
        this.lastRatingRequestTime = lastRatingRequestTime;
        getGamePreferencesValues().put("lastRatingRequestTime", getLastRatingRequestTime());
        gamePreferences.write();
    }
    
    public int getGamesPlayedSinceLastRatingRequest() {
        return gamesPlayedSinceLastRatingRequest;
    }
    
    public void setGamesPlayedSinceLastRatingRequest(int gamesPlayedSinceLastRatingRequest) {
        this.gamesPlayedSinceLastRatingRequest = gamesPlayedSinceLastRatingRequest;
        getGamePreferencesValues().put("gamesPlayedSinceLastRatingRequest", getGamesPlayedSinceLastRatingRequest());
        gamePreferences.write();
    }
    
    public int getLastNewsShownIndex() {
        return lastNewsShownIndex;
    }
    
    public void setLastNewsShownIndex(int lastNewsShownIndex) {
        this.lastNewsShownIndex = lastNewsShownIndex;
        getGamePreferencesValues().put("lastNewsShownIndex", getLastNewsShownIndex());
        gamePreferences.write();
    }
    
    public boolean isSoundOn() {
        return isSoundOn;
    }
    
    public void setSoundOn(boolean isSoundOn) {
        this.isSoundOn = isSoundOn;
        getGamePreferencesValues().put("isSoundOn", isSoundOn());
        gamePreferences.write();
    }
    
    public boolean isVibrationOn() {
        return isVibrationOn;
    }
    
    public void setVibrationOn(boolean isVibrationOn) {
        this.isVibrationOn = isVibrationOn;
        getGamePreferencesValues().put("isVibrationOn", isVibrationOn());
        gamePreferences.write();
    }
    
    public boolean isConfirmDialogsEnabled() {
        return isConfirmDialogsEnabled;
    }
    
    public void setConfirmDialogsEnabled(boolean isConfirmDialogsEnabled) {
        this.isConfirmDialogsEnabled = isConfirmDialogsEnabled;
        getGamePreferencesValues().put("isConfirmDialogsEnabled", isConfirmDialogsEnabled());
        gamePreferences.write();
    }
    
    public String getSelectedScoreLines() {
        return selectedScoreLines;
    }
    
    public void setSelectedScoreLines(String selectedScoreLines) {
        this.selectedScoreLines = selectedScoreLines;
        getGamePreferencesValues().put("selectedScoreLines", getSelectedScoreLines());
        gamePreferences.write();
    }
    
    private ObjectMap<String, Object> getGamePreferencesValues() {
        return gamePreferences.getValues();
    }
}
