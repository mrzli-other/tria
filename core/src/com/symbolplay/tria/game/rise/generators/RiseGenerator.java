package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;

public final class RiseGenerator {
    
    private static final float MIN_HEIGHT_TO_GENERATE = 50.0f;
    
    private static final float NORMAL_PLATFORM_HEIGHT_DISTANCE = 160.0f;
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final RiseGeneratorCrumble riseGeneratorCrumble;
    private final RiseGeneratorVisibleOnJump riseGeneratorVisibleOnJump;
    private final RiseGeneratorRevealOnJump riseGeneratorRevealOnJump;
    private final RiseGeneratorToggleSpikes riseGeneratorToggleSpikes;
    private final RiseGeneratorTimedSpikes riseGeneratorTimedSpikes;
    private final RiseGeneratorAttachedSpikes riseGeneratorAttachedSpikes;
    private final RiseGeneratorEnemy riseGeneratorEnemy;
    private final RiseGeneratorCoin riseGeneratorCoin;
    
    public RiseGenerator(AssetManager assetManager) {
        riseGeneratorStandard = new RiseGeneratorStandard(assetManager);
        riseGeneratorCrumble = new RiseGeneratorCrumble(riseGeneratorStandard, assetManager);
        riseGeneratorVisibleOnJump = new RiseGeneratorVisibleOnJump(riseGeneratorStandard, assetManager);
        riseGeneratorRevealOnJump = new RiseGeneratorRevealOnJump(riseGeneratorStandard, assetManager);
        riseGeneratorToggleSpikes = new RiseGeneratorToggleSpikes(riseGeneratorStandard, assetManager);
        riseGeneratorTimedSpikes = new RiseGeneratorTimedSpikes(riseGeneratorStandard, assetManager);
        riseGeneratorAttachedSpikes = new RiseGeneratorAttachedSpikes(riseGeneratorStandard, assetManager);
        riseGeneratorEnemy = new RiseGeneratorEnemy(riseGeneratorStandard, assetManager);
        riseGeneratorCoin = new RiseGeneratorCoin(riseGeneratorStandard, assetManager);
    }
    
    public void reset() {
    }
    
    public void generateNext(RiseGeneratorData rgd) {
        float targetGenerationHeight = rgd.relativeHeight + MIN_HEIGHT_TO_GENERATE;
        
        while (rgd.relativeHeight < targetGenerationHeight) {
            generatePart(rgd);
        }
    }
    
    private void generatePart(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        if (absoluteHeight <= 60.0f) {
            riseGeneratorStandard.generateInitial(rgd);
        } else if (absoluteHeight <= 500.0f) {
            generateLow(rgd);
        } else {
            generateHigh(rgd);
        }
    }
    
