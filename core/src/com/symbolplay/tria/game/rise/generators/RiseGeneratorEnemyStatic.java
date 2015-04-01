package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.StaticEnemy;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.features.JumpBoostPlatformFeature;

public final class RiseGeneratorEnemyStatic {
    
    private static final float MAX_PLATFORM_X = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH;
    private static final float MIDDLE_PLATFORM_X = MAX_PLATFORM_X / 2.0f;
    
    //private final RiseGeneratorStandard riseGeneratorStandard;
    private final CoinGenerator coinGenerator;
    private final AssetManager assetManager;
    
    public RiseGeneratorEnemyStatic(RiseGeneratorStandard riseGeneratorStandard, CoinGenerator coinGenerator,  AssetManager assetManager) {
        //this.riseGeneratorStandard = riseGeneratorStandard;
        this.coinGenerator = coinGenerator;
        this.assetManager = assetManager;
    }
    
    public void generate01(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 3.4f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 4.4f, 4.0f);
        addPlatformWithCoinsDefault(rgd, 1.0f, 5.5f);
        addPlatformWithCoinsDefault(rgd, 6.8f, 6.5f);
        addPlatformWithCoinsDefault(rgd, 8.8f, 7.5f);
        addPlatformWithCoinsDefault(rgd, 5.8f, 8.5f);
        
        RiseGeneratorUtils.addStaticEnemy(rgd, 2.7f, 7.2f, assetManager);
        
