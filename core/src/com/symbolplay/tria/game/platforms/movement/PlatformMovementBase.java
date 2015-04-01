package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;

public abstract class PlatformMovementBase {
    
    protected static final Vector2 PLATFORM_CENTER_OFFSET;
    
    protected final Vector2 position;
    
    static {
        PLATFORM_CENTER_OFFSET = new Vector2(Platform.WIDTH / 2.0f, Platform.HEIGHT / 2.0f);
    }
    
    public PlatformMovementBase(Vector2 initialPosition) {
        position = new Vector2(initialPosition);
    }
    
    public final void update(float delta) {
        updateImpl(delta);
    }
    
    protected void updateImpl(float delta) {
    }
    
    public void render(SpriteBatch batch, float alpha) {
    }
    
    public void updatePlatformModifier(PlatformModifier modifier) {
    }
    
    public void applyContact(CollisionEffects collisionEffects) {
    }
    
    public void applyEffects(CollisionEffects collisionEffects) {
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public void offsetPositionY(float offset) {
        position.y += offset;
    }
    
    public abstract boolean hasVerticalMovement();
}
