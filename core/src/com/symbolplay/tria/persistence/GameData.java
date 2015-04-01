package com.symbolplay.tria.persistence;

import com.badlogic.gdx.Gdx;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferences;
import com.symbolplay.tria.persistence.gamepreferences.GamePreferencesWrapper;
import com.symbolplay.tria.persistence.userdata.AchievementsData;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.persistence.userdata.UserData;
import com.symbolplay.tria.resources.ResourceNames;

public final class GameData {
    
    private final GamePreferences gamePreferences;
    private final GamePreferencesWrapper gamePreferencesWrapper;
    
    private final UserData userData;
    private final AchievementsData achievementsData;
    private final CareerData careerData;
    
    public GameData() {
        gamePreferences = new GamePreferences(Gdx.files.local(ResourceNames.GAME_PREFERENCES_FILE));
        gamePreferences.read();
        gamePreferencesWrapper = new GamePreferencesWrapper(gamePreferences);
        
        userData = new UserData(Gdx.files.local(ResourceNames.USER_DATA_FILE));
        userData.read();
        achievementsData = new AchievementsData(userData);
        careerData = new CareerData(userData);
    }
    
    public void dispose() {
        gamePreferences.write();
        userData.write();
    }
    
    public GamePreferencesWrapper getGamePreferences() {
        return gamePreferencesWrapper;
    }
    
    public AchievementsData getAchievementsData() {
        return achievementsData;
    }
    
    public CareerData getCareerData() {
        return careerData;
    }
}
