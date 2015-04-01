package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class AttachedSpikesPlatformFeature extends PlatformFeatureBase {
    
    private static final float BOTTOM_WIDTH = 1.8f;
    private static final float BOTTOM_HEIGHT = 0.75f;
    private static final float BOTTOM_OFFSET_X = (Platform.WIDTH - BOTTOM_WIDTH) / 2.0f;
    private static final float BOTTOM_OFFSET_Y = -BOTTOM_HEIGHT + 0.05f;
    private static final float BOTTOM_COLLISION_PADDING_X_MULTIPLIER = 0.1f;
    private static final float BOTTOM_COLLISION_PADDING_Y_MULTIPLIER = 0.2f;
    private static final float BOTTOM_COLLISION_OFFSET_X = BOTTOM_OFFSET_X + BOTTOM_COLLISION_PADDING_X_MULTIPLIER * BOTTOM_WIDTH;
    private static final float BOTTOM_COLLISION_OFFSET_Y = BOTTOM_OFFSET_Y + BOTTOM_COLLISION_PADDING_Y_MULTIPLIER * BOTTOM_HEIGHT;
    private static final float BOTTOM_COLLISION_WIDTH = BOTTOM_WIDTH * (1.0f - BOTTOM_COLLISION_PADDING_X_MULTIPLIER * 2.0f);
    private static final float BOTTOM_COLLISION_HEIGHT = BOTTOM_HEIGHT * (1.0f - BOTTOM_COLLISION_PADDING_Y_MULTIPLIER);
    
    private static final float LEFT_WIDTH = 0.75f;
    private static final float LEFT_HEIGHT = 2.3f;
    private static final float LEFT_OFFSET_X = -0.5f;
    private static final float LEFT_OFFSET_Y = Platform.HEIGHT - 0.3f;
    private static final float LEFT_COLLISION_OFFSET_X = LEFT_OFFSET_X + 0.0f;
    private static final float LEFT_COLLISION_OFFSET_Y = LEFT_OFFSET_Y + 0.3f;
    private static final float LEFT_COLLISION_WIDTH = 0.5f;
    private static final float LEFT_COLLISION_HEIGHT = 2.0f;
    
    private static final float RIGHT_WIDTH = 0.75f;
    private static final float RIGHT_HEIGHT = 2.3f;
    private static final float RIGHT_OFFSET_X = Platform.WIDTH - 0.25f;
    private static final float RIGHT_OFFSET_Y = Platform.HEIGHT - 0.3f;
    private static final float RIGHT_COLLISION_OFFSET_X = RIGHT_OFFSET_X + 0.25f;
    private static final float RIGHT_COLLISION_OFFSET_Y = RIGHT_OFFSET_Y + 0.3f;
    private static final float RIGHT_COLLISION_WIDTH = 0.5f;
    private static final float RIGHT_COLLISION_HEIGHT = 2.0f;
    
    private final Sprite bottomSprite;
    private final Sprite leftSprite;
    private final Sprite rightSprite;
    
    private final boolean isBottom;
    private final boolean isLeft;
    private final boolean isRight;
    
    private final Rectangle bottomCollisionRect;
    private final Rectangle leftCollisionRect;
    private final Rectangle rightCollisionRect;
    
    public AttachedSpikesPlatformFeature(Platform platform, boolean isBottom, boolean isLeft, boolean isRight, AssetManager assetManager) {
        super(platform);
        
        this.isBottom = isBottom;
        this.isLeft = isLeft;
        this.isRight = isRight;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        
        if (isBottom) {
            bottomSprite = atlas.createSprite(ResourceNames.PLATFORM_BOTTOM_SPIKES_IMAGE_NAME);
            bottomSprite.setSize(BOTTOM_WIDTH, BOTTOM_HEIGHT);
            bottomCollisionRect = new Rectangle(0.0f, 0.0f, BOTTOM_COLLISION_WIDTH, BOTTOM_COLLISION_HEIGHT);
        } else {
            bottomSprite = null;
            bottomCollisionRect = null;
        }
        
        if (isLeft) {
            leftSprite = atlas.createSprite(ResourceNames.PLATFORM_LEFT_SPIKES_IMAGE_NAME);
            leftSprite.setSize(LEFT_WIDTH, LEFT_HEIGHT);
            leftCollisionRect = new Rectangle(0.0f, 0.0f, LEFT_COLLISION_WIDTH, LEFT_COLLISION_HEIGHT);
        } else {
            leftSprite = null;
            leftCollisionRect = null;
        }
        
        if (isRight) {
            rightSprite = atlas.createSprite(ResourceNames.PLATFORM_RIGHT_SPIKES_IMAGE_NAME);
            rightSprite.setSize(RIGHT_WIDTH, RIGHT_HEIGHT);
            rightCollisionRect = new Rectangle(0.0f, 0.0f, RIGHT_COLLISION_WIDTH, RIGHT_COLLISION_HEIGHT);
        } else {
            rightSprite = null;
            rightCollisionRect = null;
        }
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (isBottom) {
            bottomCollisionRect.x = platformPosition.x + BOTTOM_COLLISION_OFFSET_X;
            bottomCollisionRect.y = platformPosition.y + BOTTOM_COLLISION_OFFSET_Y;
        }
        if (isLeft) {
            leftCollisionRect.x = platformPosition.x + LEFT_COLLISION_OFFSET_X;
            leftCollisionRect.y = platformPosition.y + LEFT_COLLISION_OFFSET_Y;
        }
        if (isRight) {
            rightCollisionRect.x = platformPosition.x + RIGHT_COLLISION_OFFSET_X;
            rightCollisionRect.y = platformPosition.y + RIGHT_COLLISION_OFFSET_Y;
        }
    }
    
    @Override
    public void renderBackground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        if (isBottom) {
            bottomSprite.setPosition(platformPosition.x + BOTTOM_OFFSET_X, platformPosition.y + BOTTOM_OFFSET_Y);
            bottomSprite.draw(batch);
        }
        if (isLeft) {
            leftSprite.setPosition(platformPosition.x + LEFT_OFFSET_X, platformPosition.y + LEFT_OFFSET_Y);
            leftSprite.draw(batch);
        }
        if (isRight) {
            rightSprite.setPosition(platformPosition.x + RIGHT_OFFSET_X, platformPosition.y + RIGHT_OFFSET_Y);
            rightSprite.draw(batch);
        }
    }
    
    @Override
    public boolean isSpecialCollisionFeature() {
        return true;
    }
    
    @Override
    public boolean isSpecialCollision(Rectangle characterRect, Vector2 characterSpeed) {
        if (isBottom && characterSpeed.y > 0.0f && Intersector.overlaps(characterRect, bottomCollisionRect)) {
            return true;
        }
        if (isLeft && Intersector.overlaps(characterRect, leftCollisionRect)) {
            return true;
        }
        if (isRight && Intersector.overlaps(characterRect, rightCollisionRect)) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void applySpecialCollision(CollisionEffects collisionEffects) {
        collisionEffects.set(CollisionEffects.IMPALE_ATTACHED_SPIKES);
    }
}
