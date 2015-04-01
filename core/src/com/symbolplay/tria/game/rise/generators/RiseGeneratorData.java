package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorData {
    
    private static final int PLATFORMS_CAPACITY = 200;
    private static final int ENEMIES_CAPACITY = 50;
    private static final int ITEMS_CAPACITY = 50;
    
    private static final double INITIAL_LAST_HEIGHT = -1000.0;
    private static final double INITIAL_LAST_ITEM_HEIGHT = -500.0;
    
    public final Array<Platform> platforms;
    public final Array<EnemyBase> enemies;
    public final Array<ItemBase> items;
    
    public int nextPlatformId;
    public int nextGroupId;
    
    public float relativeHeight;
    public double absoluteHeight;
    
    public double lastNormalPlatformAbsoluteHeight;
    public double lastJumpBoostReachAbsoluteHeight;
    
    public double lastCrumbleHeight;
    public double lastVisibleOnJumpHeight;
    public double lastRevealOnJumpHeight;
    public double lastToggleSpikesHeight;
    public double lastTimedSpikesHeight;
    public double lastAttachedSpikesHeight;
    
    public double lastLifeItemHeight;
    public double lastAntiGravityItemHeight;
    public double lastRocketItemHeight;
    
    public RiseGeneratorData() {
        platforms = new Array<Platform>(true, PLATFORMS_CAPACITY);
        enemies = new Array<EnemyBase>(true, ENEMIES_CAPACITY);
        items = new Array<ItemBase>(true, ITEMS_CAPACITY);
        
        resetComplete();
    }
    
    public void resetComplete() {
        platforms.clear();
        enemies.clear();
        items.clear();
        
        nextPlatformId = 0;
        nextGroupId = 0;
        
        relativeHeight = 0.0f;
        absoluteHeight = 0.0;
        
        lastNormalPlatformAbsoluteHeight = INITIAL_LAST_HEIGHT;
        lastJumpBoostReachAbsoluteHeight = INITIAL_LAST_HEIGHT;
        
        lastCrumbleHeight = INITIAL_LAST_HEIGHT;
        lastVisibleOnJumpHeight = INITIAL_LAST_HEIGHT;
        lastRevealOnJumpHeight = INITIAL_LAST_HEIGHT;
        lastToggleSpikesHeight = INITIAL_LAST_HEIGHT;
        lastTimedSpikesHeight = INITIAL_LAST_HEIGHT;
        lastAttachedSpikesHeight = INITIAL_LAST_HEIGHT;
        
        lastLifeItemHeight = INITIAL_LAST_ITEM_HEIGHT;
        lastAntiGravityItemHeight = INITIAL_LAST_ITEM_HEIGHT;
        lastRocketItemHeight = INITIAL_LAST_ITEM_HEIGHT;
    }
    
    public void resetForNextGeneration() {
        platforms.clear();
        enemies.clear();
        items.clear();
    }
    
    public void changeGeneratedHeight(float change) {
        relativeHeight += change;
        absoluteHeight += change;
    }
    
    public float getAbsoluteHeightFloat() {
        return (float) absoluteHeight;
    }
}
