package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.platforms.PlatformModifier;

public final class VerticalPlatformMovement extends PlatformMovementBase {
    
    private static final Color COLOR;
    
    private final float speed;
    
    private final float bottomLimit;
    private final float topLimit;
    private boolean isUpMovement;
    
    static {
        COLOR = new Color(0.7f, 0.13f, 0.13f, 1.0f);
    }
    
    public VerticalPlatformMovement(Vector2 initialPosition, float range, float speed, float initialOffset, AssetManager assetManager) {
        super(initialPosition);
        
        this.speed = speed;
        
        bottomLimit = initialPosition.y;
        topLimit = initialPosition.y + range;
        isUpMovement = true;
        
        if (initialOffset <= range) {
            changePosition(initialOffset);
            isUpMovement = true;
        } else {
            changePosition(range - initialOffset % range);
            isUpMovement = false;
        }
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        if (!isUpMovement) {
            travelled = -travelled;
        }
        
        changePosition(travelled);
    }
    
    private void changePosition(float change) {
        position.y += change;
        if (position.y <= bottomLimit){
            isUpMovement = true;
        } else if (position.y >= topLimit) {
            isUpMovement = false;
        }
        
        position.y = MathUtils.clamp(position.y, bottomLimit, topLimit);
    }
    
    @Override
    public void updatePlatformModifier(PlatformModifier modifier) {
        GameUtils.setRgb(modifier.platformColor, COLOR);
        GameUtils.setRgb(modifier.featuresColor, COLOR);
    }
    
    @Override
    public boolean hasVerticalMovement() {
        return true;
    }
}
