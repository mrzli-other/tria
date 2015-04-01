package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.game.enemies.movement.LoopMovement;
import com.symbolplay.tria.resources.ResourceNames;

public final class OrbiterEnemy extends EnemyBase {
    
    public static final float WIDTH = 1.5f;
    public static final float HEIGHT = 1.5f;
    
    private static final float COLLISION_PADDING = 0.0f;
    
    private final float speed;
    
    private final float radius;
    private final Vector2[] rotationCenterOffsets;
    
    private final Vector2 spriteCenterOffset;
    
    private final LoopMovement loopMovement;
    
    public OrbiterEnemy(float x, float y, float speed, float initialOffset, AssetManager assetManager) {
        super(x, y, ResourceNames.ENEMY_ORBITER_IMAGE_NAME, EnemyCollisionBase.CIRCLE_BOUNDING_GEOMETRY_TYPE, COLLISION_PADDING, assetManager);
        
        this.speed = speed;
        
        float range = GameContainer.GAME_AREA_WIDTH - size.x;
        radius = range / 4.0f;
        
        spriteCenterOffset = new Vector2(size.x / 2.0f, size.y / 2.0f);
        
        rotationCenterOffsets = new Vector2[2];
        rotationCenterOffsets[0] = new Vector2(spriteCenterOffset.x + radius, spriteCenterOffset.y);
        rotationCenterOffsets[1] = new Vector2(rotationCenterOffsets[0].x + radius * 2.0f, rotationCenterOffsets[0].y);
        
        float trajectoryLength = range * MathUtils.PI;
        loopMovement = new LoopMovement(trajectoryLength);
        loopMovement.update(initialOffset);
        
        setOffset();
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        loopMovement.update(travelled);
        
        setOffset();
    }
    
    private void setOffset() {
        float trajectoryOffset = loopMovement.getOffset();
        
        float degreeOffset = trajectoryOffset / radius * MathUtils.radDeg;
        float degrees = degreeOffset <= 360.0f ? degreeOffset : 540.0f - degreeOffset;
        Vector2 rotationCenter = degreeOffset <= 360.0f ? rotationCenterOffsets[0] : rotationCenterOffsets[1];
        
        offset.x = rotationCenter.x + MathUtils.cosDeg(degrees) * radius - spriteCenterOffset.x;
        offset.y = rotationCenter.y + MathUtils.sinDeg(degrees) * radius - spriteCenterOffset.y;
    }
    
    @Override
    public int getType() {
        return ORBITER_TYPE;
    }
}
