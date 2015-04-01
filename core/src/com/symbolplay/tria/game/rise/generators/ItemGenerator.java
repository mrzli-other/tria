package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.items.AntiGravityItem;
import com.symbolplay.tria.game.items.LifeItem;
import com.symbolplay.tria.game.items.RocketItem;
import com.symbolplay.tria.game.platforms.Platform;

public final class ItemGenerator {
    
    public static final int ITEM_TYPE_TO_GENERATE_NONE = 0;
    public static final int ITEM_TYPE_TO_GENERATE_LIFE = 1;
    public static final int ITEM_TYPE_TO_GENERATE_ANTI_GRAVITY = 2;
    public static final int ITEM_TYPE_TO_GENERATE_ROCKET = 3;
    
    public static int getItemTypeToGenerate(RiseGeneratorData rgd) {
        if (getDistanceFromLastItem(rgd) <= 50.0f) {
            return ITEM_TYPE_TO_GENERATE_NONE;
        }
        
        float lifeItemChance = getLifeItemChance(rgd);
        if (lifeItemChance > 0.0f && MathUtils.random() < lifeItemChance) {
            return ITEM_TYPE_TO_GENERATE_LIFE;
        }
        
        float antiGravityItemChance = getAntiGravityItemChance(rgd);
        if (antiGravityItemChance > 0.0f && MathUtils.random() < antiGravityItemChance) {
            return ITEM_TYPE_TO_GENERATE_ANTI_GRAVITY;
        }
        
        float rocketItemChance = getRocketItemChance(rgd);
        if (rocketItemChance > 0.0f && MathUtils.random() < rocketItemChance) {
            return ITEM_TYPE_TO_GENERATE_ROCKET;
        }
        
        return ITEM_TYPE_TO_GENERATE_NONE;
    }
    
    private static float getDistanceFromLastItem(RiseGeneratorData rgd) {
        float distanceFromLastLifeItem = (float) (rgd.absoluteHeight - rgd.lastLifeItemHeight);
        float distanceFromLastAntiGravityItem = (float) (rgd.absoluteHeight - rgd.lastAntiGravityItemHeight);
        float distanceFromLastRocketItem = (float) (rgd.absoluteHeight - rgd.lastRocketItemHeight);
        
        return Math.min(Math.min(distanceFromLastLifeItem, distanceFromLastAntiGravityItem), distanceFromLastRocketItem);
    }
    
    private static float getLifeItemChance(RiseGeneratorData rgd) {
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastLifeItemHeight);
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float distance0 = getLifeDistance0(absoluteHeight);
        float distance1 = getLifeDistance1(absoluteHeight);
        return getItemChance(distanceFromLast, distance0, distance1, 0.0f, 0.2f);
    }
    
    private static float getAntiGravityItemChance(RiseGeneratorData rgd) {
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastAntiGravityItemHeight);
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float distance0 = getAntiGravityDistance0(absoluteHeight);
        float distance1 = getAntiGravityDistance1(absoluteHeight);
        return getItemChance(distanceFromLast, distance0, distance1, 0.0f, 0.2f);
    }
    
    private static float getRocketItemChance(RiseGeneratorData rgd) {
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastRocketItemHeight);
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float distance0 = getRocketDistance0(absoluteHeight);
        float distance1 = getRocketDistance1(absoluteHeight);
        return getItemChance(distanceFromLast, distance0, distance1, 0.0f, 0.2f);
    }
    
    private static float getItemChance(float distanceFromLast, float distance0, float distance1, float chance0, float chance1) {
        if (distanceFromLast <= distance0) {
            return chance0;
        } else if (distanceFromLast <= distance1) {
            return chance0 + (distanceFromLast - distance0) / (distance1 - distance0) * (chance1 - chance0);
        } else {
            return chance1;
        }
    }
    
    private static float getLifeDistance0(float absoluteHeight) {
        if (absoluteHeight <= 5000.0f) {
            return 600.0f + absoluteHeight * 0.08f;
        } else {
            return 1000.0f;
        }
    }
    
    private static float getLifeDistance1(float absoluteHeight) {
        if (absoluteHeight <= 5000.0f) {
            return 1400.0f + absoluteHeight * 0.12f;
        } else {
            return 2000.0f;
        }
    }
    
    private static float getAntiGravityDistance0(float absoluteHeight) {
        return 600.0f;
    }
    
    private static float getAntiGravityDistance1(float absoluteHeight) {
        return 1000.0f;
    }
    
    private static float getRocketDistance0(float absoluteHeight) {
        return 800.0f;
    }
    
    private static float getRocketDistance1(float absoluteHeight) {
        return 1200.0f;
    }
    
    public static void addLifeItem(RiseGeneratorData rgd, Platform platform, AssetManager assetManager) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float x = MathUtils.random(platformInitialPosition.x, platformInitialPosition.x + Platform.WIDTH - LifeItem.WIDTH);
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        LifeItem item = new LifeItem(x, y, assetManager);
        platform.attachItem(item);
        
        rgd.items.add(item);
        
        rgd.lastLifeItemHeight = y;
    }
    
    public static void addAntiGravityItem(RiseGeneratorData rgd, Platform platform, AssetManager assetManager) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float x = MathUtils.random(platformInitialPosition.x, platformInitialPosition.x + Platform.WIDTH - AntiGravityItem.WIDTH);
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        AntiGravityItem item = new AntiGravityItem(x, y, assetManager);
        platform.attachItem(item);
        
        rgd.items.add(item);
        
        rgd.lastAntiGravityItemHeight = y;
    }
    
    public static void addRocketItem(RiseGeneratorData rgd, Platform platform, AssetManager assetManager) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float x = MathUtils.random(platformInitialPosition.x, platformInitialPosition.x + Platform.WIDTH - RocketItem.WIDTH);
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        RocketItem item = new RocketItem(x, y, assetManager);
        platform.attachItem(item);
        
        rgd.items.add(item);
        
        rgd.lastRocketItemHeight = y;
    }
}
