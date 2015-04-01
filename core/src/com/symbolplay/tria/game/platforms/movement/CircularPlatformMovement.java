package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.platforms.PlatformModifier;

public final class CircularPlatformMovement extends PlatformMovementBase {
    
    private static final Color COLOR;
    
    private final float radius;
    private final boolean isCcw;
    
    private final float angleSpeed;
    private float angle;
    private final Vector2 rotationCenter;
    
    static {
        COLOR = new Color(0.8f, 0.36f, 0.36f, 1.0f);
    }
    
    public CircularPlatformMovement(Vector2 initialPosition, float radius, float speed, float initialDegrees, boolean isCcw, AssetManager assetManager) {
        super(initialPosition);
        
        this.radius = radius;
        this.isCcw = isCcw;
        
        angleSpeed = speed / radius * MathUtils.radDeg;
        angle = 0.0f;
        rotationCenter = new Vector2(
                initialPosition.x + PLATFORM_CENTER_OFFSET.x + radius,
                initialPosition.y + PLATFORM_CENTER_OFFSET.y);
        
        changePosition(initialDegrees);
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelledAngle = angleSpeed * delta;
        if (!isCcw) {
            travelledAngle = -travelledAngle;
        }
        
        changePosition(travelledAngle);
    }
    
    @Override
    public void updatePlatformModifier(PlatformModifier modifier) {
        GameUtils.setRgb(modifier.platformColor, COLOR);
        GameUtils.setRgb(modifier.featuresColor, COLOR);
    }
    
    private void changePosition(float change) {
        angle += change;
        angle = GameUtils.getPositiveModulus(angle, 360.0f);
        
        position.x = rotationCenter.x + MathUtils.cosDeg(angle) * radius - PLATFORM_CENTER_OFFSET.x;
        position.y = rotationCenter.y + MathUtils.sinDeg(angle) * radius - PLATFORM_CENTER_OFFSET.y;
    }
    
    @Override
    public boolean hasVerticalMovement() {
        return true;
    }
}
