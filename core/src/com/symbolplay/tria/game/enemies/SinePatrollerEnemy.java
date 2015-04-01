package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.game.enemies.movement.BackForthMovement;
import com.symbolplay.tria.resources.ResourceNames;

public final class SinePatrollerEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.25f;
    public static final float HEIGHT = 1.5f;
    
    private static final float SIN_AMPLITUDE = 0.2f;
    private static final float SIN_PERIOD = 0.5f;
    
    private static final float COLLISION_PADDING = 0.1f;
    
    private final float speed;
    private final BackForthMovement backForthMovement;
    
    private float sinTime;
    
    public SinePatrollerEnemy(float x, float y, float range, float speed, float initialOffset, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_SINE_PATROLLER_IMAGE_NAME, EnemyCollisionBase.RECT_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
        
        this.speed = speed;
        
        backForthMovement = new BackForthMovement(range);
        backForthMovement.update(initialOffset);
        
        sinTime = 0.0f;
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        backForthMovement.update(travelled);
        
        offset.x = backForthMovement.getOffset();
        
        sinTime = (sinTime + delta) % SIN_PERIOD;
        offset.y = MathUtils.sinDeg(sinTime / SIN_PERIOD * 360.0f) * SIN_AMPLITUDE;
    }
    
    @Override
    public int getType() {
        return SINE_PATROLLER_TYPE;
    }
}
