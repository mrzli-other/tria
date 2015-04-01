package com.symbolplay.tria.persistence.userdata;

import com.badlogic.gdx.utils.ObjectMap;

public final class CareerData {
    
    private final UserData userData;
    
    private int coins;
    private int antiGravityLevel;
    private int rocketLevel;
    private int initialLivesNextGame;
    
    public CareerData(UserData userData) {
        this.userData = userData;
        
        coins = ((Long) getCareerDataValues(userData).get("coins")).intValue();
        antiGravityLevel = ((Long) getCareerDataValues(userData).get("antiGravityLevel")).intValue();
        rocketLevel = ((Long) getCareerDataValues(userData).get("rocketLevel")).intValue();
        initialLivesNextGame = ((Long) getCareerDataValues(userData).get("initialLivesNextGame")).intValue();
    }
    
    public int getCoins() {
        return coins;
    }
    
    public void setCoins(int coins) {
        this.coins = coins;
        getCareerDataValues(userData).put("coins", getCoins());
        userData.write();
    }
    
    public void changeCoins(int change) {
        if (change != 0) {
            setCoins(coins + change);
        }
    }
    
    public int getAntiGravityLevel() {
        return antiGravityLevel;
    }
    
    public void setAntiGravityLevel(int antiGravityLevel) {
        this.antiGravityLevel = antiGravityLevel;
        getCareerDataValues(userData).put("antiGravityLevel", getAntiGravityLevel());
        userData.write();
    }
    
    public int getRocketLevel() {
        return rocketLevel;
    }
    
    public void setRocketLevel(int rocketLevel) {
        this.rocketLevel = rocketLevel;
        getCareerDataValues(userData).put("rocketLevel", getRocketLevel());
        userData.write();
    }
    
    public int getInitialLivesNextGame() {
        return initialLivesNextGame;
    }
    
    public void setInitialLivesNextGame(int initialLivesNextGame) {
        this.initialLivesNextGame = initialLivesNextGame;
        getCareerDataValues(userData).put("initialLivesNextGame", getInitialLivesNextGame());
        userData.write();
    }
    
    // READ FROM/WRITE TO USER DATA
    @SuppressWarnings("unchecked")
    private static ObjectMap<String, Object> getCareerDataValues(UserData userData) {
        ObjectMap<String, Object> userDataValues = userData.getValues();
        ObjectMap<String, Object> careerDataValues = (ObjectMap<String, Object>) userDataValues.get("careerData");
        return careerDataValues;
    }
    // END READ FROM/WRITE TO USER DATA
}
