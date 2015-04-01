package com.symbolplay.tria.game.rise.generators;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.items.CoinItem;
import com.symbolplay.tria.game.platforms.Platform;

final class CoinGenerator {
    
    private final AssetManager assetManager;
    
    private final float[] coinConfigurationCumulativeWeights;
    
    public CoinGenerator(AssetManager assetManager) {
        this.assetManager = assetManager;
        
        coinConfigurationCumulativeWeights = new float[6];
    }
    
    public void setAbsoluteHeight(float absoluteHeight) {
        if (absoluteHeight <= 500.0f) { // 1, 0, 0, 0, 0, 0
            coinConfigurationCumulativeWeights[0] = 1.0f;
            coinConfigurationCumulativeWeights[1] = 1.0f;
            coinConfigurationCumulativeWeights[2] = 1.0f;
            coinConfigurationCumulativeWeights[3] = 1.0f;
            coinConfigurationCumulativeWeights[4] = 1.0f;
            coinConfigurationCumulativeWeights[5] = 1.0f;
        } else if (absoluteHeight <= 1000.0f) { // 1, 1, 0, 0, 0, 0
            coinConfigurationCumulativeWeights[0] = 1.0f;
            coinConfigurationCumulativeWeights[1] = 2.0f;
            coinConfigurationCumulativeWeights[2] = 2.0f;
            coinConfigurationCumulativeWeights[3] = 2.0f;
            coinConfigurationCumulativeWeights[4] = 2.0f;
            coinConfigurationCumulativeWeights[5] = 2.0f;
        } else if (absoluteHeight <= 1500.0f) { // 1, 2, 1, 0, 0, 0
            coinConfigurationCumulativeWeights[0] = 1.0f;
            coinConfigurationCumulativeWeights[1] = 3.0f;
            coinConfigurationCumulativeWeights[2] = 4.0f;
            coinConfigurationCumulativeWeights[3] = 4.0f;
            coinConfigurationCumulativeWeights[4] = 4.0f;
            coinConfigurationCumulativeWeights[5] = 4.0f;
        } else if (absoluteHeight <= 2000.0f) { // 0, 1, 1, 0, 0, 0
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 1.0f;
            coinConfigurationCumulativeWeights[2] = 2.0f;
            coinConfigurationCumulativeWeights[3] = 2.0f;
            coinConfigurationCumulativeWeights[4] = 2.0f;
            coinConfigurationCumulativeWeights[5] = 2.0f;
        } else if (absoluteHeight <= 2500.0f) { // 0, 1, 2, 1, 0, 0
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 1.0f;
            coinConfigurationCumulativeWeights[2] = 3.0f;
            coinConfigurationCumulativeWeights[3] = 4.0f;
            coinConfigurationCumulativeWeights[4] = 4.0f;
            coinConfigurationCumulativeWeights[5] = 4.0f;
        } else if (absoluteHeight <= 3000.0f) { // 0, 0, 1, 1, 0, 0
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 0.0f;
            coinConfigurationCumulativeWeights[2] = 1.0f;
            coinConfigurationCumulativeWeights[3] = 2.0f;
            coinConfigurationCumulativeWeights[4] = 2.0f;
            coinConfigurationCumulativeWeights[5] = 2.0f;
        } else if (absoluteHeight <= 3500.0f) { // 0, 0, 1, 2, 1, 0
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 0.0f;
            coinConfigurationCumulativeWeights[2] = 1.0f;
            coinConfigurationCumulativeWeights[3] = 3.0f;
            coinConfigurationCumulativeWeights[4] = 4.0f;
            coinConfigurationCumulativeWeights[5] = 4.0f;
        } else if (absoluteHeight <= 4000.0f) { // 0, 0, 0, 1, 1, 0
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 0.0f;
            coinConfigurationCumulativeWeights[2] = 0.0f;
            coinConfigurationCumulativeWeights[3] = 1.0f;
            coinConfigurationCumulativeWeights[4] = 2.0f;
            coinConfigurationCumulativeWeights[5] = 2.0f;
        } else { // 0, 0, 0, 1, 2, 1
            coinConfigurationCumulativeWeights[0] = 0.0f;
            coinConfigurationCumulativeWeights[1] = 0.0f;
            coinConfigurationCumulativeWeights[2] = 0.0f;
            coinConfigurationCumulativeWeights[3] = 1.0f;
            coinConfigurationCumulativeWeights[4] = 3.0f;
            coinConfigurationCumulativeWeights[5] = 4.0f;
        }
    }
    
