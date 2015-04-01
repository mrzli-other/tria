package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;

public final class RiseGeneratorEnemy {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final CoinGenerator coinGenerator;
    
    private final RiseGeneratorEnemyStatic rgeStatic;
    private final RiseGeneratorEnemyPlatformPatroller rgePlatformPatroller;
    private final RiseGeneratorEnemySinePatroller rgeSinePatroller;
    private final RiseGeneratorEnemyEasePatroller rgeEasePatroller;
    private final RiseGeneratorEnemyOrbiter rgeOrbiter;
    private final RiseGeneratorEnemySaw rgeSaw;
    
    public RiseGeneratorEnemy(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        coinGenerator = new CoinGenerator(assetManager);
        
        rgeStatic = new RiseGeneratorEnemyStatic(riseGeneratorStandard, coinGenerator, assetManager);
        rgePlatformPatroller = new RiseGeneratorEnemyPlatformPatroller(riseGeneratorStandard, assetManager);
        rgeSinePatroller = new RiseGeneratorEnemySinePatroller(riseGeneratorStandard, assetManager);
        rgeEasePatroller = new RiseGeneratorEnemyEasePatroller(riseGeneratorStandard, assetManager);
        rgeOrbiter = new RiseGeneratorEnemyOrbiter(riseGeneratorStandard, assetManager);
        rgeSaw = new RiseGeneratorEnemySaw(riseGeneratorStandard, coinGenerator, assetManager);
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
        coinGenerator.setAbsoluteHeight(absoluteHeight);
        
        float staticWeight = 3.0f;
        float patrollerWeight = 1.0f;
        float sawWeight = 4.0f;
        
        float totalWeight = staticWeight + patrollerWeight * 4 + sawWeight;
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < staticWeight) {
            generateStatic(rgd);
        } else if (randomValue < staticWeight + patrollerWeight) {
            rgePlatformPatroller.generate(rgd, false);
        } else if (randomValue < staticWeight + patrollerWeight * 2) {
            rgeSinePatroller.generate(rgd, false);
        } else if (randomValue < staticWeight + patrollerWeight * 3) {
            rgeEasePatroller.generate(rgd, false);
        } else if (randomValue < staticWeight + patrollerWeight * 4) {
            rgeOrbiter.generate(rgd, false);
        } else {
            generateSaw(rgd);
        }
    }
    
    private void generateStatic(RiseGeneratorData rgd) {
        
        // 01 - low
        // 02 - low
        // 03 - low
        // 05 - low-med
        // 07 - low-med
        // 08 - low-med
        // 04 - med
        // 06 - med
        // Crumble01 - med-high
        // Crumble07 - med-high
        // RevealOnJump01 - med-high
        // Crumble02 - high
        // Crumble03 - high
        // Crumble04 - high
        // Crumble05 - high
        // Crumble06 - high
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float lowDiffWeight = getStaticLowDifficultyWeight(absoluteHeight);
        float lowMedDiffWeight = getStaticLowMedDifficultyWeight(absoluteHeight);
        float medDiffWeight = getStaticMedDifficultyWeight(absoluteHeight);
        float medHighDiffWeight = getStaticMedHighDifficultyWeight(absoluteHeight);
        float highDiffWeight = getStaticHighDifficultyWeight(absoluteHeight);
        float jumpBoostWeight = getStaticJumpBoostWeight(absoluteHeight);
        
        float lowDiffCumulativeWeight = lowDiffWeight * 3;
        float lowMedDiffCumulativeWeight = lowDiffCumulativeWeight + lowMedDiffWeight * 3;
        float medDiffCumulativeWeight = lowMedDiffCumulativeWeight + medDiffWeight * 2;
        float medHighDiffCumulativeWeight = medDiffCumulativeWeight + medHighDiffWeight * 3;
        float highDiffCumulativeWeight = medHighDiffCumulativeWeight + highDiffWeight * 5;
        
        float totalWeight = highDiffCumulativeWeight + jumpBoostWeight * 2;
        
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < lowDiffWeight) {
            rgeStatic.generate01(rgd);
        } else if (randomValue < lowDiffWeight * 2) {
            rgeStatic.generate02(rgd);
        } else if (randomValue < lowDiffWeight * 3) {
            rgeStatic.generate03(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight) {
            rgeStatic.generate05(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 2) {
            rgeStatic.generate07(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 3) {
            rgeStatic.generate08(rgd);
        } else if (randomValue < lowMedDiffCumulativeWeight + medDiffWeight) {
            rgeStatic.generate04(rgd);
        } else if (randomValue < lowMedDiffCumulativeWeight + medDiffWeight * 2) {
            rgeStatic.generate06(rgd);
        } else if (randomValue < medDiffCumulativeWeight + medHighDiffWeight) {
            rgeStatic.generateCrumble01(rgd);
        } else if (randomValue < medDiffCumulativeWeight + medHighDiffWeight * 2) {
            rgeStatic.generateCrumble07(rgd);
        } else if (randomValue < medDiffCumulativeWeight + medHighDiffWeight * 3) {
            rgeStatic.generateRevealOnJump01(rgd);
        } else if (randomValue < medHighDiffCumulativeWeight + highDiffWeight) {
            rgeStatic.generateCrumble02(rgd);
        } else if (randomValue < medHighDiffCumulativeWeight + highDiffWeight * 2) {
            rgeStatic.generateCrumble03(rgd);
        } else if (randomValue < medHighDiffCumulativeWeight + highDiffWeight * 3) {
            rgeStatic.generateCrumble04(rgd);
        } else if (randomValue < medHighDiffCumulativeWeight + highDiffWeight * 4) {
            rgeStatic.generateCrumble05(rgd);
        } else if (randomValue < medHighDiffCumulativeWeight + highDiffWeight * 5) {
            rgeStatic.generateCrumble06(rgd);
        } else if (randomValue < highDiffCumulativeWeight + jumpBoostWeight) {
            rgeStatic.generateJumpBoostColumn(rgd);
        } else {
            rgeStatic.generateJumpBoostVoid(rgd);
        }
    }
    
    private void generateSaw(RiseGeneratorData rgd) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float sideJumpWeight = 1.0f;
        float singleWeight = 1.0f;
        float doubleWeight = getSawDoubleWeight(absoluteHeight);
        float waterfallWeight = 1.0f;
        float divideWeight = getSawDivideWeight(absoluteHeight);
        float fixedWeight = getSawFixedWeight(absoluteHeight);
        
        float singleCumulativeWeight = sideJumpWeight + singleWeight;
        float doubleCumulativeWeight = singleCumulativeWeight + doubleWeight;
        float waterfallCumulativeWeight = doubleCumulativeWeight + waterfallWeight;
        float divideCumulativeWeight = waterfallCumulativeWeight + divideWeight;
        float totalWeight = divideCumulativeWeight + fixedWeight;
        
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < sideJumpWeight) {
            rgeSaw.generateSideJump(rgd);
        } else if (randomValue < singleCumulativeWeight) {
            rgeSaw.generateSingle(rgd, false);
        } else if (randomValue < doubleCumulativeWeight) {
            rgeSaw.generateDouble(rgd, false);
        } else if (randomValue < waterfallCumulativeWeight) {
            rgeSaw.generateWaterfall(rgd, false);
        } else if (randomValue < divideCumulativeWeight) {
            rgeSaw.generateMiddleDivide(rgd);
        } else {
            generateSawFixed(rgd);
        }
    }
    
    private void generateSawFixed(RiseGeneratorData rgd) {
        
        // 05 - low
        // 07 - low
        // 08 - low
        // 01 - low-med
        // 03 - low-med
        // 04 - low-med
        // 06 - low-med
        // 09 - low-med
        // 11 - low-med
        // 02 - med
        // 12 - med-high
        // 10 - high
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
        float lowDiffWeight = 1.0f;
        float lowMedDiffWeight = getSawFixedLowMedDifficultyWeight(absoluteHeight);
        float medDiffWeight = getSawFixedMedDifficultyWeight(absoluteHeight);
        float medHighDiffWeight = getSawFixedMedHighDifficultyWeight(absoluteHeight);
        float highDiffWeight = getSawFixedHighDifficultyWeight(absoluteHeight);
        
        float lowDiffCumulativeWeight = lowDiffWeight * 3;
        float lowMedDiffCumulativeWeight = lowDiffCumulativeWeight + lowMedDiffWeight * 6;
        float medDiffCumulativeWeight = lowMedDiffCumulativeWeight + medDiffWeight;
        float medHighDiffCumulativeWeight = medDiffCumulativeWeight + medHighDiffWeight;
        float highDiffCumulativeWeight = medHighDiffCumulativeWeight + highDiffWeight;
        float totalWeight = highDiffCumulativeWeight;
        
        float randomValue = MathUtils.random(totalWeight);
        
        if (randomValue < lowDiffWeight) {
            rgeSaw.generate05(rgd);
        } else if (randomValue < lowDiffWeight * 2) {
            rgeSaw.generate07(rgd);
        } else if (randomValue < lowDiffWeight * 3) {
            rgeSaw.generate08(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight) {
            rgeSaw.generate01(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 2) {
            rgeSaw.generate03(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 3) {
            rgeSaw.generate04(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 4) {
            rgeSaw.generate06(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 5) {
            rgeSaw.generate09(rgd);
        } else if (randomValue < lowDiffCumulativeWeight + lowMedDiffWeight * 6) {
            rgeSaw.generate11(rgd);
        } else if (randomValue < lowMedDiffCumulativeWeight + medDiffWeight) {
            rgeSaw.generate02(rgd);
        } else if (randomValue < medDiffCumulativeWeight + medHighDiffWeight) {
            rgeSaw.generate12(rgd);
        } else {
            rgeSaw.generate10(rgd);
        }
    }
    
    private static float getStaticLowDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 800.0f) {
            return 1.0f - absoluteHeight * 0.00125f;
        } else {
            return 0.0f;
        }
    }
    
    private static float getStaticLowMedDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 800.0f) {
            return absoluteHeight * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getStaticMedDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1300.0f) {
            return (absoluteHeight - 500.0f) * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getStaticMedHighDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1800.0f) {
            return (absoluteHeight - 1000.0f) * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getStaticHighDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 1800.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 2600.0f) {
            return (absoluteHeight - 1800.0f) * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getStaticJumpBoostWeight(float absoluteHeight) {
        if (absoluteHeight <= 1600.0f) {
            return absoluteHeight * 0.00125f;
        } else {
            return 2.0f;
        }
    }
    
    private static float getSawDoubleWeight(float absoluteHeight) {
        if (absoluteHeight <= 1500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 2000.0f) {
            return (absoluteHeight - 1500.0f) * 0.002f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getSawDivideWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1500.0f) {
            return (absoluteHeight - 500.0f) * 0.002f;
        } else {
            return 2.0f;
        }
    }
    
    private static float getSawFixedWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 4.0f;
        } else if (absoluteHeight <= 1500.0f) {
            return 4.0f + (absoluteHeight - 1000.0f) * 0.004f;
        } else {
            return 6.0f;
        }
    }
    
    private static float getSawFixedLowMedDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 800.0f) {
            return absoluteHeight * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getSawFixedMedDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1300.0f) {
            return (absoluteHeight - 500.0f) * 0.00125f;
        } else {
            return 1.0f;
        }
    }
    
    private static float getSawFixedMedHighDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 1800.0f) {
            return (absoluteHeight - 1000.0f) * 0.0025f;
        } else {
            return 2.0f;
        }
    }
    
    private static float getSawFixedHighDifficultyWeight(float absoluteHeight) {
        if (absoluteHeight <= 1500.0f) {
            return 0.0f;
        } else if (absoluteHeight <= 2300.0f) {
            return (absoluteHeight - 1500.0f) * 0.0025f;
        } else {
            return 2.0f;
        }
    }
}
