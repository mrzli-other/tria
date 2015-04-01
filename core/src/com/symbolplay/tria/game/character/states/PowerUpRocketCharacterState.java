package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.character.graphics.CharacterGraphicsComposition;
import com.symbolplay.tria.game.character.states.utils.CharacterStateUtils;
import com.symbolplay.tria.game.character.states.utils.ItemCollisionHandler;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.rise.RiseActiveData;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;

final class PowerUpRocketCharacterState extends CharacterStateBase {
    
    private static final float SPEED = GameCharacter.JUMP_SPEED * 1.0f;
    private static final float RELEASE_SPEED = GameCharacter.JUMP_SPEED * 1.0f;
    private static final float MIN_SPEED = 5.0f;
    private static final float ACCELERATION = 35.0f;
    private static final float DECELERATION_TIME = 1.0f;
    
    private static final float END_SHIELD_DURATION = 3.0f;
    
    private final CharacterGraphicsComposition characterGraphics;
    
    private final Rectangle rect;
    
    private final ItemCollisionHandler itemCollisionHandler;
    private final Array<Integer> blockedItemEffects;
    
    public PowerUpRocketCharacterState(CharacterStateManager characterStateManager, CareerData careerData, AssetManager assetManager) {
        super(characterStateManager);
        
        characterGraphics = new CharacterGraphicsComposition(gameCharacter, careerData, true, false, true, assetManager);
        
        rect = new Rectangle(0.0f, 0.0f, GameCharacter.WIDTH, GameCharacter.HEIGHT);
        
        itemCollisionHandler = new ItemCollisionHandler();
        blockedItemEffects = getBlockedItemEffects();
    }
    
    @Override
    public void reset() {
        characterGraphics.reset();
    }
    
    @Override
    public void start(CharacterStateChangeData changeData) {
        Sounds.loop(ResourceNames.SOUND_ROCKET);
    }
    
    @Override
    public void end() {
        Sounds.stop(ResourceNames.SOUND_ROCKET);
    }
    
    @Override
    public void update(CharacterStateUpdateData updateData) {
        if (isStateChangePending()) {
            return;
        }
        
        Vector2 position = gameCharacter.getPosition();
        Vector2 speed = gameCharacter.getSpeed();
        CharacterEffects characterEffects = gameCharacter.getCharacterEffects();
        float horizontalSpeed = updateData.horizontalSpeed;
        Array<Platform> platforms = updateData.platforms;
        Array<ItemBase> items = updateData.items;
        RiseActiveData riseActiveData = updateData.riseActiveData;
        float visibleAreaPosition = updateData.visibleAreaPosition;
        float delta = updateData.delta;
        
        characterEffects.update(delta);
        
        if (!characterEffects.isRocket()) {
            boolean isPlatformInRange = CharacterStateUtils.isVisibleCollidablePlatformInCharacterRange(platforms, position.y, 0.0f, 3.5f);
            if (isPlatformInRange) {
                speed.y = RELEASE_SPEED;
                characterEffects.setShield(END_SHIELD_DURATION);
                changeState(CharacterStateManager.NORMAL_CHARACTER_STATE);
                return;
            } else {
                characterEffects.setRocketAdditionalDuration();
            }
        }
        
        speed.x = horizontalSpeed;
        speed.y = getSpeed(speed.y, delta);
        CharacterStateUtils.updatePosition(position, speed, delta);
        
        rect.x = position.x;
        rect.y = position.y;
        
        itemCollisionHandler.handleCollisionWithItems(
                position,
                items,
                riseActiveData.minActiveItemIndex,
                riseActiveData.maxActiveItemIndex,
                blockedItemEffects,
                characterEffects,
                visibleAreaPosition,
                rect);
        
        itemCollisionHandler.handleCoinAutoPickup(
                position.y,
                items,
                riseActiveData.minActiveItemIndex,
                riseActiveData.maxActiveItemIndex,
                characterEffects,
                visibleAreaPosition);
        
        characterGraphics.update(delta);
    }
    
    @Override
    public void render(CharacterStateRenderData renderData) {
        SpriteBatch batch = renderData.batch;
        
        characterGraphics.render(batch);
    }
    
    private float getSpeed(float currentSpeed, float delta) {
        float remainingDuration = gameCharacter.getCharacterEffects().getRocketRemaining();
        currentSpeed += ACCELERATION * delta;
        float maxSpeed = remainingDuration >= DECELERATION_TIME ? SPEED : (RELEASE_SPEED + (remainingDuration / DECELERATION_TIME) * (SPEED - RELEASE_SPEED));
        return MathUtils.clamp(currentSpeed, MIN_SPEED, maxSpeed);
    }
    
    private static Array<Integer> getBlockedItemEffects() {
        Array<Integer> blockedItemEffects = new Array<Integer>(true, ItemBase.NUM_EFFECTS);
        blockedItemEffects.add(ItemBase.ROCKET_EFFECT);
        blockedItemEffects.add(ItemBase.ANTI_GRAVITY_EFFECT);
        blockedItemEffects.add(ItemBase.COIN_EFFECT);
        return blockedItemEffects;
    }
}