        rgd.changeGeneratedHeight(12.0f);
    }
    
    public void generate02(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 3.2f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 6.0f, 4.0f);
        RiseGeneratorUtils.addPlatform(rgd, 0.8f, 5.0f, assetManager);
        addPlatformWithCoinsDefault(rgd, 5.8f, 7.5f);
        
        RiseGeneratorUtils.addStaticEnemy(rgd, 1.0f, 6.0f, assetManager);
        
        rgd.changeGeneratedHeight(10.0f);
    }
    
    public void generate03(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 4.2f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 3.2f, 3.0f);
        addPlatformWithCoinsDefault(rgd, 1.0f, 6.0f);
        addPlatformWithCoinsDefault(rgd, 8.0f, 7.0f);
        addPlatformWithCoinsDefault(rgd, 5.8f, 10.5f);
        
        RiseGeneratorUtils.addStaticEnemy(rgd, 6.0f, 5.8f, assetManager);
        
        rgd.changeGeneratedHeight(14.0f);
    }
    
    public void generate04(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 3.3f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 6.2f, 2.5f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 9.0f, 5.0f, assetManager);
        addPlatformWithCoinsDefault(rgd, 1.2f, 7.5f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 4.0f, 10.5f, assetManager);
        addPlatformWithCoinsDefault(rgd, 8.2f, 12.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 0.3f, 14.1f, assetManager);
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 16.5f);
        
        rgd.changeGeneratedHeight(19.0f);
    }
    
    public void generate05(RiseGeneratorData rgd) {
        
        boolean isLeftFirst = MathUtils.randomBoolean();
        
        addPlatformWithCoinsDefault(rgd, 2.2f, 0.0f);
        addPlatformWithCoinsDefault(rgd, MAX_PLATFORM_X - 2.3f, 0.8f);
        RiseGeneratorUtils.addPlatform(rgd, 0.7f, 5.0f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, MAX_PLATFORM_X - 1.1f, 5.2f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, 2.5f, 9.8f, assetManager);
        RiseGeneratorUtils.addPlatform(rgd, MAX_PLATFORM_X - 1.7f, 9.7f, assetManager);
        addPlatformWithCoinsDefault(rgd, 2.2f, 14.4f);
        addPlatformWithCoinsDefault(rgd, MAX_PLATFORM_X - 1.3f, 15.3f);
        
        RiseGeneratorUtils.addStaticEnemy(rgd, isLeftFirst ? 1.2f : MAX_PLATFORM_X - 1.0f, 6.5f, assetManager);
        RiseGeneratorUtils.addStaticEnemy(rgd, isLeftFirst ? MAX_PLATFORM_X - 2.0f : 2.2f, 11.5f, assetManager);
        
        rgd.changeGeneratedHeight(20.0f);
    }
    
    public void generate06(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 8.2f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 2.2f, 5.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 1.2f, 6.7f, assetManager);
        addPlatformWithCoinsDefault(rgd, 7.3f, 9.4f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 6.1f, 11.1f, assetManager);
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 15.5f);
        
        rgd.changeGeneratedHeight(19.0f);
    }
    
    public void generate07(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 1.1f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 2.2f, 5.2f);
        addPlatformWithCoinsDefault(rgd, 4.2f, 7.7f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 2.6f, 11.4f, assetManager);
        RiseGeneratorUtils.addStaticEnemy(rgd, 7.1f, 12.3f, assetManager);
        addPlatformWithCoinsDefault(rgd, 4.5f, 13.0f);
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 17.5f);
        
        rgd.changeGeneratedHeight(21.0f);
    }
    
    public void generate08(RiseGeneratorData rgd) {
        
        addPlatformWithCoinsDefault(rgd, 5.5f, 0.0f);
        addPlatformWithCoinsDefault(rgd, 2.0f, 4.9f);
        addPlatformWithCoinsDefault(rgd, 7.5f, 10.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 5.7f, 10.3f, assetManager);
        addPlatformWithCoinsDefault(rgd, 1.1f, 11.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 1.3f, 14.1f, assetManager);
        addPlatformWithCoinsDefault(rgd, 4.5f, 15.6f);
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 17.5f);
        
        rgd.changeGeneratedHeight(21.0f);
    }
    
    public void generateCrumble01(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 2.5f, 0.0f);
        addCrumblePlatform(rgd, 7.5f, 4.7f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 9.0f, 5.8f, assetManager);
        addCrumblePlatform(rgd, 1.1f, 7.7f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 0.5f, 8.7f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 12.5f);
        
        rgd.changeGeneratedHeight(15.0f);
    }
    
    public void generateCrumble02(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 2.3f, 0.0f);
        addCrumblePlatform(rgd, 7.8f, 4.9f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 6.8f, 5.8f, assetManager);
        addCrumblePlatform(rgd, 5.3f, 9.6f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 9.0f, 10.3f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 13.5f);
        
        rgd.changeGeneratedHeight(17.0f);
    }
    
    public void generateCrumble03(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 1.5f, 0.0f);
        addCrumblePlatform(rgd, 1.7f, 5.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 4.3f, 5.8f, assetManager);
        addCrumblePlatform(rgd, 3.9f, 9.9f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 2.0f, 9.6f, assetManager);
        addCrumblePlatform(rgd, 7.7f, 14.7f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 9.7f, 16.0f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 18.5f);
        
        rgd.changeGeneratedHeight(22.0f);
    }
    
    public void generateCrumble04(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 5.8f, 0.0f);
        addCrumblePlatform(rgd, 8.8f, 5.0f);
        addCrumblePlatform(rgd, 6.5f, 9.8f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 8.5f, 10.6f, assetManager);
        RiseGeneratorUtils.addStaticEnemy(rgd, 1.5f, 12.6f, assetManager);
        addCrumblePlatform(rgd, 2.7f, 14.7f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 4.5f, 15.6f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 19.0f);
        
        rgd.changeGeneratedHeight(23.0f);
    }
    
    public void generateCrumble05(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 2.5f, 0.0f);
        addCrumblePlatform(rgd, 8.7f, 4.9f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 7.6f, 6.1f, assetManager);
        addCrumblePlatform(rgd, 6.9f, 9.8f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 3.8f, 10.4f, assetManager);
        RiseGeneratorUtils.addStaticEnemy(rgd, 1.8f, 11.3f, assetManager);
        addCrumblePlatform(rgd, 0.9f, 15.1f);
        
        rgd.changeGeneratedHeight(19.0f);
    }
    
    public void generateCrumble06(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 5.5f, 0.0f);
        addCrumblePlatform(rgd, 3.2f, 4.5f);
        addCrumblePlatform(rgd, 6.9f, 9.5f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 3.3f, 9.4f, assetManager);
        RiseGeneratorUtils.addStaticEnemy(rgd, 8.6f, 10.6f, assetManager);
        addCrumblePlatform(rgd, 9.0f, 14.2f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 7.5f, 15.4f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 19.0f);
        
        rgd.changeGeneratedHeight(23.0f);
    }
    
    public void generateCrumble07(RiseGeneratorData rgd) {
        
        addCrumblePlatform(rgd, 3.5f, 0.0f);
        addCrumblePlatform(rgd, 3.1f, 5.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 3.3f, 9.9f, assetManager);
        addCrumblePlatform(rgd, 8.1f, 10.0f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 5.6f, 13.0f, assetManager);
        addCrumblePlatform(rgd, 5.2f, 14.8f);
        addCrumblePlatform(rgd, 3.2f, 19.3f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 5.0f, 20.5f, assetManager);
        
        addCrumblePlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 25.0f);
        
        rgd.changeGeneratedHeight(28.0f);
    }
    
    public void generateRevealOnJump01(RiseGeneratorData rgd) {
        
        addRevealOnJumpPlatform(rgd, 6.5f, 0.0f, true);
        addRevealOnJumpPlatform(rgd, 1.7f, 5.2f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 0.8f, 6.2f, assetManager);
        addRevealOnJumpPlatform(rgd, 6.9f, 10.2f);
        addRevealOnJumpPlatform(rgd, 4.9f, 13.2f);
        RiseGeneratorUtils.addStaticEnemy(rgd, 6.8f, 14.3f, assetManager);
        
        addRevealOnJumpPlatform(rgd, RiseGeneratorUtils.getRandomPlatformX(), 18.5f);
        
        rgd.changeGeneratedHeight(22.0f);
    }
    
    public void generateJumpBoostColumn(RiseGeneratorData rgd) {
        
        float columnX = RiseGeneratorUtils.getRandomPlatformX(0.5f, 0.5f);
        
        addJumpBoostPlatform(rgd, columnX, 0.0f, 0.3f, JumpBoostPlatformFeature.SIZE_MEDIUM);
        
        addPlatformWithCoinsJumpBoost(rgd, columnX - 0.3f, 3.5f);
        addPlatformWithCoinsJumpBoost(rgd, columnX + 0.11f, 8.2f);
        addPlatformWithCoinsJumpBoost(rgd, columnX + 0.25f, 12.5f);
        addPlatformWithCoinsJumpBoost(rgd, columnX - 0.23f, 16.9f);
        addPlatformWithCoinsJumpBoost(rgd, columnX + 0.17f, 21.8f);
        addPlatformWithCoinsJumpBoost(rgd, columnX - 0.1f, 26.0f);
        
        float sideX = columnX <= MIDDLE_PLATFORM_X ? columnX + 3.4f : columnX - 3.4f;
        addPlatformWithCoinsDefault(rgd, sideX - 0.1f, 27.2f);
        addPlatformWithCoinsDefault(rgd, sideX + 0.22f, 32.2f);
        
        float enemyX = columnX + (Platform.WIDTH - StaticEnemy.WIDTH) / 2.0f;
        RiseGeneratorUtils.addStaticEnemy(rgd, enemyX, 30.7f, assetManager);
        
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 36.2f);
        
        rgd.changeGeneratedHeight(40.0f);
    }
    
    public void generateJumpBoostVoid(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        boolean isCrumble = isCrumbleStaticJumpBoostVoid(absoluteHeight);
        int minNumRepeats = getMinNumRepeatsStaticJumpBoostVoid(absoluteHeight, isCrumble);
        int maxNumRepeats = getMaxNumRepeatsStaticJumpBoostVoid(absoluteHeight, isCrumble);
        int numRepeats = MathUtils.random(minNumRepeats, maxNumRepeats);
        
        if (isCrumble) {
            addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
            rgd.changeGeneratedHeight(4.0f);
        }
        
        for (int i = 0; i < numRepeats; i++) {
            generateStaticJumpBoostVoidPart(rgd, isCrumble);
        }
        
        addPlatformWithCoinsDefault(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
        rgd.changeGeneratedHeight(4.0f);
    }
    
    private static int getMinNumRepeatsStaticJumpBoostVoid(float absoluteHeight, boolean isCrumble) {
        int repeats;
        
        if (absoluteHeight <= 3200.0f) {
            repeats = 1 + (int) (absoluteHeight * 0.000625f);
        } else {
            repeats = 3;
        }
        
        if (isCrumble && repeats > 2) {
            repeats = 2;
        }
        
        return repeats;
    }
    
    private static int getMaxNumRepeatsStaticJumpBoostVoid(float absoluteHeight, boolean isCrumble) {
        int repeats;
        
        if (absoluteHeight <= 2500.0f) {
            repeats = 1 + (int) (absoluteHeight * 0.0016f);
        } else {
            repeats = 5;
        }
        
        if (isCrumble && repeats > 3) {
            repeats = 3;
        }
        
        return repeats;
    }
    
    private static boolean isCrumbleStaticJumpBoostVoid(float absoluteHeight) {
        float crumbleChance;
        if (absoluteHeight <= 1000.0f) {
            crumbleChance = 0.0f;
        } else if (absoluteHeight <= 3000.0f) {
            crumbleChance = (absoluteHeight - 1000.0f) * 0.00025f;
        } else {
            crumbleChance = 0.5f; 
        }
        
        return MathUtils.random() < crumbleChance;
    }
    
    private void generateStaticJumpBoostVoidPart(RiseGeneratorData rgd, boolean isCrumble) {
        
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        
//        int randomValue = MathUtils.random(1);
        
        int jumpBoostSize;
        float height;
        float minEnemyY;
        
//        if (randomValue == 0) {
            jumpBoostSize = JumpBoostPlatformFeature.SIZE_SMALL;
            height = 16.0f;
            minEnemyY = 9.5f;
//        } else {
//            jumpBoostSize = JumpBoostPlatformFeature.SIZE_MEDIUM;
//            topPlatformY = 24.0f;
//            minEnemyY = 11.0f;
//        } else {
//            jumpBoostSize = JumpBoostPlatformFeature.SIZE_LARGE;
//            topPlatformY = 36.0f;
//            minEnemyY = 11.0f;
//        }
        
        float platformX = RiseGeneratorUtils.getRandomPlatformX();
        if (isCrumble) {
            addJumpBoostCrumblePlatform(rgd, platformX, 0.0f, MathUtils.random(), jumpBoostSize);
        } else {
            addJumpBoostPlatform(rgd, platformX, 0.0f, MathUtils.random(), jumpBoostSize);
        }
        
        float maxEnemyOffsetX = getStaticJumpBoostVoidMaxEnemyOffsetX(absoluteHeight);
        float centerEnemyX = platformX + (Platform.WIDTH - StaticEnemy.WIDTH) * 0.5f;
        float minEnemyX = Math.max(0.5f, centerEnemyX - maxEnemyOffsetX);
        float maxEnemyX = Math.min(GameContainer.GAME_AREA_WIDTH - StaticEnemy.WIDTH - 0.5f, centerEnemyX + maxEnemyOffsetX);
        
        float maxEnemyY = Math.min(13.0f, height - 3.0f);
        
        float enemyX;
        float enemyY;
        
        enemyX = MathUtils.random(minEnemyX, maxEnemyX);
        enemyY = MathUtils.random(minEnemyY, maxEnemyY);
        RiseGeneratorUtils.addStaticEnemy(rgd, enemyX, enemyY, assetManager);
        
        if (absoluteHeight >= 3000.0f && !isCrumble) {
            enemyX = MathUtils.random(minEnemyX, maxEnemyX);
            enemyY = MathUtils.random(minEnemyY, maxEnemyY);
            RiseGeneratorUtils.addStaticEnemy(rgd, enemyX, enemyY, assetManager);
        }
        
        rgd.changeGeneratedHeight(height);
    }
    
    private static float getStaticJumpBoostVoidMaxEnemyOffsetX(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 5.0f - absoluteHeight * 0.0025f;
        } else if (absoluteHeight <= 3000.0f) {
            return 2.5f - (absoluteHeight - 1000.0f) * 0.00065f;
        } else {
            return 1.2f;
        }
    }
    
    private void addPlatformWithCoinsDefault(RiseGeneratorData rgd, float x, float y) {
        addPlatformWithCoins(rgd, x, y, 0.3f);
    }
    
    private void addPlatformWithCoinsJumpBoost(RiseGeneratorData rgd, float x, float y) {
        addPlatformWithCoins(rgd, x, y, 1.0f);
    }
    
    private void addPlatformWithCoins(RiseGeneratorData rgd, float x, float y, float coinChance) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
        coinGenerator.generate(coinChance, rgd, platform);
    }
    
    private void addCrumblePlatform(RiseGeneratorData rgd, float x, float y) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setCrumbleFeature();
    }
    
    private void addRevealOnJumpPlatform(RiseGeneratorData rgd, float x, float y, boolean isInitiallyVisible) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setCrumbleFeature();
        platform.setRevealOnJumpFeature(isInitiallyVisible, new int[] { rgd.nextPlatformId });
    }
    
    private void addRevealOnJumpPlatform(RiseGeneratorData rgd, float x, float y) {
        addRevealOnJumpPlatform(rgd, x, y, false);
    }
    
    private void addJumpBoostPlatform(RiseGeneratorData rgd, float x, float y, float positionFraction, int jumpBoostSize) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setJumpBoostFeature(positionFraction, jumpBoostSize);
    }
    
    private void addJumpBoostCrumblePlatform(RiseGeneratorData rgd, float x, float y, float positionFraction, int jumpBoostSize) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setJumpBoostFeature(positionFraction, jumpBoostSize);
        platform.setCrumbleFeature();
    }
}
