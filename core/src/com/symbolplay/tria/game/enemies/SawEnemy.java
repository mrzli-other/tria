package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.resources.ResourceNames;

public final class SawEnemy extends EnemyBase {
    
    public static final float WIDTH = 2.4f;
    public static final float HEIGHT = 2.4f;
    
    private static final float ROTATION_SPEED = 180.0f;
    private static final float COLLISION_PADDING = 0.125f;
    
    public SawEnemy(float x, float y, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_SAW_IMAGE_NAME, EnemyCollisionBase.CIRCLE_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
    }
    
    @Override
    protected void updateImpl(float delta) {
        rotateSprite(-ROTATION_SPEED * delta);
    }
    
    @Override
    public int getType() {
        return SAW_TYPE;
    }
}
