package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;

final class RocketDriveGraphics extends CharacterGraphicsBase {
    
    private static final float ENGINE_WIDTH = 1.0f;
    private static final float ENGINE_HEIGHT = 0.8f;
    private static final float ENGINE_OFFSET_X = 0.0f;
    private static final float ENGINE_OFFSET_Y = 0.4f - ENGINE_HEIGHT;
    
    private static final float UPGRADE_DOTS_WIDTH = 0.7f;
    private static final float UPGRADE_DOTS_HEIGHT = 0.25f;
    private static final float UPGRADE_DOTS_OFFSET_X = ENGINE_OFFSET_X + (ENGINE_WIDTH - UPGRADE_DOTS_WIDTH) / 2.0f;
    private static final float UPGRADE_DOTS_OFFSET_Y = ENGINE_OFFSET_Y + ENGINE_HEIGHT - 0.1f - UPGRADE_DOTS_HEIGHT;
    
    private static final float EXHAUST_WIDTH = 0.75f;
    private static final float EXHAUST_HEIGHT = 2.0f;
    private static final float EXHAUST_OFFSET_X = ENGINE_OFFSET_X + (ENGINE_WIDTH - EXHAUST_WIDTH) / 2.0f;
    private static final float EXHAUST_OFFSET_Y = ENGINE_OFFSET_Y - EXHAUST_HEIGHT + 0.05f;
    
    private static final float EXHAUST_FRAME_DURATION = 0.005f;
    
    private static final float HIGH_ALPHA = 1.0f;
    private static final float LOW_ALPHA = 0.1f;
    private static final float MID_ALPHA = (HIGH_ALPHA + LOW_ALPHA) / 2.0f;
    private static final float ALPHA_HALF_RANGE = (HIGH_ALPHA - LOW_ALPHA) / 2.0f;
    private static final float ALPHA_CHANGE_PERIOD = 0.3f;
    private static final float ALPHA_CHANGE_ANGULAR_VELOCITY = MathUtils.PI2 / ALPHA_CHANGE_PERIOD;
    
    private static final float EXHAUST_SCALE_AMPLITUDE = 0.2f;
    private static final float EXHAUST_SCALE_CHANGE_PERIOD = 0.1f; 
    private static final float EXHAUST_SCALE_CHANGE_ANGULAR_VELOCITY = MathUtils.PI2 / EXHAUST_SCALE_CHANGE_PERIOD;
    
    private final Sprite engineSprite;
    private final Animation exhaustAnimation;
    private final Sprite exhaustSprite;
    
    private final Sprite upgradeDotsSprite;
    private final Array<AtlasRegion> upgradeDotsRegions;
    
    private final CareerData careerData;
    
    private boolean isLeft;
    
    private float internalTime;
    private float effectRemaining;
    
    public RocketDriveGraphics(CareerData careerData, AssetManager assetManager) {
        
        this.careerData = careerData;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        
        engineSprite = atlas.createSprite(ResourceNames.CHARACTER_ROCKET_ENGINE_IMAGE_NAME);
        engineSprite.setSize(ENGINE_WIDTH, ENGINE_HEIGHT);
        
        Array<AtlasRegion> fireAtlasRegions = atlas.findRegions(ResourceNames.CHARACTER_ROCKET_EXHAUST_IMAGE_NAME);
        exhaustAnimation = new Animation(EXHAUST_FRAME_DURATION, fireAtlasRegions, Animation.PlayMode.LOOP_PINGPONG);
        exhaustSprite = new Sprite();
        exhaustSprite.setOrigin(EXHAUST_WIDTH * 0.5f, EXHAUST_HEIGHT);
        exhaustSprite.setSize(EXHAUST_WIDTH, EXHAUST_HEIGHT);
        
        upgradeDotsRegions = atlas.findRegions(ResourceNames.CHARACTER_UPGRADE_DOTS_IMAGE_NAME);
        upgradeDotsSprite = new Sprite();
        upgradeDotsSprite.setSize(UPGRADE_DOTS_WIDTH, UPGRADE_DOTS_HEIGHT);
        upgradeDotsSprite.setRegion((TextureRegion) upgradeDotsRegions.get(0));
        
        isLeft = false;
    }
    
    @Override
    public void reset() {
        internalTime = 0.0f;
        effectRemaining = 0.0f;
        
        upgradeDotsSprite.setRegion((TextureRegion) upgradeDotsRegions.get(careerData.getRocketLevel()));
    }
    
    @Override
    public void update(float delta) {
        internalTime += delta;
    }
    
    @Override
    public void render(SpriteBatch batch, Vector2 characterPosition) {
        if (effectRemaining <= 0.0f) {
            return;
        }
        
        TextureRegion exhaustAnimationFrame = exhaustAnimation.getKeyFrame(internalTime);
        exhaustSprite.setRegion(exhaustAnimationFrame);
//        batch.draw(exhaustAnimationAnimationFrame,
//                platformPosition.x, platformPosition.y + PlatformData.HEIGHT,
//                FIRE_SPRITE_WIDTH, FIRE_SPRITE_HEIGHT);
        
        if (effectRemaining < CharacterEffects.ROCKET_EFFECT_FINISHING_DURATION) {
            float alpha = MID_ALPHA + ALPHA_HALF_RANGE * MathUtils.sin(internalTime * ALPHA_CHANGE_ANGULAR_VELOCITY);
            setAllSpritesAlpha(alpha);
        } else if (engineSprite.getColor().a != 1.0f) {
            setAllSpritesAlpha(1.0f);
        }
        
        exhaustSprite.setPosition(characterPosition.x + EXHAUST_OFFSET_X, characterPosition.y + EXHAUST_OFFSET_Y);
        exhaustSprite.setScale(1.0f, 1.0f + EXHAUST_SCALE_AMPLITUDE * MathUtils.sin(internalTime * EXHAUST_SCALE_CHANGE_ANGULAR_VELOCITY));
        exhaustSprite.draw(batch);
        
        engineSprite.setPosition(characterPosition.x + ENGINE_OFFSET_X, characterPosition.y + ENGINE_OFFSET_Y);
        engineSprite.draw(batch);
        
        upgradeDotsSprite.setPosition(characterPosition.x + UPGRADE_DOTS_OFFSET_X, characterPosition.y + UPGRADE_DOTS_OFFSET_Y);
        upgradeDotsSprite.draw(batch);
    }
    
    private void setAllSpritesAlpha(float alpha) {
        engineSprite.setAlpha(alpha);
        exhaustSprite.setAlpha(alpha);
        upgradeDotsSprite.setAlpha(alpha);
    }
    
    public void setOrientation(boolean isLeft) {
        if (this.isLeft != isLeft) {
            this.isLeft = isLeft;
        }
    }
    
    public void setEffectRemaining(float effectRemaining) {
        this.effectRemaining = effectRemaining;
    }
}
