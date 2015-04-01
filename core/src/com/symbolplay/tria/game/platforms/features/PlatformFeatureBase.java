package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;

public abstract class PlatformFeatureBase {
    
    protected final Platform platform;
    
    protected float backgroundRenderPrecedence;
    protected float foregroundRenderPrecedence;
    protected float contactPrecedence;
    
    public PlatformFeatureBase(Platform platform) {
        this.platform = platform;
        
        backgroundRenderPrecedence = 0.0f;
        foregroundRenderPrecedence = 0.0f;
        contactPrecedence = 0.0f;
    }
    
    public void update(float delta, Vector2 platformPosition) {
    }
    
    public void renderBackground(SpriteBatch batch, Vector2 platformPosition, Color color) {
    }
    
    public void renderForeground(SpriteBatch batch, Vector2 platformPosition, Color color) {
    }
    
    public void updatePlatformModifier(PlatformModifier modifier) {
    }
    
    public boolean isContact(float relativeCollisionPointX) {
        return false;
    }
    
    // updates collision effects (like jump boost, character burning or triggering reposition)
    // and triggers some graphic effect (like the discharge on jump boost)
    public void applyContact(CollisionEffects collisionEffects) {
    }
    
    // applies rise section effects, such as reposition (which has no effect on features, only on movement) and
    // visible on jump which can be triggered by other platforms
    public void applyEffects(CollisionEffects collisionEffects) {
    }
    
    public final float getBackgroundRenderPrecedence() {
        return backgroundRenderPrecedence;
    }
    
    public final float getForegroundRenderPrecedence() {
        return foregroundRenderPrecedence;
    }
    
    public final float getContactPrecedence() {
        return contactPrecedence;
    }
    
    public boolean isSpecialCollisionFeature() {
        return false;
    }
    
    public boolean isSpecialCollision(Rectangle characterRect, Vector2 characterSpeed) {
        return false;
    }
    
    public void applySpecialCollision(CollisionEffects collisionEffects) {
    }
}
