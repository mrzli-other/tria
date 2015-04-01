package com.symbolplay.tria.persistence.userdata;

import java.util.Comparator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public final class AchievementsData {
    
    private static final Comparator<HighScoreData> HIGH_SCORE_COMPARATOR;
    
    private final UserData userData;
    
    private Array<HighScoreData> highScores;
    
    static {
        HIGH_SCORE_COMPARATOR = new Comparator<HighScoreData>() {
            @Override
            public int compare(HighScoreData hs1, HighScoreData hs2) {
                if (hs1.getScore() > hs2.getScore()) {
                    return -1;
                } else if (hs1.getScore() < hs2.getScore()) {
                    return 1;
                } else {
                    if (hs1.getTime() < hs2.getTime()) {
                        return -1;
                    } else if (hs1.getTime() > hs2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        };
    }
    
    public AchievementsData(UserData userData) {
        this.userData = userData;
        
        highScores = readHighScoresFromUserData(userData);
        clampHighScores(highScores);
    }
    
    public Array<HighScoreData> getHighScores() {
        return highScores;
    }
    
    public boolean isHighScore(int score) {
        return score > getHighScores().peek().getScore();
    }
    
    public int getScorePlacementIndex(int score) {
        Array<HighScoreData> highScores = getHighScores();
        for (int i = 0; i < highScores.size; i++) {
            if (score > highScores.get(i).getScore()) {
                return i;
            }
        }
        
        return -1;
    }
    
    public void insertHighScore(String name, int score, long time) {
        Array<HighScoreData> highScores = getHighScores();
        HighScoreData highScore = new HighScoreData(name, score, time);
        highScores.add(highScore);
        highScores.sort(HIGH_SCORE_COMPARATOR);
        highScores.pop();
        
        writeHighScoresToUserData(highScores, userData);
        userData.write();
    }
    
    private static void clampHighScores(Array<HighScoreData> highScores) {
        if (highScores.size > UserData.NUM_HIGH_SCORES) {
            highScores.removeRange(UserData.NUM_HIGH_SCORES, highScores.size - 1);
        }
    }
    
    // READ FROM/WRITE TO USER DATA
    @SuppressWarnings("unchecked")
    private static Array<HighScoreData> readHighScoresFromUserData(UserData userData) {
        Array<Object> highScoresDataList = getHighScoresDataList(userData);
        
        Array<HighScoreData> highScores = new Array<HighScoreData>(highScoresDataList.size);
        for (Object highScoreDataValues : highScoresDataList) {
            HighScoreData highScore = highScoreDataValuesToHighScore((ObjectMap<String, Object>) highScoreDataValues);
            highScores.add(highScore);
        }
        
        return highScores;
    }
    
    private static HighScoreData highScoreDataValuesToHighScore(ObjectMap<String, Object> highScoreDataValues) {
        String name = (String) highScoreDataValues.get("name");
        int score = ((Long) highScoreDataValues.get("score")).intValue();
        long time = (Long) highScoreDataValues.get("time");
        return new HighScoreData(name, score, time); 
    }
    
    private static void writeHighScoresToUserData(Array<HighScoreData> highScores, UserData userData) {
        Array<Object> highScoresDataList = getHighScoresDataList(userData);
        highScoresDataList.clear();
        
        for (HighScoreData highScore : highScores) {
            ObjectMap<String, Object> highScoreDataValues = highScoreToHighScoreDataValues(highScore);
            highScoresDataList.add(highScoreDataValues);
        }
    }
    
    private static ObjectMap<String, Object> highScoreToHighScoreDataValues(HighScoreData highScore) {
        ObjectMap<String, Object> highScoreDataValues = new ObjectMap<String, Object>(3);
        highScoreDataValues.put("name", highScore.getName());
        highScoreDataValues.put("score", (long) highScore.getScore());
        highScoreDataValues.put("time", highScore.getTime());
        return highScoreDataValues;
    }
    
    @SuppressWarnings("unchecked")
    private static Array<Object> getHighScoresDataList(UserData userData) {
        ObjectMap<String, Object> userDataValues = userData.getValues();
        ObjectMap<String, Object> achievementsDataValues = (ObjectMap<String, Object>) userDataValues.get("achievementsData");
        Array<Object> highScoresDataList = (Array<Object>) achievementsDataValues.get("highScores");
        
        return highScoresDataList;
    }
    // END READ FROM/WRITE TO USER DATA
}
