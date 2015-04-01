package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
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

final class AntiGravityDriveGraphics extends CharacterGraphicsBase {
    
    private static final float ENGINE_WIDTH = 1.0f;
    private static final float ENGINE_HEIGHT = 0.65f;
    private static final float ENGINE_OFFSET_X = 0.0f;
    private static final float ENGINE_OFFSET_Y = 0.4f - ENGINE_HEIGHT;
    
    private static final float UPGRADE_DOTS_WIDTH = 0.7f;
    private static final float UPGRADE_DOTS_HEIGHT = 0.25f;
    private static final float UPGRADE_DOTS_OFFSET_X = ENGINE_OFFSET_X + (ENGINE_WIDTH - UPGRADE_DOTS_WIDTH) / 2.0f;
    private static final float UPGRADE_DOTS_OFFSET_Y = ENGINE_OFFSET_Y + ENGINE_HEIGHT - 0.1f - UPGRADE_DOTS_HEIGHT;
    
    private static final float EXHAUST_ELEMENT_FIRST_WIDTH = 1.3f;
    private static final float EXHAUST_ELEMENT_FIRST_HEIGHT = 0.55f;
    private static final float EXHAUST_ELEMENT_FIRST_OFFSET_X = ENGINE_OFFSET_X + (ENGINE_WIDTH - EXHAUST_ELEMENT_FIRST_WIDTH) / 2.0f;
    private static final float EXHAUST_ELEMENT_FIRST_OFFSET_Y = ENGINE_OFFSET_Y + ENGINE_HEIGHT - EXHAUST_ELEMENT_FIRST_HEIGHT - 0.25f;
    private static final float EXHAUST_ELEMENT_SIZE_STEP = 0.15f;
    
    private static final int NUM_EXHAUST_ELEMENTS = 3;
    
    private static final float HIGH_ALPHA = 1.0f;
    private static final float LOW_ALPHA = 0.1f;
    private static final float MID_ALPHA = (HIGH_ALPHA + LOW_ALPHA) / 2.0f;
    private static final float ALPHA_HALF_RANGE = (HIGH_ALPHA - LOW_ALPHA) / 2.0f;
    private static final float ALPHA_CHANGE_PERIOD = 0.3f;
    private static final float ALPHA_CHANGE_ANGULAR_VELOCITY = MathUtils.PI2 / ALPHA_CHANGE_PERIOD;
    
    private static final float EXHAUST_ELEMENT_HIGH_ALPHA = 1.0f;
    private static final float EXHAUST_ELEMENT_LOW_ALPHA = -1.0f;
    private static final float EXHAUST_ELEMENT_MID_ALPHA = (EXHAUST_ELEMENT_HIGH_ALPHA + EXHAUST_ELEMENT_LOW_ALPHA) / 2.0f;
    private static final float EXHAUST_ELEMENT_ALPHA_HALF_RANGE = (EXHAUST_ELEMENT_HIGH_ALPHA - EXHAUST_ELEMENT_LOW_ALPHA) / 2.0f;
    private static final float EXHAUST_ELEMENT_ALPHA_CHANGE_PERIOD = 2.0f;
    private static final float EXHAUST_ELEMENT_ALPHA_CHANGE_ANGULAR_VELOCITY = MathUtils.PI2 / EXHAUST_ELEMENT_ALPHA_CHANGE_PERIOD;
    private static final float EXHAUST_ELEMENT_ALPHA_CHANGE_PHASE_STEP = EXHAUST_ELEMENT_ALPHA_CHANGE_PERIOD / NUM_EXHAUST_ELEMENTS;
    
    private final Sprite engineSprite;
    
    private final Sprite[] exhaustElementSprites;
    private final float[] exhaustElementOffsetsX;
    private final float[] exhaustElementOffsetsY;
    
    private final Sprite upgradeDotsSprite;
    private final Array<AtlasRegion> upgradeDotsRegions;
    
    private final CareerData careerData;
    
    private float internalTime;
    private float effectRemaining;
    
