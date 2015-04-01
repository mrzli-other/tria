package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.game.character.CharacterEffects;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.character.graphics.CharacterGraphicsComposition;
import com.symbolplay.tria.game.character.states.utils.ItemCollisionHandler;
import com.symbolplay.tria.game.character.states.utils.PlatformCollisionHandler;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.rise.RiseActiveData;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;

final class NormalCharacterState extends CharacterStateBase {
    
    private final CharacterGraphicsComposition characterGraphics;
    
    private final Rectangle rect;
    
    private final CharacterStateChangeData characterStateChangeData;
    
    private final PlatformCollisionHandler platformCollisionHandler;
    private final ItemCollisionHandler itemCollisionHandler;
    
    public NormalCharacterState(CharacterStateManager characterStateManager, CareerData careerData, AssetManager assetManager) {
        super(characterStateManager);
        
        characterGraphics = new CharacterGraphicsComposition(gameCharacter, careerData, true, false, false, assetManager);
        
        rect = new Rectangle(0.0f, 0.0f, GameCharacter.WIDTH, GameCharacter.HEIGHT);
        
        characterStateChangeData = new CharacterStateChangeData();
        
        platformCollisionHandler = new PlatformCollisionHandler(this);
        itemCollisionHandler = new ItemCollisionHandler();
    }
    
    @Override
    public void reset() {
        characterGraphics.reset();
    }
    
    @Override
    public void start(CharacterStateChangeData changeData) {
    }
    
    @Override
    public void update(CharacterStateUpdateData updateData) {
        
        if (isStateChangePending()) {
            return;
        }
        
        Vector2 position = gameCharacter.getPosition();
        Vector2 speed = gameCharacter.getSpeed();
        CharacterEffects characterEffects = gameCharacter.getCharacterEffects();
        float visibleAreaPosition = updateData.visibleAreaPosition;
        Array<Platform> platforms = updateData.platforms;
        RiseActiveData riseActiveData = updateData.riseActiveData;
        float horizontalSpeed = updateData.horizontalSpeed;
        float delta = updateData.delta;
        
        characterEffects.update(delta);
        
        if (characterEffects.isAntiGravity()) {
            changeState(CharacterStateManager.POWER_UP_ANTI_GRAVITY_DRIVE_CHARACTER_STATE);
            return;
        } else if (characterEffects.isRocket()) {
            changeState(CharacterStateManager.POWER_UP_JETPACK_CHARACTER_STATE);
            return;
        }
        
        if (!isStateChangePending()) {
            platformCollisionHandler.handleCollisionWithPlatform(
                    position,
                    speed,
                    visibleAreaPosition,
                    GameCharacter.JUMP_SPEED,
                    updateData.platformToCharCollisionData,
                    platforms,
                    riseActiveData.minActivePlatformIndex,
                    riseActiveData.maxActivePlatformIndex,
                    characterEffects,
                    delta);
            
            speed.x = horizontalSpeed;
            position.x = GameUtils.getPositiveModulus(
                    position.x + GameCharacter.CHARACTER_CENTER_X_OFFSET, GameContainer.GAME_AREA_WIDTH) -
                    GameCharacter.CHARACTER_CENTER_X_OFFSET;
        }
        
        // TODO: remember to remove this
//        if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
//            float screenTouchedY = Gdx.input.getY(0);
//            if (200.0f <= screenTouchedY && screenTouchedY <= 400.0f) {
//                speed.y = GameCharacter.JUMP_SPEED * 5.0f;
//            } else if (screenTouchedY > 400.0f) {
//                speed.y = GameCharacter.JUMP_SPEED * 0.5f;
//            }
//        }
        
        rect.x = position.x;
        rect.y = position.y;
        
        if (!isStateChangePending()) {
            platformCollisionHandler.handleCollisionWithPlatformFeatures(
                    platforms,
                    riseActiveData.minActivePlatformIndex,
                    riseActiveData.maxActivePlatformIndex,
                    rect,
                    speed,
                    characterEffects);
        }
        
        if (!isStateChangePending() && !characterEffects.isMovementPowerUpActive()) {
            handleCollisionWithEnemies(
                    position,
                    updateData.enemies,
                    riseActiveData.minActiveEnemyIndex,
                    riseActiveData.maxActiveEnemyIndex,
                    characterEffects);
        }
        
        if (!isStateChangePending()) {
            itemCollisionHandler.handleCollisionWithItems(
                    position,
                    updateData.items,
                    riseActiveData.minActiveItemIndex,
                    riseActiveData.maxActiveItemIndex,
                    null,
                    characterEffects,
                    visibleAreaPosition,
                    rect);
        }
        
        characterGraphics.update(delta);
    }
    
    @Override
    public void render(CharacterStateRenderData renderData) {
        SpriteBatch batch = renderData.batch;
        
        characterGraphics.render(batch);
    }
    
    private void handleCollisionWithEnemies(Vector2 position, Array<EnemyBase> enemies, int minActiveIndex, int maxActiveIndex, CharacterEffects characterEffects) {
        
        if (characterEffects.isShield()) {
            return;
        }
        
        for (int i = minActiveIndex; i <= maxActiveIndex; i++) {
            EnemyBase enemy = enemies.get(i);
            if (enemy.isCollision(rect)) {
                if (characterEffects.getLives() <= 0) {
                    changeStateWithEnemyTypeData(enemy);
                } else {
                    characterEffects.subtractLife();
                    Sounds.play(ResourceNames.SOUND_OUCH);
                    Vibration.vibrate();
                    characterEffects.setShield(CharacterEffects.DEATH_SHIELD_DURATION);
                }
                return;
            }
        }
    }
    
    private void changeStateWithEnemyTypeData(EnemyBase enemy) {
        characterStateChangeData.clear();
        int enemyType = enemy.getType() == EnemyBase.SAW_TYPE ? CharacterStateChangeData.SAW_ENEMY_TYPE_VALUE : CharacterStateChangeData.GENERAL_ENEMY_TYPE_VALUE;
        characterStateChangeData.setData(CharacterStateChangeData.ENEMY_TYPE_KEY, enemyType);
        changeState(CharacterStateManager.DYING_ENEMY_CHARACTER_STATE, characterStateChangeData);
    }
}
