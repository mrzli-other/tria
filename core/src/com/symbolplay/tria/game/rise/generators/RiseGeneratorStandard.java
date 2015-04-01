package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.features.JumpBoostPlatformFeature;

public final class RiseGeneratorStandard {
    
    public static final float REACH_JUMP_BOOST_SMALL = 22.0f;
    public static final float REACH_JUMP_BOOST_MEDIUM = 32.0f;
    public static final float REACH_JUMP_BOOST_LARGE = 45.0f;
    
    private final CoinGenerator coinGenerator;
    private final AssetManager assetManager;
    
    public RiseGeneratorStandard(AssetManager assetManager) {
        this.assetManager = assetManager;
        coinGenerator = new CoinGenerator(assetManager);
    }
    
    public void generateInitial(RiseGeneratorData rgd) {
        generate(rgd, 1.0f, 0.0f, 0.0f, 30.0f, 1.0f, 1.0f, 0.5f, 1.0f, false, false, false);
        generate(rgd, 1.0f, 0.0f, 0.0f, 40.0f, 1.0f, 1.0f, 0.5f, 1.0f, true, false, true);
    }
    
    public void generateLow(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float height = getGenerationHeight(absoluteHeight);
        generateLow(rgd, height, true);
    }
    
    public void generateLow(RiseGeneratorData rgd, float height, boolean hasJumpBoost) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minStepY = RiseGeneratorUtils.getStandardMinStepY(absoluteHeight);
        float maxStepY = RiseGeneratorUtils.getStandardMaxStepY(absoluteHeight);
        
