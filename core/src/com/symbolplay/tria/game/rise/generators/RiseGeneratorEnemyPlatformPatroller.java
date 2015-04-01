package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.PlatformPatrollerEnemy;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorEnemyPlatformPatroller {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    
    private final AssetManager assetManager;
    
    public RiseGeneratorEnemyPlatformPatroller(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generate(RiseGeneratorData rgd, boolean isLow) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        int minNumRepeats = getMinNumRepeats(absoluteHeight);
        int maxNumRepeats = getMaxNumRepeats(absoluteHeight);
        int numRepeats = MathUtils.random(minNumRepeats, maxNumRepeats);
        
        for (int i = 0; i < numRepeats; i++) {
            int type = MathUtils.random(2);
            
            if (type == 0) {
                generateFullRange(rgd, isLow);
            } else if (type == 1) {
                generateTwoPlatforms(rgd, isLow);
            } else {
                generateThreePlatforms(rgd, isLow);
            }
        }
    }
    
    private static int getMinNumRepeats(float absoluteHeight) {
        if (absoluteHeight <= 4000.0f) {
            return 1 + (int) (absoluteHeight * 0.0005f);
        } else {
            return 3;
        }
    }
    
    private static int getMaxNumRepeats(float absoluteHeight) {
        if (absoluteHeight <= 3200.0f) {
            return 1 + (int) (absoluteHeight * 0.00125f);
        } else {
            return 5;
        }
    }
    
    private void generateFullRange(RiseGeneratorData rgd, boolean isLow) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = getPlatformMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = getPlatformMaxMovingSpeed(absoluteHeight);
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 9.9f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 14.9f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        }
        
        float plarformsY = 0.0f;
        float platformDistance = 0.3125f;
        RiseGeneratorUtils.addPlatform(rgd, 0.0f, plarformsY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, Platform.WIDTH + platformDistance, plarformsY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, (Platform.WIDTH + platformDistance) * 2.0f, plarformsY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, (Platform.WIDTH + platformDistance) * 3.0f, plarformsY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, (Platform.WIDTH + platformDistance) * 4.0f, plarformsY, assetManager);
        
        float range = GameContainer.GAME_AREA_WIDTH - PlatformPatrollerEnemy.WIDTH;
        RiseGeneratorUtils.addPlatformPatrollerEnemy(rgd, 0.0f, plarformsY + Platform.HEIGHT, range, assetManager);
        
        rgd.changeGeneratedHeight(5.0f);
    }
    
    private void generateTwoPlatforms(RiseGeneratorData rgd, boolean isLow) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = getPlatformMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = getPlatformMaxMovingSpeed(absoluteHeight);
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 4.9f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 7.4f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        }
        
        float maxPatrolPlatformX = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH * 2.0f;
        float halfMaxPatrolPlatformX = maxPatrolPlatformX * 0.5f;
        float lowPlatformPossibleOverlap = 0.5f;
        
        float patrolPlatformX = MathUtils.random(maxPatrolPlatformX);
        float lowPlatformXRelative = MathUtils.random(halfMaxPatrolPlatformX + lowPlatformPossibleOverlap - Platform.WIDTH);
        
        float lowPlatformX;
        if (patrolPlatformX > halfMaxPatrolPlatformX) {
            lowPlatformX = patrolPlatformX + lowPlatformPossibleOverlap - Platform.WIDTH - lowPlatformXRelative;
        } else {
            lowPlatformX = patrolPlatformX + Platform.WIDTH * 2.0f - lowPlatformPossibleOverlap + lowPlatformXRelative;
        }
        
        float patrolPlatformY = 1.8f;
        
        RiseGeneratorUtils.addPlatform(rgd, lowPlatformX, 0.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, patrolPlatformX, patrolPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, patrolPlatformX + Platform.WIDTH, patrolPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 5.0f, assetManager);
        
        float range = Platform.WIDTH * 2.0f - PlatformPatrollerEnemy.WIDTH;
        RiseGeneratorUtils.addPlatformPatrollerEnemy(rgd, patrolPlatformX, patrolPlatformY + Platform.HEIGHT, range, assetManager);
        
        rgd.changeGeneratedHeight(8.5f);
    }
    
    private void generateThreePlatforms(RiseGeneratorData rgd, boolean isLow) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = getPlatformMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = getPlatformMaxMovingSpeed(absoluteHeight);
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 7.4f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 9.9f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        }
        
        float maxPatrolPlatformX = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH * 3.0f;
        float halfMaxPatrolPlatformX = maxPatrolPlatformX * 0.5f;
        float lowPlatformPossibleOverlap = 0.5f;
        
        float patrolPlatformX = MathUtils.random(maxPatrolPlatformX);
        float lowPlatformXRelative = MathUtils.random(halfMaxPatrolPlatformX + lowPlatformPossibleOverlap - Platform.WIDTH);
        
        float lowPlatformX;
        if (patrolPlatformX > halfMaxPatrolPlatformX) {
            lowPlatformX = patrolPlatformX + lowPlatformPossibleOverlap - Platform.WIDTH - lowPlatformXRelative;
        } else {
            lowPlatformX = patrolPlatformX + Platform.WIDTH * 3.0f - lowPlatformPossibleOverlap + lowPlatformXRelative;
        }
        
        float patrolPlatformY = 1.8f;
        
        RiseGeneratorUtils.addPlatform(rgd, lowPlatformX, 0.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, patrolPlatformX, patrolPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, patrolPlatformX + Platform.WIDTH, patrolPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, patrolPlatformX + Platform.WIDTH * 2.0f, patrolPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 5.0f, assetManager);
        
        float range = Platform.WIDTH * 3.0f - PlatformPatrollerEnemy.WIDTH;
        RiseGeneratorUtils.addPlatformPatrollerEnemy(rgd, patrolPlatformX, patrolPlatformY + Platform.HEIGHT, range, assetManager);
        
        rgd.changeGeneratedHeight(8.5f);
    }
    
    private static float getPlatformMinMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f + absoluteHeight * 0.004f;
        } else {
            return 5.0f;
        }
    }
    
    private static float getPlatformMaxMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.5f + absoluteHeight * 0.0035f;
        } else {
            return 6.0f;
        }
    }
}
