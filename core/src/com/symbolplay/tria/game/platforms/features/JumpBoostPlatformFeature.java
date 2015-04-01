package com.symbolplay.tria.game.platforms.features;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.gamelibrary.util.ExceptionThrower;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class JumpBoostPlatformFeature extends PlatformFeatureBase {
    
    public static final int SIZE_SMALL = 0;
    public static final int SIZE_MEDIUM = 1;
    public static final int SIZE_LARGE = 2;
    
    private static final float SMALL_WIDTH = 0.5f;
    private static final float MEDIUM_WIDTH = 0.75f;
    private static final float LARGE_WIDTH = 1.0f;
    private static final float CONTRACTED_HEIGHT = 0.15f;
    private static final float EXTENDED_HEIGHT = 0.25f;
    private static final float BOOST_IN_PLATFORM_DEPTH = 0.05f;
    
    private static final float SMALL_POWER_MULTIPLIER = 1.6f;
    private static final float MEDIUM_POWER_MULTIPLIER = 2.0f;
    private static final float LARGE_POWER_MULTIPLIER = 2.4f;
    
    private static final float CONTRACTED_DURATION = 0.05f;
    
    private static final float LOW_POWER_SOUND_VOLUME = 0.33f;
    private static final float MEDIUM_POWER_SOUND_VOLUME = 0.66f;
    private static final float HIGH_POWER_SOUND_VOLUME = 1.0f;
    
    private final Sprite extendedSprite;
    private final Sprite contractedSprite;
    private final Vector2 offset;
    
    private float contractedElapsed;
    
    private final float width;
    private final float jumpBoostSpeed;
    private final float soundVolume;
    
    public JumpBoostPlatformFeature(Platform platform, float positionFraction, int jumpBoostSize, AssetManager assetManager) {
        super(platform);
        
        JumpBoostSizeData sizeData = getJumpBoostSizeData(jumpBoostSize);
        
        width = sizeData.craterWidth;
        
        TextureAtlas platformsAtlas = assetManager.get(ResourceNames.PLATFORMS_ATLAS);
        
        extendedSprite = platformsAtlas.createSprite(sizeData.extendedImageName);
        extendedSprite.setSize(width, EXTENDED_HEIGHT);
        
        contractedSprite = platformsAtlas.createSprite(sizeData.contractedImageName);
        contractedSprite.setSize(width, CONTRACTED_HEIGHT);
        
        jumpBoostSpeed = sizeData.speed;
        soundVolume = sizeData.soundVolume;
        
        offset = new Vector2(
                (Platform.WIDTH - width) * positionFraction,
                Platform.HEIGHT - BOOST_IN_PLATFORM_DEPTH);
        
        contractedElapsed = CONTRACTED_DURATION;
        
        backgroundRenderPrecedence = 1.0f;
        foregroundRenderPrecedence = 1.0f;
    }
    
    @Override
    public void update(float delta, Vector2 platformPosition) {
        if (contractedElapsed < CONTRACTED_DURATION) {
            contractedElapsed += delta;
        }
    }
    
    @Override
    public void renderBackground(SpriteBatch batch, Vector2 platformPosition, Color color) {
        
        if (contractedElapsed < CONTRACTED_DURATION) {
            contractedSprite.setPosition(platformPosition.x + offset.x, platformPosition.y + offset.y);
            contractedSprite.setColor(color);
            contractedSprite.draw(batch);
        } else {
            extendedSprite.setPosition(platformPosition.x + offset.x, platformPosition.y + offset.y);
            extendedSprite.setColor(color);
            extendedSprite.draw(batch);
        }
    }
    
    @Override
    public boolean isContact(float relativeCollisionPointX) {
        float charX1 = relativeCollisionPointX + GameCharacter.COLLISION_WIDTH_OFFSET;
        float charX2 = charX1 + GameCharacter.COLLISION_WIDTH;
        
        float featureX1 = offset.x;
        float featureX2 = featureX1 + width;
        
        // http://eli.thegreenplace.net/2008/08/15/intersection-of-1d-segments/
        return charX2 >= featureX1 && featureX2 >= charX1;
    }
    
    @Override
    public void applyContact(CollisionEffects collisionEffects) {
        collisionEffects.setJumpBoostEffect(jumpBoostSpeed, soundVolume);
        contract();
    }
    
    private void contract() {
        contractedElapsed = 0.0f;
    }
    
    private static JumpBoostSizeData getJumpBoostSizeData(int jumpBoostSize) {
        
        switch (jumpBoostSize) {
            case SIZE_SMALL:
                return new JumpBoostSizeData(
                        ResourceNames.PLATFORM_JUMP_BOOST_SMALL_EXTENDED_IMAGE_NAME,
                        ResourceNames.PLATFORM_JUMP_BOOST_SMALL_CONTRACTED_IMAGE_NAME,
                        SMALL_WIDTH,
                        GameCharacter.JUMP_SPEED * SMALL_POWER_MULTIPLIER,
                        LOW_POWER_SOUND_VOLUME);
            case SIZE_MEDIUM:
                return new JumpBoostSizeData(
                        ResourceNames.PLATFORM_JUMP_BOOST_MEDIUM_EXTENDED_IMAGE_NAME,
                        ResourceNames.PLATFORM_JUMP_BOOST_MEDIUM_CONTRACTED_IMAGE_NAME,
                        MEDIUM_WIDTH,
                        GameCharacter.JUMP_SPEED * MEDIUM_POWER_MULTIPLIER,
                        MEDIUM_POWER_SOUND_VOLUME);
            case SIZE_LARGE:
                return new JumpBoostSizeData(
                        ResourceNames.PLATFORM_JUMP_BOOST_LARGE_EXTENDED_IMAGE_NAME,
                        ResourceNames.PLATFORM_JUMP_BOOST_LARGE_CONTRACTED_IMAGE_NAME,
                        LARGE_WIDTH,
                        GameCharacter.JUMP_SPEED * LARGE_POWER_MULTIPLIER,
                        HIGH_POWER_SOUND_VOLUME);
            default:
                ExceptionThrower.throwException("Invalid jump boost size: %d", jumpBoostSize);
                return null;
        }
    }
    
    private static class JumpBoostSizeData {
        final String extendedImageName;
        final String contractedImageName;
        final float craterWidth;
        final float speed;
        final float soundVolume;
        
        public JumpBoostSizeData(
                String extendedImageName,
                String contractedImageName,
                float craterWidth,
                float speed,
                float soundVolume) {
            
            this.extendedImageName = extendedImageName;
            this.contractedImageName = contractedImageName;
            this.craterWidth = craterWidth;
            this.speed = speed;
            this.soundVolume = soundVolume;
        }
    }
}
