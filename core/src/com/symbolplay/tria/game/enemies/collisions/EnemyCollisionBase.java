package com.symbolplay.tria.game.enemies.collisions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class EnemyCollisionBase {
    
    public static final int CIRCLE_BOUNDING_GEOMETRY_TYPE = 0;
    public static final int RECT_BOUNDING_GEOMETRY_TYPE = 1;
    
    public abstract void update(Sprite sprite);
    
    public abstract boolean isCollision(Rectangle characterRect);
}
