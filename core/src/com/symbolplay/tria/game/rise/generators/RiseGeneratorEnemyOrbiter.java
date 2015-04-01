package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.symbolplay.tria.game.enemies.OrbiterEnemy;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorEnemyOrbiter {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    
    public RiseGeneratorEnemyOrbiter(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generate(RiseGeneratorData rgd, boolean isLow) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = getMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = getMaxMovingSpeed(absoluteHeight);
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 5.1f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 10.1f, 2.5f, 2.5f, minMovingSpeed, maxMovingSpeed, false);
        }
        
        float orbiterPlatformY = 2.5f;
        
        //RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, 2.3f, orbiterPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.MAX_PLATFORM_X - 2.3f, orbiterPlatformY, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), orbiterPlatformY + 5.0f, assetManager);
        
        float enemyY = orbiterPlatformY - (OrbiterEnemy.HEIGHT - Platform.HEIGHT) / 2.0f;
        RiseGeneratorUtils.addOrbiterEnemy(rgd, 0.0f, enemyY, assetManager);
        
        rgd.changeGeneratedHeight(orbiterPlatformY + 8.5f);
    }
    
    private static float getMinMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f + absoluteHeight * 0.004f;
        } else {
            return 5.0f;
        }
    }
    
    private static float getMaxMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.5f + absoluteHeight * 0.0035f;
        } else {
            return 6.0f;
        }
    }
}
