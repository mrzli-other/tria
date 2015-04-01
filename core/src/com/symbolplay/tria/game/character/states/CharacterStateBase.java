package com.symbolplay.tria.game.character.states;

import com.symbolplay.tria.game.character.GameCharacter;

public abstract class CharacterStateBase {
    
    private final CharacterStateManager characterStateManager;
    protected final GameCharacter gameCharacter;
    
    public CharacterStateBase(CharacterStateManager characterStateManager) {
        this.characterStateManager = characterStateManager;
        this.gameCharacter = this.characterStateManager.getGameCharacter();
    }
    
    public void reset() {
    }
    
    public void start(CharacterStateChangeData changeData) {
    }
    
    public void end() {
    }
    
    public void update(CharacterStateUpdateData updateData) {
    }
    
    public void render(CharacterStateRenderData renderData) {
    }
    
    public boolean isFinished() {
        return false;
    }
    
    public void changeState(String state) {
        changeState(state, null);
    }
    
    public void changeState(String state, CharacterStateChangeData changeData) {
        characterStateManager.changeState(state, changeData);
    }
    
    public boolean isStateChangePending() {
        return characterStateManager.isStateChangePending();
    }
}