    public AntiGravityDriveGraphics(CareerData careerData, AssetManager assetManager) {
        
        this.careerData = careerData;
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        
        engineSprite = atlas.createSprite(ResourceNames.CHARACTER_ANTI_GRAVITY_ENGINE_IMAGE_NAME);
        engineSprite.setSize(ENGINE_WIDTH, ENGINE_HEIGHT);
        
        exhaustElementSprites = new Sprite[NUM_EXHAUST_ELEMENTS];
        exhaustElementOffsetsX = new float[NUM_EXHAUST_ELEMENTS];
        exhaustElementOffsetsY = new float[NUM_EXHAUST_ELEMENTS];
        float exhaustElementOffsetX = EXHAUST_ELEMENT_FIRST_OFFSET_X;
        float exhaustElementOffsetY = EXHAUST_ELEMENT_FIRST_OFFSET_Y;
        float exhaustElementOffsetWidth = EXHAUST_ELEMENT_FIRST_WIDTH;
        float exhaustElementOffsetHeight = EXHAUST_ELEMENT_FIRST_HEIGHT;
        for (int i = 0; i < NUM_EXHAUST_ELEMENTS; i++) {
            Sprite exhaustElementSprite = atlas.createSprite(ResourceNames.CHARACTER_ANTI_GRAVITY_EXHAUST_ELEMENT_RESOURCE_NAMES.getResourceName(i));
            exhaustElementSprite.setSize(exhaustElementOffsetWidth, exhaustElementOffsetHeight);
            exhaustElementSprites[i] = exhaustElementSprite;
            
            exhaustElementOffsetsX[i] = exhaustElementOffsetX;
            exhaustElementOffsetsY[i] = exhaustElementOffsetY;
            
            exhaustElementOffsetX -= EXHAUST_ELEMENT_SIZE_STEP;
            exhaustElementOffsetY -= EXHAUST_ELEMENT_SIZE_STEP;
            exhaustElementOffsetWidth += EXHAUST_ELEMENT_SIZE_STEP * 2.0f;
            exhaustElementOffsetHeight += EXHAUST_ELEMENT_SIZE_STEP;
        }
        
        upgradeDotsRegions = atlas.findRegions(ResourceNames.CHARACTER_UPGRADE_DOTS_IMAGE_NAME);
        upgradeDotsSprite = new Sprite();
        upgradeDotsSprite.setSize(UPGRADE_DOTS_WIDTH, UPGRADE_DOTS_HEIGHT);
        upgradeDotsSprite.setRegion((TextureRegion) upgradeDotsRegions.get(0));
    }
    
    @Override
    public void reset() {
        internalTime = 0.0f;
        effectRemaining = 0.0f;
        
        upgradeDotsSprite.setRegion((TextureRegion) upgradeDotsRegions.get(careerData.getAntiGravityLevel()));
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
        
        if (effectRemaining < CharacterEffects.ANTI_GRAVITY_EFFECT_FINISHING_DURATION) {
            float alpha = MID_ALPHA + ALPHA_HALF_RANGE * MathUtils.sin(internalTime * ALPHA_CHANGE_ANGULAR_VELOCITY);
            setAllSpritesAlpha(alpha);
        } else if (engineSprite.getColor().a != 1.0f) {
            setAllSpritesAlpha(1.0f);
        }
        
        for (int i = 0; i < NUM_EXHAUST_ELEMENTS; i++) {
            Sprite exhaustElementSprite = exhaustElementSprites[i];
            exhaustElementSprite.setPosition(characterPosition.x + exhaustElementOffsetsX[i], characterPosition.y + exhaustElementOffsetsY[i]);
            float exhaustElementAlpha = EXHAUST_ELEMENT_MID_ALPHA + EXHAUST_ELEMENT_ALPHA_HALF_RANGE *
                    MathUtils.sin(internalTime * EXHAUST_ELEMENT_ALPHA_CHANGE_ANGULAR_VELOCITY - i * EXHAUST_ELEMENT_ALPHA_CHANGE_PHASE_STEP);
            exhaustElementAlpha = MathUtils.clamp(exhaustElementAlpha, 0.0f, 1.0f);
            exhaustElementSprite.setAlpha(exhaustElementSprite.getColor().a * exhaustElementAlpha);
            exhaustElementSprite.draw(batch);
        }
        
        engineSprite.setPosition(characterPosition.x + ENGINE_OFFSET_X, characterPosition.y + ENGINE_OFFSET_Y);
        engineSprite.draw(batch);
        
        upgradeDotsSprite.setPosition(characterPosition.x + UPGRADE_DOTS_OFFSET_X, characterPosition.y + UPGRADE_DOTS_OFFSET_Y);
        upgradeDotsSprite.draw(batch);
    }
    
    private void setAllSpritesAlpha(float alpha) {
        engineSprite.setAlpha(alpha);
        for (int i = 0; i < NUM_EXHAUST_ELEMENTS; i++) {
            exhaustElementSprites[i].setAlpha(alpha);
        }
        upgradeDotsSprite.setAlpha(alpha);
    }
    
    public void setEffectRemaining(float effectRemaining) {
        this.effectRemaining = effectRemaining;
    }
}
