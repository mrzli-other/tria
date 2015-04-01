package com.symbolplay.tria.persistence.userdata;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

final class UserDataDefault {
    
    public static ObjectMap<String, Object> getDefaultUserDataValues() {
        ObjectMap<String, Object> userDataValues = new ObjectMap<String, Object>();
        userDataValues.put("version", UserData.VERSION);
        userDataValues.put("careerData", getDefaultCareerDataValues());
        userDataValues.put("achievementsData", getDefaultAchievementsDataValues());
        return userDataValues;
    }
    
    private static ObjectMap<String, Object> getDefaultCareerDataValues() {
        ObjectMap<String, Object> careerDataValues = new ObjectMap<String, Object>();
        careerDataValues.put("coins", 0L);
        careerDataValues.put("antiGravityLevel", 0L);
        careerDataValues.put("rocketLevel", 0L);
        careerDataValues.put("initialLivesNextGame", 0L);
        return careerDataValues;
    }
    
    private static ObjectMap<String, Object> getDefaultAchievementsDataValues() {
        ObjectMap<String, Object> achievementsDataValues = new ObjectMap<String, Object>();
        achievementsDataValues.put("highScores", getDefaultHighScoresDataList());
        return achievementsDataValues;
    }
    
    private static Array<Object> getDefaultHighScoresDataList() {
        Array<Object> highScoresDataList = new Array<Object>(true, UserData.NUM_HIGH_SCORES);
        for (int i = 0; i < UserData.NUM_HIGH_SCORES; i++) {
            highScoresDataList.add(getDefaultHighScoreDataValues());
        }
        return highScoresDataList;
    }
    
    private static ObjectMap<String, Object> getDefaultHighScoreDataValues() {
        ObjectMap<String, Object> highScoreDataValues = new ObjectMap<String, Object>();
        highScoreDataValues.put("name", UserData.DEFAULT_HIGH_SCORE_NAME);
        highScoreDataValues.put("score", 0L);
        highScoreDataValues.put("time", 0L);
        return highScoreDataValues;
    }
}
