package com.symbolplay.tria.game.character.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.symbolplay.tria.game.Sounds;
import com.symbolplay.tria.game.Vibration;
import com.symbolplay.tria.game.character.graphics.CharacterGraphicsComposition;
import com.symbolplay.tria.game.character.states.utils.CharacterStateUtils;
import com.symbolplay.tria.persistence.userdata.CareerData;
import com.symbolplay.tria.resources.ResourceNames;

final class DyingFallCharacterState extends CharacterStateBase {
    
    private static final float FALL_STOP_LIMIT = 3.0f;
    private static final float DYING_DURATION = 2.0f;
    
    private final CharacterGraphicsComposition characterGraphics;
    
    private float dyingCountdown;
    
    public DyingFallCharacterState(CharacterStateManager characterStateManager, CareerData careerData, AssetManager assetManager) {
        super(characterStateManager);
        
        characterGraphics = new CharacterGraphicsComposition(gameCharacter, careerData, false, false, false, assetManager);
    }
    
    @Override
    public void reset() {
        characterGraphics.reset();
        
        dyingCountdown = DYING_DURATION;
    }
    
    @Override
    public void update(CharacterStateUpdateData updateData) {
        
        if (dyingCountdown <= 0.0f) {
            changeState(CharacterStateManager.FINISHED_CHARACTER_STATE);
            return;
        }
        
        Vector2 position = gameCharacter.getPosition();
        Vector2 speed = gameCharacter.getSpeed();
        float visibleAreaPosition = updateData.visibleAreaPosition;
        float delta = updateData.delta;
        
        if (position.y > visibleAreaPosition - FALL_STOP_LIMIT) {
            CharacterStateUtils.updatePositionAndSpeed(position, speed, updateData.horizontalSpeed, delta);
        }
        
        dyingCountdown -= delta;
        
        characterGraphics.update(delta);
    }
    
    @Override
    public void render(CharacterStateRenderData renderData) {
        SpriteBatch batch = renderData.batch;
        
        characterGraphics.render(batch);
    }
    
    @Override
    public void start(CharacterStateChangeData changeData) {
        Sounds.play(ResourceNames.SOUND_FALL);
        Vibration.vibrate();
    }
    
    @Override
    public void end() {
        Sounds.stop(ResourceNames.SOUND_FALL);
    }
}
