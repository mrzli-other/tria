package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class HorizontalPlatformMovement extends PlatformMovementBase {
    
    private static final float WIDTH = 1.9f;
    private static final float HEIGHT = 0.4f;
    private static final float OFFSET_X = (Platform.WIDTH - WIDTH) / 2.0f;
    private static final float OFFSET_Y = (Platform.HEIGHT - HEIGHT) / 2.0f;
    
    private final Sprite sprite;
    
    private final float speed;
    
    private final float leftLimit;
    private final float rightLimit;
    private boolean isRightMovement;
    
    public HorizontalPlatformMovement(Vector2 initialPosition, float range, float speed, float initialOffset, AssetManager assetManager) {
        super(initialPosition);
        
        this.speed = speed;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        sprite = atlas.createSprite(ResourceNames.PLATFORM_MOVEMENT_HORIZONTAL_IMAGE_NAME);
        sprite.setSize(WIDTH, HEIGHT);
        
        leftLimit = initialPosition.x;
        rightLimit = initialPosition.x + range;
        
        if (initialOffset <= range) {
            changePosition(initialOffset);
            isRightMovement = true;
        } else {
            changePosition(range - initialOffset % range);
            isRightMovement = false;
        }
    }
    
    @Override
    protected void updateImpl(float delta) {
        float travelled = speed * delta;
        if (!isRightMovement) {
            travelled = -travelled;
        }
        
        changePosition(travelled);
    }
    
    @Override
    public void render(SpriteBatch batch, float alpha) {
        sprite.setPosition(position.x + OFFSET_X, position.y + OFFSET_Y);
        sprite.setAlpha(alpha);
        sprite.draw(batch);
    }
    
    private void changePosition(float change) {
        position.x += change;
        if (position.x <= leftLimit) {
            isRightMovement = true;
        } else if (position.x >= rightLimit) {
            isRightMovement = false;
        }
        
        position.x = MathUtils.clamp(position.x, leftLimit, rightLimit);
    }
    
    @Override
    public boolean hasVerticalMovement() {
        return false;
    }
}
