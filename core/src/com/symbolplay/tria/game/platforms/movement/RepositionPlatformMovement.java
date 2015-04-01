package com.symbolplay.tria.game.platforms.movement;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class RepositionPlatformMovement extends PlatformMovementBase {
    
    private static final float WIDTH = 1.9f;
    private static final float HEIGHT = 0.4f;
    private static final float OFFSET_X = (Platform.WIDTH - WIDTH) / 2.0f;
    private static final float OFFSET_Y = (Platform.HEIGHT - HEIGHT) / 2.0f;
    
    private static final float REPOSITION_TIME = 0.2f;
    
    private final Sprite sprite;
    
    private final float minRepositionAmount;
    private final float maxRepositionAmount;
    
    private final float leftLimit;
    private final float rightLimit;
    
    private float repositionElapsed;
    private float startPosition;
    private float targetPosition;
    
    public RepositionPlatformMovement(Vector2 initialPosition, float range, float minRepositionAmount, float maxRepositionAmount, float initialOffset, AssetManager assetManager) {
        super(initialPosition);
        
        this.minRepositionAmount = minRepositionAmount;
        this.maxRepositionAmount = maxRepositionAmount;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        sprite = atlas.createSprite(ResourceNames.PLATFORM_MOVEMENT_REPOSITION_IMAGE_NAME);
        sprite.setSize(WIDTH, HEIGHT);
        
        leftLimit = initialPosition.x;
        rightLimit = initialPosition.x + range;
        
        if (initialOffset <= range) {
            changePosition(initialOffset);
        } else {
            changePosition(range - initialOffset % range);
        }
        
        repositionElapsed = REPOSITION_TIME;
        startPosition = position.x;
        targetPosition = position.x;
    }
    
    @Override
    protected void updateImpl(float delta) {
        if (repositionElapsed >= REPOSITION_TIME) {
            return;
        }
        
        repositionElapsed += delta;
        float t = MathUtils.clamp(repositionElapsed / REPOSITION_TIME, 0.0f, 1.0f);
        t = Interpolation.sine.apply(t);
        setPosition(startPosition + t * (targetPosition - startPosition));
    }
    
    @Override
    public void render(SpriteBatch batch, float alpha) {
        sprite.setPosition(position.x + OFFSET_X, position.y + OFFSET_Y);
        sprite.setAlpha(alpha);
        sprite.draw(batch);
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.set(CollisionEffects.REPOSITION_PLATFORMS);
    }
    
    @Override
    public void applyEffects(CollisionEffects collisionEffects) {
        if (collisionEffects.isEffectActive(CollisionEffects.REPOSITION_PLATFORMS)) {
            reposition();
        }
    }
    
    private void changePosition(float change) {
        position.x += change;
        position.x = MathUtils.clamp(position.x, leftLimit, rightLimit);
    }
    
    private void setPosition(float position) {
        this.position.x = MathUtils.clamp(position, leftLimit, rightLimit);
    }
    
    private void reposition() {
        repositionElapsed = 0.0f;
        startPosition = position.x;
        targetPosition = GameUtils.getRandomPosition(position.x, minRepositionAmount, maxRepositionAmount, leftLimit, rightLimit);
    }
    
    @Override
    public boolean hasVerticalMovement() {
        return false;
    }
}
