package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;
import com.symbolplay.tria.resources.ResourceNames;

public final class VisibleOnJumpPlatformFeature extends PlatformFeatureBase {
    
    private static final float WIDTH = 0.225f;
    private static final float HEIGHT = 0.35f;
    private static final float OFFSET_X = (Platform.WIDTH - WIDTH) / 2.0f;
    private static final float OFFSET_Y = (Platform.HEIGHT - HEIGHT) / 2.0f;
    
    private static final float VISIBILITY_DURATION = 0.6f;
    
    private final Sprite foregroundSprite;
    
    private float visibilityCountdown;
    
    public VisibleOnJumpPlatformFeature(Platform platform, AssetManager assetManager) {
        super(platform);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        foregroundSprite = atlas.createSprite(ResourceNames.PLATFORM_FEATURE_VISIBLE_ON_JUMP_FOREGROUND_IMAGE_NAME);
        foregroundSprite.setSize(WIDTH, HEIGHT);
        
        foregroundRenderPrecedence = 2.0f;
        
        visibilityCountdown = 0.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (visibilityCountdown > 0.0f) {
            visibilityCountdown -= delta;
        }
    }
    
    @Override
    public void renderForeground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        foregroundSprite.setPosition(platformPosition.x + OFFSET_X, platformPosition.y + OFFSET_Y);
        foregroundSprite.setColor(color);
        foregroundSprite.draw(batch);
    }
    
    @Override
    public void updatePlatformModifier(PlatformModifier modifier) {
        if (visibilityCountdown <= 0.0f) {
            modifier.isVisible = false;
        }
        float alpha = visibilityCountdown / VISIBILITY_DURATION;
        modifier.platformColor.a *= alpha;
        modifier.featuresColor.a *= alpha;
    }
    
    @Override
    public void applyEffects(CollisionEffects collisionEffects) {
        if (collisionEffects.isEffectActive(CollisionEffects.VISIBLE_ON_JUMP)) {
            visibilityCountdown = VISIBILITY_DURATION;
        }
    }
}
