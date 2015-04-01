package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.platforms.PlatformModifier;
import com.symbolplay.tria.resources.ResourceNames;

public final class CrumblePlatformFeature extends PlatformFeatureBase {
    
    private static final float FADE_OUT_DURATION = 0.5f;
    
    private final AtlasRegion crumbleAtlasRegion;
    
    private boolean isCrumbling;
    private boolean isActive;
    
    private float fadeOutElapsed;
    
    public CrumblePlatformFeature(Platform platform, AssetManager assetManager) {
        super(platform);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        crumbleAtlasRegion = atlas.findRegion(ResourceNames.PLATFORM_CRUMBLE_IMAGE_NAME);
        
        fadeOutElapsed = 0.0f;
        
        isCrumbling = false;
        isActive = true;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (isCrumbling && isActive) {
            fadeOutElapsed += delta;
            if (fadeOutElapsed >= FADE_OUT_DURATION) {
                isActive = false;
            }
        }
    }
    
    @Override
    public void updatePlatformModifier(PlatformModifier modifier) {
        if (!isActive) {
            modifier.isVisible = false;
            modifier.isActive = false;
        }
        
        if (isCrumbling) {
            modifier.isMoving = false;
            modifier.isCollidable = false;
            
            float alpha = MathUtils.clamp(1.0f - fadeOutElapsed / FADE_OUT_DURATION, 0.0f, 1.0f);
            modifier.platformColor.a *= alpha;
            modifier.featuresColor.a *= alpha;
        }
        
        modifier.textureRegion = crumbleAtlasRegion;
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        return true;
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        isCrumbling = true;
    }
}
