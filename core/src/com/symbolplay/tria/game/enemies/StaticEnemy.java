package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.resources.ResourceNames;

public final class StaticEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.2f;
    public static final float HEIGHT = 1.2f;
    
    private static final float MOVEMENT_RADIUS = 0.3f;
    private static final float SPEED = 1.5f;
    
    private static final float MIN_DESTINATION_DEGREES_CHANGE = 0.0f;
    private static final float MAX_DESTINATION_DEGREES_CHANGE = 360.0f - MIN_DESTINATION_DEGREES_CHANGE;
    
    private final Vector2 startOffset;
    private final Vector2 destinationOffset;
    private float destinationDegrees;
    private float totalDistanceToDestination;
    private float travelledToDestination;
    
    public StaticEnemy(float x, float y, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_STATIC_RESOURCE_NAMES.getRandomResourceName(), EnemyCollisionBase.CIRCLE_BOUNDING_GEOMETRY_TYPE, 0.0f, assetManager);
        
        startOffset = new Vector2();
        destinationOffset = new Vector2();
        destinationDegrees = 0.0f;
        setDestination();
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = SPEED * delta;
        travelledToDestination += travelled;
        
        float t = MathUtils.clamp(travelledToDestination / totalDistanceToDestination, 0.0f, 1.0f);
        t = Interpolation.sine.apply(t);
        float oneMinusT = 1.0f - t;
        
        offset.x = startOffset.x * oneMinusT + destinationOffset.x * t;
        offset.y = startOffset.y * oneMinusT + destinationOffset.y * t;
        
        if (t >= 1.0f) {
            calculateNewDestinationDegrees();
            setDestination();
        }
    }
    
    private void calculateNewDestinationDegrees() {
        float destinationDegreesChange = MathUtils.random(MIN_DESTINATION_DEGREES_CHANGE, MAX_DESTINATION_DEGREES_CHANGE);
        destinationDegrees = GameUtils.getPositiveModulus(destinationDegrees + destinationDegreesChange, 360.0f);
    }
    
    private void setDestination() {
        startOffset.set(destinationOffset);
        
        destinationOffset.x = MathUtils.cosDeg(destinationDegrees) * MOVEMENT_RADIUS;
        destinationOffset.y = MathUtils.sinDeg(destinationDegrees) * MOVEMENT_RADIUS;
        
        totalDistanceToDestination = startOffset.dst(destinationOffset);
        travelledToDestination = 0.0f;
    }
    
    @Override
    public int getType() {
        return STATIC_TYPE;
    }
}