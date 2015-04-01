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

final class DyingEnemyCharacterState extends CharacterStateBase {
    
    private static final float DYING_STATIC_DURATION = 0.6f;
    private static final float DYING_MOVING_DURATION = 3.0f;
    private static final float DYING_TOTAL_DURATION = DYING_STATIC_DURATION + DYING_MOVING_DURATION;
    
    private final CharacterGraphicsComposition characterGraphics;
    
    private float dyingElapsed;
    private boolean isStaticPhaseStarted;
    private boolean isMovingPhaseStarted;
    private float horizontalSpeed;
    
    private int enemyType;
    
    public DyingEnemyCharacterState(CharacterStateManager characterStateManager, CareerData careerData, AssetManager assetManager) {
        super(characterStateManager);
        
        characterGraphics = new CharacterGraphicsComposition(gameCharacter, careerData, false, false, false, assetManager);
    }
    
    @Override
    public void reset() {
        characterGraphics.reset();
        
        dyingElapsed = 0.0f;
        isStaticPhaseStarted = false;
        isMovingPhaseStarted = false;
        horizontalSpeed = 0.0f;
    }
    
    @Override
    public void start(CharacterStateChangeData changeData) {
        enemyType = changeData.getData(CharacterStateChangeData.ENEMY_TYPE_KEY);
        playStopSound(true);
        Vibration.vibrate();
    }
    
    @Override
    public void end() {
        playStopSound(false);
    }
    
    @Override
    public void update(CharacterStateUpdateData updateData) {
        
        if (dyingElapsed >= DYING_TOTAL_DURATION) {
            changeState(CharacterStateManager.FINISHED_CHARACTER_STATE);
            return;
        }
        
        if (!isStaticPhaseStarted) {
            horizontalSpeed = updateData.horizontalSpeed;
            isStaticPhaseStarted = true;
        }
        
        Vector2 position = gameCharacter.getPosition();
        Vector2 speed = gameCharacter.getSpeed();
        float delta = updateData.delta;
        
        if (dyingElapsed >= DYING_STATIC_DURATION) {
            if (!isMovingPhaseStarted) {
                speed.y = 0.0f;
                isMovingPhaseStarted = true;
            } else {
                CharacterStateUtils.updatePositionAndSpeed(position, speed, horizontalSpeed, delta);
            }
        }
        
        dyingElapsed += delta;
        
        characterGraphics.update(delta);
    }
    
    @Override
    public void render(CharacterStateRenderData renderData) {
        SpriteBatch batch = renderData.batch;
        
        characterGraphics.render(batch);
    }
    
    private void playStopSound(boolean isPlay) {
        switch (enemyType) {
            case CharacterStateChangeData.GENERAL_ENEMY_TYPE_VALUE:
                if (isPlay) {
                    Sounds.play(ResourceNames.SOUND_OUCH);
                } else {
                    Sounds.stop(ResourceNames.SOUND_OUCH);
                }
                break;
            
            case CharacterStateChangeData.SAW_ENEMY_TYPE_VALUE:
                if (isPlay) {
                    Sounds.play(ResourceNames.SOUND_SAW);
                } else {
                    Sounds.stop(ResourceNames.SOUND_SAW);
                }
                break;
                
            case CharacterStateChangeData.SPIKES_ENEMY_TYPE_VALUE:
                if (isPlay) {
                    Sounds.play(ResourceNames.SOUND_SPIKES);
                } else {
                    Sounds.stop(ResourceNames.SOUND_SPIKES);
                }
                break;
        }
    }
}
