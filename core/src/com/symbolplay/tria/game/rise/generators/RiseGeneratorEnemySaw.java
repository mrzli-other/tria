package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.SawEnemy;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorEnemySaw {
    
    private static final float MAX_PLATFORM_X = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH;
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final CoinGenerator coinGenerator;
    private final AssetManager assetManager;
    
    public RiseGeneratorEnemySaw(RiseGeneratorStandard riseGeneratorStandard, CoinGenerator coinGenerator, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.coinGenerator = coinGenerator;
        this.assetManager = assetManager;
    }
    
    public void generateSideJump(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
        
        boolean isLeftSaw = MathUtils.randomBoolean();
        addPlatformWithCoins(rgd, isLeftSaw ? MAX_PLATFORM_X - 1.1f : 1.1f, 4.5f);
        
        addPlatformWithCoins(rgd, isLeftSaw ? 0.2f : MAX_PLATFORM_X - 0.2f, 8.5f);
        float sawX = isLeftSaw ? 0.5f + Platform.WIDTH : MAX_PLATFORM_X - 0.5f - SawEnemy.WIDTH;
        RiseGeneratorUtils.addSawEnemy(rgd, sawX, 10.0f, assetManager);
        
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 14.0f);
        
        rgd.changeGeneratedHeight(17.0f);
    }
    
    public void generateSingle(RiseGeneratorData rgd, boolean isLow) {
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 4.9f, 2.5f, 2.5f, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 4.9f, 2.5f, 2.5f, false);
        }
        
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
        
        float maxPositionX = GameContainer.GAME_AREA_WIDTH - Platform.WIDTH * 2.0f - SawEnemy.WIDTH;
        float referentX = MathUtils.random(maxPositionX);
        
        addPlatformWithCoins(rgd, referentX, 5.0f);
        RiseGeneratorUtils.addSawEnemy(rgd, referentX + Platform.WIDTH, 5.0f, assetManager);
        addPlatformWithCoins(rgd, referentX + Platform.WIDTH + SawEnemy.WIDTH, 5.0f);
        
        rgd.changeGeneratedHeight(10.0f);
    }
    
    public void generateDouble(RiseGeneratorData rgd, boolean isLow) {
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 4.9f, 2.5f, 2.5f, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 4.9f, 2.5f, 2.5f, false);
        }
        
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 3.0f);
        
        float objectDistance = 0.1125f;
        float objectsY = 8.0f;
        
        addPlatformWithCoins(rgd, 0.0f, objectsY);
        RiseGeneratorUtils.addSawEnemy(rgd, Platform.WIDTH + objectDistance, objectsY, assetManager);
        addPlatformWithCoins(rgd, Platform.WIDTH + SawEnemy.WIDTH + objectDistance * 2.0f, objectsY);
        RiseGeneratorUtils.addSawEnemy(rgd, Platform.WIDTH * 2.0f + SawEnemy.WIDTH + objectDistance * 3.0f, objectsY, assetManager);
        addPlatformWithCoins(rgd, (Platform.WIDTH + SawEnemy.WIDTH) * 2.0f + objectDistance * 4.0f, objectsY);
        
        rgd.changeGeneratedHeight(13.0f);
    }
    
    public void generateWaterfall(RiseGeneratorData rgd, boolean isLow) {
        
        if (isLow) {
            riseGeneratorStandard.generateLow(rgd, 9.9f, 2.5f, 2.5f, false);
        } else {
            riseGeneratorStandard.generateHigh(rgd, 9.9f, 2.5f, 2.5f, false);
        }
        
        addPlatformWithCoins(rgd, RiseGeneratorUtils.getRandomPlatformX(), 0.0f);
        
        boolean isLeftLow = MathUtils.randomBoolean();
        
        float objectDistance = 0.1125f;
        float y1 = 5.0f;
        float y2 = y1 + SawEnemy.HEIGHT - Platform.HEIGHT;
        float y3 = y2 + SawEnemy.HEIGHT - Platform.HEIGHT;
        
        float x1 = 0.0f;
        float x2 = x1 + Platform.WIDTH + objectDistance;
        float x3 = x2 + SawEnemy.WIDTH + objectDistance;
        float x4 = x3 + Platform.WIDTH + objectDistance;
        float x5 = x4 + SawEnemy.WIDTH + objectDistance;
        
        if (isLeftLow) {
            addPlatformWithCoins(rgd, x1, y1);
            RiseGeneratorUtils.addSawEnemy(rgd, x2, y1, assetManager);
            addPlatformWithCoins(rgd, x3, y2);
            RiseGeneratorUtils.addSawEnemy(rgd, x4, y2, assetManager);
            addPlatformWithCoins(rgd, x5, y3);
        } else {
            addPlatformWithCoins(rgd, x5, y1);
            RiseGeneratorUtils.addSawEnemy(rgd, x4, y1, assetManager);
            addPlatformWithCoins(rgd, x3, y2);
            RiseGeneratorUtils.addSawEnemy(rgd, x2, y2, assetManager);
            addPlatformWithCoins(rgd, x1, y3);
        }
        
        rgd.changeGeneratedHeight(13.0f);
    }
    
    public void generateMiddleDivide(RiseGeneratorData rgd) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        ;
        float height = getSawMiddleDivideHeight(absoluteHeight);
        int type = getSawMiddleDividePlatformType(absoluteHeight);
        float minStepY = getSawMiddleDivideMinStepY(absoluteHeight, type);
        float maxStepY = getSawMiddleDivideMaxStepY(absoluteHeight, type);
        
        generateSawMiddleDivide(rgd, height, type, minStepY, maxStepY);
    }
    
    private float getSawMiddleDivideHeight(float absoluteHeight) {
        float minHeight;
        float maxHeight;
        if (absoluteHeight <= 1000.0f) {
            minHeight = 15.0f;
            maxHeight = 20.0f;
        } else if (absoluteHeight <= 3000.0f) {
            minHeight = 15.0f + (absoluteHeight - 1000.0f) * 0.015f;
            maxHeight = 20.0f + (absoluteHeight - 1000.0f) * 0.02f;
        } else {
            minHeight = 45.0f;
            maxHeight = 60.0f;
        }
        
        return MathUtils.random(minHeight, maxHeight);
    }
    
    private int getSawMiddleDividePlatformType(float absoluteHeight) {
        float normalWeight;
        float crumbleWeight;
        float revealOnJumpWeight;
        if (absoluteHeight <= 1000.0f) {
            normalWeight = 1.0f - absoluteHeight * 0.001f;
            crumbleWeight = absoluteHeight * 0.001f;
            revealOnJumpWeight = 0.0f;
        } else if (absoluteHeight <= 2000.0f) {
            normalWeight = 0.0f;
            crumbleWeight = 1.0f - (absoluteHeight - 1000.0f) * 0.001f;
            revealOnJumpWeight = (absoluteHeight - 1000.0f) * 0.001f;
        } else {
            normalWeight = 0.0f;
            crumbleWeight = 0.0f;
            revealOnJumpWeight = 1.0f;
        }
        
        float totalWeight = normalWeight + crumbleWeight + revealOnJumpWeight;
        float randomValue = MathUtils.random(totalWeight);
        if (randomValue < normalWeight) {
            return 0;
        } else if (randomValue < normalWeight + crumbleWeight) {
            return 1;
        } else {
            return 2;
        }
    }
    
    private float getSawMiddleDivideMinStepY(float absoluteHeight, int type) {
        if (type <= 1) {
            return 5.5f;
        } else {
            if (absoluteHeight <= 2000.0f) {
                return 2.5f;
            } else if (absoluteHeight <= 3000.0f) {
                return 2.5f + (absoluteHeight - 2000.0f) * 0.0005f;
            } else {
                return 3.0f;
            }
        }
    }
    
    private float getSawMiddleDivideMaxStepY(float absoluteHeight, int type) {
        if (type <= 1) {
            return 6.5f;
        } else {
            if (absoluteHeight <= 2000.0f) {
                return 3.0f;
            } else if (absoluteHeight <= 3000.0f) {
                return 3.0f + (absoluteHeight - 2000.0f) * 0.0005f;
            } else {
                return 3.5f;
            }
        }
    }
    
    private void generateSawMiddleDivide(RiseGeneratorData rgd, float height, int type, float minStepY, float maxStepY) {
        
        float sawRangeX = 1.4f;
        float sawAreaWidth = sawRangeX + SawEnemy.WIDTH;
        float platformAreaWidth = GameContainer.GAME_AREA_WIDTH - sawAreaWidth;
        float platformSingleSideWidth = platformAreaWidth * 0.5f;
        float platformSingleSideRangeX = platformSingleSideWidth - Platform.WIDTH;
        float platformRightSideX = platformSingleSideWidth + sawAreaWidth;
        
        float minSawX = platformSingleSideWidth;
        float maxSawX = minSawX + sawRangeX;
        
        float sawY = 5.0f;
        float maxSawY = height - 3.0f;
        
        while (sawY < maxSawY) {
            float sawX = MathUtils.random(minSawX, maxSawX);
            
            RiseGeneratorUtils.addSawEnemy(rgd, sawX, sawY, assetManager);
            
            sawY += SawEnemy.HEIGHT + MathUtils.random(2.0f);
        }
        
        float platformY = 0.0f;
        boolean isFirstPlatform = true;
        int consecutiveLeftCount = 0;
        int consecutiveRightCount = 0;
        
        while (platformY < height) {
            
            int consecutive = consecutiveLeftCount + consecutiveRightCount; // can add because one of them will always be 0
            float changeSideFraction;
            if (consecutive <= 1) {
                changeSideFraction = 0.5f;
            } else if (consecutive == 2) {
                changeSideFraction = 0.75f;
            } else {
                changeSideFraction = 0.9f;
            }
            
            boolean isChangeSide = MathUtils.randomBoolean(changeSideFraction);
            boolean isCurrentLeft = consecutiveLeftCount > 0;
            boolean isNextLeft = isCurrentLeft ^ isChangeSide;
            
            float platformX = MathUtils.random(platformSingleSideRangeX);
            platformX += isNextLeft ? 0.0f : platformRightSideX;
            
            if (type == 0) {
                addPlatformWithCoins(rgd, platformX, platformY);
            } else if (type == 1) {
                addCrumblePlatform(rgd, platformX, platformY);
            } else {
                addRevealOnJumpPlatform(rgd, platformX, platformY, isFirstPlatform);
            }
            
            isFirstPlatform = false;
            
            float stepY = MathUtils.random(minStepY, maxStepY);
            platformY += stepY;
        }
        
        rgd.changeGeneratedHeight(platformY);
    }
    
    public void generate01(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 5.5f, 0.0f);
        addPlatformWithCoins(rgd, 0.35f, 2.2f);
        addPlatformWithCoins(rgd, 8.35f, 3.42f);
        addPlatformWithCoins(rgd, 0.8f, 7.0f);
        addPlatformWithCoins(rgd, 5.32f, 7.02f);
        addPlatformWithCoins(rgd, 8.25f, 11.62f);
        addPlatformWithCoins(rgd, 2.43f, 12.45f);
        addPlatformWithCoins(rgd, 7.58f, 15.45f);
        addPlatformWithCoins(rgd, 1.23f, 16.66f);
        addPlatformWithCoins(rgd, 6.73f, 20.44f);
        addPlatformWithCoins(rgd, 8.05f, 25.6f);
        addPlatformWithCoins(rgd, 2.45f, 26.26f);
        addPlatformWithCoins(rgd, 8.52f, 29.01f);
        addPlatformWithCoins(rgd, 0.0f, 34.53f);
        addPlatformWithCoins(rgd, 4.65f, 34.55f);
        addPlatformWithCoins(rgd, 6.7f, 40.18f);
        addPlatformWithCoins(rgd, 2.31f, 40.2f);
        addPlatformWithCoins(rgd, 4.65f, 44.92f);
        addPlatformWithCoins(rgd, 7.38f, 50.05f);
        addPlatformWithCoins(rgd, 1.78f, 50.08f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 2.86f, 7.07f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 3.43f, 16.72f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.18f, 20.39f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 20.51f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 26.23f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.81f, 34.57f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.14f, 34.6f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 40.3f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.15f, 44.95f, assetManager);
        
        rgd.changeGeneratedHeight(54.0f);
    }
    
    public void generate02(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 2.72f, 0.0f);
        addPlatformWithCoins(rgd, 7.02f, 0.0f);
        addPlatformWithCoins(rgd, 1.2f, 3.88f);
        addPlatformWithCoins(rgd, 7.72f, 4.32f);
        addPlatformWithCoins(rgd, 4.28f, 7.42f);
        addPlatformWithCoins(rgd, 0.0f, 11.05f);
        addPlatformWithCoins(rgd, 9.25f, 11.05f);
        addPlatformWithCoins(rgd, 4.63f, 11.1f);
        addPlatformWithCoins(rgd, 4.65f, 16.18f);
        addPlatformWithCoins(rgd, 0.0f, 17.1f);
        addPlatformWithCoins(rgd, 6.72f, 21.22f);
        addPlatformWithCoins(rgd, 2.12f, 21.26f);
        addPlatformWithCoins(rgd, 7.7f, 26.02f);
        addPlatformWithCoins(rgd, 2.43f, 26.74f);
        addPlatformWithCoins(rgd, 6.15f, 30.87f);
        addPlatformWithCoins(rgd, 0.97f, 33.98f);
        addPlatformWithCoins(rgd, 6.74f, 36.58f);
        addPlatformWithCoins(rgd, 2.08f, 40.82f);
        addPlatformWithCoins(rgd, 7.3f, 41.3f);
        addPlatformWithCoins(rgd, 5.38f, 44.6f);
        addPlatformWithCoins(rgd, 1.02f, 46.95f);
        addPlatformWithCoins(rgd, 4.7f, 50.3f);
        addPlatformWithCoins(rgd, 9.25f, 50.3f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 2.17f, 11.13f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.78f, 11.2f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 21.19f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.2f, 21.24f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 26.79f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.26f, 36.61f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 36.68f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 7.48f, 44.74f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.8f, 50.37f, assetManager);
        
        rgd.changeGeneratedHeight(55.0f);
    }
    
    public void generate03(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 3.1f, 0.0f);
        addPlatformWithCoins(rgd, 8.38f, 0.5f);
        addPlatformWithCoins(rgd, 0.25f, 2.95f);
        addPlatformWithCoins(rgd, 7.22f, 3.47f);
        addPlatformWithCoins(rgd, 1.53f, 5.57f);
        addPlatformWithCoins(rgd, 4.18f, 5.57f);
        addPlatformWithCoins(rgd, 9.25f, 8.88f);
        addPlatformWithCoins(rgd, 8.48f, 13.7f);
        addPlatformWithCoins(rgd, 4.05f, 14.88f);
        addPlatformWithCoins(rgd, 1.63f, 17.57f);
        addPlatformWithCoins(rgd, 8.56f, 19.75f);
        addPlatformWithCoins(rgd, 3.82f, 21.79f);
        addPlatformWithCoins(rgd, 6.28f, 23.06f);
        addPlatformWithCoins(rgd, 7.95f, 26.51f);
        addPlatformWithCoins(rgd, 3.78f, 26.8f);
        addPlatformWithCoins(rgd, 3.38f, 31.38f);
        addPlatformWithCoins(rgd, 7.9f, 31.48f);
        addPlatformWithCoins(rgd, 1.0f, 35.46f);
        addPlatformWithCoins(rgd, 3.3f, 37.88f);
        addPlatformWithCoins(rgd, 7.93f, 39.85f);
        addPlatformWithCoins(rgd, 1.7f, 42.28f);
        addPlatformWithCoins(rgd, 2.38f, 46.13f);
        addPlatformWithCoins(rgd, 6.81f, 46.13f);
        addPlatformWithCoins(rgd, 5.85f, 52.93f);
        addPlatformWithCoins(rgd, 3.7f, 52.96f);
        addPlatformWithCoins(rgd, 4.8f, 57.58f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 6.74f, 8.95f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 1.58f, 14.95f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 5.98f, 19.82f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 1.33f, 21.87f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 5.51f, 31.4f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.85f, 31.43f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 5.4f, 37.93f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.32f, 46.16f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 46.17f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 46.2f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 7.97f, 52.95f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 1.33f, 53.0f, assetManager);
        
        rgd.changeGeneratedHeight(61.0f);
    }
    
    public void generate04(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.65f, 0.0f);
        addPlatformWithCoins(rgd, 7.35f, 0.0f);
        addPlatformWithCoins(rgd, 4.65f, 2.83f);
        addPlatformWithCoins(rgd, 2.53f, 5.2f);
        addPlatformWithCoins(rgd, 6.73f, 5.22f);
        addPlatformWithCoins(rgd, 2.22f, 10.95f);
        addPlatformWithCoins(rgd, 6.7f, 11.11f);
        addPlatformWithCoins(rgd, 0.18f, 16.5f);
        addPlatformWithCoins(rgd, 4.68f, 16.52f);
        addPlatformWithCoins(rgd, 8.2f, 19.62f);
        addPlatformWithCoins(rgd, 5.42f, 21.13f);
        addPlatformWithCoins(rgd, 2.5f, 21.7f);
        addPlatformWithCoins(rgd, 7.52f, 23.85f);
        addPlatformWithCoins(rgd, 0.0f, 26.32f);
        addPlatformWithCoins(rgd, 2.25f, 29.44f);
        addPlatformWithCoins(rgd, 8.07f, 31.89f);
        addPlatformWithCoins(rgd, 2.15f, 33.74f);
        addPlatformWithCoins(rgd, 7.68f, 36.54f);
        addPlatformWithCoins(rgd, 3.13f, 36.59f);
        addPlatformWithCoins(rgd, 0.0f, 41.12f);
        addPlatformWithCoins(rgd, 6.62f, 41.69f);
        addPlatformWithCoins(rgd, 0.0f, 45.11f);
        addPlatformWithCoins(rgd, 4.54f, 47.09f);
        addPlatformWithCoins(rgd, 9.17f, 49.2f);
        addPlatformWithCoins(rgd, 2.48f, 52.05f);
        addPlatformWithCoins(rgd, 7.33f, 52.55f);
        addPlatformWithCoins(rgd, 6.83f, 57.53f);
        addPlatformWithCoins(rgd, 2.35f, 57.62f);
        addPlatformWithCoins(rgd, 4.55f, 62.3f);
        addPlatformWithCoins(rgd, 9.25f, 62.35f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 5.2f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 5.33f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.33f, 11.06f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.8f, 11.15f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.25f, 16.55f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.4f, 26.4f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.05f, 26.45f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 5.18f, 36.59f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.1f, 45.17f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.6f, 47.28f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 52.08f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 57.5f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.35f, 57.6f, assetManager);
        
        rgd.changeGeneratedHeight(66.0f);
    }
    
    public void generate05(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 5.88f, 0.0f);
        addPlatformWithCoins(rgd, 1.58f, 0.05f);
        addPlatformWithCoins(rgd, 6.77f, 4.68f);
        addPlatformWithCoins(rgd, 0.0f, 4.75f);
        addPlatformWithCoins(rgd, 0.0f, 8.9f);
        addPlatformWithCoins(rgd, 4.43f, 8.96f);
        addPlatformWithCoins(rgd, 6.92f, 14.4f);
        addPlatformWithCoins(rgd, 2.45f, 18.03f);
        addPlatformWithCoins(rgd, 7.8f, 19.77f);
        addPlatformWithCoins(rgd, 2.48f, 23.76f);
        addPlatformWithCoins(rgd, 6.88f, 23.7f);
        addPlatformWithCoins(rgd, 9.25f, 28.05f);
        addPlatformWithCoins(rgd, 2.33f, 29.81f);
        addPlatformWithCoins(rgd, 6.88f, 29.83f);
        addPlatformWithCoins(rgd, 4.91f, 34.95f);
        addPlatformWithCoins(rgd, 9.25f, 35.1f);
        addPlatformWithCoins(rgd, 2.55f, 37.03f);
        addPlatformWithCoins(rgd, 6.9f, 37.13f);
        addPlatformWithCoins(rgd, 9.13f, 39.63f);
        addPlatformWithCoins(rgd, 7.0f, 41.6f);
        addPlatformWithCoins(rgd, 2.68f, 41.62f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 4.75f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.05f, 9.05f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 14.35f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 17.9f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.55f, 23.79f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 23.83f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.39f, 29.88f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 37.1f, assetManager);
        
        rgd.changeGeneratedHeight(45.0f);
    }
    
    public void generate06(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.33f, 0.0f);
        addPlatformWithCoins(rgd, 6.63f, 0.0f);
        addPlatformWithCoins(rgd, 0.0f, 1.88f);
        addPlatformWithCoins(rgd, 8.68f, 2.12f);
        addPlatformWithCoins(rgd, 3.17f, 3.87f);
        addPlatformWithCoins(rgd, 5.23f, 3.87f);
        addPlatformWithCoins(rgd, 1.61f, 9.23f);
        addPlatformWithCoins(rgd, 6.18f, 9.23f);
        addPlatformWithCoins(rgd, 3.06f, 15.12f);
        addPlatformWithCoins(rgd, 5.55f, 15.12f);
        addPlatformWithCoins(rgd, 6.73f, 21.05f);
        addPlatformWithCoins(rgd, 2.11f, 21.09f);
        addPlatformWithCoins(rgd, 7.32f, 27.05f);
        addPlatformWithCoins(rgd, 0.07f, 27.16f);
        addPlatformWithCoins(rgd, 9.25f, 31.8f);
        addPlatformWithCoins(rgd, 0.13f, 33.06f);
        addPlatformWithCoins(rgd, 4.56f, 33.14f);
        addPlatformWithCoins(rgd, 6.65f, 37.5f);
        addPlatformWithCoins(rgd, 0.0f, 38.67f);
        addPlatformWithCoins(rgd, 2.45f, 41.58f);
        addPlatformWithCoins(rgd, 6.98f, 41.6f);
        addPlatformWithCoins(rgd, 4.8f, 45.91f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 8.31f, 9.24f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 3.7f, 9.25f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 7.78f, 15.12f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 15.15f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 21.12f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.22f, 21.15f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.26f, 27.05f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.9f, 27.08f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.16f, 33.15f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 37.5f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.53f, 41.6f, assetManager);
        
        rgd.changeGeneratedHeight(49.0f);
    }
    
    public void generate07(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.58f, 0.0f);
        addPlatformWithCoins(rgd, 6.22f, 2.3f);
        addPlatformWithCoins(rgd, 1.62f, 2.67f);
        addPlatformWithCoins(rgd, 6.8f, 5.75f);
        addPlatformWithCoins(rgd, 2.33f, 9.57f);
        addPlatformWithCoins(rgd, 4.88f, 14.08f);
        addPlatformWithCoins(rgd, 1.18f, 18.01f);
        addPlatformWithCoins(rgd, 7.48f, 18.55f);
        addPlatformWithCoins(rgd, 4.63f, 23.21f);
        addPlatformWithCoins(rgd, 6.75f, 23.21f);
        addPlatformWithCoins(rgd, 5.05f, 28.29f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 5.9f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 9.6f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.95f, 14.25f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 23.21f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.13f, 23.27f, assetManager);
        
        rgd.changeGeneratedHeight(32.0f);
    }
    
    public void generate08(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 6.6f, 0.0f);
        addPlatformWithCoins(rgd, 2.38f, 4.57f);
        addPlatformWithCoins(rgd, 6.22f, 8.62f);
        addPlatformWithCoins(rgd, 1.85f, 9.47f);
        addPlatformWithCoins(rgd, 8.66f, 12.2f);
        addPlatformWithCoins(rgd, 3.27f, 14.65f);
        addPlatformWithCoins(rgd, 5.55f, 19.8f);
        addPlatformWithCoins(rgd, 2.56f, 24.56f);
        addPlatformWithCoins(rgd, 8.08f, 24.65f);
        addPlatformWithCoins(rgd, 4.63f, 29.68f);
        addPlatformWithCoins(rgd, 6.75f, 29.71f);
        addPlatformWithCoins(rgd, 1.65f, 33.77f);
        addPlatformWithCoins(rgd, 7.48f, 35.37f);
        addPlatformWithCoins(rgd, 2.33f, 37.5f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 8.33f, 8.77f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.8f, 14.65f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 7.58f, 19.92f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 24.64f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 29.7f, assetManager);
        
        rgd.changeGeneratedHeight(40.0f);
    }
    
    public void generate09(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 4.43f, 0.0f);
        addPlatformWithCoins(rgd, 7.96f, 3.47f);
        addPlatformWithCoins(rgd, 2.4f, 5.18f);
        addPlatformWithCoins(rgd, 2.38f, 10.15f);
        addPlatformWithCoins(rgd, 6.83f, 10.22f);
        addPlatformWithCoins(rgd, 4.8f, 15.2f);
        addPlatformWithCoins(rgd, 8.47f, 17.37f);
        addPlatformWithCoins(rgd, 0.08f, 17.49f);
        addPlatformWithCoins(rgd, 2.68f, 23.1f);
        addPlatformWithCoins(rgd, 9.25f, 26.95f);
        addPlatformWithCoins(rgd, 2.7f, 31.12f);
        addPlatformWithCoins(rgd, 9.25f, 33.7f);
        addPlatformWithCoins(rgd, 4.66f, 35.57f);
        addPlatformWithCoins(rgd, 4.98f, 40.6f);
        addPlatformWithCoins(rgd, 2.53f, 45.03f);
        addPlatformWithCoins(rgd, 6.85f, 45.05f);
        addPlatformWithCoins(rgd, 4.93f, 48.7f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 5.22f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 10.1f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 10.2f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.48f, 10.23f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.15f, 17.6f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.25f, 20.23f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.9f, 23.12f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.73f, 33.7f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.17f, 35.77f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.42f, 40.63f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.1f, 45.07f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 45.19f, assetManager);
        
        rgd.changeGeneratedHeight(51.0f);
    }
    
    public void generate10(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 2.58f, 0.0f);
        addPlatformWithCoins(rgd, 7.02f, 2.82f);
        addPlatformWithCoins(rgd, 2.33f, 6.1f);
        addPlatformWithCoins(rgd, 5.1f, 11.22f);
        addPlatformWithCoins(rgd, 3.37f, 15.95f);
        addPlatformWithCoins(rgd, 2.35f, 20.75f);
        addPlatformWithCoins(rgd, 6.75f, 20.93f);
        addPlatformWithCoins(rgd, 4.78f, 24.5f);
        addPlatformWithCoins(rgd, 2.48f, 27.85f);
        addPlatformWithCoins(rgd, 9.25f, 32.7f);
        addPlatformWithCoins(rgd, 6.58f, 36.15f);
        addPlatformWithCoins(rgd, 4.32f, 38.72f);
        addPlatformWithCoins(rgd, 6.8f, 42.1f);
        addPlatformWithCoins(rgd, 2.4f, 43.47f);
        addPlatformWithCoins(rgd, 6.73f, 47.52f);
        addPlatformWithCoins(rgd, 4.38f, 52.7f);
        addPlatformWithCoins(rgd, 0.53f, 56.82f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 5.95f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 7.18f, 9.25f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 11.25f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.58f, 13.0f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 19.78f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 19.8f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.4f, 20.75f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 27.85f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 30.17f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.35f, 30.47f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.15f, 36.22f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 1.88f, 38.85f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 42.18f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 0.0f, 43.52f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.42f, 45.7f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 1.92f, 50.82f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.47f, 52.75f, assetManager);
        
        rgd.changeGeneratedHeight(59.0f);
    }
    
    public void generate11(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 2.35f, 0.0f);
        addPlatformWithCoins(rgd, 7.6f, 0.0f);
        addPlatformWithCoins(rgd, 6.98f, 4.89f);
        addPlatformWithCoins(rgd, 2.38f, 4.9f);
        addPlatformWithCoins(rgd, 2.25f, 9.84f);
        addPlatformWithCoins(rgd, 6.8f, 9.85f);
        addPlatformWithCoins(rgd, 9.25f, 15.21f);
        addPlatformWithCoins(rgd, 3.32f, 15.3f);
        addPlatformWithCoins(rgd, 1.2f, 18.48f);
        addPlatformWithCoins(rgd, 6.72f, 20.25f);
        addPlatformWithCoins(rgd, 0.0f, 23.4f);
        addPlatformWithCoins(rgd, 9.25f, 24.0f);
        addPlatformWithCoins(rgd, 9.18f, 29.15f);
        addPlatformWithCoins(rgd, 4.61f, 29.22f);
        addPlatformWithCoins(rgd, 0.0f, 32.77f);
        addPlatformWithCoins(rgd, 6.82f, 35.49f);
        addPlatformWithCoins(rgd, 2.65f, 37.27f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 4.48f, 4.97f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.33f, 9.82f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 8.85f, 9.93f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 4.23f, 20.29f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.07f, 23.5f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.73f, 29.22f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.15f, 32.92f, assetManager);
        
        rgd.changeGeneratedHeight(40.0f);
    }
    
    public void generate12(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 4.0f, 0.0f);
        addPlatformWithCoins(rgd, 8.43f, 1.37f);
        addPlatformWithCoins(rgd, 1.18f, 3.05f);
        addPlatformWithCoins(rgd, 6.58f, 4.77f);
        addPlatformWithCoins(rgd, 0.0f, 7.55f);
        addPlatformWithCoins(rgd, 4.35f, 9.6f);
        addPlatformWithCoins(rgd, 9.25f, 11.65f);
        addPlatformWithCoins(rgd, 4.38f, 14.95f);
        addPlatformWithCoins(rgd, 9.2f, 16.75f);
        addPlatformWithCoins(rgd, 0.0f, 16.97f);
        addPlatformWithCoins(rgd, 0.15f, 22.15f);
        addPlatformWithCoins(rgd, 4.5f, 22.3f);
        addPlatformWithCoins(rgd, 6.7f, 17.71f);
        addPlatformWithCoins(rgd, 9.25f, 22.39f);
        addPlatformWithCoins(rgd, 2.53f, 27.04f);
        addPlatformWithCoins(rgd, 6.77f, 27.08f);
        addPlatformWithCoins(rgd, 9.25f, 29.06f);
        addPlatformWithCoins(rgd, 0.02f, 32.16f);
        addPlatformWithCoins(rgd, 4.75f, 36.2f);
        addPlatformWithCoins(rgd, 9.25f, 36.28f);
        addPlatformWithCoins(rgd, 4.64f, 40.95f);
        
        RiseGeneratorUtils.addSawEnemy(rgd, 2.1f, 7.67f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.45f, 9.75f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.48f, 14.87f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.28f, 22.12f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.6f, 22.24f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 2.11f, 32.27f, assetManager);
        RiseGeneratorUtils.addSawEnemy(rgd, 6.83f, 36.3f, assetManager);
        
        rgd.changeGeneratedHeight(44.0f);
    }
    
    private void addPlatformWithCoins(RiseGeneratorData rgd, float x, float y) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
        coinGenerator.generate(0.3f, rgd, platform);
    }
    
    private void addCrumblePlatform(RiseGeneratorData rgd, float x, float y) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setCrumbleFeature();
    }
    
    private void addRevealOnJumpPlatform(RiseGeneratorData rgd, float x, float y, boolean isInitiallyVisible) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setCrumbleFeature();
        platform.setRevealOnJumpFeature(isInitiallyVisible, new int[] { rgd.nextPlatformId + 1 });
    }
}