        generateLow(rgd, height, minStepY, maxStepY, hasJumpBoost);
    }
    
    public void generateLow(RiseGeneratorData rgd, float height, float minStepY, float maxStepY, boolean hasJumpBoost) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = RiseGeneratorUtils.getStandardMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = RiseGeneratorUtils.getStandardMaxMovingSpeed(absoluteHeight);
        
        generateLow(rgd, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, hasJumpBoost);
    }
    
    public void generateLow(
            RiseGeneratorData rgd,
            float height,
            float minStepY,
            float maxStepY,
            float minMovingSpeed,
            float maxMovingSpeed,
            boolean isGenerateJumpBoost) {
            
        int randomValue = MathUtils.random(4);
        
        if (randomValue == 0) {
            generate(rgd, 1.0f, 0.0f, 0.2f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        } else if (randomValue == 1 || randomValue == 2) {
            generate(rgd, 1.0f, 0.2f, 1.0f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        } else if (randomValue == 3) {
            generate(rgd, 0.3f, 1.0f, 0.2f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        } else {
            generate(rgd, 0.3f, 0.1f, 1.0f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        }
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float height = getGenerationHeight(absoluteHeight);
        generateHigh(rgd, height, true);
    }
    
    public void generateHigh(RiseGeneratorData rgd, float height, boolean hasJumpBoost) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minStepY = RiseGeneratorUtils.getStandardMinStepY(absoluteHeight);
        float maxStepY = RiseGeneratorUtils.getStandardMaxStepY(absoluteHeight);
        
        generateHigh(rgd, height, minStepY, maxStepY, hasJumpBoost);
    }
    
    public void generateHigh(RiseGeneratorData rgd, float height, float minStepY, float maxStepY, boolean hasJumpBoost) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float minMovingSpeed = RiseGeneratorUtils.getStandardMinMovingSpeed(absoluteHeight);
        float maxMovingSpeed = RiseGeneratorUtils.getStandardMaxMovingSpeed(absoluteHeight);
        
        generateHigh(rgd, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, hasJumpBoost);
    }
    
    public void generateHigh(
            RiseGeneratorData rgd,
            float height,
            float minStepY,
            float maxStepY,
            float minMovingSpeed,
            float maxMovingSpeed,
            boolean isGenerateJumpBoost) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float movingSectionWeight = RiseGeneratorUtils.getStandardMovingSectionWeight(absoluteHeight);
        float repositionSectionWeight = RiseGeneratorUtils.getStandardRepositionSectionWeight(absoluteHeight);
        float mixedSectionWeight = RiseGeneratorUtils.getStandardMixedSectionWeight(absoluteHeight);
        float totalSectionsWeight = movingSectionWeight + repositionSectionWeight + mixedSectionWeight;
        
        float randomValue = MathUtils.random(totalSectionsWeight);
        
        if (randomValue <= movingSectionWeight) {
            float movingWeight = getMovingSectionMovingWeight(absoluteHeight);
            float repositionWeight = getMovingSectionRepositionWeight(absoluteHeight);
            generate(rgd, 0.1f, movingWeight, repositionWeight, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        } else if (randomValue <= movingSectionWeight + repositionSectionWeight) {
            generate(rgd, 0.1f, 0.2f, 1.0f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        } else {
            generate(rgd, 0.1f, 1.0f, 1.0f, height, minStepY, maxStepY, minMovingSpeed, maxMovingSpeed, isGenerateJumpBoost, true, true);
        }
    }
    
    public void generateRestingPlace(RiseGeneratorData rgd) {
        float height = MathUtils.random(2.0f, 6.0f);
        generate(rgd, 1.0f, 0.0f, 0.0f, height, 2.0f, 4.0f, 0.5f, 1.0f, false, false, true);
    }
    
    public void generateLowJumpBoostBufferZone(RiseGeneratorData rgd) {
        if (rgd.lastJumpBoostReachAbsoluteHeight > rgd.absoluteHeight) {
            float height = (float) (rgd.lastJumpBoostReachAbsoluteHeight - rgd.absoluteHeight);
            generateLow(rgd, height, false);
        }
    }
    
    public void generateHighJumpBoostBufferZone(RiseGeneratorData rgd) {
        if (rgd.lastJumpBoostReachAbsoluteHeight > rgd.absoluteHeight) {
            float height = (float) (rgd.lastJumpBoostReachAbsoluteHeight - rgd.absoluteHeight);
            generateHigh(rgd, height, false);
        }
    }
    
    public void generateTest(RiseGeneratorData rgd) {
        Array<Platform> platforms = rgd.platforms;
        
//        {
//            float x = 0.0f;
//            float y = 0.0f;
//            
//            Array<PlatformFeatureBaseData> featuresData = new Array<PlatformFeatureBaseData>(true, 1);
//            PlatformFeatureBaseData jumpBoostData = new JumpBoostPlatformFeatureData(0.0f, JumpBoostPlatformFeatureData.SIZE_SMALL);
//            featuresData.add(jumpBoostData);
//            
//            PlatformData platformData = new PlatformData(rgd.nextPlatformId, rgd.nextGroupId, x, y, featuresData);
//            Platform platform = new Platform(platformData, relativeHeight, assetManager);
//            platforms.add(platform);
//            
//            rgd.nextPlatformId++;
//            rgd.nextGroupId++;
//            
//            rgd.changeGeneratedHeight(12.0f);
//        }
//        
//        {
//            float x = 2.0f;
//            float y = 0.0f;
//            
//            Array<PlatformFeatureBaseData> featuresData = new Array<PlatformFeatureBaseData>(true, 1);
//            PlatformFeatureBaseData jumpBoostData = new JumpBoostPlatformFeatureData(0.0f, JumpBoostPlatformFeatureData.SIZE_MEDIUM);
//            featuresData.add(jumpBoostData);
//            
//            PlatformData platformData = new PlatformData(rgd.nextPlatformId, rgd.nextGroupId, x, y, featuresData);
//            Platform platform = new Platform(platformData, relativeHeight, assetManager);
//            platforms.add(platform);
//            
//            rgd.nextPlatformId++;
//            rgd.nextGroupId++;
//            
//            rgd.changeGeneratedHeight(12.0f);
//        }
//        
//        {
//            float x = 4.0f;
//            float y = 0.0f;
//            
//            Array<PlatformFeatureBaseData> featuresData = new Array<PlatformFeatureBaseData>(true, 1);
//            PlatformFeatureBaseData jumpBoostData = new JumpBoostPlatformFeatureData(0.0f, JumpBoostPlatformFeatureData.SIZE_LARGE);
//            featuresData.add(jumpBoostData);
//            
//            PlatformData platformData = new PlatformData(rgd.nextPlatformId, rgd.nextGroupId, x, y, featuresData);
//            Platform platform = new Platform(platformData, relativeHeight, assetManager);
//            platforms.add(platform);
//            
//            rgd.nextPlatformId++;
//            rgd.nextGroupId++;
//            
//            rgd.changeGeneratedHeight(12.0f);
//        }
        
        {
            float x = 0.0f;
            float y = 0.0f;
            
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + rgd.relativeHeight, assetManager);
            platforms.add(platform);
            
            ItemGenerator.addAntiGravityItem(rgd, platform, assetManager);
            
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            rgd.changeGeneratedHeight(5.0f);
        }
        
        {
            float x = 0.0f;
            float y = 0.0f;
            
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + rgd.relativeHeight, assetManager);
            platforms.add(platform);
            
            ItemGenerator.addRocketItem(rgd, platform, assetManager);
            
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            rgd.changeGeneratedHeight(5.0f);
        }
        
        {
            float x = 0.0f;
            float y = 0.0f;
            
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + rgd.relativeHeight, assetManager);
            platforms.add(platform);
            
            ItemGenerator.addLifeItem(rgd, platform, assetManager);
            
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            rgd.changeGeneratedHeight(5.0f);
        }
    }
    
    private void generate(
            RiseGeneratorData rgd,
            float normalWeight,
            float movingWeight,
            float repositionWeight,
            float height,
            float minStepY,
            float maxStepY,
            float minMovingSpeed,
            float maxMovingSpeed,
            boolean isGenerateJumpBoost,
            boolean isGenerateItems,
            boolean isGenerateCoins) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float x;
        float y = 0.0f;
        
        float minRepositionAmount = RiseGeneratorUtils.getStandardMinRepositionAmount(absoluteHeight);
        float maxRepositionAmount = RiseGeneratorUtils.getStandardMaxRepositionAmount(absoluteHeight);
        
        float jumpBoostTotalChance = 0.075f;
        
        int itemTypeToGenerate = ItemGenerator.getItemTypeToGenerate(rgd);
        float itemGenerationHeight = 0.0f;
        boolean isItemGenerated = false;
        if (itemTypeToGenerate != ItemGenerator.ITEM_TYPE_TO_GENERATE_NONE) {
            itemGenerationHeight = MathUtils.random(height * 0.8f);
        }
        
        coinGenerator.setAbsoluteHeight(absoluteHeight);
        
        while (y < height) {
            
            int platformType = RiseGeneratorUtils.getStandardRandomPlatformType(normalWeight, movingWeight, repositionWeight);
            if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_NORMAL) {
                x = RiseGeneratorUtils.getRandomPlatformX();
                rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
            } else {
                x = 0.0f;
            }
            
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + relativeHeight, assetManager);
            
            if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_MOVING) {
                RiseGeneratorUtils.setHorizontalPlatformMovement(platform, minMovingSpeed, maxMovingSpeed);
            } else if (platformType == RiseGeneratorUtils.STANDARD_MOVEMENT_TYPE_REPOSITION) {
                RiseGeneratorUtils.setRepositionPlatformMovement(platform, minRepositionAmount, maxRepositionAmount);
            }
            
            boolean isJumpBoostThisStep = false;
            if (isGenerateJumpBoost && MathUtils.random() < jumpBoostTotalChance) {
                setJumpBoostPlatformFeature(platform, rgd, rgd.absoluteHeight + y);
                isJumpBoostThisStep = true;
            }
            
            boolean isItemThisStep = false;
            if (isGenerateItems && itemTypeToGenerate != ItemGenerator.ITEM_TYPE_TO_GENERATE_NONE && y >= itemGenerationHeight && !isJumpBoostThisStep && !isItemGenerated) {
                if (itemTypeToGenerate == ItemGenerator.ITEM_TYPE_TO_GENERATE_LIFE) {
                    ItemGenerator.addLifeItem(rgd, platform, assetManager);
                } else if (itemTypeToGenerate == ItemGenerator.ITEM_TYPE_TO_GENERATE_ANTI_GRAVITY) {
                    ItemGenerator.addAntiGravityItem(rgd, platform, assetManager);
                } else if (itemTypeToGenerate == ItemGenerator.ITEM_TYPE_TO_GENERATE_ROCKET) {
                    ItemGenerator.addRocketItem(rgd, platform, assetManager);
                }
                
                isItemGenerated = true;
                isItemThisStep = true;
            }
            
            boolean isCoinThisStep = false;
            if (isGenerateCoins && !isJumpBoostThisStep && !isItemThisStep) {
                coinGenerator.generate(0.3f, rgd, platform);
                isCoinThisStep = true;
            }
            
            platforms.add(platform);
            
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            float yStep;
            if (isJumpBoostThisStep) {
                yStep = 4.0f;
            } else if (isItemThisStep) {
                yStep = MathUtils.random(minStepY, maxStepY);
                yStep = Math.max(yStep, 2.5f);
            } else if (isCoinThisStep) {
                yStep = MathUtils.random(minStepY, maxStepY);
                yStep = Math.max(yStep, 1.5f);
            } else {
                yStep = MathUtils.random(minStepY, maxStepY);
            }
            
            y += yStep;
        }
        
        rgd.changeGeneratedHeight(y);
    }
    
    private static void setJumpBoostPlatformFeature(Platform platform, RiseGeneratorData rgd, double currentAbsoluteHeight) {
        
        float jumpBoostSmallWeight = 1.0f;
        float jumpBoostMediumWeight = 1.0f;
        float jumpBoostLargeWeight = 0.2f;
        float jumpBoostTotalWeight = jumpBoostSmallWeight + jumpBoostMediumWeight + jumpBoostLargeWeight;
        
        float randomValue = MathUtils.random(jumpBoostTotalWeight);
        
        int size;
        float jumpReach;
        if (randomValue < jumpBoostSmallWeight) {
            size = JumpBoostPlatformFeature.SIZE_SMALL;
            jumpReach = REACH_JUMP_BOOST_SMALL;
        } else if (randomValue < jumpBoostSmallWeight + jumpBoostMediumWeight) {
            size = JumpBoostPlatformFeature.SIZE_MEDIUM;
            jumpReach = REACH_JUMP_BOOST_MEDIUM;
        } else {
            size = JumpBoostPlatformFeature.SIZE_LARGE;
            jumpReach = REACH_JUMP_BOOST_LARGE;
        }
        
        double currentJumpBoostReachAbsoluteHeight = currentAbsoluteHeight + jumpReach;
        if (currentJumpBoostReachAbsoluteHeight > rgd.lastJumpBoostReachAbsoluteHeight) {
            rgd.lastJumpBoostReachAbsoluteHeight = currentJumpBoostReachAbsoluteHeight;
        }
        
        platform.setJumpBoostFeature(MathUtils.random(), size);
    }
    
    private static float getGenerationHeight(float absoluteHeight) {
        float min;
        if (absoluteHeight <= 4000.0f) {
            min = 10.0f + absoluteHeight * 0.01f;
        } else {
            min = 50.0f;
        }
        
        float max;
        if (absoluteHeight <= 4000.0f) {
            max = 30.0f + absoluteHeight * 0.01f;
        } else {
            max = 70.0f;
        }
        
        return MathUtils.random(min, max);
    }
    
    // used on high, to increase movement weight on moving rise sections on greater heights to increase difficulty
    private static float getMovingSectionMovingWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 1.0f;
        } else if (absoluteHeight <= 2000.0f) {
            return 1.0f + (absoluteHeight - 1000.0f) * 0.002f;
        } else {
            return 3.0f;
        }
    }
    
    // used on high, to increase movement weight on moving rise sections on greater heights to increase difficulty
    private static float getMovingSectionRepositionWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 0.2f;
        } else if (absoluteHeight <= 2000.0f) {
            return 0.2f - (absoluteHeight - 1000.0f) * 0.0001f;
        } else {
            return 0.1f;
        }
    }
}
