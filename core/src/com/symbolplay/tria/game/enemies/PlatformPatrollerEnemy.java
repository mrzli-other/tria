package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.game.enemies.movement.BackForthMovement;
import com.symbolplay.tria.resources.ResourceNames;

public final class PlatformPatrollerEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.25f;
    public static final float HEIGHT = 1.5f;
    
    private static final float COLLISION_PADDING = 0.0f;
    
    private final float speed;
    private final BackForthMovement backForthMovement;
    
    public PlatformPatrollerEnemy(float x, float y, float range, float speed, float initialOffset, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_PLATFORM_PATROLLER_IMAGE_NAME, EnemyCollisionBase.RECT_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
        
        this.speed = speed;
        
        backForthMovement = new BackForthMovement(range);
        backForthMovement.update(initialOffset);
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        backForthMovement.update(travelled);
        
        offset.x = backForthMovement.getOffset();
    }
    
    @Override
    public int getType() {
        return PLATFORM_PATROLLER_TYPE;
    }
}
