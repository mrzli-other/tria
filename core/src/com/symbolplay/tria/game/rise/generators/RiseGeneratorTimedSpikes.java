package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorTimedSpikes {
    
    private static final float STEP_Y_2 = 3.5f;
    private static final float STEP_Y_3 = 5.5f;
    private static final float IN_STEP_OFFSET = 0.5f;
    
    private final AssetManager assetManager;
    private final RiseGeneratorStandard riseGeneratorStandard;
    
    private final float[] positionsX;
    
    public RiseGeneratorTimedSpikes(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
        
        positionsX = new float[3];
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateHighJumpBoostBufferZone(rgd);
        
        int numRepeats = MathUtils.random(2) + 1;
        float minHeight = 15.0f + (3 - numRepeats) * 7.0f;
        float maxHeight = 20.0f + (3 - numRepeats) * 9.0f;
        
        for (int i = 0; i < numRepeats; i++) {
            if (i > 0) {
                riseGeneratorStandard.generateHigh(rgd, 3.0f, false);
            }
            
            boolean isThreePositions = MathUtils.randomBoolean();
            float height = MathUtils.random(minHeight, maxHeight);
            generate(rgd, isThreePositions, height);
        }
    }
    
    private void generate(RiseGeneratorData rgd, boolean isThreePositions, float height) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        int platformsPerStepY = isThreePositions ? 3 : 2;
        float y = 0.0f;
        float stepY = isThreePositions ? STEP_Y_3 : STEP_Y_2;
        
        float cycleDuration = getCycleDuration(absoluteHeight, isThreePositions);
        
        while (y < height) {
            RiseGeneratorUtils.getPositionsX(positionsX, isThreePositions);
            
            for (int i = 0; i < platformsPerStepY; i++) {
                
                float minCurrY = Math.max(y - IN_STEP_OFFSET, 0.0f);
                float maxCurrY = y + IN_STEP_OFFSET;
                float currY = MathUtils.random(minCurrY, maxCurrY);
                Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, positionsX[i], currY + relativeHeight, assetManager);
                
                setTimedSpikesPlatformFeatureData(platform, cycleDuration, i, isThreePositions);
                
                platforms.add(platform);
                
                rgd.nextPlatformId++;
            }
            
            rgd.nextGroupId++;
            
            y += stepY;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastTimedSpikesHeight = rgd.absoluteHeight;
    }
    
    private static void setTimedSpikesPlatformFeatureData(Platform platform, float cycleDuration, int indexInStepY, boolean isThreePositions) {
        int platformsPerStepY = isThreePositions ? 3 : 2;
        
        float cycleOffset = cycleDuration / platformsPerStepY * indexInStepY;
        float activeDuration = platformsPerStepY == 2 ? cycleDuration / 4.0f : cycleDuration / 3.0f;
        float inactiveDuration = cycleDuration - activeDuration;
        
        platform.setTimedSpikesFeature(cycleOffset, activeDuration, inactiveDuration);
    }
    
    private static float getCycleDuration(float absoluteHeight, boolean isThreePositions) {
        if (isThreePositions) {
            if (absoluteHeight <= 500.0f) {
                return 8.0f - absoluteHeight * 0.0088f;
            } else if (absoluteHeight <= 3000.0f) {
                return 3.6f - (absoluteHeight - 500.0f) * 0.00084f;
            } else {
                return 1.5f;
            }
        } else {
            if (absoluteHeight <= 500.0f) {
                return 8.0f - absoluteHeight * 0.008f;
            } else if (absoluteHeight <= 3000.0f) {
                return 4.0f - (absoluteHeight - 500.0f) * 0.00072f;
            } else {
                return 2.2f;
            }
        }
    }
}
