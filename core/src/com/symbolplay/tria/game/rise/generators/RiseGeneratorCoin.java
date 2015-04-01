package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.enemies.SinePatrollerEnemy;
import com.symbolplay.tria.game.enemies.StaticEnemy;
import com.symbolplay.tria.game.platforms.Platform;

public final class RiseGeneratorCoin {
    
    private final RiseGeneratorStandard riseGeneratorStandard;
    private final AssetManager assetManager;
    private final CoinGenerator coinGenerator;
    
    public RiseGeneratorCoin(RiseGeneratorStandard riseGeneratorStandard, AssetManager assetManager) {
        this.riseGeneratorStandard = riseGeneratorStandard;
        this.assetManager = assetManager;
        coinGenerator = new CoinGenerator(assetManager);
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
        coinGenerator.setAbsoluteHeight(absoluteHeight);
        
        int randomValue = MathUtils.random(5);
        
        if (randomValue == 0) {
            generate01(rgd);
        } else if (randomValue == 1) {
            generate02(rgd);
        } else if (randomValue == 2) {
            generate03(rgd);
        } else if (randomValue == 3) {
            generate04(rgd);
        } else if (randomValue == 4) {
            generate05(rgd);
        } else {
            generate06(rgd);
        }
    }
    
    private void generate01(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.18f, 0.0f);
        addPlatformWithCoins(rgd, 8.13f, 0.03f);
        addPlatformWithCoins(rgd, 4.56f, 1.52f);
        addPlatformWithCoins(rgd, 1.17f, 3.02f);
        addSpikesVisibleOnJumpPlatform(rgd, 7.67f, 3.15f);
        addPlatformWithCoins(rgd, 4.0f, 5.0f);
        addPlatformWithCoins(rgd, 2.2f, 6.22f);
        addPlatformWithCoins(rgd, 6.48f, 6.27f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.78f, 7.62f);
        addPlatformWithCoins(rgd, 7.88f, 7.7f);
        addPlatformWithCoins(rgd, 0.3f, 8.95f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.98f, 9.38f);
        addPlatformWithCoins(rgd, 0.18f, 10.6f);
        addPlatformWithCoins(rgd, 8.65f, 11.02f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.5f, 12.35f);
        addPlatformWithCoins(rgd, 7.2f, 12.55f);
        addPlatformWithCoins(rgd, 4.15f, 13.9f);
        addPlatformWithCoins(rgd, 2.28f, 15.07f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.22f, 15.17f);
        addPlatformWithCoins(rgd, 1.05f, 16.45f);
        addPlatformWithCoins(rgd, 7.98f, 16.72f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.23f, 18.0f);
        addPlatformWithCoins(rgd, 8.62f, 18.22f);
        addPlatformWithCoins(rgd, 0f, 19.62f);
        addPlatformWithCoins(rgd, 9.25f, 19.8f);
        addPlatformWithCoins(rgd, 0.0f, 21.08f);
        addSpikesVisibleOnJumpPlatform(rgd, 9.25f, 21.4f);
        addPlatformWithCoins(rgd, 0.7f, 22.45f);
        addPlatformWithCoins(rgd, 8.3f, 22.9f);
        addSpikesVisibleOnJumpPlatform(rgd, 2.55f, 24.0f);
        addPlatformWithCoins(rgd, 6.48f, 24.1f);
        addPlatformWithCoins(rgd, 4.58f, 25.31f);
        addPlatformWithCoins(rgd, 0.58f, 25.95f);
        addPlatformWithCoins(rgd, 8.6f, 26.4f);
        addPlatformWithCoins(rgd, 5.62f, 28.57f);
        addPlatformWithCoins(rgd, 8.9f, 30.62f);
        addPlatformWithCoins(rgd, 2.98f, 30.63f);
        
        addStaticEnemy(rgd, 9.45f, 14.09f);
        addStaticEnemy(rgd, 4.9f, 21.64f);
        addStaticEnemy(rgd, 1.18f, 28.7f);
        
