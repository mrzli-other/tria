package com.symbolplay.tria.game.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.symbolplay.gamelibrary.util.ExceptionThrower;
import com.symbolplay.tria.resources.ResourceNames;

public final class CoinItem extends ItemBase {
    
    public static final float WIDTH = 0.5f;
    
    public static final int COIN_TYPE_1 = 0;
    public static final int COIN_TYPE_5 = 1;
    public static final int COIN_TYPE_10 = 2;
    
    private final int coinValue;
    
    private final Circle collisionCircle;
    
    public CoinItem(float x, float y, int coinType, AssetManager assetManager) {
        super(x, y, getImageName(coinType), assetManager);
        
        coinValue = getCoinValue(coinType);
        
        collisionCircle = new Circle();
        
        setPickedUpText("+" + String.valueOf(coinValue));
        
        updatePositionImpl();
    }
    
    @Override
    protected void updatePositionImpl() {
        sprite.setPosition(position.x, position.y);
        collisionCircle.set(position.x + radius, position.y + radius, radius);
    }
    
    @Override
    public boolean isCollision(Rectangle rect) {
        return Intersector.overlaps(collisionCircle, rect);
    }
    
    @Override
    public int getEffect() {
        return COIN_EFFECT;
    }
    
    @Override
    public Object getValue() {
        return coinValue;
    }
    
    private static String getImageName(int coinType) {
        switch (coinType) {
            case COIN_TYPE_1:
                return ResourceNames.ITEM_COIN_1_IMAGE_NAME;
            case COIN_TYPE_5:
                return ResourceNames.ITEM_COIN_5_IMAGE_NAME;
            case COIN_TYPE_10:
                return ResourceNames.ITEM_COIN_10_IMAGE_NAME;
            default:
                ExceptionThrower.throwException("Invalid coin type: %d", coinType);
                return "";
        }
    }
    
    private static int getCoinValue(int coinType) {
        switch (coinType) {
            case COIN_TYPE_1:
                return 1;
            case COIN_TYPE_5:
                return 5;
            case COIN_TYPE_10:
                return 10;
            default:
                ExceptionThrower.throwException("Invalid coin type: %d", coinType);
                return 0;
        }
    }
}
