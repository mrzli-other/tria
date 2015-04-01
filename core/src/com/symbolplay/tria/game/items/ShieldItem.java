package com.symbolplay.tria.game.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.symbolplay.tria.resources.ResourceNames;

public final class ShieldItem extends ItemBase {
    
    public static final float WIDTH = 1.0f;
    
    private static final float SHIELD_DURATION = 15.0f;
    
    private final Rectangle collisionRect;
    
    public ShieldItem(float x, float y, AssetManager assetManager) {
        super(x, y, ResourceNames.ITEM_SHIELD_IMAGE_NAME, assetManager);
        
        collisionRect = new Rectangle();
        
        setPickedUpText("SHIELD");
        
        updatePositionImpl();
    }
    
    @Override
    protected void updatePositionImpl() {
        sprite.setPosition(position.x, position.y);
        collisionRect.set(position.x, position.y, size.x, size.y);
    }
    
    @Override
    public boolean isCollision(Rectangle rect) {
        return Intersector.overlaps(collisionRect, rect);
    }
    
    @Override
    public int getEffect() {
        return SHIELD_EFFECT;
    }
    
    @Override
    public Object getValue() {
        return SHIELD_DURATION;
    }
}
