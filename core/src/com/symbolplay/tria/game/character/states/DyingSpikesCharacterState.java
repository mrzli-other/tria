package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.game.character.graphics.CharacterGraphicsComposition;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;

final class DyingSpikesCharacterState extends CharacterStateBase {
    
    private static final float DYING_TOTAL_DURATION = 3.0f;
    
    private final CharacterGraphicsComposition characterGraphics;
    
    private float dyingElapsed;
    
    public DyingSpikesCharacterState(CharacterStateManager characterStateManager, CareerData careerData, AssetManager assetManager) {
        super(characterStateManager);
        
        characterGraphics = new CharacterGraphicsComposition(gameCharacter, careerData, false, false, false, assetManager);
    }
    
    @Override
    public void reset() {
        characterGraphics.reset();
        
        dyingElapsed = 0.0f;
    }
    
    @Override
    public void update(CharacterStateUpdateData updateData) {
        
        float delta = updateData.delta;
        
        if (dyingElapsed >= DYING_TOTAL_DURATION) {
            changeState(CharacterStateManager.FINISHED_CHARACTER_STATE);
            return;
        }
        
        dyingElapsed += delta;
        
        characterGraphics.update(delta);
    }
    
    @Override
    public void render(CharacterStateRenderData renderData) {
        SpriteBatch batch = renderData.batch;
        
        characterGraphics.render(batch);
    }
    
    @Override
    public void start(CharacterStateChangeData changeData) {
        Sounds.play(ResourceNames.SOUND_SPIKES);
        Vibration.vibrate();
    }
    
    @Override
    public void end() {
        Sounds.stop(ResourceNames.SOUND_SPIKES);
    }
}
