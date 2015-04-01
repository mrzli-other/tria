package com.symbolplay.tria.game.enemies.collisions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.symbolplay.gamelibrary.util.ExceptionThrower;

public final class EnemyCollisionFactory {
    
    public static EnemyCollisionBase create(int boundingGeometryType, Sprite sprite, float collisionPadding) {
        
        switch (boundingGeometryType) {
            case EnemyCollisionBase.CIRCLE_BOUNDING_GEOMETRY_TYPE:
                return new CircleEnemyCollision(sprite, collisionPadding);
                
            case EnemyCollisionBase.RECT_BOUNDING_GEOMETRY_TYPE:
                return new RectangleEnemyCollision(sprite, collisionPadding);
            
            default:
                ExceptionThrower.throwException("Invalid bounding geomerty type: %d", boundingGeometryType);
                return null;
        }
    }
}
