package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.persistence.userdata.CareerData;

public final class CharacterStateManager {
    
    public static final String NORMAL_CHARACTER_STATE = "normal";
    public static final String DYING_FALL_CHARACTER_STATE = "dyingfall";
    public static final String DYING_ENEMY_CHARACTER_STATE = "dyingenemy";
    public static final String DYING_SPIKES_CHARACTER_STATE = "dyingspikes";
    public static final String POWER_UP_ANTI_GRAVITY_DRIVE_CHARACTER_STATE = "powerupantigravitydrive";
    public static final String POWER_UP_JETPACK_CHARACTER_STATE = "powerupjetpack";
    public static final String FINISHED_CHARACTER_STATE = "finished";
    
    private static final int CHARACTER_STATES_CAPACITY = 10;
    
    private final GameCharacter gameCharacter;
    private final ObjectMap<String, CharacterStateBase> characterStates;
    
    private CharacterStateBase currentState;
    private CharacterStateBase nextState;
    
    private CharacterStateChangeData changeData;
    
    public CharacterStateManager(GameCharacter gameCharacter, CareerData careerData, AssetManager assetManager) {
        this.gameCharacter = gameCharacter;
        
        characterStates = new ObjectMap<String, CharacterStateBase>(CHARACTER_STATES_CAPACITY);
        characterStates.put(NORMAL_CHARACTER_STATE, new NormalCharacterState(this, careerData, assetManager));
        characterStates.put(DYING_FALL_CHARACTER_STATE, new DyingFallCharacterState(this, careerData, assetManager));
        characterStates.put(DYING_ENEMY_CHARACTER_STATE, new DyingEnemyCharacterState(this, careerData, assetManager));
        characterStates.put(DYING_SPIKES_CHARACTER_STATE, new DyingSpikesCharacterState(this, careerData, assetManager));
        characterStates.put(POWER_UP_ANTI_GRAVITY_DRIVE_CHARACTER_STATE, new PowerUpAntiGravityCharacterState(this, careerData, assetManager));
        characterStates.put(POWER_UP_JETPACK_CHARACTER_STATE, new PowerUpRocketCharacterState(this, careerData, assetManager));
        characterStates.put(FINISHED_CHARACTER_STATE, new FinishedCharacterState(this));
    }
    
    public void reset() {
        for (CharacterStateBase characterState : characterStates.values()) {
            characterState.reset();
        }
        
        nextState = characterStates.get(NORMAL_CHARACTER_STATE);
        currentState = nextState;
        currentState.start(null);
    }
    
    public void changeState(String state, CharacterStateChangeData changeData) {
        this.changeData = changeData;
        nextState = characterStates.get(state);
    }
    
    public boolean isStateChangePending() {
        return nextState != currentState;
    }
    
    // this is used so that update/render operation sequence state is atomic
    public void applyStateChange() {
        if (isStateChangePending()) {
            currentState.end();
            currentState = nextState;
            currentState.start(changeData);
        }
    }
    
    public CharacterStateBase getCurrentState() {
        return currentState;
    }
    
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }
}
