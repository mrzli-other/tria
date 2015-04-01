package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;
import com.symbolplay.tria.resources.ResourceNames;

public final class SpikesPlatformFeature extends PlatformFeatureBase {
    
    private static final float OFFSET_Y = Platform.HEIGHT - 1.0f * GameContainer.PIXEL_TO_METER;
    
    private static final float DEFAULT_COLOR_VALUE = 0.5f;
    private static final Color COLOR;
    
    private final Sprite sprite;
    
    static {
        COLOR = new Color(DEFAULT_COLOR_VALUE, DEFAULT_COLOR_VALUE, DEFAULT_COLOR_VALUE, 1.0f);
    }
    
    public SpikesPlatformFeature(Platform platform, AssetManager assetManager) {
        super(platform);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        
        sprite = atlas.createSprite(ResourceNames.PLATFORM_SPIKES_IMAGE_NAME);
        GameUtils.multiplySpriteSize(sprite, GameContainer.PIXEL_TO_METER);
        sprite.setColor(COLOR);
        
        backgroundRenderPrecedence = 2.0f;
        contactPrecedence = 2.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
    }
    
    @Override
    public void renderBackground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        sprite.setPosition(platformPosition.x, platformPosition.y + OFFSET_Y);
        sprite.setColor(color);
        sprite.draw(batch);
    }
    
    @Override
    public void updatePlatformModifier(PlatformModifier modifier) {
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        return true;
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.set(CollisionEffects.IMPALE_SPIKES);
    }
}
