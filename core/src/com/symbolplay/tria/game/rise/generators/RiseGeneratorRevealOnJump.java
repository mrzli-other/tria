package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorRevealOnJump {
    
    private static final float IN_STEP_OFFSET = 0.5f;
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    
    public RiseGeneratorRevealOnJump(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
    }
    
    public void generateLow(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateLowJumpBoostBufferZone(rgd);
        generateLowHigh(rgd);
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateHighJumpBoostBufferZone(rgd);
        generateLowHigh(rgd);
    }
    
    private void generateLowHigh(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float multiplePerStepYChance = getMultiPerStepYChance(absoluteHeight);
        if (MathUtils.randomBoolean(multiplePerStepYChance)) {
            generateMulti(rgd, MathUtils.randomBoolean());
        } else {
            generateSingle(rgd);
        }
    }
    
    private void generateSingle(RiseGeneratorData rgd) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float minStepY = getSingleMinStepY(absoluteHeight);
        float maxStepY = getSingleMaxStepY(absoluteHeight);
        float height = getGenerationHeight(absoluteHeight, minStepY, maxStepY);
        
        float spikedPlatformChance = getSpikedPlatformChance(absoluteHeight);
        float spikedPlatformMaxOffsetX = getSpikedPlatformMaxOffsetX(absoluteHeight);
        
        Array<Float> stepsY = getSingleStepsY(height, minStepY, maxStepY);
        
        float y = 0.0f;
        boolean isNextStepSpikedPlatform = false;
        
        for (int i = 0; i < stepsY.size; i++) {
            float yStep = stepsY.get(i);
            
            boolean isInitiallyVisible = i == 1;
            boolean isLastStep = i == stepsY.size - 1;
            
            boolean isThisStepSpikedPlatform = isNextStepSpikedPlatform;
            
            if (i > 0 && i < stepsY.size - 1) {
                float nextStepY = stepsY.get(i + 1);
                boolean isNextStepSpikedPlatformReachableFromCurrentStep = yStep < 4.5f;
                boolean isEnoughSpaceForNextStepSpikedPlatform = nextStepY > 3.0f;
                boolean isNextStepSpikedPlatformFeasible = isNextStepSpikedPlatformReachableFromCurrentStep && isEnoughSpaceForNextStepSpikedPlatform;
                isNextStepSpikedPlatform = isNextStepSpikedPlatformFeasible && MathUtils.randomBoolean(spikedPlatformChance);
            } else {
                isNextStepSpikedPlatform = false;
            }
            
            int firstNextStepPlatformId = rgd.nextPlatformId + (isThisStepSpikedPlatform ? 2 : 1);
            
            float x = RiseGeneratorUtils.getRandomPlatformX();
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, relativeHeight + y, assetManager);
            
            if (i == 0) {
                rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
            } else {
                setSinglePlatformFeatures(
                        platform,
                        isInitiallyVisible,
                        isLastStep,
                        isNextStepSpikedPlatform,
                        firstNextStepPlatformId,
                        false);
            }
            
            platforms.add(platform);
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            
            if (isThisStepSpikedPlatform && i > 0) {
                float prevStepY = stepsY.get(i - 1);
                float spikedPlatformOffset = getSpikedPlatformOffsetY(prevStepY, yStep);
                
                Platform spikedPlatform = new Platform(
                        rgd.nextPlatformId,
                        rgd.nextGroupId,
                        RiseGeneratorUtils.getRandomNextPlatformX(x, 0.0f, spikedPlatformMaxOffsetX, 0.0f, 0.0f),
                        relativeHeight + y + spikedPlatformOffset,
                        assetManager);
                
                setSinglePlatformFeatures(
                        spikedPlatform,
                        isInitiallyVisible,
                        isLastStep,
                        isNextStepSpikedPlatform,
                        firstNextStepPlatformId,
                        true);
                
                platforms.add(spikedPlatform);
                rgd.nextPlatformId++;
                rgd.nextGroupId++;
            }
            
            y += yStep;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastRevealOnJumpHeight = rgd.absoluteHeight;
    }
    
    private void generateMulti(RiseGeneratorData rgd, boolean isThreePositions) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float minStepY = getMultiMinStepY(absoluteHeight);
        float maxStepY = getMultiMaxStepY(absoluteHeight);
        float height = getGenerationHeight(absoluteHeight, minStepY, maxStepY);
        
        Array<Float> stepsY = getMultiStepsY(height, minStepY, maxStepY);
        
        int platformsPerStepY = isThreePositions ? 3 : 2;
        
        float[] positionsX = new float[platformsPerStepY];
        
        float y = 0.0f;
        
        for (int i = 0; i < stepsY.size; i++) {
            float yStep = stepsY.get(i);
            
            boolean isInitiallyVisible = i == 1;
            boolean isLastStep = i == stepsY.size - 1;
            
            if (i == 0) {
                Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, RiseGeneratorUtils.getRandomPlatformX(), relativeHeight + y, assetManager);
                platforms.add(platform);
                rgd.nextPlatformId++;
                rgd.nextGroupId++;
                
                rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
            } else {
                RiseGeneratorUtils.getPositionsX(positionsX, isThreePositions);
                
                int nextStepFirstPlatformId = rgd.nextPlatformId + (isThreePositions ? 3 : 2);
                
                for (int j = 0; j < platformsPerStepY; j++) {
                    
                    float minCurrY = Math.max(y - IN_STEP_OFFSET, 0.0f);
                    float maxCurrY = y + IN_STEP_OFFSET;
                    float currY = MathUtils.random(minCurrY, maxCurrY);
                    Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, positionsX[j], currY + relativeHeight, assetManager);
                    setMultiSpikesPlatformFeature(
                            platform,
                            isInitiallyVisible,
                            isLastStep,
                            j,
                            isThreePositions,
                            nextStepFirstPlatformId);
                    
                    platforms.add(platform);
                    rgd.nextPlatformId++;
                }
                
                rgd.nextGroupId++;
            }
            
            y += yStep;
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastRevealOnJumpHeight = rgd.absoluteHeight;
    }
    
    private static Array<Float> getSingleStepsY(float height, float minStepY, float maxStepY) {
        Array<Float> stepsY = new Array<Float>(30);
        float y = 0.0f;
        
        boolean isFirstStep = true;
        
        while (y < height) {
            float yStep;
            if (isFirstStep) {
                yStep = MathUtils.random(3.5f, 4.0f);
                isFirstStep = false;
            } else {
                yStep = MathUtils.random(minStepY, maxStepY);
            }
            
            stepsY.add(yStep);
            y += yStep;
        }
        
        return stepsY;
    }
    
    private static Array<Float> getMultiStepsY(float height, float minStepY, float maxStepY) {
        Array<Float> stepsY = new Array<Float>(30);
        float y = 0.0f;
        
        boolean isFirstStep = true;
        
        while (y < height) {
            float yStep;
            if (isFirstStep) {
                yStep = 4.0f;
                isFirstStep = false;
            } else {
                yStep = Math.min(MathUtils.random(minStepY, maxStepY), 6.5f - IN_STEP_OFFSET);
            }
            
            stepsY.add(yStep);
            y += yStep;
        }
        
        return stepsY;
    }
    
    private static void setSinglePlatformFeatures(
            Platform platform,
            boolean isInitiallyVisible,
            boolean isLastStep,
            boolean isNextStepSpikedPlatform,
            int firstNextStepPlatformId,
            boolean isSpiked) {
        
        int[] revealIds;
        if (isLastStep) {
            revealIds = new int[0];
        } else if (isNextStepSpikedPlatform) {
            revealIds = new int[] { firstNextStepPlatformId, firstNextStepPlatformId + 1 };
        } else {
            revealIds = new int[] { firstNextStepPlatformId };
        }
        
        platform.setRevealOnJumpFeature(isInitiallyVisible, revealIds);
        platform.setCrumbleFeature();
        if (isSpiked) {
            platform.setSpikesFeature();
        }
    }
    
    private static void setMultiSpikesPlatformFeature(
            Platform platform,
            boolean isInitiallyVisible,
            boolean isLastStep,
            int indexInStepY,
            boolean isThreePositions,
            int nextStepFirstPlatformId) {
        
        int[] revealIds;
        if (isLastStep) {
            revealIds = new int[0];
        } else if (isThreePositions) {
            revealIds = new int[] { nextStepFirstPlatformId, nextStepFirstPlatformId + 1, nextStepFirstPlatformId + 2 };
        } else {
            revealIds = new int[] { nextStepFirstPlatformId, nextStepFirstPlatformId + 1 };
        }
        
        platform.setRevealOnJumpFeature(isInitiallyVisible, revealIds);
        platform.setCrumbleFeature();
        if (indexInStepY != 0) {
            platform.setSpikesFeature();
        }
    }
    
    private static float getMultiPerStepYChance(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1300.0f) {
            return 0.0f + (absoluteHeight - 500.0f) * 0.0005f;
        } else if (absoluteHeight <= 2100.0f) {
            return 0.4f - (absoluteHeight - 1300.0f) * 0.0005f;
        } else {
            return 0.0f;
        }
    }
    
    private static float getSpikedPlatformOffsetY(float prevStepY, float stepY) {
        float max1 = 6.5f - prevStepY; // so it can be reached from previous step
        float max2 = stepY - 1.0f; // so it fits in current step
        float min = 2.0f;
        float max = Math.min(max1, max2);
        
        return MathUtils.random(min, max);
    }
    
    private static float getSpikedPlatformMaxOffsetX(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 5.0f;
        } else if (absoluteHeight <= 2500.0f) {
            return 5.0f - (absoluteHeight - 500.0f) * 0.002f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getGenerationHeight(float absoluteHeight, float minStepY, float maxStepY) {
        
        float minExpectedStepsInGenerationHeight;
        if (absoluteHeight <= 2000.0f) {
            minExpectedStepsInGenerationHeight = 8.0f + absoluteHeight * 0.002f;
        } else {
            minExpectedStepsInGenerationHeight = 12.0f;
        }
        
        float maxExpectedStepsInGenerationHeight;
        if (absoluteHeight <= 2000.0f) {
            maxExpectedStepsInGenerationHeight = 10.0f + absoluteHeight * 0.002f;
        } else {
            maxExpectedStepsInGenerationHeight = 14.0f;
        }
        
        float expectedStepsInGenerationHeight = MathUtils.random(minExpectedStepsInGenerationHeight, maxExpectedStepsInGenerationHeight);
        float avgStepY = (minStepY + maxStepY) / 2.0f;
        
        return expectedStepsInGenerationHeight * avgStepY;
    }
    
    private static float getSingleMinStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 2.0f;
        } else if (absoluteHeight <= 1000.0f) {
            return 2.0f + (absoluteHeight - 500.0f) * 0.002f;
        } else {
            return 3.0f;
        }
    }
    
    private static float getSingleMaxStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 2.5f;
        } else if (absoluteHeight <= 1000.0f) {
            return 2.5f + (absoluteHeight - 500.0f) * 0.007f;
        } else if (absoluteHeight <= 1250.0f) {
            return 6.0f + (absoluteHeight - 1000.0f) * 0.002f;
        } else {
            return 6.5f;
        }
    }
    
    private static float getSpikedPlatformChance(float absoluteHeight) {
        if (absoluteHeight <= 750.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1000.0f) {
            return 0.0f + (absoluteHeight - 750.0f) * 0.0016f;
        } else if (absoluteHeight <= 2500.0f) {
            return 0.4f + (absoluteHeight - 1000.0f) * 0.0004f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getMultiMinStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 3.0f;
        } else if (absoluteHeight <= 1300.0f) {
            return 3.0f + (absoluteHeight - 500.0f) * 0.003125f;
        } else {
            return 5.5f;
        }
    }
    
    private static float getMultiMaxStepY(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 3.5f;
        } else if (absoluteHeight <= 1300.0f) {
            return 3.5f + (absoluteHeight - 500.0f) * 0.003125f;
        } else {
            return 6.0f;
        }
    }
}