        rgd.changeGeneratedHeight(33.0f);
    }
    
    private void generate02(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 7.98f, 0.0f);
        addPlatformWithCoins(rgd, 4.4f, 0.02f);
        addPlatformWithCoins(rgd, 0.95f, 0.05f);
        addPlatformWithCoins(rgd, 4.15f, 1.88f);
        addPlatformWithCoins(rgd, 2.1f, 3.2f);
        addPlatformWithCoins(rgd, 1.33f, 4.4f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.9f, 6.3f);
        addPlatformWithCoins(rgd, 4.5f, 7.0f);
        addPlatformWithCoins(rgd, 1.02f, 8.52f);
        addPlatformWithCoins(rgd, 2.88f, 10.48f);
        addPlatformWithCoins(rgd, 4.58f, 12.0f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.82f, 13.57f);
        addPlatformWithCoins(rgd, 7.12f, 14.9f);
        addPlatformWithCoins(rgd, 2.3f, 15.4f);
        addPlatformWithCoins(rgd, 7.98f, 16.25f);
        addPlatformWithCoins(rgd, 7.55f, 18.07f);
        addPlatformWithCoins(rgd, 3.2f, 18.82f);
        addSpikesVisibleOnJumpPlatform(rgd, 7.68f, 19.67f);
        addPlatformWithCoins(rgd, 6.7f, 21.32f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.45f, 22.12f);
        addPlatformWithCoins(rgd, 6.1f, 24.0f);
        addPlatformWithCoins(rgd, 0.0f, 24.58f);
        addPlatformWithCoins(rgd, 2.9f, 24.6f);
        addPlatformWithCoins(rgd, 9.05f, 24.72f);
        
        addStaticEnemy(rgd, 9.98f, 8.92f);
        addStaticEnemy(rgd, 0.88f, 18.48f);
        
        rgd.changeGeneratedHeight(28.0f);
    }
    
    private void generate03(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.53f, 0.0f);
        addPlatformWithCoins(rgd, 4.4f, 0.0f);
        addPlatformWithCoins(rgd, 6.9f, 0.0f);
        addPlatformWithCoins(rgd, 4.38f, 1.32f);
        addPlatformWithCoins(rgd, 4.25f, 3.05f);
        addPlatformWithCoins(rgd, 4.3f, 4.5f);
        addPlatformWithCoins(rgd, 5.0f, 6.02f);
        addPlatformWithCoins(rgd, 8.25f, 7.82f);
        addPlatformWithCoins(rgd, 3.7f, 7.95f);
        addPlatformWithCoins(rgd, 0.6f, 8.38f);
        addSpikesVisibleOnJumpPlatform(rgd, 4.15f, 9.45f);
        addPlatformWithCoins(rgd, 4.35f, 11.3f);
        addPlatformWithCoins(rgd, 8.18f, 12.77f);
        addPlatformWithCoins(rgd, 4.43f, 13.62f);
        addPlatformWithCoins(rgd, 0.5f, 13.77f);
        addPlatformWithCoins(rgd, 4.45f, 15.56f);
        addPlatformWithCoins(rgd, 8.12f, 16.12f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.5f, 16.4f);
        addPlatformWithCoins(rgd, 3.3f, 19.52f);
        addPlatformWithCoins(rgd, 5.9f, 19.58f);
        
        addStaticEnemy(rgd, 8.8f, 5.42f);
        addStaticEnemy(rgd, 1.2f, 5.62f);
        addStaticEnemy(rgd, 4.84f, 17.58f);
        
        rgd.changeGeneratedHeight(23.0f);
    }
    
    private void generate04(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 1.43f, 0.0f);
        addPlatformWithCoins(rgd, 6.2f, 0.0f);
        addPlatformWithCoins(rgd, 6.6f, 1.59f);
        addPlatformWithCoins(rgd, 1.55f, 2.0f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.32f, 3.52f);
        addPlatformWithCoins(rgd, 6.72f, 3.75f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.55f, 5.44f);
        addPlatformWithCoins(rgd, 1.83f, 5.47f);
        addPlatformWithCoins(rgd, 1.58f, 7.52f);
        addPlatformWithCoins(rgd, 6.43f, 7.55f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.92f, 9.45f);
        addPlatformWithCoins(rgd, 1.98f, 9.82f);
        addPlatformWithCoins(rgd, 7.52f, 11.2f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.62f, 11.62f);
        addPlatformWithCoins(rgd, 7.0f, 13.32f);
        addPlatformWithCoins(rgd, 1.88f, 14.07f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.87f, 15.55f);
        addPlatformWithCoins(rgd, 1.65f, 15.88f);
        addPlatformWithCoins(rgd, 6.93f, 17.76f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.43f, 18.27f);
        addPlatformWithCoins(rgd, 6.82f, 19.9f);
        addPlatformWithCoins(rgd, 1.55f, 20.34f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.73f, 21.86f);
        addPlatformWithCoins(rgd, 1.72f, 23.0f);
        addPlatformWithCoins(rgd, 6.8f, 24.35f);
        addPlatformWithCoins(rgd, 1.6f, 24.9f);
        addPlatformWithCoins(rgd, 6.9f, 26.46f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.65f, 27.33f);
        addSpikesVisibleOnJumpPlatform(rgd, 7.0f, 28.83f);
        addPlatformWithCoins(rgd, 1.82f, 29.69f);
        addPlatformWithCoins(rgd, 6.8f, 31.06f);
        addPlatformWithCoins(rgd, 1.93f, 31.87f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.86f, 33.67f);
        addPlatformWithCoins(rgd, 1.85f, 34.27f);
        addPlatformWithCoins(rgd, 6.68f, 36.07f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.83f, 36.3f);
        addPlatformWithCoins(rgd, 2.05f, 38.47f);
        addPlatformWithCoins(rgd, 6.83f, 38.65f);
        addPlatformWithCoins(rgd, 1.98f, 40.37f);
        addPlatformWithCoins(rgd, 6.58f, 40.45f);
        
        addStaticEnemy(rgd, 9.78f, 6.65f);
        addStaticEnemy(rgd, 4.98f, 12.1f);
        addStaticEnemy(rgd, 0.38f, 21.46f);
        addStaticEnemy(rgd, 4.3f, 28.25f);
        addStaticEnemy(rgd, 9.75f, 32.03f);
        
        rgd.changeGeneratedHeight(42.0f);
    }
    
    private void generate05(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 0.53f, 0.0f);
        addPlatformWithCoins(rgd, 2.38f, 1.35f);
        addSpikesVisibleOnJumpPlatform(rgd, 4.58f, 2.93f);
        addPlatformWithCoins(rgd, 6.45f, 4.45f);
        addPlatformWithCoins(rgd, 8.22f, 6.02f);
        addPlatformWithCoins(rgd, 0.58f, 5.42f);
        addPlatformWithCoins(rgd, 2.36f, 7.0f);
        addPlatformWithCoins(rgd, 4.28f, 8.67f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.6f, 10.56f);
        addPlatformWithCoins(rgd, 8.22f, 12.45f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.65f, 10.42f);
        addPlatformWithCoins(rgd, 2.0f, 11.97f);
        addPlatformWithCoins(rgd, 4.15f, 13.57f);
        addPlatformWithCoins(rgd, 5.82f, 15.52f);
        addSpikesVisibleOnJumpPlatform(rgd, 7.8f, 17.75f);
        addPlatformWithCoins(rgd, 0.73f, 14.56f);
        addPlatformWithCoins(rgd, 2.48f, 16.36f);
        addPlatformWithCoins(rgd, 4.2f, 18.51f);
        addSpikesVisibleOnJumpPlatform(rgd, 5.85f, 20.38f);
        addPlatformWithCoins(rgd, 7.78f, 22.78f);
        addPlatformWithCoins(rgd, 0.8f, 20.15f);
        addSpikesVisibleOnJumpPlatform(rgd, 2.53f, 21.71f);
        addPlatformWithCoins(rgd, 3.95f, 23.43f);
        addPlatformWithCoins(rgd, 5.93f, 25.2f);
        addPlatformWithCoins(rgd, 7.75f, 26.88f);
        addPlatformWithCoins(rgd, 0.75f, 25.15f);
        addPlatformWithCoins(rgd, 2.5f, 26.85f);
        addSpikesVisibleOnJumpPlatform(rgd, 4.28f, 28.73f);
        addPlatformWithCoins(rgd, 5.78f, 30.25f);
        addSpikesVisibleOnJumpPlatform(rgd, 7.43f, 31.83f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.73f, 29.45f);
        addPlatformWithCoins(rgd, 2.05f, 31.4f);
        addPlatformWithCoins(rgd, 3.78f, 33.25f);
        addSpikesVisibleOnJumpPlatform(rgd, 5.88f, 34.92f);
        addPlatformWithCoins(rgd, 7.58f, 36.62f);
        addPlatformWithCoins(rgd, 0.78f, 34.75f);
        addPlatformWithCoins(rgd, 2.5f, 36.55f);
        addPlatformWithCoins(rgd, 4.33f, 38.18f);
        
        addStaticEnemy(rgd, 9.33f, 9.72f);
        addStaticEnemy(rgd, 3.55f, 10.37f);
        addStaticEnemy(rgd, 9.68f, 20.17f);
        addStaticEnemy(rgd, 3.63f, 25.19f);
        addStaticEnemy(rgd, 0.73f, 37.97f);
        
        rgd.changeGeneratedHeight(40.0f);
    }
    
    private void generate06(RiseGeneratorData rgd) {
        
        addPlatformWithCoins(rgd, 0.97f, 0.0f);
        addPlatformWithCoins(rgd, 8.28f, 0.0f);
        addPlatformWithCoins(rgd, 0.92f, 1.12f);
        addPlatformWithCoins(rgd, 8.33f, 1.27f);
        addPlatformWithCoins(rgd, 0.9f, 2.87f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.48f, 3.07f);
        addSpikesVisibleOnJumpPlatform(rgd, 3.35f, 4.27f);
        addPlatformWithCoins(rgd, 0.85f, 4.3f);
        addPlatformWithCoins(rgd, 5.85f, 4.3f);
        addPlatformWithCoins(rgd, 8.48f, 4.42f);
        addPlatformWithCoins(rgd, 0.85f, 6.05f);
        addPlatformWithCoins(rgd, 8.6f, 6.22f);
        addPlatformWithCoins(rgd, 0.83f, 8.05f);
        addPlatformWithCoins(rgd, 8.57f, 8.05f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.87f, 9.78f);
        addPlatformWithCoins(rgd, 8.6f, 9.8f);
        addPlatformWithCoins(rgd, 0.9f, 11.27f);
        addSpikesVisibleOnJumpPlatform(rgd, 6.08f, 11.34f);
        addPlatformWithCoins(rgd, 8.62f, 11.35f);
        addPlatformWithCoins(rgd, 3.55f, 11.37f);
        addPlatformWithCoins(rgd, 0.93f, 13.0f);
        addPlatformWithCoins(rgd, 8.68f, 13.42f);
        addPlatformWithCoins(rgd, 0.9f, 14.7f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.6f, 15.05f);
        addSpikesVisibleOnJumpPlatform(rgd, 0.92f, 16.32f);
        addPlatformWithCoins(rgd, 8.73f, 16.55f);
        addPlatformWithCoins(rgd, 1.08f, 18.15f);
        addPlatformWithCoins(rgd, 8.73f, 18.25f);
        addPlatformWithCoins(rgd, 6.16f, 18.27f);
        addPlatformWithCoins(rgd, 3.42f, 18.32f);
        addPlatformWithCoins(rgd, 1.0f, 19.84f);
        addPlatformWithCoins(rgd, 8.65f, 20.29f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.3f, 21.77f);
        addPlatformWithCoins(rgd, 8.73f, 21.87f);
        addPlatformWithCoins(rgd, 8.95f, 23.19f);
        addPlatformWithCoins(rgd, 1.15f, 23.29f);
        addPlatformWithCoins(rgd, 1.23f, 24.89f);
        addPlatformWithCoins(rgd, 3.88f, 24.94f);
        addPlatformWithCoins(rgd, 8.6f, 24.94f);
        addPlatformWithCoins(rgd, 6.32f, 24.99f);
        addPlatformWithCoins(rgd, 1.18f, 26.26f);
        addPlatformWithCoins(rgd, 8.65f, 26.64f);
        addPlatformWithCoins(rgd, 1.18f, 27.94f);
        addPlatformWithCoins(rgd, 1.18f, 29.41f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.74f, 29.61f);
        addPlatformWithCoins(rgd, 8.62f, 31.06f);
        addSpikesVisibleOnJumpPlatform(rgd, 1.03f, 31.09f);
        addPlatformWithCoins(rgd, 6.08f, 31.09f);
        addPlatformWithCoins(rgd, 3.8f, 31.11f);
        addPlatformWithCoins(rgd, 8.68f, 32.45f);
        addPlatformWithCoins(rgd, 1.08f, 32.54f);
        addPlatformWithCoins(rgd, 1.13f, 33.7f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.7f, 33.72f);
        addPlatformWithCoins(rgd, 8.55f, 37.42f);
        addPlatformWithCoins(rgd, 3.85f, 37.45f);
        addPlatformWithCoins(rgd, 1.12f, 37.47f);
        addPlatformWithCoins(rgd, 6.08f, 37.5f);
        addPlatformWithCoins(rgd, 1.28f, 38.97f);
        addPlatformWithCoins(rgd, 8.56f, 39.13f);
        addPlatformWithCoins(rgd, 1.27f, 40.6f);
        addPlatformWithCoins(rgd, 8.58f, 40.67f);
        addPlatformWithCoins(rgd, 1.12f, 41.95f);
        addSpikesVisibleOnJumpPlatform(rgd, 8.68f, 42.1f);
        addPlatformWithCoins(rgd, 6.2f, 43.32f);
        addPlatformWithCoins(rgd, 8.63f, 43.35f);
        addSpikesVisibleOnJumpPlatform(rgd, 3.87f, 43.38f);
        addPlatformWithCoins(rgd, 1.27f, 43.42f);
        
        addStaticEnemy(rgd, 5.02f, 8.49f);
        addStaticEnemy(rgd, 5.66f, 15.36f);
        addStaticEnemy(rgd, 9.08f, 27.93f);
        addSinePatrollerEnemy(rgd, 0.0f, 35.17f);
        addStaticEnemy(rgd, 4.55f, 41.0f);
        
        rgd.changeGeneratedHeight(45.0f);
    }
    
    private void addPlatformWithCoins(RiseGeneratorData rgd, float x, float y) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        rgd.lastNormalPlatformAbsoluteHeight = rgd.absoluteHeight + y;
        coinGenerator.generate(1.0f, rgd, platform);
    }
    
    private void addSpikesVisibleOnJumpPlatform(RiseGeneratorData rgd, float x, float y) {
        Platform platform = RiseGeneratorUtils.addPlatform(rgd, x, y, assetManager);
        platform.setSpikesFeature();
        platform.setVisibleOnJumpFeature();
    }
    
    private void addStaticEnemy(RiseGeneratorData rgd, float x, float y) {
        EnemyBase enemy = new StaticEnemy(x, y + rgd.relativeHeight, assetManager);
        rgd.enemies.add(enemy);
    }
    
    private void addSinePatrollerEnemy(RiseGeneratorData rgd, float x, float y) {
        float absoluteHeight = rgd.getAbsoluteHeightFloat();
        float speed = getSinePatrollerSpeed(absoluteHeight);
        float range = GameContainer.GAME_AREA_WIDTH - SinePatrollerEnemy.WIDTH;
        float initialOffset = MathUtils.random(range * 2.0f);
        
        EnemyBase enemy = new SinePatrollerEnemy(x, y + rgd.relativeHeight, range, speed, initialOffset, assetManager);
        rgd.enemies.add(enemy);
    }
    
    private static float getSinePatrollerSpeed(float absoluteHeight) {
        if (absoluteHeight <= 1000.0f) {
            return 3.0f + absoluteHeight * 0.003f;
        } else if (absoluteHeight <= 3000.0f) {
            return 6.0f + (absoluteHeight - 1000.0f) * 0.001f;
        } else {
            return 8.0f;
        }
    }
}
