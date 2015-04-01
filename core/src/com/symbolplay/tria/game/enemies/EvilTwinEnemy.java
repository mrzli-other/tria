package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.game.enemies.movement.BackForthMovement;
import com.symbolplay.tria.resources.ResourceNames;

public final class EvilTwinEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.5f;
    
    private static final float COLLISION_PADDING = 0.05f;
    
    private final float speed;
    
    private final float[] radiuses;
    private final float[] trajectorySectionCumulativeRanges;
    private final Vector2[] rotationCenterOffsets;
    
    private final Vector2 spriteCenterOffset;
    
    private final BackForthMovement backForthMovement;
    
    public EvilTwinEnemy(float x, float y, float[] ranges, float speed, float initialOffset, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_EVIL_TWIN_IMAGE_NAME, EnemyCollisionBase.RECT_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
        
        this.speed = speed;
        
        spriteCenterOffset = new Vector2(size.x / 2.0f, size.y / 2.0f);
        
        int numCurves = ranges.length;
        float singleDirectionRange = 0.0f;
        radiuses = new float[numCurves];
        trajectorySectionCumulativeRanges = new float[numCurves];
        rotationCenterOffsets = new Vector2[numCurves];
        for (int i = 0; i < numCurves; i++) {
            radiuses[i] = ranges[i] / 2.0f;
            trajectorySectionCumulativeRanges[i] = radiuses[i] * MathUtils.PI + (i > 0 ? trajectorySectionCumulativeRanges[i - 1] : 0.0f);
            rotationCenterOffsets[i] = new Vector2(spriteCenterOffset.x + singleDirectionRange + radiuses[i], spriteCenterOffset.y);
            
            singleDirectionRange += ranges[i];
        }
        
        float singleDirectionTrajectoryLength = singleDirectionRange * MathUtils.PI / 2.0f;
        
        backForthMovement = new BackForthMovement(singleDirectionTrajectoryLength);
        backForthMovement.update(initialOffset);
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        backForthMovement.update(travelled);
        setSpriteDirection(backForthMovement.isInitialDirection());
        
        setOffset();
    }
    
    private void setOffset() {
        float trajectoryOffset = backForthMovement.getOffset();
        int sectionIndex = getSectionIndex(trajectoryOffset);
        
        Vector2 rotationCenterOffset = rotationCenterOffsets[sectionIndex];
        float radius = radiuses[sectionIndex];
        
        float trajectorySectionOffset = trajectoryOffset - (sectionIndex > 0 ? trajectorySectionCumulativeRanges[sectionIndex - 1] : 0.0f);
        float degreeOffset = trajectorySectionOffset / radius * MathUtils.radDeg;
        float degrees = 180.0f - degreeOffset;
        
        offset.x = rotationCenterOffset.x + MathUtils.cosDeg(degrees) * radius - spriteCenterOffset.x;
        offset.y = rotationCenterOffset.y + MathUtils.sinDeg(degrees) * radius - spriteCenterOffset.y;
    }
    
    private int getSectionIndex(float trajectoryOffset) {
        for (int i = 0; i < trajectorySectionCumulativeRanges.length; i++) {
            if (trajectorySectionCumulativeRanges[i] >= trajectoryOffset) {
                return i;
            }
        }
        
        return trajectorySectionCumulativeRanges.length - 1;
    }
    
    @Override
    public int getType() {
        return EVIL_TWIN_TYPE;
    }
}