    private void generateLow(RiseGeneratorData rgd) {
        if (isRestingPlaceRequired(rgd)) {
            riseGeneratorStandard.generateRestingPlace(rgd);
        }
        
        float standardWeight = 12.0f;
        float visibleOnJumpWeight = getVisibleOnJumpWeight(rgd);
        float attachedSpikesWeight = getAttachedSpikesWeight(rgd);
        float enemyWeight = 4.0f;
        float coinWeight = getCoinWeight(rgd);
        
        float visibleOnJumpCumulativeWeight = standardWeight + visibleOnJumpWeight;
        float attachedSpikesCumulativeWeight = visibleOnJumpCumulativeWeight + attachedSpikesWeight;
        float enemyCumulativeWeight = attachedSpikesCumulativeWeight + enemyWeight;
        
        float totalWeight = standardWeight + visibleOnJumpWeight + attachedSpikesWeight + enemyWeight + coinWeight;
        
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < standardWeight) {
            riseGeneratorStandard.generateLow(rgd);
        } else if (randomValue < visibleOnJumpCumulativeWeight) {
            riseGeneratorVisibleOnJump.generateLow(rgd);
        } else if (randomValue < attachedSpikesCumulativeWeight) {
            riseGeneratorAttachedSpikes.generateLow(rgd);
        } else if (randomValue < enemyCumulativeWeight) {
            riseGeneratorEnemy.generateLow(rgd);
        } else {
            riseGeneratorCoin.generateLow(rgd);
        }
    }
    
    private void generateHigh(RiseGeneratorData rgd) {
        if (isRestingPlaceRequired(rgd)) {
            riseGeneratorStandard.generateRestingPlace(rgd);
        }
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float standardWeight = getStandardWeight(absoluteHeight);
        float crumbleWeight = getCrumbleWeight(rgd);
        float visibleOnJumpWeight = getVisibleOnJumpWeight(rgd);
        float revealOnJumpWeight = getRevealOnJumpWeight(rgd);
        float toggleSpikesWeight = getToggleSpikesWeight(rgd);
        float timedSpikesWeight = getTimedSpikesWeight(rgd);
        float attachedSpikesWeight = getAttachedSpikesWeight(rgd);
        float enemyWeight = 4.0f;
        float coinWeight = getCoinWeight(rgd);
        
        float crumbleCumulativeWeight = standardWeight + crumbleWeight;
        float visibleOnJumpCumulativeWeight = crumbleCumulativeWeight + visibleOnJumpWeight;
        float revealOnJumpCumulativeWeight = visibleOnJumpCumulativeWeight + revealOnJumpWeight;
        float toggleSpikesCumulativeWeight = revealOnJumpCumulativeWeight + toggleSpikesWeight;
        float timedSpikesCumulativeWeight = toggleSpikesCumulativeWeight + timedSpikesWeight;
        float attachedSpikesCumulativeWeight = timedSpikesCumulativeWeight + attachedSpikesWeight;
        float enemyCumulativeWeight = attachedSpikesCumulativeWeight + enemyWeight;
        
        float totalWeight = standardWeight + crumbleWeight + visibleOnJumpWeight + revealOnJumpWeight + toggleSpikesWeight + timedSpikesWeight + attachedSpikesWeight +
                enemyWeight + coinWeight;
        
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < standardWeight) {
            riseGeneratorStandard.generateHigh(rgd);
        } else if (randomValue < crumbleCumulativeWeight) {
            riseGeneratorCrumble.generateHigh(rgd);
        } else if (randomValue < visibleOnJumpCumulativeWeight) {
            riseGeneratorVisibleOnJump.generateHigh(rgd);
        } else if (randomValue < revealOnJumpCumulativeWeight) {
            riseGeneratorRevealOnJump.generateHigh(rgd);
        } else if (randomValue < toggleSpikesCumulativeWeight) {
            riseGeneratorToggleSpikes.generateHigh(rgd);
        } else if (randomValue < timedSpikesCumulativeWeight) {
            riseGeneratorTimedSpikes.generateHigh(rgd);
        } else if (randomValue < attachedSpikesCumulativeWeight) {
            riseGeneratorAttachedSpikes.generateHigh(rgd);
        } else if (randomValue < enemyCumulativeWeight) {
            riseGeneratorEnemy.generateHigh(rgd);
        } else {
            riseGeneratorCoin.generateHigh(rgd);
        }
    }
    
    private static boolean isRestingPlaceRequired(RiseGeneratorData rgd) {
        return rgd.absoluteHeight >= rgd.lastNormalPlatformAbsoluteHeight + NORMAL_PLATFORM_HEIGHT_DISTANCE;
    }
    
    private static float getStandardWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 12.0f;
        } else if (absoluteHeight <= 2000.0f) {
            return 12.0f - (absoluteHeight - 1000.0f) * 0.004f;
        } else {
            return 8.0f;
        }
    }
    
    private static float getCrumbleWeight(RiseGeneratorData rgd) {
        float weight = 1.0f;
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastCrumbleHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getVisibleOnJumpWeight(RiseGeneratorData rgd) {
        float weight = 1.0f;
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastVisibleOnJumpHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getRevealOnJumpWeight(RiseGeneratorData rgd) {
        float weight = 1.0f;
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastRevealOnJumpHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getToggleSpikesWeight(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float weight;
        if (absoluteHeight <= 2000.0f) {
            weight = 1.0f;
        } else if (absoluteHeight <= 3000.0f) {
            weight = 1.0f - (absoluteHeight - 2000.0f) * 0.001f;
        } else {
            weight = 0.0f;
        }
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastToggleSpikesHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getTimedSpikesWeight(RiseGeneratorData rgd) {
        float weight = 1.0f;
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastTimedSpikesHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getAttachedSpikesWeight(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float weight;
        if (absoluteHeight <= 400.0f) {
            weight = 0.2f;
        } else if (absoluteHeight <= 600.0f) {
            weight = 0.2f + (absoluteHeight - 400.0f) * 0.004f;
        } else {
            weight = 1.0f;
        }
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastCrumbleHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
    
    private static float getCoinWeight(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float weight;
        if (absoluteHeight <= 400.0f) {
            weight = 0.2f;
        } else if (absoluteHeight <= 1200.0f) {
            weight = 0.2f + (absoluteHeight - 400.0f) * 0.001f;
        } else {
            weight = 1.0f;
        }
        
        float distanceFromLast = (float) (rgd.absoluteHeight - rgd.lastCrumbleHeight);
        if (distanceFromLast <= 100.0f) {
            weight *= 0.0f;
        } else if (distanceFromLast <= 200.0f) {
            weight *= 0.5f;
        }
        
        return weight;
    }
}
