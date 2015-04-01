package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class ToggleSpikesPlatformFeature extends PlatformFeatureBase {
    
    private static final float BACKGROUND_WIDTH = 2.0f;
    private static final float BACKGROUND_HEIGHT = 0.425f;
    private static final float BACKGROUND_OFFSET_X = (Platform.WIDTH - BACKGROUND_WIDTH) / 2.0f;
    private static final float BACKGROUND_OFFSET_Y = Platform.HEIGHT - 0.025f;
    
    private static final float FOREGROUND_WIDTH = 0.35f;
    private static final float FOREGROUND_HEIGHT = 0.35f;
    private static final float FOREGROUND_OFFSET_X = (Platform.WIDTH - FOREGROUND_WIDTH) / 2.0f;
    private static final float FOREGROUND_OFFSET_Y = (Platform.HEIGHT - FOREGROUND_HEIGHT) / 2.0f;
    
    private static final float TRANSITION_DURATION = 0.1f;
    
    private final Sprite backgroundSprite;
    private final Sprite foregroundSpriteActive;
    private final Sprite foregroundSpriteInactive;
    
    private final int[] states;
    private int previousStateIndex;
    private int currentStateIndex;
    
    private float transitionElapsed;
    private float scaleY;
    
    public ToggleSpikesPlatformFeature(Platform platform, int[] states, AssetManager assetManager) {
        super(platform);
        
        this.states = states;
        previousStateIndex = 0;
        currentStateIndex = 0;
        transitionElapsed = 0.0f;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        
        backgroundSprite = atlas.createSprite(ResourceNames.PLATFORM_SPIKES_IMAGE_NAME);
        backgroundSprite.setSize(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        backgroundSprite.setOrigin(0.0f, 0.0f);
        
        foregroundSpriteActive = atlas.createSprite(ResourceNames.PLATFORM_FEATURE_TOGGLE_SPIKES_ACTIVE_FOREGROUND_IMAGE_NAME);
        foregroundSpriteActive.setSize(FOREGROUND_WIDTH, FOREGROUND_HEIGHT);
        foregroundSpriteInactive = atlas.createSprite(ResourceNames.PLATFORM_FEATURE_TOGGLE_SPIKES_INACTIVE_FOREGROUND_IMAGE_NAME);
        foregroundSpriteInactive.setSize(FOREGROUND_WIDTH, FOREGROUND_HEIGHT);
        
        backgroundRenderPrecedence = 2.0f;
        foregroundRenderPrecedence = 2.0f;
        contactPrecedence = 2.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (transitionElapsed < TRANSITION_DURATION) {
            transitionElapsed += delta;
            float t = MathUtils.clamp(transitionElapsed / TRANSITION_DURATION, 0.0f, 1.0f);
            if (isActive()) {
                scaleY = isPreviousActive() ? 1.0f : t;
            } else {
                scaleY = isPreviousActive() ? (1.0f - t) : 0.0f;
            }
        } else {
            scaleY = isActive() ? 1.0f : 0.0f;
        }
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
        Sprite foregroundSprite = isActive() ? foregroundSpriteActive : foregroundSpriteInactive;
        foregroundSprite.setPosition(platformPosition.x + FOREGROUND_OFFSET_X, platformPosition.y + FOREGROUND_OFFSET_Y);
        foregroundSprite.setColor(color);
        foregroundSprite.draw(batch);
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        return true;
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.set(CollisionEffects.TOGGLE_SPIKES);
        if (isActive()) {
            collisionEffects.set(CollisionEffects.IMPALE_SPIKES);
        }
    }
    
    @Override
    public void applyEffects(CollisionEffects collisionEffects) {
        if (collisionEffects.isEffectActive(CollisionEffects.TOGGLE_SPIKES)) {
            changeState();
        }
    }
    
    private void changeState() {
        previousStateIndex = currentStateIndex;
        currentStateIndex = (currentStateIndex + 1) % states.length;
        transitionElapsed = 0.0f;
    }
    
    private boolean isPreviousActive() {
        return states[previousStateIndex] != 0;
    }
    
    private boolean isActive() {
        return states[currentStateIndex] != 0;
    }
}
