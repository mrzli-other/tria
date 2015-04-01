package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorToggleSpikes {
    
    private static final float STEP_Y_2 = 3.5f;
    private static final float STEP_Y_3 = 5.5f;
    private static final float IN_STEP_OFFSET = 0.5f;
    
    private final AssetManager assetManager;
    private final RiseGeneratorStandard riseGeneratorStandard;
    
    public RiseGeneratorToggleSpikes(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateHighJumpBoostBufferZone(rgd);
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float height = getGenerationHeight(absoluteHeight);
        
        float multiWeight = getMultiWeight(absoluteHeight);
        float adjacentWeight = getAdjacentWeight(absoluteHeight);
        float totalWeight = multiWeight + adjacentWeight;
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < multiWeight) {
            generateMulti(rgd, MathUtils.randomBoolean(), height);
        } else {
            generate2PerStepY(rgd, false, height);
        }
    }
    
    private void generateMulti(RiseGeneratorData rgd, boolean isThreePositions, float height) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        
        int platformsPerStepY = isThreePositions ? 3 : 2;
        
        float[] positionsX = new float[platformsPerStepY];
        
        float y = 0.0f;
        float stepY = isThreePositions ? STEP_Y_3 : STEP_Y_2;
        
        while (y < height) {
            RiseGeneratorUtils.getPositionsX(positionsX, isThreePositions);
            
            for (int i = 0; i < platformsPerStepY; i++) {
                float minCurrY = Math.max(y - IN_STEP_OFFSET, 0.0f);
                float maxCurrY = y + IN_STEP_OFFSET;
                float currY = MathUtils.random(minCurrY, maxCurrY);
                Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, positionsX[i], currY + relativeHeight, assetManager);
                
                setToggleSpikesPlatformFeatureData(platform, i, isThreePositions);
                
                platforms.add(platform);
                
                rgd.nextPlatformId++;
            }
            
            rgd.nextGroupId++;
            
            y += stepY;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastToggleSpikesHeight = rgd.absoluteHeight;
    }
    
    private void generate2PerStepY(RiseGeneratorData rgd, boolean isThreePositions, float height) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float maxChange = getAdjacentMaxChange(absoluteHeight);
        
        int platformsPerStepY = isThreePositions ? 3 : 2;
        
        float[] positionsX = new float[platformsPerStepY];
        
        float y = 0.0f;
        float stepY = isThreePositions ? STEP_Y_3 : STEP_Y_2;
        
        boolean isFirstStep = true;
        float leftSideX = 0.0f;
        
        while (y < height) {
            
            if (isFirstStep) {
                float rightOffset = isThreePositions ? Platform.WIDTH * 2.0f : Platform.WIDTH;
                leftSideX = RiseGeneratorUtils.getRandomPlatformX(0.0f, rightOffset);
                RiseGeneratorUtils.getRandomAdjacentPositionsX(leftSideX, positionsX, isThreePositions);
                isFirstStep = false;
            } else {
                RiseGeneratorUtils.getRandomNextAdjacentPositionsX(leftSideX, 0.0f, maxChange, positionsX, isThreePositions);
            }
            
            for (int i = 0; i < platformsPerStepY; i++) {
                float minCurrY = Math.max(y - IN_STEP_OFFSET, 0.0f);
                float maxCurrY = y + IN_STEP_OFFSET;
                float currY = MathUtils.random(minCurrY, maxCurrY);
                Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, positionsX[i], currY + relativeHeight, assetManager);
                
                setToggleSpikesPlatformFeatureData(platform, i, isThreePositions);
                
                platforms.add(platform);
                rgd.nextPlatformId++;
            }
            
            rgd.nextGroupId++;
            
            y += stepY;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastToggleSpikesHeight = rgd.absoluteHeight;
    }
    
    private static void setToggleSpikesPlatformFeatureData(Platform platform, int indexInStepY, boolean isThreePositions) {
        int platformsPerStepY = isThreePositions ? 3 : 2;
        
        int[] states = new int[platformsPerStepY];
        for (int i = 0; i < platformsPerStepY; i++) {
            states[i] = i == indexInStepY ? 0 : 1;
        }
        
        platform.setToggleSpikesFeature(states);
    }
    
    private static float getGenerationHeight(float absoluteHeight) {
        float min;
        if (absoluteHeight <= 2000.0f) {
            min = 40.0f + absoluteHeight * 0.005f;
        } else {
            min = 50.0f;
        }
        
        float max;
        if (absoluteHeight <= 2000.0f) {
            max = 50.0f + absoluteHeight * 0.005f;
        } else {
            max = 60.0f;
        }
        
        return MathUtils.random(min, max);
    }
    
    private static float getMultiWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 1.0f;
        } else if (absoluteHeight <= 2500.0f) {
            return 1.0f - (absoluteHeight - 500.0f) * 0.0005f;
        } else {
            return 0.0f;
        }
    }
    
    private static float getAdjacentWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 2500.0f) {
            return 0.0f + (absoluteHeight - 500.0f) * 0.0005f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getAdjacentMaxChange(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 4.0f;
        } else if (absoluteHeight <= 2100.0f) {
            return 4.0f - (absoluteHeight - 500.0f) * 0.0025f;
        } else {
            return 0.0f;
        }
    }
}
