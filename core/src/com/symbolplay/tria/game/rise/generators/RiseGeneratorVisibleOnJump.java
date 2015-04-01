package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.enemies.OrbiterEnemy;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorVisibleOnJump {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    
    public RiseGeneratorVisibleOnJump(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generateLow(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateLowJumpBoostBufferZone(rgd);
        generateLowHigh(rgd);
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateLowJumpBoostBufferZone(rgd);
        generateLowHigh(rgd);
    }
    
    private void generateLowHigh(RiseGeneratorData rgd) {
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
        
        if (randomValue < movingSectionWeight) {
            generate(rgd, 0.1f, 1.0f, 0.2f, height, minStepY, maxStepY);
        } else if (randomValue < movingSectionWeight + repositionSectionWeight) {
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
        
        float minMovingSpeed = getMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = getMaxMovingSpeed(absoluteHeight);
        
        float minRepositionAmount = RiseGeneratorUtils.getStandardMinRepositionAmount(absoluteHeight);
        float maxRepositionAmount = RiseGeneratorUtils.getStandardMaxRepositionAmount(absoluteHeight);
        
        float enemyPartChance = getEnemyPartChance(absoluteHeight);
        float spikedPlatformChance = getSpikedPlatformChance(absoluteHeight);
        
        boolean isStationary = normalWeight > 0.0f && movingWeight == 0.0f && repositionWeight == 0.0f;
        
        while (y < height) {
            
            boolean isGenerateEnemyPart = isStationary && MathUtils.randomBoolean(enemyPartChance);
            if (isGenerateEnemyPart) {
                float enemyRandomValue = MathUtils.random(2);
                if (enemyRandomValue == 0) {
                    y += generateEasePatrollerPart(rgd, y);
                } else if (enemyRandomValue == 1) {
                    y += generateSinePatrollerPart(rgd, y);
                } else {
                    y += generateOrbiterPart(rgd, y);
                }
            }
            
            int platformType = RiseGeneratorUtils.getStandardRandomPlatformType(normalWeight, movingWeight, repositionWeight);
            Platform platform = RiseGeneratorUtils.getStandardPlatform(
                    rgd, y, platformType, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount, assetManager);
            
            platform.setVisibleOnJumpFeature();
            
            platforms.add(platform);
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            float yStep = MathUtils.random(minStepY, maxStepY);
            
            if (!isGenerateEnemyPart) {
                if (yStep >= 6.0f && isStationary) {
                    float spikedStepY1 = MathUtils.random(2.0f, 3.0f);
                    addSpikedPlatform(rgd, normalWeight, movingWeight, repositionWeight, y + spikedStepY1, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount);
                    float spikedStepY2 = MathUtils.random(4.0f, yStep - 1.5f);
                    addSpikedPlatform(rgd, normalWeight, movingWeight, repositionWeight, y + spikedStepY2, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount);
                } else if (yStep >= 4.0f && (isStationary || MathUtils.randomBoolean(spikedPlatformChance))) {
                    float spikedStepY = MathUtils.random(2.0f, yStep - 1.5f);
                    addSpikedPlatform(rgd, normalWeight, movingWeight, repositionWeight, y + spikedStepY, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount);
                }
            }
            
            y += yStep;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastVisibleOnJumpHeight = rgd.absoluteHeight;
    }
    
    private float generateSinePatrollerPart(RiseGeneratorData rgd, float y) {
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y + 3.0f, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addSinePatrollerEnemy(rgd, 0.0f, y + 5.2f, assetManager);
        
        return 8.0f;
    }
    
    private float generateEasePatrollerPart(RiseGeneratorData rgd, float y) {
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y + 3.0f, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y + 6.0f, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addEasePatrollerEnemy(rgd, 0.0f, y + 8.2f, assetManager);
        
        return 11.0f;
    }
    
    private float generateOrbiterPart(RiseGeneratorData rgd, float y) {
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y + 2.5f, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), y + 5.0f, assetManager).setVisibleOnJumpFeature();
        
        float orbiterPlatformY = y + 10.0f;
        
        RiseGeneratorUtils.addPlatform(rgd, 2.3f, orbiterPlatformY, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.MAX_PLATFORM_X - 2.3f, orbiterPlatformY, assetManager).setVisibleOnJumpFeature();
        RiseGeneratorUtils.addPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), orbiterPlatformY + 5.0f, assetManager).setVisibleOnJumpFeature();
        
        float enemyY = orbiterPlatformY - (OrbiterEnemy.HEIGHT - Platform.HEIGHT) / 2.0f;
        RiseGeneratorUtils.addOrbiterEnemy(rgd, 0.0f, enemyY, assetManager);
        
        return 18.5f;
    }
    
    private void addSpikedPlatform(RiseGeneratorData rgd, float normalWeight, float movingWeight, float repositionWeight, float y, float minMovingSpeed,
            float maxMovingSpeed, float minRepositionAmount, float maxRepositionAmount) {
        
        int spikedPlatformType = RiseGeneratorUtils.getStandardRandomPlatformType(normalWeight, movingWeight, repositionWeight);
        Platform spikedPlatform = RiseGeneratorUtils.getStandardPlatform(
                rgd, y, spikedPlatformType, minMovingSpeed, maxMovingSpeed, minRepositionAmount, maxRepositionAmount, assetManager);
        spikedPlatform.setVisibleOnJumpFeature();
        spikedPlatform.setSpikesFeature();
        rgd.platforms.add(spikedPlatform);
        rgd.nextPlatformId++;
        rgd.nextGroupId++;
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
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else {
            return 1.0f;
        }
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
        if (absoluteHeight <= 800.0f) {
            return 2.0f + absoluteHeight * 0.005f;
        } else {
            return 6.0f;
        }
    }
    
    private static float getStationaryMaxStepY(float absoluteHeight) {
        if (absoluteHeight <= 800.0f) {
            return 2.5f + absoluteHeight * 0.005f;
        } else {
            return 6.5f;
        }
    }
    
    private static float getEnemyPartChance(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1500.0f) {
            return (absoluteHeight - 500.0f) * 0.0002f;
        } else {
            return 0.2f;
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
    
    public static float getMinMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f + absoluteHeight * 0.0025f;
        } else if (absoluteHeight <= 3000.0f) {
            return 3.5f + (absoluteHeight - 1000.0f) * 0.0005f;
        } else {
            return 4.5f;
        }
    }
    
    public static float getMaxMovingSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.5f + absoluteHeight * 0.003f;
        } else if (absoluteHeight <= 3000.0f) {
            return 4.5f + (absoluteHeight - 1000.0f) * 0.00075f;
        } else {
            return 6.0f;
        }
    }
}
