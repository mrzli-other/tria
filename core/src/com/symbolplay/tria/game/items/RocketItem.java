package com.symbolplay.tria.game.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.symbolplay.tria.resources.ResourceNames;

public final class RocketItem extends ItemBase {
    
    public static final float WIDTH = 1.0f;
    
    private final Rectangle collisionRect;
    
    public RocketItem(float x, float y, AssetManager assetManager) {
        super(x, y, ResourceNames.ITEM_ROCKET_IMAGE_NAME, assetManager);
        
        collisionRect = new Rectangle();
        
        setPickedUpText("ROCKET");
        
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
        return ROCKET_EFFECT;
    }
    
    @Override
    public Object getValue() {
        return 0;
    }
}
