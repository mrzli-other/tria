package com.symbolplay.tria.game.character.states.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.tria.GameContainer;
import com.symbolplay.tria.game.character.GameCharacter;
import com.symbolplay.tria.game.platforms.Platform;

public final class CharacterStateUtils {
    
    public static void updatePositionAndSpeed(Vector2 position, Vector2 speed, float horizontalSpeed, float delta) {
        updatePositionAndSpeed(position, speed, horizontalSpeed, 0.0f, GameContainer.GAME_AREA_WIDTH, delta);
    }
    
    public static void updatePositionAndSpeed(
            Vector2 position, Vector2 speed, float horizontalSpeed,
            float minPositionX, float rangeX, float delta) {
        
        position.x += speed.x * delta;
        position.y += speed.y * delta;
        speed.x = horizontalSpeed;
        speed.y = Math.max(speed.y - GameCharacter.GRAVITY * delta, -GameCharacter.JUMP_SPEED);
        
        float leftGoThroughtOffsetX = minPositionX - GameCharacter.CHARACTER_CENTER_X_OFFSET;
        position.x = GameUtils.getPositiveModulus(position.x - leftGoThroughtOffsetX, rangeX) + leftGoThroughtOffsetX;
    }
    
    public static void updatePosition(Vector2 position, Vector2 speed, float delta) {
        updatePosition(position, speed, 0.0f, GameContainer.GAME_AREA_WIDTH, delta);
    }
    
    public static void updatePosition(Vector2 position, Vector2 speed, float minPositionX, float rangeX, float delta) {
        position.x += speed.x * delta;
        position.y += speed.y * delta;
        
        float leftGoThroughtOffsetX = minPositionX - GameCharacter.CHARACTER_CENTER_X_OFFSET;
        position.x = GameUtils.getPositiveModulus(position.x - leftGoThroughtOffsetX, rangeX) + leftGoThroughtOffsetX;
    }
    
    public static boolean isVisibleCollidablePlatformInCharacterRange(Array<Platform> platforms, float characterY, float belowOffset, float aboveOffset) {
        for (Platform platform : platforms) {
            float platformY = platform.getPosition().y;
            if (platform.isVisible() && platform.isCollidable() && characterY - belowOffset <= platformY && platformY <= characterY + aboveOffset) {
                return true;
            }
        }
        
        return false;
    }
}
