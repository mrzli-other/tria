package com.symbolplay.tria.game.character;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.PlatformToCharCollisionData;
import com.symbolplay.tria.game.character.states.CharacterStateManager;
import com.symbolplay.tria.game.character.states.CharacterStateRenderData;
import com.symbolplay.tria.game.character.states.CharacterStateUpdateData;
import com.symbolplay.tria.game.enemies.EnemyBase;
import com.symbolplay.tria.game.items.ItemBase;
import com.symbolplay.tria.game.platforms.Platform;
import com.symbolplay.tria.game.rise.RiseActiveData;
import com.symbolplay.tria.persistence.userdata.CareerData;

public final class GameCharacter {
    
    public static final int ORIENTATION_CENTER = 0;
    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;
    
    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.5f;
    public static final float CHARACTER_CENTER_X_OFFSET = WIDTH / 2.0f;
    public static final float COLLISION_WIDTH = 0.5f;
    public static final float COLLISION_WIDTH_OFFSET = (WIDTH - COLLISION_WIDTH) / 2.0f;
    public static final float COLLISION_LINE_LENGTH = COLLISION_WIDTH + COLLISION_WIDTH_OFFSET;
    
    public static final float JUMP_SPEED = 21.9f;
    public static final float GRAVITY = 35.0f;
    
    private final Vector2 position;
    private final Vector2 speed;
    
    private final CharacterStateManager characterStateManager;
    private final CharacterEffects characterEffects;
    
    private final CharacterStateUpdateData characterStateUpdateData;
    private final CharacterStateRenderData characterStateRenderData;
    
    public GameCharacter(CareerData careerData, AssetManager assetManager) {
        
        position = new Vector2();
        speed = new Vector2();
        
        characterStateManager = new CharacterStateManager(this, careerData, assetManager);
        characterEffects = new CharacterEffects(careerData);
        
        characterStateUpdateData = new CharacterStateUpdateData();
        characterStateRenderData = new CharacterStateRenderData();
    }
    
    public void reset() {
        position.set(GameContainer.GAME_AREA_WIDTH / 2.0f - CHARACTER_CENTER_X_OFFSET, 0.0f);
        speed.set(0.0f, JUMP_SPEED);
        
        characterStateManager.reset();
        characterEffects.reset();
    }
    
    public void update(float horizontalSpeed,
            PlatformToCharCollisionData platformToCharCollisionData,
            Array<Platform> platforms,
            Array<EnemyBase> enemies,
            Array<ItemBase> items,
            RiseActiveData riseActiveData,
            float visibleAreaPosition,
            float delta) {
        
        characterStateUpdateData.horizontalSpeed = horizontalSpeed;
        characterStateUpdateData.platformToCharCollisionData = platformToCharCollisionData;
        characterStateUpdateData.platforms = platforms;
        characterStateUpdateData.enemies = enemies;
        characterStateUpdateData.items = items;
        characterStateUpdateData.riseActiveData = riseActiveData;
        characterStateUpdateData.visibleAreaPosition = visibleAreaPosition;
        characterStateUpdateData.delta = delta;
        
        characterStateManager.getCurrentState().update(characterStateUpdateData);
    }
    
    public void render(SpriteBatch batch) {
        characterStateRenderData.batch = batch;
        
        characterStateManager.getCurrentState().render(characterStateRenderData);
        characterStateManager.applyStateChange();
    }
    
    public boolean isFinished() {
        return characterStateManager.getCurrentState().isFinished();
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public Vector2 getSpeed() {
        return speed;
    }
    
    public int getOrientation() {
        return speed.x < 0.0f ? ORIENTATION_LEFT : (speed.x > 0.0f ? ORIENTATION_RIGHT : ORIENTATION_CENTER);
    }
    
    public CharacterEffects getCharacterEffects() {
        return characterEffects;
    }
    
    public int getScore() {
        return characterEffects.getScore();
    }
    
    public int getLives() {
        return characterEffects.getLives();
    }
    
    public int getCoins() {
        return characterEffects.getCoins();
    }
}
