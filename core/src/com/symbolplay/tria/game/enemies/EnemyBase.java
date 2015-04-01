package com.symbolplay.tria.game.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionBase;
import com.symbolplay.tria.game.enemies.collisions.EnemyCollisionFactory;
import com.symbolplay.tria.game.rise.RiseObject;
import com.symbolplay.tria.resources.ResourceNames;

public abstract class EnemyBase implements RiseObject {
    
    public static final int SAW_TYPE = 0;
    public static final int SINE_PATROLLER_TYPE = 1;
    public static final int EASE_PATROLLER_TYPE = 2;
    public static final int ORBITER_TYPE = 3;
    public static final int EVIL_TWIN_TYPE = 4;
    public static final int PLATFORM_PATROLLER_TYPE = 5;
    public static final int STATIC_TYPE = 6;
    
    private final Sprite sprite;
    private boolean isInitialSpriteDirection;
    
    private final Vector2 initialPosition;
    protected final Vector2 size;
    protected final Vector2 offset;
    private final Vector2 position;
    
    private final EnemyCollisionBase enemyCollision;
    
    public EnemyBase(float x, float y, String imageName, int boundingGeometryType, float collisionPadding, AssetManager assetManager) {
        
        initialPosition = new Vector2(x, y);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.ENEMIES_ATLAS);
        sprite = atlas.createSprite(imageName);
        sprite.setPosition(initialPosition.x, initialPosition.y);
        GameUtils.multiplySpriteSize(sprite, GameContainer.PIXEL_TO_METER);
        sprite.setOriginCenter();
        isInitialSpriteDirection = true;
        
        size = new Vector2(sprite.getWidth(), sprite.getHeight());
        offset = new Vector2();
        position = new Vector2();
        
        enemyCollision = EnemyCollisionFactory.create(boundingGeometryType, sprite, collisionPadding);
    }
    
    public final void update(float delta) {
        updateImpl(delta);
        
        position.x = initialPosition.x + offset.x;
        position.y = initialPosition.y + offset.y;
        
        sprite.setPosition(position.x, position.y);
        
        enemyCollision.update(sprite);
    }
    
    protected abstract void updateImpl(float delta);
    
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
    
    public final boolean isCollision(Rectangle rect) {
        return enemyCollision.isCollision(rect);
    }
    
    protected void setSpriteDirection(boolean isInitialDirection) {
        if (isInitialSpriteDirection ^ isInitialDirection) {
            sprite.flip(true, false);
            isInitialSpriteDirection = !isInitialSpriteDirection;
        }
    }
    
    protected void rotateSprite(float degrees) {
        sprite.rotate(degrees);
    }
    
    protected void setSpriteRotation(float degrees) {
        sprite.setRotation(degrees);
    }
    
    public abstract int getType();
    
    @Override
    public int getGroupId() {
        return -1;
    }
    
    @Override
    public float getPositionY() {
        return position.y;
    }
    
    @Override
    public void offsetPositionY(float offset) {
        initialPosition.y += offset;
        position.y += offset;
    }
}