    public boolean generate(float coinChance, RiseGeneratorData rgd, Platform platform) {
        if (MathUtils.random() < coinChance) {
            int coinConfiguration = getRandomCoinConfiguration();
            switch (coinConfiguration) {
                case 0:
                    addCoins1(rgd, platform);
                    break;
                
                case 1:
                    addCoins2(rgd, platform);
                    break;
                    
                case 2:
                    addCoins3(rgd, platform);
                    break;
                    
                case 3:
                    addCoins5(rgd, platform);
                    break;
                    
                case 4:
                    addCoins7(rgd, platform);
                    break;
                    
                case 5:
                    addCoins10(rgd, platform);
                    break;
                    
                default:
                    break;
            }
            return true;
        } else {
            return false;
        }
    }
    
    private int getRandomCoinConfiguration() {
        float totalWeight = coinConfigurationCumulativeWeights[5];
        float randomValue = MathUtils.random(totalWeight);
        for (int i = 0; i < coinConfigurationCumulativeWeights.length; i++) {
            if (randomValue < coinConfigurationCumulativeWeights[i]) {
                return i;
            }
        }
        
        return coinConfigurationCumulativeWeights.length - 1;
    }
    
    private void addCoins1(RiseGeneratorData rgd, Platform platform) {
        addCoinItem1(rgd, platform, CoinItem.COIN_TYPE_1);
    }
    
    private void addCoins2(RiseGeneratorData rgd, Platform platform) {
        addCoinItem2(rgd, platform, CoinItem.COIN_TYPE_1, CoinItem.COIN_TYPE_1);
    }
    
    private void addCoins3(RiseGeneratorData rgd, Platform platform) {
        addCoinItem3(rgd, platform, CoinItem.COIN_TYPE_1, CoinItem.COIN_TYPE_1, CoinItem.COIN_TYPE_1);
    }
    
    private void addCoins5(RiseGeneratorData rgd, Platform platform) {
        addCoinItem1(rgd, platform, CoinItem.COIN_TYPE_5);
    }
    
    private void addCoins7(RiseGeneratorData rgd, Platform platform) {
        addCoinItem3(rgd, platform, CoinItem.COIN_TYPE_1, CoinItem.COIN_TYPE_5, CoinItem.COIN_TYPE_1);
    }
    
    private void addCoins10(RiseGeneratorData rgd, Platform platform) {
        addCoinItem1(rgd, platform, CoinItem.COIN_TYPE_10);
    }
    
    private void addCoinItem1(RiseGeneratorData rgd, Platform platform, int coin1Type) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        float x1 = platformInitialPosition.x + (Platform.WIDTH - CoinItem.WIDTH) * 0.5f; 
        CoinItem coin1 = new CoinItem(x1, y, coin1Type, assetManager);
        platform.attachItem(coin1);
        rgd.items.add(coin1);
    }
    
    private void addCoinItem2(RiseGeneratorData rgd, Platform platform, int coin1Type, int coin2Type) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        float x1 = platformInitialPosition.x + Platform.WIDTH * 0.5f - CoinItem.WIDTH;
        CoinItem coin1 = new CoinItem(x1, y, coin1Type, assetManager);
        platform.attachItem(coin1);
        rgd.items.add(coin1);
        
        float x2 = x1 + CoinItem.WIDTH;
        CoinItem coin2 = new CoinItem(x2, y, coin2Type, assetManager);
        platform.attachItem(coin2);
        rgd.items.add(coin2);
    }
    
    private void addCoinItem3(RiseGeneratorData rgd, Platform platform, int coin1Type, int coin2Type, int coin3Type) {
        Vector2 platformInitialPosition = platform.getInitialPosition();
        float y = platformInitialPosition.y + Platform.HEIGHT;
        
        float x1 = platformInitialPosition.x + Platform.WIDTH * 0.5f - CoinItem.WIDTH * 1.5f;
        CoinItem coin1 = new CoinItem(x1, y, coin1Type, assetManager);
        platform.attachItem(coin1);
        rgd.items.add(coin1);
        
        float x2 = x1 + CoinItem.WIDTH;
        CoinItem coin2 = new CoinItem(x2, y, coin2Type, assetManager);
        platform.attachItem(coin2);
        rgd.items.add(coin2);
        
        float x3 = x2 + CoinItem.WIDTH;
        CoinItem coin3 = new CoinItem(x3, y, coin3Type, assetManager);
        platform.attachItem(coin3);
        rgd.items.add(coin3);
    }
}
