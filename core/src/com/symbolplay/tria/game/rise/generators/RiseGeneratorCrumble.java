package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorCrumble {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    
    public RiseGeneratorCrumble(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateLowJumpBoostBufferZone(rgd);
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float stationaryWeight = getStationaryWeight(absoluteHeight);
        float movingWeight = getMovingWeight(absoluteHeight);
        float totalWeight = stationaryWeight + movingWeight;
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < stationaryWeight) {
            generateStationary(rgd);
        } else {
            generateMoving(rgd);
        }
    }
    
    private void generateStationary(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float height = getGenerationHeight(absoluteHeight);
        float minStepY = getStationaryMinStepY(absoluteHeight);
        float maxStepY = getStationaryMaxStepY(absoluteHeight);
        
        generate(rgd, 1.0f, 0.0f, 0.0f, height, minStepY, maxStepY);
    }
    
    private void generateMoving(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float height = getGenerationHeight(absoluteHeight);
        float minStepY = 3.0f;
        float maxStepY = 6.0f;
        
        float movingSectionWeight = RiseGeneratorUtils.getStandardMovingSectionWeight(absoluteHeight);
        float repositionSectionWeight = RiseGeneratorUtils.getStandardRepositionSectionWeight(absoluteHeight);
        float mixedSectionWeight = RiseGeneratorUtils.getStandardMixedSectionWeight(absoluteHeight);
        float totalSectionsWeight = movingSectionWeight + repositionSectionWeight + mixedSectionWeight;
        
        float randomValue = MathUtils.random(totalSectionsWeight);
        
        if (randomValue <= movingSectionWeight) {
            generate(rgd, 0.1f, 1.0f, 0.2f, height, minStepY, maxStepY);
        } else if (randomValue <= movingSectionWeight + repositionSectionWeight) {
            generate(rgd, 0.1f, 0.2f, 1.0f, height, minStepY, maxStepY);
        } else {
            generate(rgd, 0.1f, 1.0f, 1.0f, height, minStepY, maxStepY);
        }
    }
    
    private void generate(
            RiseGeneratorData rgd,
            float normalWeight,
            float movingWeight,
            float repositionWeight,
            float height,
            float minStepY,
            float maxStepY) {
        
        Array<Platform> platforms = rgd.platforms;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float y = 0.0f;
        
        float minMovingSpeed = RiseGeneratorUtils.getStandardMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = RiseGeneratorUtils.getStandardMaxMovingSpeed(absoluteHeight);
        
        float minRepositionAmount = RiseGeneratorUtils.getStandardMinRepositionAmount(absoluteHeight);
        float maxRepositionAmount = RiseGeneratorUtils.getStandardMaxRepositionAmount(absoluteHeight);
        
        float spikedPlatformChance = getSpikedPlatformChance(absoluteHeight);
        
        boolean isFirstPlatform = true;
        
        while (y < height) {
            
            int platformType = RiseGeneratorUtils.getStandardRandomPlatformType(normalWeight, movingWeight, repositionWeight);
            Platform platform = RiseGeneratorUtils.getStandardPlatform(
                    rgd, y, platformType, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount, assetManager);
            if (!isFirstPlatform) {
                platform.setCrumbleFeature();
            } else if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_NORMAL) {
                rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
            }
            
            platforms.add(platform);
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            float yStep;
            if (isFirstPlatform) {
                yStep = MathUtils.random(3.5f, 4.0f);
            } else {
                yStep = MathUtils.random(minStepY, maxStepY);
            }
            
            if (yStep >= 4.0f && MathUtils.randomBoolean(spikedPlatformChance)) {
                float spikedStepY = MathUtils.random(2.0f, yStep - 1.5f);
                int spikedPlatformType = RiseGeneratorUtils.getStandardRandomPlatformType(normalWeight, movingWeight, repositionWeight);
                Platform spikedPlatform = RiseGeneratorUtils.getStandardPlatform(
                        rgd, y + spikedStepY, spikedPlatformType, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount, assetManager);
                spikedPlatform.setCrumbleFeature();
                spikedPlatform.setSpikesFeature();
                platforms.add(spikedPlatform);
                rgd.nextPlatformId++;
                rgd.nextGroupId++;
            }
            
            y += yStep;
            
            isFirstPlatform = false;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastCrumbleHeight = rgd.absoluteHeight;
    }
    
    private static float getStationaryWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 4.0f;
        } else if (absoluteHeight <= 1000.0f) {
            return 4.0f - (absoluteHeight - 500.0f) * 0.006f;
        } else if (absoluteHeight <= 1500.0f) {
            return 1.0f - (absoluteHeight - 1000.0f) * 0.002f;
        } else {
            return 0.0f;
        }
    }
    
    private static float getMovingWeight(float absoluteHeight) {
        return 1.0f;
    }
    
    private static float getGenerationHeight(float absoluteHeight) {
        float min;
        if (absoluteHeight <= 4000.0f) {
            min = 20.0f + absoluteHeight * 0.005f;
        } else {
            min = 40.0f;
        }
        
        float max;
        if (absoluteHeight <= 4000.0f) {
            max = 30.0f + absoluteHeight * 0.005f;
        } else {
            max = 50.0f;
        }
        
        return MathUtils.random(min, max);
    }
    
    private static float getStationaryMinStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 2.0f;
        } else if (absoluteHeight <= 1000.0f) {
            return 2.0f + (absoluteHeight - 500.0f) * 0.008f;
        } else {
            return 6.0f;
        }
    }
    
    private static float getStationaryMaxStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 2.5f;
        } else if (absoluteHeight <= 1000.0f) {
            return 2.5f + (absoluteHeight - 500.0f) * 0.008f;
        } else {
            return 6.5f;
        }
    }
    
    private static float getSpikedPlatformChance(float absoluteHeight) {
        if (absoluteHeight <= 800.0f) {
            return 0.3f;
        } else if (absoluteHeight <= 1600.0f) {
            return 0.3f + (absoluteHeight - 800.0f) * 0.000125f;
        } else {
            return 0.4f;
        }
    }
}
