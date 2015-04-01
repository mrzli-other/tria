package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorAttachedSpikes {
    
    private static final float SIDE_SPIKES_WIDTH = 0.5f;
    
    private static final float NO_SIDE_SPIKES_MOVEMENT_RANGE = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH;
    private static final float NO_SIDE_SPIKES_MAX_MOVEMENT_OFFSET = NO_SIDE_SPIKES_MOVEMENT_RANGE * 2.0f;
    
    private static final float SIDE_SPIKES_MOVEMENT_RANGE = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH - SIDE_SPIKES_WIDTH;
    private static final float SIDE_SPIKES_MAX_MOVEMENT_OFFSET = SIDE_SPIKES_MOVEMENT_RANGE * 2.0f;
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final CoinGenerator coinGenerator;
    private final AssetManager assetManager;
    
    public RiseGeneratorAttachedSpikes(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.coinGenerator = new CoinGenerator(assetManager);
        this.assetManager = assetManager;
    }
    
    public void generateLow(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateLowJumpBoostBufferZone(rgd);
        
        float height = MathUtils.random(25.0f, 35.0f);
        generate(rgd, false, height);
    }
    
    public void generateHigh(RiseGeneratorData rgd) {
        riseGeneratorStandard.generateHighJumpBoostBufferZone(rgd);
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        boolean isSideSpikes = isSideSpikes(absoluteHeight);
        
        int numRepeats = isSideSpikes ? 1 : MathUtils.random(1, 2);
        float minHeight = 35.0f + (2 - numRepeats) * 10.0f;
        float maxHeight = 45.0f + (2 - numRepeats) * 10.0f;
        
        for (int i = 0; i < numRepeats; i++) {
            if (i > 0) {
                float standardHeight = MathUtils.random(3.0f, 7.0f);
                riseGeneratorStandard.generateHigh(rgd, standardHeight, false);
            }
            
            float height = MathUtils.random(minHeight, maxHeight);
            generate(rgd, isSideSpikes, height);
        }
    }
    
    private void generate(RiseGeneratorData rgd, boolean isSideSpikes, float height) {
        
        Array<Platform> platforms = rgd.platforms;
        float relativeHeight = rgd.relativeHeight;
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float x = 0.0f;
        float y = 0.0f;
        
        float minMovingSpeed = getMinMovingSpeed(absoluteHeight, isSideSpikes);
        float maxMovingSpeed = getMaxMovingSpeed(absoluteHeight, isSideSpikes);
        
        coinGenerator.setAbsoluteHeight(absoluteHeight);
        
        int stepYIndex = 0;
        while (y < height) {
            
            boolean isBottomSpikes = stepYIndex > 1 && (!isSideSpikes || stepYIndex % 2 == 0);
            
            boolean isSideSpikesCurr = stepYIndex > 1 && isSideSpikes && stepYIndex % 2 == 0;
            boolean isLeftSpikesCurr = isSideSpikesCurr && MathUtils.randomBoolean();
            boolean isRightSpikesCurr = isSideSpikesCurr && !isLeftSpikesCurr;
            
            if (isLeftSpikesCurr) {
                x = SIDE_SPIKES_WIDTH;
            } else {
                x = 0.0f;
            }
            
            Platform platform = new Platform(rgd.nextPlatformId, rgd.nextGroupId, x, y + relativeHeight, assetManager);
            coinGenerator.generate(0.3f, rgd, platform);
            
            setHorizontalPlatformMovement(platform, minMovingSpeed, maxMovingSpeed, isSideSpikesCurr);
            
            if (isBottomSpikes || isLeftSpikesCurr || isRightSpikesCurr) {
                platform.setAttachedSpikesFeature(isBottomSpikes, isLeftSpikesCurr, isRightSpikesCurr);
            }
            
            platforms.add(platform);
            
            rgd.nextPlatformId++;
            rgd.nextGroupId++;
            stepYIndex++;
            
            y += MathUtils.random(5.5f, 6.0f);
        }
        
        rgd.changeGeneratedHeight(y);
        rgd.lastAttachedSpikesHeight = rgd.absoluteHeight;
    }
    
    private static void setHorizontalPlatformMovement(Platform platform, float minMovingSpeed, float maxMovingSpeed, boolean isSideSpikes) {
        float speed = MathUtils.random(minMovingSpeed, maxMovingSpeed);
        float initialOffset = MathUtils.random(isSideSpikes ? SIDE_SPIKES_MAX_MOVEMENT_OFFSET : NO_SIDE_SPIKES_MAX_MOVEMENT_OFFSET);
        platform.setHorizontalMovement(isSideSpikes ? SIDE_SPIKES_MOVEMENT_RANGE : NO_SIDE_SPIKES_MOVEMENT_RANGE, speed, initialOffset);
    }
    
    private static boolean isSideSpikes(float absoluteHeight) {
        float sideSpikesChance;
        if (absoluteHeight <= 2000.0f) {
            sideSpikesChance = 0.0f;
        } else if (absoluteHeight <= 3000.0f) {
            sideSpikesChance = (absoluteHeight - 2000.0f) * 0.0004f;
        } else {
            sideSpikesChance = 0.4f;
        }
        
        return MathUtils.random() < sideSpikesChance;
    }
    
    private static float getMinMovingSpeed(float absoluteHeight, boolean isSideSpikes) {
        if (isSideSpikes) {
            if (absoluteHeight <= 2500.0f) {
                return 0.5f + absoluteHeight * 0.0002f;
            } else {
                return 1.0f;
            }
        } else {
            if (absoluteHeight <= 1000.0f) {
                return 0.5f + absoluteHeight * 0.002f;
            } else if (absoluteHeight <= 3000.0f) {
                return 2.5f + (absoluteHeight - 1000.0f) * 0.00075f;
            } else {
                return 4.0f;
            }
        }
    }
    
    private static float getMaxMovingSpeed(float absoluteHeight, boolean isSideSpikes) {
        if (isSideSpikes) {
            if (absoluteHeight <= 2500.0f) {
                return 1.0f + absoluteHeight * 0.0004f;
            } else {
                return 2.0f;
            }
        } else {
            if (absoluteHeight <= 1000.0f) {
                return 2.0f + absoluteHeight * 0.002f;
            } else if (absoluteHeight <= 3000.0f) {
                return 4.0f + (absoluteHeight - 1000.0f) * 0.001f;
            } else {
                return 6.0f;
            }
        }
    }
}
