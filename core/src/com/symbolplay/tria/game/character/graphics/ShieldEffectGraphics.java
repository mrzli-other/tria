package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.resources.ResourceNames;

final class ShieldEffectGraphics extends CharacterGraphicsBase {
    
    private static final float WIDTH = 2.5f;
    private static final float HEIGHT = 2.5f;
    private static final float OFFSET_X = (GameCharacter.WIDTH - WIDTH) / 2.0f;
    private static final float OFFSET_Y = (GameCharacter.HEIGHT - HEIGHT) / 2.0f;
    
    private static final float HIGH_ALPHA = 1.0f;
    private static final float LOW_ALPHA = 0.1f;
    private static final float MID_ALPHA = (HIGH_ALPHA + LOW_ALPHA) / 2.0f;
    private static final float ALPHA_HALF_RANGE = (HIGH_ALPHA - LOW_ALPHA) / 2.0f;
    private static final float ALPHA_CHANGE_PERIOD = 0.3f;
    
    private final Sprite sprite;
    
    private float internalTime;
    private float effectRemaining;
    
    public ShieldEffectGraphics(AssetManager assetManager) {
        
        TextureAtlas atlas = assetManager.get(ResourceNames.CHARACTER_ATLAS);
        sprite = atlas.createSprite(ResourceNames.CHARACTER_SHIELD_EFFECT_IMAGE_NAME);
        sprite.setSize(WIDTH, HEIGHT);
    }
    
    @Override
    public void reset() {
        internalTime = 0.0f;
        effectRemaining = 0.0f;
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
        
        if (effectRemaining < CharacterEffects.SHIELD_EFFECT_FINISHING_DURATION) {
            float alpha = MID_ALPHA + ALPHA_HALF_RANGE * MathUtils.sinDeg(internalTime / ALPHA_CHANGE_PERIOD * 360.0f);
            sprite.setAlpha(alpha);
        } else if (sprite.getColor().a != 1.0f) {
            sprite.setAlpha(1.0f);
        }
        
        sprite.setPosition(characterPosition.x + OFFSET_X, characterPosition.y + OFFSET_Y);
        sprite.draw(batch);
    }
    
    public void setEffectRemaining(float effectRemaining) {
        this.effectRemaining = effectRemaining;
    }
}
