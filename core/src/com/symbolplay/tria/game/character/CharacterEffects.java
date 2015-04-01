package com.symbolplay.tria.game.character;

import com.symbolplay.tria.persistence.userdata.CareerData;

public final class CharacterEffects {
    
    public static final float SHIELD_EFFECT_FINISHING_DURATION = 2.0f;
    public static final float ANTI_GRAVITY_EFFECT_FINISHING_DURATION = 2.0f;
    public static final float ROCKET_EFFECT_FINISHING_DURATION = 2.0f;
    
    public static final float DEATH_SHIELD_DURATION = 2.0f;
    
    private static final float POWERUP_ADDITIONAL_DURATION = 0.05f;
    
    private final CareerData careerData;
    private int antiGravityLevel;
    private int rocketLevel;
    
    private int lives;
    private int coins;
    private int score;
    
    private float shieldRemaining;
    private float antiGravityRemaining;
    private float rocketRemaining;
    
    public CharacterEffects(CareerData careerData) {
        this.careerData = careerData;
    }
    
    public void reset() {
        antiGravityLevel = careerData.getAntiGravityLevel();
        rocketLevel = careerData.getRocketLevel();
        
        // TODO: lives added only for testing, remove them after
        lives = careerData.getInitialLivesNextGame();
        coins = 0;
        score = 0;
        
        shieldRemaining = 0.0f;
        antiGravityRemaining = 0;
        rocketRemaining = 0.0f;
    }
    
    public void update(float delta) {
        shieldRemaining = Math.max(shieldRemaining - delta, 0.0f);
        antiGravityRemaining = Math.max(antiGravityRemaining - delta, 0.0f);
        rocketRemaining = Math.max(rocketRemaining - delta, 0.0f);
    }
    
    public int getLives() {
        return lives;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isShield() {
        return shieldRemaining > 0.0f;
    }
    
    public float getShieldRemaining() {
        return shieldRemaining;
    }
    
    public boolean isAntiGravity() {
        return antiGravityRemaining > 0;
    }
    
    public float getAntiGravityRemaining() {
        return antiGravityRemaining;
    }
    
    public boolean isRocket() {
        return rocketRemaining > 0.0f;
    }
    
    public float getRocketRemaining() {
        return rocketRemaining;
    }
    
    public void addLife() {
        lives++;
    }
    
    public void subtractLife() {
        lives--;
    }
    
    public boolean isMovementPowerUpActive() {
        return isAntiGravity() || isRocket();
    }
    
    public void addCoins(int coins) {
        this.coins += coins;
    }
    
    public void addScore(int score) {
        this.score += score;
    }
    
    public void setShield(float duration) {
        shieldRemaining = Math.max(shieldRemaining, duration);
    }
    
    public void setAntiGravityItemDuration() {
        setAntiGravity(UpgradesUtil.getAntiGravityDuration(antiGravityLevel));
    }
    
    public void setAntiGravityAdditionalDuration() {
        setAntiGravity(POWERUP_ADDITIONAL_DURATION);
    }
    
    private void setAntiGravity(float duration) {
        antiGravityRemaining = Math.max(antiGravityRemaining, duration);
    }
    
    public void setRocketItemDuration() {
        setRocket(UpgradesUtil.getRocketDuration(rocketLevel));
    }
    
    public void setRocketAdditionalDuration() {
        setRocket(POWERUP_ADDITIONAL_DURATION);
    }
    
    private void setRocket(float duration) {
        rocketRemaining = Math.max(rocketRemaining, duration);
    }
}
