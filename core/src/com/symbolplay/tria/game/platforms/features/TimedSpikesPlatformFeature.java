package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class TimedSpikesPlatformFeature extends PlatformFeatureBase {
    
    private static final float BACKGROUND_WIDTH = 2.0f;
    private static final float BACKGROUND_HEIGHT = 0.425f;
    private static final float BACKGROUND_OFFSET_X = (Platform.WIDTH - BACKGROUND_WIDTH) / 2.0f;
    private static final float BACKGROUND_OFFSET_Y = Platform.HEIGHT - 0.025f;
    
    private static final float FOREGROUND_WIDTH = 0.35f;
    private static final float FOREGROUND_HEIGHT = 0.35f;
    private static final float FOREGROUND_OFFSET_X = (Platform.WIDTH - FOREGROUND_WIDTH) / 2.0f;
    private static final float FOREGROUND_OFFSET_Y = (Platform.HEIGHT - FOREGROUND_HEIGHT) / 2.0f;
    
    private static final float TRANSITION_DURATION = 0.15f;
    private static final float MAX_FRAME_DURATION = 1.0f;
    
    private final Sprite backgroundSprite;
    private final Sprite foregroundSprite;
    
    private final Array<AtlasRegion> foregroundInactiveRegions;
    private final Array<AtlasRegion> foregroundActiveRegions;
    
    private final float inactiveDuration;
    private final float cycleDuration;
    
    private final float activeFrameDuration;
    private final float inactiveFrameDuration;
    
    private float cycleTimeElapsed;
    private float scaleY;
    
    public TimedSpikesPlatformFeature(Platform platform, float cycleOffset, float activeDuration, float inactiveDuration, AssetManager assetManager) {
        super(platform);
        
        this.inactiveDuration = inactiveDuration;
        cycleDuration = inactiveDuration + activeDuration;
        
        changeCycleTime(-cycleOffset);
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        foregroundInactiveRegions = atlas.findRegions(ResourceNames.PLATFORM_FEATURE_TIMED_SPIKES_INACTIVE_COUNTDOWN_IMAGE_NAME);
        foregroundActiveRegions = atlas.findRegions(ResourceNames.PLATFORM_FEATURE_TIMED_SPIKES_ACTIVE_COUNTDOWN_IMAGE_NAME);
        
        backgroundSprite = atlas.createSprite(ResourceNames.PLATFORM_SPIKES_IMAGE_NAME);
        backgroundSprite.setSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        backgroundSprite.setOrigin(0.0f, 0.0f);
        
        foregroundSprite = new Sprite();
        foregroundSprite.setSize(FOREGROUND_WIDTH, FOREGROUND_HEIGHT);
        
        activeFrameDuration = Math.min(activeDuration / foregroundActiveRegions.size, MAX_FRAME_DURATION);
        inactiveFrameDuration = Math.min(inactiveDuration / foregroundInactiveRegions.size, MAX_FRAME_DURATION);
        
        backgroundRenderPrecedence = 2.0f;
        foregroundRenderPrecedence = 2.0f;
        contactPrecedence = 2.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        changeCycleTime(delta);
        scaleY = getScaleY();
    }
    
    @Override
    public void renderBackground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        backgroundSprite.setPosition(platformPosition.x + BACKGROUND_OFFSET_X, platformPosition.y + BACKGROUND_OFFSET_Y);
        backgroundSprite.setScale(1.0f, scaleY);
        backgroundSprite.setColor(color);
        backgroundSprite.draw(batch);
    }
    
    @Override
    public void renderForeground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        foregroundSprite.setRegion(getRegion());
        foregroundSprite.setPosition(platformPosition.x + FOREGROUND_OFFSET_X, platformPosition.y + FOREGROUND_OFFSET_Y);
        foregroundSprite.setColor(color);
        foregroundSprite.draw(batch);
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        return isSpikesActive();
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.set(CollisionEffects.IMPALE_SPIKES);
    }
    
    private boolean isSpikesActive() {
        return cycleTimeElapsed > inactiveDuration;
    }
    
    private void changeCycleTime(float change) {
        cycleTimeElapsed = GameUtils.getPositiveModulus(cycleTimeElapsed + change, cycleDuration);
    }
    
    private float getScaleY() {
        if (cycleTimeElapsed < TRANSITION_DURATION) {
            return MathUtils.clamp(1.0f - cycleTimeElapsed / TRANSITION_DURATION, 0.0f, 1.0f);
        } else if (cycleTimeElapsed < inactiveDuration) {
            return 0.0f;
        } else if (cycleTimeElapsed < inactiveDuration + TRANSITION_DURATION) {
            return MathUtils.clamp((cycleTimeElapsed - inactiveDuration) / TRANSITION_DURATION, 0.0f, 1.0f);
        } else {
            return 1.0f;
        }
    }
    
    private AtlasRegion getRegion() {
        if (cycleTimeElapsed < inactiveDuration) {
            float timeRemaining = inactiveDuration - cycleTimeElapsed;
            int framesRemaining = MathUtils.floorPositive(timeRemaining / inactiveFrameDuration);
            int frame = MathUtils.clamp(foregroundInactiveRegions.size - 1 - framesRemaining, 0, foregroundInactiveRegions.size - 1);
            return foregroundInactiveRegions.get(frame);
        } else {
            float timeRemaining = cycleDuration - cycleTimeElapsed;
            int framesRemaining = MathUtils.floorPositive(timeRemaining / activeFrameDuration);
            int frame = MathUtils.clamp(foregroundActiveRegions.size - 1 - framesRemaining, 0, foregroundActiveRegions.size - 1);
            return foregroundActiveRegions.get(frame);
        }
    }
}
