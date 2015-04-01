package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.resources.ResourceNames;

public final class EasePatrollerEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.2f;
    public static final float HEIGHT = 1.2f;
    
    private static final float COLLISION_PADDING = 0.05f;
    
    private final float bothDirectionPeriod;
    private final float singleDiractionPeriod;
    private final float range;
    
    private float travelTime;
    
    public EasePatrollerEnemy(float x, float y, float range, float travelPeriod, float startTimeOffset, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_EASE_PATROLLER_IMAGE_NAME, EnemyCollisionBase.RECT_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
        
        bothDirectionPeriod = travelPeriod;
        travelTime = startTimeOffset;
        this.range = range;
        
        singleDiractionPeriod = bothDirectionPeriod / 2.0f;
    }
    
    @Override
    protected void updateImpl(float delta) {
        travelTime = (travelTime + delta) % bothDirectionPeriod;
        offset.x = range * getPositionFraction();
    }
    
    private float getPositionFraction() {
        float t = travelTime / singleDiractionPeriod;
        if (t > 1.0f) {
            t = 2.0f - t;
        }
        t = MathUtils.clamp(t, 0.0f, 1.0f);
        t = Interpolation.pow5.apply(t);
        
        return t;
    }
    
    @Override
    public int getType() {
        return EASE_PATROLLER_TYPE;
    }
}
