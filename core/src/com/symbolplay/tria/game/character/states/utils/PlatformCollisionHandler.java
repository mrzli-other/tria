package com.symbolplay.tria.game.character.states.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.gamelibrary.util.Pools;
import com.symbolplay.tria.game.CollisionEffects;
import com.symbolplay.tria.game.JumpBoostCollisionEffectData;
import com.symbolplay.tria.game.PlatformToCharCollisionData;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.character.states.CharacterStateBase;
import com.symbolplay.tria.game.character.states.CharacterStateChangeData;
import com.symbolplay.tria.game.character.states.CharacterStateManager;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.resources.ResourceNames;

public final class PlatformCollisionHandler {
    
    private final CharacterStateBase characterState;
    
    private final CharCollisionData charCollisionData;
    private final CollisionEffects collisionEffects;
    
    private final CharacterStateChangeData characterStateChangeData;
    
    public PlatformCollisionHandler(CharacterStateBase characterState) {
        this.characterState = characterState;
        
        charCollisionData = new CharCollisionData();
        collisionEffects = new CollisionEffects();
        
        characterStateChangeData = new CharacterStateChangeData();
    }
    
    public void handleCollisionWithPlatform(
            Vector2 position,
            Vector2 speed,
            float visibleAreaPosition,
            float jumpSpeed,
            PlatformToCharCollisionData platformToCharCollisionData,
            Array<Platform> platforms,
            int minActiveIndex,
            int maxActiveIndex,
            CharacterEffects characterEffects,
            float delta) {
        
        if (!platformToCharCollisionData.isCollision) {
            Vector2 cpNext = Pools.obtainVector();
            cpNext.set(position.x + speed.x * delta, position.y + speed.y * delta);
            Vector2 intersection = Pools.obtainVector();
            
            if (isCollisionWithPlatform(platforms, minActiveIndex, maxActiveIndex, position, cpNext, intersection, charCollisionData)) {
                position.set(intersection);
                
                handleFall(position, speed, jumpSpeed, visibleAreaPosition, characterEffects);
                if (!characterState.isStateChangePending()) {
                    handleCollisionWithPlatform(position, speed, jumpSpeed, platforms, minActiveIndex, maxActiveIndex, characterEffects);
                }
            } else {
                position.set(cpNext);
                speed.y = Math.max(speed.y - GameCharacter.GRAVITY * delta, -GameCharacter.JUMP_SPEED);
                handleFall(position, speed, jumpSpeed, visibleAreaPosition, characterEffects);
            }
            
            Pools.freeVector(cpNext);
            Pools.freeVector(intersection);
        } else {
            position.y = platformToCharCollisionData.collisionPoint.y;
            charCollisionData.collisionPlatform = platformToCharCollisionData.collisionPlatform;
            charCollisionData.collisionPointX = platformToCharCollisionData.collisionPoint.x;
            
            handleFall(position, speed, jumpSpeed, visibleAreaPosition, characterEffects);
            if (!characterState.isStateChangePending()) {
                handleCollisionWithPlatform(position, speed, jumpSpeed, platforms, minActiveIndex, maxActiveIndex, characterEffects);
            }
        }
    }
    
