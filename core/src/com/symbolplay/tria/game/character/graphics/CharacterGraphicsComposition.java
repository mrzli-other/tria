package com.symbolplay.tria.game.character.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.persistence.userdata.CareerData;

public final class CharacterGraphicsComposition {
    
    private static final float MIN_ORIENTATION_CHANGE_INTERVAL = 0.5f;
    
    private final GameCharacter gameCharacter;
    
    private final AutoPickUpLineGraphics autoPickUpLineGraphics;
    private final CharacterLegsGraphics characterLegsGraphics;
    private final AntiGravityDriveGraphics antiGravityDriveGraphics;
    private final CharacterBodyGraphics characterBodyGraphics;
    private final RocketDriveGraphics rocketDriveGraphics;
    private final ShieldEffectGraphics shieldEffectGraphics;
    
    private float orientationChangeElapsed;
    private boolean isLeft;
    
    public CharacterGraphicsComposition(GameCharacter gameCharacter, CareerData careerData, boolean isShieldEffect, boolean isAntiGravityEffect,
            boolean isRocketEffect, AssetManager assetManager) {
        
        this.gameCharacter = gameCharacter;
        
        autoPickUpLineGraphics = (isAntiGravityEffect || isRocketEffect) ? new AutoPickUpLineGraphics(assetManager) : null;
        characterLegsGraphics = new CharacterLegsGraphics(assetManager);
        antiGravityDriveGraphics = isAntiGravityEffect ? new AntiGravityDriveGraphics(careerData, assetManager) : null;
        rocketDriveGraphics = isRocketEffect ? new RocketDriveGraphics(careerData, assetManager) : null;
        characterBodyGraphics = new CharacterBodyGraphics(assetManager);
        shieldEffectGraphics = isShieldEffect ? new ShieldEffectGraphics(assetManager) : null;
    }
    
    public void reset() {
        orientationChangeElapsed = MIN_ORIENTATION_CHANGE_INTERVAL;
        isLeft = false;
        
        if (autoPickUpLineGraphics != null) {
            autoPickUpLineGraphics.reset();
        }
        
        characterLegsGraphics.reset();
        
        if (antiGravityDriveGraphics != null) {
            antiGravityDriveGraphics.reset();
        }
        
        if (rocketDriveGraphics != null) {
            rocketDriveGraphics.reset();
        }
        
        characterBodyGraphics.reset();
        
        if (shieldEffectGraphics != null) {
            shieldEffectGraphics.reset();
        }
    }
    
    public void update(float delta) {
        if (orientationChangeElapsed < MIN_ORIENTATION_CHANGE_INTERVAL) {
            orientationChangeElapsed += delta;
        }
        
        flipIfNecessary();
        
        CharacterEffects characterEffects = gameCharacter.getCharacterEffects();
        
        if (autoPickUpLineGraphics != null) {
            autoPickUpLineGraphics.update(delta);
        }
        
        characterLegsGraphics.update(delta);
        
        if (antiGravityDriveGraphics != null) {
            antiGravityDriveGraphics.update(delta);
            antiGravityDriveGraphics.setEffectRemaining(characterEffects.getAntiGravityRemaining());
        }
        
        if (rocketDriveGraphics != null) {
            rocketDriveGraphics.update(delta);
            rocketDriveGraphics.setEffectRemaining(characterEffects.getRocketRemaining());
            rocketDriveGraphics.setOrientation(isLeft);
        }
        
        characterBodyGraphics.update(delta);
        characterBodyGraphics.setOrientation(isLeft);
        
        if (shieldEffectGraphics != null) {
            shieldEffectGraphics.update(delta);
            shieldEffectGraphics.setEffectRemaining(characterEffects.getShieldRemaining());
        }
    }
    
    public void render(SpriteBatch batch) {
        Vector2 position = gameCharacter.getPosition();
        
        if (autoPickUpLineGraphics != null) {
            autoPickUpLineGraphics.render(batch, position);
        }
        
        characterLegsGraphics.render(batch, position);
        
        if (antiGravityDriveGraphics != null) {
            antiGravityDriveGraphics.render(batch, position);
        }
        
        if (rocketDriveGraphics != null) {
            rocketDriveGraphics.render(batch, position);
        }
        
        characterBodyGraphics.render(batch, position);
        
        if (shieldEffectGraphics != null) {
            shieldEffectGraphics.render(batch, position);
        }
    }
    
    private void flipIfNecessary() {
        if (orientationChangeElapsed >= MIN_ORIENTATION_CHANGE_INTERVAL && isFlipCondition()) {
            isLeft = !isLeft;
            orientationChangeElapsed = 0.0f;
        }
    }
    
    private boolean isFlipCondition() {
        return (isLeft && gameCharacter.getOrientation() == GameCharacter.ORIENTATION_RIGHT) || (!isLeft && gameCharacter.getOrientation() == GameCharacter.ORIENTATION_LEFT);
    }
}
