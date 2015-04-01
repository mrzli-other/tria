package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.RevealOnJumpCollisionEffectData;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;
import com.symbolplay.tria.resources.ResourceNames;

public final class RevealOnJumpPlatformFeature extends PlatformFeatureBase {
    
    private static final float WIDTH = 0.3f;
    private static final float HEIGHT = 0.3f;
    private static final float OFFSET_X = (Platform.WIDTH - WIDTH) / 2.0f;
    private static final float OFFSET_Y = (Platform.HEIGHT - HEIGHT) / 2.0f;
    
    private static final float FADE_IN_DURATION = 0.4f;
    
    private final Sprite foregroundSprite;
    
    private boolean isVisible;
    private final int[] revealIds;
    
    private float fadeInElapsed;
    
    public RevealOnJumpPlatformFeature(Platform platform, boolean isInitiallyVisible, int[] revealIds, AssetManager assetManager) {
        super(platform);
        
        this.isVisible = isInitiallyVisible;
        this.revealIds = revealIds;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        foregroundSprite = atlas.createSprite(ResourceNames.PLATFORM_FEATURE_REVEAL_ON_JUMP_FOREGROUND_IMAGE_NAME);
        foregroundSprite.setSize(WIDTH, HEIGHT);
        
        foregroundRenderPrecedence = 2.0f;
        
        fadeInElapsed = 0.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (isVisible) {
            if (fadeInElapsed < FADE_IN_DURATION) {
                fadeInElapsed += delta;
            } else {
                fadeInElapsed = FADE_IN_DURATION;
            }
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
        if (!isVisible) {
            modifier.isVisible = false;
            modifier.isCollidable = false;
        }
        
        if (isVisible) {
            float alpha = MathUtils.clamp(fadeInElapsed / FADE_IN_DURATION, 0.0f, 1.0f);
            modifier.platformColor.a *= alpha;
            modifier.featuresColor.a *= alpha;
        }
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        return true;
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.setRevealOnJumpEffect(revealIds);
    }
    
    @Override
    public void applyEffects(CollisionEffects collisionEffects) {
        if (isSetVisible(collisionEffects)) {
            isVisible = true;
        }
    }
    
    private boolean isSetVisible(CollisionEffects collisionEffects) {
        if (!collisionEffects.isEffectActive(CollisionEffects.REVEAL_ON_JUMP)) {
            return false;
        }
        
        RevealOnJumpCollisionEffectData revealOnJumpCollisionEffectData = (RevealOnJumpCollisionEffectData) collisionEffects.getEffectData(CollisionEffects.REVEAL_ON_JUMP);
        
        return GameUtils.contains(revealOnJumpCollisionEffectData.revealOnJumpIds, platform.getPlatformId());
    }
}
