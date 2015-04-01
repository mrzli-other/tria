package com.symbolplay.tria.game.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.symbolplay.tria.resources.ResourceNames;

public final class LifeItem extends ItemBase {
    
    public static final float WIDTH = 1.0f;
    
    private final Rectangle collisionRect;
    
    public LifeItem(float x, float y, AssetManager assetManager) {
        super(x, y, ResourceNames.ITEM_LIFE_IMAGE_NAME, assetManager);
        
        collisionRect = new Rectangle();
        
        setPickedUpText("+1 LIFE");
        
        updatePositionImpl();
    }
    
    @Override
    protected void updatePositionImpl() {
        sprite.setPosition(position.x, position.y);
        collisionRect.set(position.x, position.y, size.x, size.y);
    }
    
    @Override
    public boolean isCollision(Rectangle rect) {
        return Intersector.overlaps(rect, collisionRect);
    }
    
    @Override
    public int getEffect() {
        return LIFE_EFFECT;
    }
    
    @Override
    public Object getValue() {
        return null;
    }
}
