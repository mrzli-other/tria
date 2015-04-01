package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;

public final class RiseGeneratorEnemyEasePatroller {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    
    public RiseGeneratorEnemyEasePatroller(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generate(RiseGeneratorData rgd, boolean isLow) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        int minNumRepeats = getMinNumRepeats(absoluteHeight);
        int maxNumRepeats = getMaxNumRepeats(absoluteHeight);
        int numRepeats = MathUtils.random(minNumRepeats, maxNumRepeats);
        
        for (int i = 0; i < numRepeats; i++) {
            if (i > 0) {
                float standardHeight = MathUtils.random(3.0f, 7.0f);
                if (isLow) {
                    riseGeneratorStandard.generateLow(rgd, standardHeight, false);
                } else {
                    riseGeneratorStandard.generateHigh(rgd, standardHeight, false);
                }
            }
            
            generatePart(rgd);
        }
    }
    
    private static int getMinNumRepeats(float absoluteHeight) {
        if (absoluteHeight <= 3200.0f) {
            return 1 + (int) (absoluteHeight * 0.000625f);
        } else {
            return 3;
        }
    }
    
    private static int getMaxNumRepeats(float absoluteHeight) {
        if (absoluteHeight <= 2500.0f) {
            return 1 + (int) (absoluteHeight * 0.0016f);
        } else {
            return 5;
        }
    }
    
    public void generatePart(RiseGeneratorData rgd) {
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 3.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 6.0f, assetManager);
        RiseGeneratorUtils.addEasePatrollerEnemy(rgd, 0.0f, 8.2f, assetManager);
        
        rgd.changeGeneratedHeight(11.0f);
    }
}