    private void handleCollisionWithPlatform(
            Vector2 position,
            Vector2 speed,
            float jumpSpeed,
            Array<Platform> platforms,
            int minActiveIndex,
            int maxActiveIndex,
            CharacterEffects characterEffects) {
        
        charCollisionData.collisionPlatform.applyContactToCollisionEffects(charCollisionData.collisionPointX, collisionEffects);
        
        if (collisionEffects.isEffectActive(CollisionEffects.IMPALE_SPIKES) && !characterEffects.isShield()) {
            if (characterEffects.getLives() <= 0) {
                collisionEffects.clear();
                characterState.changeState(CharacterStateManager.DYING_SPIKES_CHARACTER_STATE);
                return;
            } else {
                characterEffects.subtractLife();
                Sounds.play(ResourceNames.SOUND_OUCH);
                Vibration.vibrate();
                characterEffects.setShield(CharacterEffects.DEATH_SHIELD_DURATION);
            }
        }
        
        if (collisionEffects.isEffectActive(CollisionEffects.JUMP_BOOST)) {
            JumpBoostCollisionEffectData jumpBoostCollisionEffectData = (JumpBoostCollisionEffectData) collisionEffects.getEffectData(CollisionEffects.JUMP_BOOST);
            speed.y = jumpBoostCollisionEffectData.jumpBoostSpeed;
            float volume = jumpBoostCollisionEffectData.soundVolume;
            Sounds.play(ResourceNames.SOUND_JUMP_BOOST, volume);
        } else {
            speed.y = jumpSpeed;
            Sounds.play(ResourceNames.SOUND_JUMP);
        }
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            Platform platform = platforms.get(i);
            platform.applyEffects(collisionEffects);
        }
        
        collisionEffects.clear();
    }
    
    public void handleCollisionWithPlatformFeatures(
            Array<Platform> platforms,
            int minActiveIndex,
            int maxActiveIndex,
            Rectangle characterRect,
            Vector2 characterSpeed,
            CharacterEffects characterEffects) {
        
        if (characterEffects.isShield()) {
            return;
        }
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            Platform platform = platforms.get(i);
            platform.applyFeaturesCollisionEffects(characterRect, characterSpeed, collisionEffects);
        }
        
        if (collisionEffects.isEffectActive(CollisionEffects.IMPALE_ATTACHED_SPIKES) && !characterEffects.isShield()) {
            if (characterEffects.getLives() <= 0) {
                collisionEffects.clear();
                changeStateWithEnemyTypeData();
                return;
            } else {
                characterEffects.subtractLife();
                Sounds.play(ResourceNames.SOUND_OUCH);
                Vibration.vibrate();
                characterEffects.setShield(CharacterEffects.DEATH_SHIELD_DURATION);
            }
        }
        
        collisionEffects.clear();
    }
    
    private void handleFall(Vector2 position, Vector2 speed, float jumpSpeed, float visibleAreaPosition, CharacterEffects characterEffects) {
        
        if (position.y <= 0.0f) {
            position.y = 0.0f;
            speed.y = jumpSpeed;
        } else if (position.y < visibleAreaPosition) {
            if (characterEffects.getLives() <= 0) {
                characterState.changeState(CharacterStateManager.DYING_FALL_CHARACTER_STATE);
            } else {
                position.y = visibleAreaPosition + GameUtils.EPSILON;
                characterEffects.subtractLife();
                Sounds.play(ResourceNames.SOUND_OUCH);
                Vibration.vibrate();
                speed.y = GameCharacter.JUMP_SPEED;
                characterState.changeState(CharacterStateManager.POWER_UP_ANTI_GRAVITY_DRIVE_CHARACTER_STATE);
            }
        }
    }
    
    private static boolean isCollisionWithPlatform(Array<Platform> platforms, int minActiveIndex, int maxActiveIndex,
            Vector2 c1, Vector2 c2, Vector2 intersection, CharCollisionData charCollisionData) {
        
        // only check for collision when character is going down
        if (c2.y >= c1.y) {
            return false;
        }
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            Platform platform = platforms.get(i);
            if (platform.isCollision(c1, c2, intersection)) {
                charCollisionData.collisionPlatform = platform;
                charCollisionData.collisionPointX = intersection.x;
                return true;
            }
        }
        
        return false;
    }
    
    private void changeStateWithEnemyTypeData() {
        characterStateChangeData.clear();
        characterStateChangeData.setData(CharacterStateChangeData.ENEMY_TYPE_KEY, CharacterStateChangeData.SPIKES_ENEMY_TYPE_VALUE);
        characterState.changeState(CharacterStateManager.DYING_ENEMY_CHARACTER_STATE, characterStateChangeData);
    }
    
    private static class CharCollisionData {
        public Platform collisionPlatform;
        public float collisionPointX;
    }
}
